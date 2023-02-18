package com.learning.service.impl;

import com.learning.constants.NumberConstant;
import com.learning.entity.BatchEntity;
import com.learning.models.BatchModel;
import com.learning.repository.BatchRepository;
import com.learning.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BatchService implements CommonService<BatchModel, Long> {
    private final BatchRepository batchRepo;
    private final ModelMapper modelMapper;


    @Override
    public List<BatchModel> getAllRecords() {
        List<BatchEntity> batchEntityList = batchRepo.findAll();
        if (!CollectionUtils.isEmpty(batchEntityList)) {
            List<BatchModel> batchModelList = batchEntityList.stream().map(batchEntity -> {
                BatchModel batchModel = new BatchModel();

                //BeanUtils.copyProperties(batchEntity, batchModel);
                modelMapper.map(batchEntity, batchModel);
                return batchModel;
            }).collect(Collectors.toList());
            return batchModelList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<BatchModel> getLimitedRecords(int count) {

        List<BatchEntity> batchEntityList = batchRepo.findAll();
        if (Objects.nonNull(batchEntityList) && batchEntityList.size() > NumberConstant.ZERO) {
            List<BatchModel> batchModelList = batchEntityList.stream().limit(count).map(batchEntity -> {
                BatchModel batchModel = modelMapper.map(batchEntity, BatchModel.class);
                return batchModel;
            }).collect(Collectors.toList());
            return batchModelList;
        } else {
            return Collections.emptyList();
        }
    }


    @Override
    public List<BatchModel> getSortedRecords(String sortBy) {

        List<BatchEntity> batchEntityList = batchRepo.findAll();
        Comparator<BatchEntity> comparator = findSuitableComparator(sortBy);
        if (Objects.nonNull(batchEntityList) && batchEntityList.size() > NumberConstant.ZERO) {
            List<BatchModel> batchModelList = batchEntityList.stream()
                    .sorted(comparator)
                    .map(batchEntity -> {
                        BatchModel batchModel = modelMapper.map(batchEntity, BatchModel.class);
                        return batchModel;
                    }).collect(Collectors.toList());
            return batchModelList;
        } else {
            return Collections.emptyList();
        }


    }

    @Override
    public BatchModel saveRecord(BatchModel batchModel) {
        if (Objects.nonNull(batchModel)) {
            BatchEntity batchEntity = new BatchEntity();
            //BeanUtils.copyProperties(batchModel, batchEntity);
            modelMapper.map(batchModel, batchEntity);
            BatchEntity savedObject = batchRepo.save(batchEntity);
        }
        return batchModel;
    }

    @Override
    public List<BatchModel> saveAll(List<BatchModel> batchModelList) {

        if (Objects.nonNull(batchModelList) && batchModelList.size() > NumberConstant.ZERO) {
            List<BatchEntity> batchEntityList = batchModelList.stream().map(batchModel -> {
                        BatchEntity entity = modelMapper.map(batchModel, BatchEntity.class);
                        return entity;
                    })
                    .collect(Collectors.toList());
            batchRepo.saveAll(batchEntityList);
        }
        return batchModelList;


    }

    @Override
    public BatchModel getRecordById(Long id) {


        Optional<BatchEntity> optionalEntity = batchRepo.findById(id);
        if (optionalEntity.isPresent()) {
            BatchEntity batchEntity = optionalEntity.get();
            BatchModel batchModel = modelMapper.map(batchEntity, BatchModel.class);
            return batchModel;
        }
        throw new IllegalArgumentException("data not found" + id);


    }


    @Override
    public void deleteRecordById(Long id) {
        batchRepo.deleteById(id);

    }

    @Override
    public BatchModel updateRecordById(Long id, BatchModel record) {

        Optional<BatchEntity> optionalBatchEntity = batchRepo.findById(id);
        if (optionalBatchEntity.isPresent()) {
            BatchEntity batchEntity = optionalBatchEntity.get();
            //BeanUtils.copyProperties(record, batchEntity);
            modelMapper.map(record, batchEntity);

            batchRepo.save(batchEntity);
        }
        return record;

    }


    private Comparator<BatchEntity> findSuitableComparator(String sortBy) {
        Comparator<BatchEntity> comparator;
        switch (sortBy) {
            case "startDate": {
                comparator = (batchOne, batchTwo) -> batchOne.getStartDate().compareTo(batchTwo.getStartDate());
                break;
            }
            case "endDate": {
                comparator = (batchOne, batchTwo) -> batchOne.getEndDate().compareTo(batchTwo.getEndDate());
                break;
            }
            case "studentCount": {
                comparator = (batchOne, batchTwo) -> batchOne.getStudentCount().compareTo(batchTwo.getStudentCount());
                break;
            }
            default: {
                comparator = (batchOne, batchTwo) -> batchOne.getId().compareTo(batchTwo.getId());
            }
        }
        return comparator;
    }
}
	



