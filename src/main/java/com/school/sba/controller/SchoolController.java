package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.entity.School;
import com.school.sba.requestdto.SchoolRequestDTO;
import com.school.sba.requestdto.UserRequestDTO;
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

	@PostMapping("/update")
	public School update(@RequestBody School school) {
		return service.update(school);
	}

	@PostMapping("/delete")
	public String delete(@RequestParam int id) {
		return service.delete(id);
	}

	@GetMapping("/getAll")
	public List<School> getAll() {
		return service.getAll();
	}

}
