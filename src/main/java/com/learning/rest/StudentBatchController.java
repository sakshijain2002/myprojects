package com.learning.rest;

import com.learning.models.StudentBatchModel;
import com.learning.service.impl.StudentBatchService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/studentBatch")

public class StudentBatchController {
    private final StudentBatchService studentBatchService;

    public StudentBatchController(StudentBatchService studentBatchService) {
        super();
        this.studentBatchService = studentBatchService;
    }

    @GetMapping
    public List<StudentBatchModel> getAllStudent() {
        return studentBatchService.getAllRecords();
    }

    @GetMapping("get-records")
    public List<StudentBatchModel> getAllRecords(@RequestParam(value = "count", required = false, defaultValue = "0") int count, @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
        if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
            return studentBatchService.getAllRecords();
        } else if (count > 0) {
            return studentBatchService.getLimitedRecords(count);
        } else {
            return studentBatchService.getSortedRecords(sortBy);
        }
    }

    @PostMapping("/all")
    public List<StudentBatchModel> save(@RequestBody List<StudentBatchModel> studentBatchModelList) {
        try {
            if (studentBatchModelList.size() == 1) {
                return Arrays.asList(studentBatchService.saveRecord(studentBatchModelList.get(0)));
            } else {
                return studentBatchService.saveAll(studentBatchModelList);
            }
        } catch (Exception exception) {
            System.out.println("Exception Occurs in StudentBatchController || saveAll");
            System.err.print(exception);
            return Collections.emptyList();
        }
    }

    @PostMapping
    public StudentBatchModel save(@RequestBody StudentBatchModel studentBatchModel) {
        return studentBatchService.saveRecord(studentBatchModel);
    }

    @PutMapping("/{id}")
    public StudentBatchModel updateById(@PathVariable Long id, @RequestBody StudentBatchModel studentBatchModel) {
        return studentBatchService.updateRecordById(id, studentBatchModel);
    }
    @PostMapping("/mongo")
    public StudentBatchModel saveRecordInMongo(@RequestBody StudentBatchModel studentBatchModel) {
        return studentBatchService.saveRecordINMongo(studentBatchModel);
    }


    @DeleteMapping()
    public void deleteRecordById(Long id) {
        studentBatchService.deleteRecordById(id);
    }
}


