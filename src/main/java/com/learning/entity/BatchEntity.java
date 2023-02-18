package com.learning.entity;

import com.learning.enums.BatchStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "batch")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
    private Integer studentCount;
    private LocalDate startDate;
    private LocalDate endDate;
    @Enumerated(value = EnumType.STRING)
    private BatchStatus batchStatus;
    private Long courseId;
    private Long timeSlotId;
}






