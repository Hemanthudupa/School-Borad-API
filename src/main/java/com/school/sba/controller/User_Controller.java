package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.entity.User;
import com.school.sba.requestdto.RequestDTO;
import com.school.sba.requestdto.ResponseDTO;
import com.school.sba.service.User_Service;
import com.school.sba.util.ResponseStructure;

@RestController
public class User_Controller {
	@Autowired
	private User_Service service;

	@PostMapping("/users/register")
	public ResponseEntity<ResponseStructure<ResponseDTO>> regesterUser(@RequestBody RequestDTO user) {
		return service.regesterUser(user);

	}

	@GetMapping("/users/{userId}")
	public ResponseEntity<ResponseStructure<ResponseDTO>> findUserById(@PathVariable int userId) {
		return service.findUserById(userId);
	}

	@DeleteMapping("/users/{userId}")
	public ResponseEntity<ResponseStructure<ResponseDTO>> deleteByUserId(@PathVariable int userId) {
		return service.deleteByUserId(userId);
	}
}
