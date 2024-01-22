package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.SubjectRequestDTO;
import com.school.sba.responnsedto.AcademicsProgramResponseDto;
import com.school.sba.responnsedto.SubjectResponseDTO;
import com.school.sba.util.ResponseStructure;

public interface SubjectService {

	ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>> addSubjects(int programId,
			SubjectRequestDTO subjectRequestDTO);

	ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>> updateSubjects(int programId,
			SubjectRequestDTO subjectRequestDTO);

	ResponseEntity<ResponseStructure<List<SubjectResponseDTO>>> findAllSubjects();

}
