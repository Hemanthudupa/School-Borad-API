package com.school.sba.serviceimpl;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.SubjectRequestDTO;
import com.school.sba.responnsedto.SubjectResponseDTO;
import com.school.sba.util.ResponseStructure;

public interface SubjectService {

	ResponseEntity<ResponseStructure<SubjectResponseDTO>> addSubjects(int programId,
			SubjectRequestDTO subjectRequestDTO);

}
