package com.learning.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.learning.models.StudentModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import com.learning.models.BatchModel;
import com.learning.models.CourseModel;
import com.learning.service.impl.BatchService;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchController {


	private final BatchService batchService;


	@GetMapping
	public List<BatchModel> getAllBatch() {
		return batchService.getAllRecords();
	}

	@GetMapping("get-records")
	public List<BatchModel> getAllRecords (@RequestParam(value = "count" ,required = false , defaultValue = "0") int count, @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
		if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
			return batchService.getAllRecords();
		} else if (count > 0) {
			return batchService.getLimitedRecords(count);
		} else {
			return batchService.getSortedRecords(sortBy);
		}
	}

	@PostMapping()
	public BatchModel save(@RequestBody BatchModel batchModel) {
		return batchService.saveRecord(batchModel);

	}
	@PostMapping("/all")
	public List<BatchModel> save(@RequestBody List<BatchModel> batchModelList) {
		try {
			if (batchModelList.size() == 1) {
				return Arrays.asList(batchService.saveRecord(batchModelList.get(0)));
			} else {
				return batchService.saveAll(batchModelList);
			}
		} catch (Exception exception) {
			System.out.println("Exception Occurs in BatchController || saveAll");
			System.err.print(exception);
			return Collections.emptyList();
		}
	}

	@PutMapping("/{id}")
	public BatchModel updateById(@PathVariable Long id, @RequestBody BatchModel batchModel) {
		return batchService.updateRecordById(id, batchModel);
	}


	@DeleteMapping("/{id}")
	public void deleteRecordById(@PathVariable Long id) {
		batchService.deleteRecordById(id);
	}

//	@PostMapping("/upload")
//	public String uploadExcelFile(@RequestParam(("file")) MultipartFile file) {
//		BatchService.saveExcelFile(file);
//		return "file uploaded successfully";
	}




	
	
	


