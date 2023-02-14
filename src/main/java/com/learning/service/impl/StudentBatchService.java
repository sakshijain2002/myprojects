package com.learning.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Id;

import com.learning.constants.NumberConstant;
import com.learning.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import com.learning.entity.CourseEntity;
import com.learning.entity.StudentBatchEntity;
import com.learning.models.StudentBatchModel;
import com.learning.repository.StudentBatchRepository;
import com.learning.service.CommonService;


	@Service
	@RequiredArgsConstructor
	public class StudentBatchService implements CommonService<StudentBatchModel, Long> {

		private final StudentBatchRepository studentBatchRepo;
		private final ModelMapper modelMapper;


		@Override
		public List<StudentBatchModel> getAllRecords() {
			List<StudentBatchEntity> studentBatchEntityList = studentBatchRepo.findAll();
			if (!CollectionUtils.isEmpty(studentBatchEntityList)) {
				List<StudentBatchModel> studentBatchModelList = studentBatchEntityList.stream().map(studentBatchEntity -> {
					StudentBatchModel studentBatchModel = new StudentBatchModel();
					modelMapper.map(studentBatchEntity, studentBatchModel);
					return studentBatchModel;
				}).collect(Collectors.toList());
				return studentBatchModelList;
			} else {
				return Collections.emptyList();
			}
		}

		@Override
		public List<StudentBatchModel> getLimitedRecords(int count) {

			List<StudentBatchEntity> studentBatchEntityList = studentBatchRepo.findAll();
			if (Objects.nonNull(studentBatchEntityList) && studentBatchEntityList.size() > NumberConstant.ZERO) {
				List<StudentBatchModel> studentBatchModelList = studentBatchEntityList.stream().limit(count)
						.map(studentBatchEntity -> {
							StudentBatchModel studentBatchModel = modelMapper.map(studentBatchEntity, StudentBatchModel.class);
							return studentBatchModel;
						}).collect(Collectors.toList());
				return studentBatchModelList;
			} else {
				return Collections.emptyList();
			}

		}

		@Override
		public List<StudentBatchModel> getSortedRecords(String sortBy) {

			List<StudentBatchEntity> studentBatchEntityList = studentBatchRepo.findAll();
			if (Objects.nonNull(studentBatchEntityList) && studentBatchEntityList.size() > NumberConstant.ZERO) {
				Comparator<StudentBatchEntity> comparator = findSuitableComparator(sortBy);
				List<StudentBatchModel> studentBatchModelList = studentBatchEntityList.stream().sorted(comparator)
						.map(studentBatchEntity -> {
							StudentBatchModel studentBatchModel = modelMapper.map(studentBatchEntity, StudentBatchModel.class);
							return studentBatchModel;
						}).collect(Collectors.toList());
				return studentBatchModelList;
			} else {
				return Collections.emptyList();
			}

		}

		@Override
		public StudentBatchModel saveRecord(StudentBatchModel studentBatchModel) {
			if (Objects.nonNull(studentBatchModel)) {
				StudentBatchEntity studentBatchEntity = new StudentBatchEntity();
				BeanUtils.copyProperties(studentBatchModel, studentBatchEntity);
				StudentBatchEntity savedObject = studentBatchRepo.save(studentBatchEntity);
			}
			return studentBatchModel;
		}

		@Override
		public List<StudentBatchModel> saveAll(List<StudentBatchModel> studentBatchModelList) {

			if (Objects.nonNull(studentBatchModelList) && studentBatchModelList.size() > NumberConstant.ZERO) {
				List<StudentBatchEntity> studentBatchEntityList = studentBatchModelList.stream().map(studentBatchModel -> {
					StudentBatchEntity entity = modelMapper.map(studentBatchModel, StudentBatchEntity.class);
					return entity;
				}).collect(Collectors.toList());
				studentBatchRepo.saveAll(studentBatchEntityList);
			}
			return studentBatchModelList;

		}

		@Override
		public StudentBatchModel getRecordById(Long id) {

			Optional<StudentBatchEntity> optionalEntity = studentBatchRepo.findById(id);
			if (optionalEntity.isPresent()) {
				StudentBatchEntity studentBatchEntity = optionalEntity.get();
				StudentBatchModel studentBatchModel = modelMapper.map(studentBatchEntity, StudentBatchModel.class);
				return studentBatchModel;
			}
			throw new DataNotFoundException("data not found"+id);

		}

		@Override
		public void deleteRecordById(Long id) {
			studentBatchRepo.deleteById(id);
		}

		@Override
		public StudentBatchModel updateRecordById(Long id, StudentBatchModel record) {
			Optional<StudentBatchEntity> optionalStudentBatchEntity = studentBatchRepo.findById(id);
			if (optionalStudentBatchEntity.isPresent()) {
				StudentBatchEntity studentBatchEntity = optionalStudentBatchEntity.get();
				modelMapper.map(record, studentBatchEntity);

				studentBatchRepo.save(studentBatchEntity);
			}
			return record;
		}


		private Comparator<StudentBatchEntity> findSuitableComparator(String sortBy) {
			Comparator<StudentBatchEntity> comparator;
			switch (sortBy) {
				case "fees": {
					comparator = (studentBatchOne, studentBatchTwo) -> studentBatchOne.getFees()
							.compareTo(studentBatchTwo.getFees());
					break;
				}
				case "batchId": {
					comparator = (studentBatchOne, studentBatchTwo) -> studentBatchOne.getBatchId()
							.compareTo(studentBatchTwo.getBatchId());
					break;
				}
				case "studentId": {
					comparator = (studentBatchOne, studentBatchTwo) -> studentBatchOne.getStudentId()
							.compareTo(studentBatchTwo.getStudentId());
					break;
				}
				default: {
					comparator = (studentBatchOne, studentBatchTwo) -> studentBatchOne.getId()
							.compareTo(studentBatchTwo.getId());
				}
			}
			return comparator;

		}
	}

		





