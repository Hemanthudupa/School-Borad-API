package com.school.sba.entity;

import java.time.LocalTime;

import com.school.sba.enums.ClassStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
public class ClassHour {
	private LocalTime beginsAtLocalTime;
	private LocalTime endsALocalTime;
	private int roomNo;
	private ClassStatus classStatus;

	@ManyToOne
	private Subject subject;
	@ManyToOne
	private AcademicProgram academicProgram;
}
