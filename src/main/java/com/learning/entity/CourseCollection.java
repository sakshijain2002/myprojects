package com.learning.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Document(collection = "course")
public class CourseCollection {
    @Id
    private Long id;
    private String name;
    private String curriculum;
    private String duration;
}
