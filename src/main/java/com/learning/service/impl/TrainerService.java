package com.learning.service.impl;

import com.learning.constants.NumberConstant;
import com.learning.entity.TrainerEntity;
import com.learning.exception.DataNotFoundException;
import com.learning.models.TrainerModel;
import com.learning.repository.TrainerRepository;
import com.learning.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TrainerService implements CommonService<TrainerModel, Long> {
    private final TrainerRepository trainerRepo;
    private final ModelMapper modelMapper;


    @Override
    public List<TrainerModel> getAllRecords() {
        List<TrainerEntity> trainerEntityList = trainerRepo.findAll();
        if (!CollectionUtils.isEmpty(trainerEntityList)) {
            List<TrainerModel> trainerModelList = trainerEntityList.stream().map(trainerEntity -> {
                TrainerModel trainerModel = new TrainerModel();
                modelMapper.map(trainerEntity, trainerModel);
                return trainerModel;
            }).collect(Collectors.toList());

            return trainerModelList;
        } else {
            return Collections.emptyList();
        }

    }

    @Override
    public List<TrainerModel> getLimitedRecords(int count) {

        List<TrainerEntity> trainerEntityList = trainerRepo.findAll();
        if (Objects.nonNull(trainerEntityList) && trainerEntityList.size() > NumberConstant.ZERO) {
            List<TrainerModel> trainerModelList = trainerEntityList.stream()
                    .limit(count)
                    .map(trainerEntity -> {
                        TrainerModel trainerModel = modelMapper.map(trainerEntity, TrainerModel.class);
                        return trainerModel;
                    }).collect(Collectors.toList());
            return trainerModelList;
        } else {
            return Collections.emptyList();
        }

    }


    @Override
    public List<TrainerModel> getSortedRecords(String sortBy) {

        List<TrainerEntity> trainerEntityList = trainerRepo.findAll();
        if (Objects.nonNull(trainerEntityList) && trainerEntityList.size() > NumberConstant.ZERO) {
            Comparator<TrainerEntity> comparator = findSuitableComparator(sortBy);
            List<TrainerModel> trainerModelList = trainerEntityList.stream()
                    .sorted(comparator)
                    .map(trainerEntity -> {
                        TrainerModel trainerModel = modelMapper.map(trainerEntity, TrainerModel.class);
                        return trainerModel;
                    }).collect(Collectors.toList());
            return trainerModelList;
        } else {
            return Collections.emptyList();

        }
    }


    @Override
    public TrainerModel saveRecord(TrainerModel trainerModel) {
        if (Objects.nonNull(trainerModel)) {
            TrainerEntity trainerEntity = new TrainerEntity();
            //BeanUtils.copyProperties(trainerModel, trainerEntity);
            modelMapper.map(trainerModel, TrainerEntity.class);
            TrainerEntity savedObject = trainerRepo.save(trainerEntity);
        }
        return trainerModel;
    }

    @Override
    public List<TrainerModel> saveAll(List<TrainerModel> trainerModelList) {
        if (Objects.nonNull(trainerModelList) && trainerModelList.size() > NumberConstant.ZERO) {
            List<TrainerEntity> trainerEntityList = trainerModelList.stream()
                    .map(trainerModel -> {
                        TrainerEntity entity = modelMapper.map(trainerModel, TrainerEntity.class);
                        return entity;
                    })
                    .collect(Collectors.toList());
            trainerRepo.saveAll(trainerEntityList);
        }
        return trainerModelList;

    }


    @Override
    public TrainerModel getRecordById(Long id) {
        Optional<TrainerEntity> optionalEntity = trainerRepo.findById(id);
        if (optionalEntity.isPresent()) {
            TrainerEntity trainerEntity = optionalEntity.get();
            TrainerModel trainerModel = modelMapper.map(trainerEntity, TrainerModel.class);
            return trainerModel;
        }
        throw new DataNotFoundException("data not found " + id);

    }


    @Override
    public void deleteRecordById(Long id) {
        trainerRepo.deleteById(id);
    }

    @Override
    public TrainerModel updateRecordById(Long id, TrainerModel record) {


        Optional<TrainerEntity> optionalTrainerEntity = trainerRepo.findById(id);
        if (optionalTrainerEntity.isPresent()) {
            TrainerEntity trainerEntity = optionalTrainerEntity.get();
            //BeanUtils.copyProperties(record, trainerEntity);
            modelMapper.map(record, trainerEntity);
            trainerEntity.setId(id);
            trainerRepo.save(trainerEntity);
        }
        return record;

    }


    private Comparator<TrainerEntity> findSuitableComparator(String sortBy) {
        Comparator<TrainerEntity> comparator;
        switch (sortBy) {
            case "name": {
                comparator = (trainerOne, trainerTwo) ->
                        trainerOne.getName().compareTo(trainerTwo.getName());
                break;
            }
            case "specialization": {
                comparator = (trainerOne, trainerTwo) ->
                        trainerOne.getSpecialization().compareTo(trainerTwo.getSpecialization());
                break;
            }
            default: {
                comparator = (trainerOne, trainerTwo) ->
                        trainerOne.getId().compareTo(trainerTwo.getId());
            }
        }
        return comparator;
    }
}

		

	






