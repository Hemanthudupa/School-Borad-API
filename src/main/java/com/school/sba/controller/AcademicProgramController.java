package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.requestdto.AcademicProgramRequestDto;
import com.school.sba.responnsedto.AcademicsProgramResponseDto;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.util.ResponseStructure;

@RestController
public class AcademicProgramController {

	@Autowired
	private AcademicProgramService service;

	@PostMapping("/schools/{schoolId}/academic-programs")
	public ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>> addAcademicsProgram(
			@PathVariable int schoolId, @RequestBody AcademicProgramRequestDto academicProgramRequestDto) {
		return service.addAcademicsProgram(schoolId, academicProgramRequestDto);
	}

	@GetMapping("/schools/{schoolId}/academic-programs")
	public ResponseEntity<ResponseStructure<List<AcademicsProgramResponseDto>>> getAllAcademicProgarm(
			@PathVariable int schoolId) {
		return service.getAllAcademicProgarm(schoolId);
	}

	@PutMapping("/academic-programs/{programId}/users/{userId}")
	public ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>> addUser(@PathVariable int programId,
			@PathVariable int userId) {
		return service.addUser(programId, userId);
	}
}
