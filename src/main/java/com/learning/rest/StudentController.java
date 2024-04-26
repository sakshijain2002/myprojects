package com.learning.rest;

import com.learning.models.StudentModel;
import com.learning.service.impl.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor

public class StudentController {

    private final StudentService studentService;


    @GetMapping("/getall")
    public List<StudentModel> getAllStudent() {
        return studentService.getAllRecords();
    }

    @GetMapping("get-records")
    public List<StudentModel> getAllRecords(@RequestParam(value = "count", required = false, defaultValue = "0") int count, @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
        if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
            return studentService.getAllRecords();
        } else if (count > 0) {
            return studentService.getLimitedRecords(count);
        } else {
            return studentService.getSortedRecords(sortBy);
        }
    }


    @PostMapping("/all")
    public List<StudentModel> save(@RequestBody List<StudentModel> studentModelList) {
        try {
            if (studentModelList.size() == 1) {
                return Arrays.asList(studentService.saveRecord(studentModelList.get(0)));
            } else {
                return studentService.saveAll(studentModelList);
            }
        } catch (Exception exception) {
            System.out.println("Exception Occurs in StudentController || saveAll");
            System.err.print(exception);
            return Collections.emptyList();
        }
    }

    @PostMapping
    public StudentModel save(@RequestBody StudentModel studentModel) {
        return studentService.saveRecord(studentModel);
    }


    @PutMapping("/{id}")
    public StudentModel updateById(@PathVariable Long id, @RequestBody StudentModel studentModel) {
        return studentService.updateRecordById(id, studentModel);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        studentService.deleteRecordById(id);

    }
    @DeleteMapping("/mongo/{id}")
    public void deleteByIdInMongo(@PathVariable Long id) {
        studentService.deleteRecordByIdInMongo(id);

    }

    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file) {
        studentService.saveExcelFile(file);
        return "file uploaded successfully";
    }

    @PostMapping("/sendmail")
    public void sendEmail() {
        studentService.emailSender();
    }

    @PostMapping("/attachment")
    public void sendEmailWithAttachment() {
        studentService.sendEmailWithAttachment();
    }

    @PostMapping("/mongo")
    public StudentModel saveRecordInMongo(@RequestBody StudentModel studentModel) {
        return studentService.saveRecordInMongo(studentModel);
    }
}


