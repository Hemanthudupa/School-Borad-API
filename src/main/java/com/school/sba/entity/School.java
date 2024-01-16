package com.school.sba.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class School {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int schoolId;
	private String shoolName;
	private long contactNo;
	private String emailId;
	private String address;
	@OneToOne
	private Schedule schedule;
}
