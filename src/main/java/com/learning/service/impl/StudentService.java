package com.learning.service.impl;

import com.learning.constants.NumberConstant;
import com.learning.entity.StudentCollection;
import com.learning.entity.StudentEntity;
import com.learning.exception.DataNotFoundException;
import com.learning.models.StudentModel;
import com.learning.repository.StudentMongoRepository;
import com.learning.repository.StudentRepository;
import com.learning.service.CommonService;
import com.learning.utility.email.EmailSender;
import com.learning.utility.excel.StudentReader;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService implements CommonService<StudentModel, Long> {
    private final StudentRepository jpaRepository;
    private final ModelMapper modelMapper;

    private final StudentReader studentReader;
    private final EmailSender emailSender;

    private final StudentMongoRepository mongoRepository;

    @Override
    public List<StudentModel> getAllRecords() {
        List<StudentCollection> studentCollectionList = mongoRepository.findAll();
        List<StudentEntity> studentEntityList = jpaRepository.findAll();
        if (!CollectionUtils.isEmpty(studentCollectionList)) {
            List<StudentModel> studentModelList = studentCollectionList.stream()
                    .map(studentCollection ->
                            modelMapper.map(studentCollection, StudentModel.class))
                    .collect(Collectors.toList());
            return studentModelList;
        } else if (!CollectionUtils.isEmpty(studentEntityList)) {
            List<StudentModel> studentModelList = studentEntityList.stream().map(studentEntity -> {
                StudentModel studentModel = new StudentModel();
                modelMapper.map(studentEntity, studentModel);
                return studentModel;
            }).collect(Collectors.toList());
            return studentModelList;
        } else {
            return Collections.emptyList();
        }

    }


    @Override
    public List<StudentModel> getLimitedRecords(int count) {
        List<StudentEntity> studentEntityList = jpaRepository.findAll();
        if (Objects.nonNull(studentEntityList) && studentEntityList.size() > NumberConstant.ZERO) {
            List<StudentModel> studentModelList = studentEntityList.stream()
                    .limit(count)
                    .map(studentEntity -> {
                        StudentModel studentModel = modelMapper.map(studentEntity, StudentModel.class);
                        return studentModel;
                    })
                    .collect(Collectors.toList());
            return studentModelList;
        } else {
            return Collections.emptyList();
        }

    }


    @Override
    public List<StudentModel> getSortedRecords(String sortBy) {
        List<StudentEntity> studentEntityList = jpaRepository.findAll();
        if (Objects.nonNull(studentEntityList) && studentEntityList.size() > NumberConstant.ZERO) {
            Comparator<StudentEntity> comparator = findSuitableComparator(sortBy);
            List<StudentModel> studentModelList = studentEntityList.stream()
                    .sorted(comparator)
                    .map(studentEntity -> {
                        StudentModel studentModel = modelMapper.map(studentEntity, StudentModel.class);
                        return studentModel;
                    })
                    .collect(Collectors.toList());
            return studentModelList;

        } else return Collections.emptyList();
    }

    //TODO:Revise completableFuture
    @Override
    public StudentModel saveRecord(StudentModel studentModel) {
        if (Objects.nonNull(studentModel)) {
            StudentEntity studentEntity = new StudentEntity();
            modelMapper.map(studentModel, StudentEntity.class);
            jpaRepository.save(studentEntity);

            Runnable runnable = () -> {
                StudentCollection studentCollection = new StudentCollection();
                modelMapper.map(studentModel, studentCollection);
                mongoRepository.save(studentCollection);
            };
            CompletableFuture.runAsync(runnable);
        }
        return studentModel;
    }

    @Override
    public List<StudentModel> saveAll(List<StudentModel> studentModelList) {
        if (Objects.nonNull(studentModelList) && studentModelList.size() > NumberConstant.ZERO) {
            List<StudentEntity> studentEntityList = studentModelList.stream()
                    .map(studentModel -> {
                        StudentEntity entity = modelMapper.map(studentModel, StudentEntity.class);

                        return entity;
                    })
                    .collect(Collectors.toList());
            jpaRepository.saveAll(studentEntityList);
            CompletableFuture.runAsync(() -> {
                List<StudentCollection> studentCollectionList = studentModelList.stream()
                        .map(studentModel -> {
                            StudentCollection studentCollection = modelMapper.map(studentModel, StudentCollection.class);
                            return studentCollection;
                        })
                        .collect(Collectors.toList());
                mongoRepository.saveAll(studentCollectionList);

            });

        }
        return studentModelList;
    }

    public void emailSender() {
        List<String> emailList = jpaRepository.findAllEmails();
        emailSender.mailSenderThread(emailList);

    }

    public void sendEmailWithAttachment() {
        List<String> emailList = jpaRepository.findAllEmails();
        emailSender.sendMailWithAttachment(emailList);
    }


    @Override
    public StudentModel getRecordById(Long id) {
        Optional<StudentEntity> optionalEntity = jpaRepository.findById(id);
        if (optionalEntity.isPresent()) {
            StudentEntity studentEntity = optionalEntity.get();
            StudentModel studentModel = modelMapper.map(studentEntity, StudentModel.class);
            return studentModel;
        }
        throw new DataNotFoundException("data not found " + id);
    }

    @Override
    public void deleteRecordById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public StudentModel updateRecordById(Long id, StudentModel record) {

        Optional<StudentEntity> optionalStudentEntity = jpaRepository.findById(id);
        if (optionalStudentEntity.isPresent()) {
            StudentEntity studentEntity = optionalStudentEntity.get();
            modelMapper.map(record, studentEntity);

            jpaRepository.save(studentEntity);
        }
        return record;

    }

    public void saveExcelFile(MultipartFile file) {
        if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            try {
                //List<StudentEntity> studentEntityList = ExcelHelper.convertExcelToListOFStudent(file.getInputStream());
                List<StudentEntity> studentEntityList = studentReader.getStudentObjects(file.getInputStream());
                jpaRepository.saveAll(studentEntityList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public StudentModel saveRecordInMongo(StudentModel studentModel) {
        if (Objects.nonNull(studentModel)) {
            StudentCollection studentCollection = new StudentCollection();
            modelMapper.map(studentModel, studentCollection);
            mongoRepository.save(studentCollection);
        }
        return studentModel;

    }

    private Comparator<StudentEntity> findSuitableComparator(String sortBy) {
        Comparator<StudentEntity> comparator;
        switch (sortBy) {
            case "name": {
                comparator = (StudentEntity studentOne, StudentEntity studentTwo) ->
                        studentOne.getName().compareTo(studentTwo.getName());
                break;
            }
            case "email":
                comparator = (studentOne, studentTwo) ->
                        studentOne.getName().compareTo(studentTwo.getName());
                break;
            default: {
                comparator = (studentOne, studentTwo) ->
                        studentOne.getId().compareTo(studentTwo.getId());
            }
        }

        return comparator;
    }
}








