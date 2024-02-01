package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.SchoolRequestDTO;
import com.school.sba.responnsedto.SchoolResponseDTO;
import com.school.sba.util.ResponseStructure;

public interface School_Service {
	public ResponseEntity<ResponseStructure<SchoolResponseDTO>> saveSchool(int userId, SchoolRequestDTO requestDto);

	public ResponseEntity<ResponseStructure<SchoolResponseDTO>> delete(int id);

	public void permanentDelete();

}
