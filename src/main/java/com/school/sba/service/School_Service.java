package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.entity.School;
import com.school.sba.responsestructure.School_ResponseStructure;

public interface School_Service {
	public ResponseEntity<School_ResponseStructure<School>> save(School school);

	public School update(School school);

	public String delete(int id);

	public List<School> getAll();

	public School getSchoolByID(int id);
}
