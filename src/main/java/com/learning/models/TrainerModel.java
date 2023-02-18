package com.learning.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerModel {

    private Long id;
    private String name;
    private String specialization;

}



