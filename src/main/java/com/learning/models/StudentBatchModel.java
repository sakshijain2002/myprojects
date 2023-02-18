package com.learning.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentBatchModel {
    private Long id;
    private Double fees;
    private Long studentId;
    private Long batchId;


}

	


