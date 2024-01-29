package com.school.sba.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.school.sba.enums.ClassStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassHour {
	private LocalDateTime beginsAtLocalTime;
	private LocalDateTime endsALocalTime;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int classHourID;
	private int roomNo;
	@Enumerated(EnumType.STRING)
	private ClassStatus classStatus;

	@ManyToOne
	private Subject subject;
	@ManyToOne
	private AcademicProgram academicProgram;

	@ManyToOne
	private User user;
}
