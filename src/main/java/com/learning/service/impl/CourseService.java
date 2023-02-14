package com.learning.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Id;

import com.learning.constants.NumberConstant;
import com.learning.entity.StudentEntity;
import com.learning.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.learning.entity.BatchEntity;
import com.learning.entity.CourseEntity;
import com.learning.models.BatchModel;
import com.learning.models.CourseModel;
import com.learning.repository.CourseRepository;
import com.learning.service.CommonService;

@Service
@RequiredArgsConstructor
public class CourseService implements CommonService<CourseModel, Long>{
		
		private final CourseRepository courseRepo;
	    private final ModelMapper modelMapper;
		


		@Override
		public List<CourseModel> getAllRecords() {
			List<CourseEntity> courseEntityList = courseRepo.findAll();
			if (!CollectionUtils.isEmpty(courseEntityList)) {
				List<CourseModel> courseModelList = courseEntityList.stream()
						.map(courseEntity -> {
							CourseModel courseModel = new CourseModel();
							modelMapper.map(courseEntity, courseModel);
							return courseModel;
						})
						.collect(Collectors.toList());
				return courseModelList;
			} else {
				return Collections.emptyList();
			}
		}

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
		public CourseModel saveRecord(CourseModel courseModel) {
			if (Objects.nonNull(courseModel)) {
				CourseEntity courseEntity = new CourseEntity();
				BeanUtils.copyProperties(courseModel, courseEntity);
			    CourseEntity savedObject =  courseRepo.save(courseEntity);
			}
			return courseModel;
			}

		@Override
		public List<CourseModel> saveAll(List<CourseModel> courseModelList) {

			if (Objects.nonNull(courseModelList) && courseModelList.size() > NumberConstant.ZERO) {
				List<CourseEntity> courseEntityList = courseModelList.stream()
						.map(courseModel -> {
							CourseEntity entity = modelMapper.map(courseModel,CourseEntity.class);
							return entity;
						})
						.collect(Collectors.toList());
				courseRepo.saveAll(courseEntityList);
			}
			return courseModelList;


		}

		@Override
		public CourseModel getRecordById(Long id) {
		Optional<CourseEntity> optionalEntity = courseRepo.findById(id);
		if (optionalEntity.isPresent()) {
			CourseEntity courseEntity = optionalEntity.get();
			CourseModel courseModel = modelMapper.map(courseEntity, CourseModel.class);
			return courseModel;
			}
		throw new DataNotFoundException("data not found"+id);
		}

		@Override
		public void deleteRecordById(Long id) {
			courseRepo.deleteById(id);
			
		}

		@Override
		public CourseModel updateRecordById(Long id, CourseModel record) {
			
				
				Optional<CourseEntity> optionalCourseEntity = courseRepo.findById(id);
				if (optionalCourseEntity.isPresent()) {
					CourseEntity courseEntity = optionalCourseEntity.get();
					modelMapper.map(record, courseEntity);

					courseRepo.save(courseEntity);
				}
				return record;
			
		}
		

		private Comparator<CourseEntity> findSuitableComparator(String sortBy) {
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
				default : {
					comparator = (courseOne, courseTwo) ->
					courseOne.getId().compareTo(courseTwo.getId());
				}
			}
			return comparator;
		}


}


