package com.learning.models;

import com.learning.enums.BatchStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchModel {


    private Long id;
    private Integer studentCount;
    private LocalDate startDate;
    private LocalDate endDate;
    private BatchStatus batchStatus;
    private Long courseId;
    private Long timeSlotId;


}