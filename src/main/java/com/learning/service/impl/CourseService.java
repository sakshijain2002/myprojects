package com.learning.service.impl;

import com.learning.constants.NumberConstant;
import com.learning.entity.CourseCollection;
import com.learning.entity.CourseEntity;
import com.learning.entity.TrainerCollection;
import com.learning.models.CourseModel;
import com.learning.models.TrainerModel;
import com.learning.repository.CourseMongoRepository;
import com.learning.repository.CourseRepository;
import com.learning.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class CourseService implements CommonService<CourseModel, Long> {

    private final CourseRepository courseRepo;
    private final ModelMapper modelMapper;

    private final CourseMongoRepository courseMongoRepository;
    @Override
    public List<CourseModel> getAllRecords() {
        List<CourseEntity> courseEntityList = courseRepo.findAll(); // Fetch data from JPA repository
        List<CourseCollection> courseCollectionList = courseMongoRepository.findAll(); // Fetch data from MongoDB repository

        List<CourseModel> combinedList = new ArrayList<>();

        // Map JPA entities to CourseModel and add to combinedList
        if (!CollectionUtils.isEmpty(courseEntityList)) {
            combinedList.addAll(courseEntityList.stream()
                    .map(courseEntity -> modelMapper.map(courseEntity, CourseModel.class))
                    .collect(Collectors.toList()));
        }

        // Map MongoDB documents to CourseModel and add to combinedList
        if (!CollectionUtils.isEmpty(courseCollectionList)) {
            combinedList.addAll(courseCollectionList.stream()
                    .map(courseCollection -> modelMapper.map(courseCollection, CourseModel.class))
                    .collect(Collectors.toList()));
        }

        return combinedList;
    }

//    @Override
//    public List<CourseModel> getAllRecords() {
//        List<CourseEntity> courseEntityList = courseRepo.findAll();
//        if (!CollectionUtils.isEmpty(courseEntityList)) {
//            List<CourseModel> courseModelList = courseEntityList.stream()
//                    .map(courseEntity -> {
//                        CourseModel courseModel = new CourseModel();
//                        modelMapper.map(courseEntity, courseModel);
//                        return courseModel;
//                    })
//                    .collect(Collectors.toList());
//            return courseModelList;
//        } else {
//            return Collections.emptyList();
//        }
//    }

    @Override
    public List<CourseModel> getLimitedRecords(int count) {

        List<CourseEntity> courseEntityList = courseRepo.findAll();
        if (Objects.nonNull(courseEntityList) && courseEntityList.size() > NumberConstant.ZERO) {
            List<CourseModel> courseModelList = courseEntityList.stream()
                    .limit(count)
                    .map(courseEntity -> {
                        CourseModel courseModel = modelMapper.map(courseEntity, CourseModel.class);
                        return courseModel;
                    })
                    .collect(Collectors.toList());
            return courseModelList;
        } else {
            return Collections.emptyList();
        }


    }

    @Override
    public List<CourseModel> getSortedRecords(String sortBy) {

        List<CourseEntity> courseEntityList = courseRepo.findAll();
        if (Objects.nonNull(courseEntityList) && courseEntityList.size() > NumberConstant.ZERO) {
            Comparator<CourseEntity> comparator = findSuitableComparator(sortBy);
            List<CourseModel> courseModelList = courseEntityList.stream()
                    .sorted(comparator)
                    .map(courseEntity -> {
                        CourseModel courseModel = modelMapper.map(courseEntity, CourseModel.class);
                        return courseModel;
                    })
                    .collect(Collectors.toList());
            return courseModelList;
        } else {
            return Collections.emptyList();
        }
    }

        @Override
        public CourseModel saveRecord(CourseModel courseModel){
            if (Objects.nonNull(courseModel)) {
                CourseEntity courseEntity = new CourseEntity();
                BeanUtils.copyProperties(courseModel, courseEntity);
                CourseEntity savedObject = courseRepo.save(courseEntity);
            }
            return courseModel;
        }

        @Override
        public List<CourseModel> saveAll(List < CourseModel > courseModelList) {

            if (Objects.nonNull(courseModelList) && courseModelList.size() > NumberConstant.ZERO) {
                List<CourseEntity> courseEntityList = courseModelList.stream()
                        .map(courseModel -> {
                            CourseEntity entity = modelMapper.map(courseModel, CourseEntity.class);
                            return entity;
                        })
                        .collect(Collectors.toList());
                courseRepo.saveAll(courseEntityList);
            }
            return courseModelList;


        }
    public CourseModel saveRecordInMongo(CourseModel courseModel) {
        if (Objects.nonNull(courseModel)) {
            CourseCollection courseCollection = new CourseCollection();
            modelMapper.map(courseModel, courseCollection);
            courseMongoRepository.save(courseCollection);
        }
        return courseModel;

    }

        @Override
        public CourseModel getRecordById (Long id){
            Optional<CourseEntity> optionalEntity = courseRepo.findById(id);
            if (optionalEntity.isPresent()) {
                CourseEntity courseEntity = optionalEntity.get();
                CourseModel courseModel = modelMapper.map(courseEntity, CourseModel.class);
                return courseModel;
            }
            throw new IllegalArgumentException("data not found" + id);
        }

        @Override
        public void deleteRecordById (Long id){
            courseRepo.deleteById(id);

        }
    public void deleteRecordByIdInMongo (Long id){
        courseMongoRepository.deleteById(id);

    }

        @Override
        public CourseModel updateRecordById (Long id, CourseModel record){


            Optional<CourseEntity> optionalCourseEntity = courseRepo.findById(id);
            if (optionalCourseEntity.isPresent()) {
                CourseEntity courseEntity = optionalCourseEntity.get();
                modelMapper.map(record, courseEntity);

                courseRepo.save(courseEntity);
            }
            return record;
        }
    public CourseModel updateRecordByIdInMongo(Long id, CourseModel record){


        Optional<CourseCollection> optionalCourseCollection = courseMongoRepository.findById(id);
        if (optionalCourseCollection.isPresent()) {
            CourseCollection courseCollection = optionalCourseCollection.get();
            modelMapper.map(record, courseCollection);

            courseMongoRepository.save(courseCollection);
        }
        return record;
    }


        private Comparator<CourseEntity> findSuitableComparator (String sortBy){
            Comparator<CourseEntity> comparator;
            switch (sortBy) {
                case "name": {
                    comparator = (courseOne, courseTwo) ->
                            courseOne.getName().compareTo(courseTwo.getName());
                    break;
                }
                case "curriculum": {
                    comparator = (courseOne, courseTwo) ->
                            courseOne.getCurriculum().compareTo(courseTwo.getCurriculum());
                    break;
                }
                case "duration": {
                    comparator = (courseOne, courseTwo) ->
                            courseOne.getDuration().compareTo(courseTwo.getDuration());
                    break;
                }
                default: {
                    comparator = (courseOne, courseTwo) ->
                            courseOne.getId().compareTo(courseTwo.getId());
                }
            }
            return comparator;


        }
    }





