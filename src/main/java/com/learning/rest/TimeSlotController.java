package com.learning.rest;

import com.learning.models.TimeSlotModel;
import com.learning.service.impl.TimeSlotService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/timeSlot")
public class TimeSlotController {
    private final TimeSlotService timeSlotService;

    public TimeSlotController(TimeSlotService timeSlotService) {
        super();
        this.timeSlotService = timeSlotService;
    }

    @GetMapping
    public List<TimeSlotModel> getAllTimeSlot() {
        return timeSlotService.getAllRecords();
    }

    @GetMapping("get-records")
    public List<TimeSlotModel> getAllRecords(@RequestParam(value = "count", required = false, defaultValue = "0") int count, @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
        if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
            return timeSlotService.getAllRecords();
        } else if (count > 0) {
            return timeSlotService.getLimitedRecords(count);
        } else {
            return timeSlotService.getSortedRecords(sortBy);
        }
    }


    @PostMapping
    public TimeSlotModel save(@RequestBody TimeSlotModel timeSlotModel) {
        TimeSlotModel timeSlotModel1 = timeSlotService.saveRecord(timeSlotModel);
        return timeSlotModel1;
    }

    @PostMapping("/all")
    public List<TimeSlotModel> save(@RequestBody List<TimeSlotModel> timeSlotModelList) {
        try {
            if (timeSlotModelList.size() == 1) {
                return Arrays.asList(timeSlotService.saveRecord(timeSlotModelList.get(0)));
            } else {
                return timeSlotService.saveAll(timeSlotModelList);
            }
        } catch (Exception exception) {
            System.out.println("Exception Occurs in TimeSlotController || saveAll");
            System.err.print(exception);
            return Collections.emptyList();
        }
    }

    @PutMapping("/{id}")
    public TimeSlotModel updateById(@PathVariable Long id, @RequestBody TimeSlotModel timeSlotModel) {
        return timeSlotService.updateRecordById(id, timeSlotModel);
    }


    @DeleteMapping("/{id}")
    public void deleteRecordById(@PathVariable Long id) {
        timeSlotService.deleteRecordById(id);
    }


}
