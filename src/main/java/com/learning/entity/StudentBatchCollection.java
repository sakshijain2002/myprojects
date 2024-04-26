package com.learning.entity;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
@Data
@Document(collection = "StudentBatch")
public class StudentBatchCollection {
    @Id

    private Long id;
    private Double fees;
    private Long studentId;
    private Long batchId;
}
