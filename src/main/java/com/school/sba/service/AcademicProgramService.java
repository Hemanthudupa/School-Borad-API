package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.AcademicProgramRequestDto;
import com.school.sba.responnsedto.AcademicsProgramResponseDto;
import com.school.sba.util.ResponseStructure;

public interface AcademicProgramService {

	ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>> addAcademicsProgram(int schoolId,
			AcademicProgramRequestDto academicProgramRequestDto);

	ResponseEntity<ResponseStructure<List<AcademicsProgramResponseDto>>> getAllAcademicProgarm(int schoolId);

	ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>> assignUser(int programId, int userId);

	ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>> deleteAcademicProgram(int programId);

	void permanentDeleteAP();

}
