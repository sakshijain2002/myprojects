package com.learning.rest;

import com.learning.models.CourseModel;
import com.learning.service.impl.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        super();
        this.courseService = courseService;
    }

    @GetMapping
    public List<CourseModel> getAllCourse() {
        return courseService.getAllRecords();

    }

    @GetMapping("get-records")
    public List<CourseModel> getAllRecords(@RequestParam(value = "count", required = false, defaultValue = "0") int count, @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
        if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
            return courseService.getAllRecords();
        } else if (count > 0) {
            return courseService.getLimitedRecords(count);
        } else {
            return courseService.getSortedRecords(sortBy);
        }
    }

    @PostMapping("/all")
    public List<CourseModel> save(@RequestBody List<CourseModel> courseModelList) {
        try {
            if (courseModelList.size() == 1) {
                return Arrays.asList(courseService.saveRecord(courseModelList.get(0)));
            } else {
                return courseService.saveAll(courseModelList);
            }
        } catch (Exception exception) {
            System.out.println("Exception Occurs in StudentController || saveAll");
            System.err.print(exception);
            return Collections.emptyList();
        }
    }

    @PostMapping
    public CourseModel save(@RequestBody CourseModel courseModel) {
        return courseService.saveRecord(courseModel);

    }

    @PutMapping("/{id}")
    public CourseModel updateById(@PathVariable Long id, @RequestBody CourseModel courseModel) {
        return courseService.updateRecordById(id, courseModel);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        courseService.deleteRecordById(id);

    }

}
