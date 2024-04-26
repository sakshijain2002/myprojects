package com.learning.rest;

import com.learning.models.StudentModel;
import com.learning.models.TrainerModel;
import com.learning.service.impl.TrainerService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/trainer")
public class TrainerController {


    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        super();
        this.trainerService = trainerService;
    }

    @GetMapping
    public List<TrainerModel> getAllTrainer() {
        return trainerService.getAllRecords();
    }

    @GetMapping("get-records")
    public List<TrainerModel> getAllRecords(@RequestParam(value = "count", required = false, defaultValue = "0") int count, @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
        if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
            return trainerService.getAllRecords();
        } else if (count > 0) {
            return trainerService.getLimitedRecords(count);
        } else {
            return trainerService.getSortedRecords(sortBy);
        }
    }

    @PostMapping("/all")
    public List<TrainerModel> save(@RequestBody List<TrainerModel> trainerModelList) {
        try {
            if (trainerModelList.size() == 1) {
                return Arrays.asList(trainerService.saveRecord(trainerModelList.get(0)));
            } else {
                return trainerService.saveAll(trainerModelList);
            }
        } catch (Exception exception) {
            System.out.println("Exception Occurs in StudentController || saveAll");
            System.err.print(exception);
            return Collections.emptyList();
        }
    }

    @PostMapping
    public TrainerModel save(@RequestBody TrainerModel trainerModel) {
        return trainerService.saveRecord(trainerModel);
    }

    @PutMapping("/{id}")
    public TrainerModel updateById(@PathVariable Long id, @RequestBody TrainerModel trainerModel) {
        return trainerService.updateRecordById(id, trainerModel);
    }


    @DeleteMapping()
    public void deleteRecordById(Long id) {
        trainerService.deleteRecordById(id);
    }
    @PostMapping("/mongo")
    public TrainerModel saveRecordInMongo(@RequestBody TrainerModel trainerModel) {
        return trainerService.saveRecordInMongo(trainerModel);
    }
}



