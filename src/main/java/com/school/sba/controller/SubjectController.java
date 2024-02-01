package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.requestdto.SubjectRequestDTO;
import com.school.sba.responnsedto.AcademicsProgramResponseDto;
import com.school.sba.responnsedto.SubjectResponseDTO;
import com.school.sba.service.SubjectService;
import com.school.sba.util.ResponseStructure;

@RestController
public class SubjectController {
	@Autowired
	private SubjectService service;

	@PostMapping("/academic-programs/{programId}/subjects")
	public ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>> addSubjects(@PathVariable int programId,
			@RequestBody SubjectRequestDTO subjectRequestDTO) {
		return service.addSubjects(programId, subjectRequestDTO);
	}

	@PutMapping("/academic-programs/{programId}")
	public ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>> updateSubjects(@PathVariable int programId,

			@RequestBody SubjectRequestDTO subjectRequestDTO) {

		return service.updateSubjects(programId, subjectRequestDTO);
	}

	@GetMapping("/subjects")
	public ResponseEntity<ResponseStructure<List<SubjectResponseDTO>>> findAllSubjects() {
		return service.findAllSubjects();
	}

	@DeleteMapping("/subjects/{subjectId}")
	ResponseEntity<ResponseStructure<SubjectResponseDTO>> delete(int id) {
		return service.delete(id);
	}

}
