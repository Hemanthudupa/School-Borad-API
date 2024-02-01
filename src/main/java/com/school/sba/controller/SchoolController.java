package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.requestdto.SchoolRequestDTO;
import com.school.sba.responnsedto.SchoolResponseDTO;
import com.school.sba.service.School_Service;
import com.school.sba.util.ResponseStructure;

@RestController
public class SchoolController {
	@Autowired
	School_Service service;

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/users/{userId}/schools")
	public ResponseEntity<ResponseStructure<SchoolResponseDTO>> saveSchool(@PathVariable int userId,
			@RequestBody SchoolRequestDTO requestDto) {
		return service.saveSchool(userId, requestDto);

	}

	@DeleteMapping("/schools/{schoolId}")
	public ResponseEntity<ResponseStructure<SchoolResponseDTO>> delete(@PathVariable int schoolId) {
		return service.delete(schoolId);
	}

}
