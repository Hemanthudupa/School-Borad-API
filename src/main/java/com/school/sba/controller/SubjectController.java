package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.requestdto.SubjectRequestDTO;
import com.school.sba.responnsedto.SubjectResponseDTO;
import com.school.sba.serviceimpl.SubjectService;
import com.school.sba.util.ResponseStructure;

@RestController
public class SubjectController {
	@Autowired
	private SubjectService service;

	@PostMapping("/academic-programs/{programId}/subjects")
	public ResponseEntity<ResponseStructure<SubjectResponseDTO>> addSubjects(@PathVariable int programId,
			@RequestBody SubjectRequestDTO subjectRequestDTO) {
		return service.addSubjects(programId, subjectRequestDTO);
	}
}
