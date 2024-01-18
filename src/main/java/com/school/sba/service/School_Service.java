package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.entity.School;
import com.school.sba.requestdto.SchoolRequestDTO;
import com.school.sba.responnsedto.SchoolResponseDTO;
import com.school.sba.util.ResponseStructure;

public interface School_Service {
	public ResponseEntity<ResponseStructure<SchoolResponseDTO>> saveSchool(int userId, SchoolRequestDTO requestDto);

	public School update(School school);

	public String delete(int id);

	public List<School> getAll();

	public School getSchoolByID(int id);
}
