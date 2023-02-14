package com.learning.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Objects;

import javax.persistence.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="timeSlot")
public class TimeSlotEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)

	private Long id;
	private LocalTime startTime;
	private LocalTime endTime;
	private Long trainerId;

}
