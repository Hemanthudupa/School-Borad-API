package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.entity.School;
import com.school.sba.responsestructure.School_ResponseStructure;
import com.school.sba.service.School_Service;

@RestController
public class School_Controller {
	@Autowired
	School_Service service;

	@PostMapping("/save")
	public ResponseEntity<School_ResponseStructure<School>> save(@RequestBody School school) {
		return service.save(school);

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
