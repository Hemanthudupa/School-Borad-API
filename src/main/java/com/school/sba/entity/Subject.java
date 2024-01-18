package com.school.sba.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
public class Subject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int subjectId;
	private String subjectName;

}
