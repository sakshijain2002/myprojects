package com.learning.service.impl;

import com.learning.constants.NumberConstant;
import com.learning.entity.TimeSlotCollection;
import com.learning.entity.TimeSlotEntity;
import com.learning.exception.DataNotFoundException;
import com.learning.models.TimeSlotModel;
import com.learning.repository.TimeSlotMongoRepo;
import com.learning.repository.TimeSlotRepository;
import com.learning.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TimeSlotService implements CommonService<TimeSlotModel, Long> {

    private final TimeSlotRepository timeSlotRepo;
    private final ModelMapper modelMapper;
    private final TimeSlotMongoRepo timeSlotMongoRepo;


    @Override
    public List<TimeSlotModel> getAllRecords() {
        List<TimeSlotEntity> timeSlotEntityList = timeSlotRepo.findAll();
        if (!CollectionUtils.isEmpty(timeSlotEntityList)) {
            List<TimeSlotModel> timeSlotModelList = timeSlotEntityList.stream().map(timeSlotEntity -> {
                TimeSlotModel timeSlotModel = new TimeSlotModel();
                modelMapper.map(timeSlotEntity, timeSlotModel);
                return timeSlotModel;
            }).collect(Collectors.toList());
            return timeSlotModelList;
        } else {
            return Collections.emptyList();
        }
    }


    @Override
    public List<TimeSlotModel> getLimitedRecords(int count) {

        List<TimeSlotEntity> timeSlotEntityList = timeSlotRepo.findAll();
        if (Objects.nonNull(timeSlotEntityList) && timeSlotEntityList.size() > NumberConstant.ZERO) {
            List<TimeSlotModel> timeSlotModelList = timeSlotEntityList.stream().limit(count)
                    .map(timeSlotEntity -> {
                        TimeSlotModel timeSlotModel = modelMapper.map(timeSlotEntity, TimeSlotModel.class);
                        return timeSlotModel;
                    }).collect(Collectors.toList());
            return timeSlotModelList;
        } else {
            return Collections.emptyList();
        }

    }

    @Override
    public List<TimeSlotModel> getSortedRecords(String sortBy) {

        List<TimeSlotEntity> timeSlotEntityList = timeSlotRepo.findAll();
        if (Objects.nonNull(timeSlotEntityList) && timeSlotEntityList.size() > NumberConstant.ZERO) {
            Comparator<TimeSlotEntity> comparator = findSuitableComparator(sortBy);
            List<TimeSlotModel> timeSlotModelList = timeSlotEntityList.stream().sorted(comparator)
                    .map(timeSlotEntity -> {
                        TimeSlotModel timeSlotModel = modelMapper.map(timeSlotEntity, TimeSlotModel.class);
                        return timeSlotModel;
                    }).collect(Collectors.toList());
            return timeSlotModelList;
        } else {
            return Collections.emptyList();

        }
    }


    @Override
    public TimeSlotModel saveRecord(TimeSlotModel timeSlotModel) {
        if (Objects.nonNull(timeSlotModel)) {
            TimeSlotEntity timeSlotEntity = new TimeSlotEntity();
            modelMapper.map(timeSlotModel, timeSlotEntity);
            timeSlotRepo.save(timeSlotEntity);
        }
        return timeSlotModel;
    }
    public TimeSlotModel saveRecordInMongo(TimeSlotModel timeSlotModel) {
        if (Objects.nonNull(timeSlotModel)) {
            TimeSlotCollection timeSlotCollection = new TimeSlotCollection();
            modelMapper.map(timeSlotModel, timeSlotCollection);
            timeSlotMongoRepo.save(timeSlotCollection);
        }
        return timeSlotModel;
    }

    @Override
    public List<TimeSlotModel> saveAll(List<TimeSlotModel> timeSlotModelList) {

        if (Objects.nonNull(timeSlotModelList) && timeSlotModelList.size() > NumberConstant.ZERO) {
            List<TimeSlotEntity> timeSlotEntityList = timeSlotModelList.stream().map(timeSlotModel -> {
                TimeSlotEntity entity = modelMapper.map(timeSlotModel, TimeSlotEntity.class);
                return entity;
            }).collect(Collectors.toList());
            timeSlotRepo.saveAll(timeSlotEntityList);
        }
        return timeSlotModelList;

    }

    @Override
    public TimeSlotModel getRecordById(Long id) {
        Optional<TimeSlotEntity> optionalEntity = timeSlotRepo.findById(id);
        if (optionalEntity.isPresent()) {
            TimeSlotEntity timeSlotEntity = optionalEntity.get();
            TimeSlotModel timeSlotModel = modelMapper.map(timeSlotEntity, TimeSlotModel.class);
            return timeSlotModel;
        }
        throw new DataNotFoundException("data not found " + id);


    }

    @Override
    public void deleteRecordById(Long id) {
        timeSlotRepo.deleteById(id);
    }
    @Override
    public TimeSlotModel updateRecordById(Long id, TimeSlotModel record) {
        Optional<TimeSlotEntity> optionalTimeSlotEntity = timeSlotRepo.findById(id);
        if (optionalTimeSlotEntity.isPresent()) {
            TimeSlotEntity timeSlotEntity = optionalTimeSlotEntity.get();
            modelMapper.map(record, timeSlotEntity);
            timeSlotEntity.setId(id);
            timeSlotRepo.save(timeSlotEntity);
        }
        return record;
    }


    private Comparator<TimeSlotEntity> findSuitableComparator(String sortBy) {
        Comparator<TimeSlotEntity> comparator;
        switch (sortBy) {
            case "startTime": {
                comparator = (timeSlotOne, timeSlotTwo) -> timeSlotOne.getStartTime()
                        .compareTo(timeSlotTwo.getStartTime());
                break;
            }
            case "endTime": {
                comparator = (timeSlotOne, timeSlotTwo) -> timeSlotOne.getEndTime()
                        .compareTo(timeSlotTwo.getEndTime());
                break;
            }
            case "trainerId": {
                comparator = (timeSlotOne, timeSlotTwo) -> timeSlotOne.getTrainerId()
                        .compareTo(timeSlotTwo.getTrainerId());
                break;
            }
            default: {
                comparator = (timeSlotOne, timeSlotTwo) -> timeSlotOne.getId()
                        .compareTo(timeSlotTwo.getId());
            }
        }
        return comparator;

    }
}

		






