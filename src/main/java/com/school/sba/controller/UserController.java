package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.requestdto.UserRequestDTO;
import com.school.sba.responnsedto.UserResponseDTO;
import com.school.sba.service.User_Service;
import com.school.sba.util.ResponseStructure;

@RestController
public class UserController {
	@Autowired
	private User_Service service;

	@PostMapping("/users/register")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> regesterUser(@RequestBody UserRequestDTO user) {
		return service.regesterUser(user);

	}

	@GetMapping("/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> findUserById(@PathVariable int userId) {
		return service.findUserById(userId);
	}

	@DeleteMapping("/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteByUserId(@PathVariable int userId) {
		return service.deleteByUserId(userId);
	}

	@PutMapping("/subjects/{subjectId}/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponseDTO>> addSubject(@PathVariable int subjectId,
			@PathVariable int userId) {
		return service.addSubject(subjectId, userId);
	}
}
