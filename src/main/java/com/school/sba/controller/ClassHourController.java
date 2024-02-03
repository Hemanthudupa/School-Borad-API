package com.school.sba.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.requestdto.ClassHourRequestDTO;
import com.school.sba.responnsedto.ClassHourResponseDTO;
import com.school.sba.service.ClassHourService;
import com.school.sba.util.ResponseStructure;

@RestController
public class ClassHourController {
	@Autowired
	ClassHourService service;

	@PostMapping("/academic-program/{programId}/class-hours")
	public ResponseEntity<ResponseStructure<List<ClassHourResponseDTO>>> generateClassHour(
			@PathVariable int programId) {
		return service.generateClassHour(programId);
	}

	@PutMapping("/class-hours")
	public ResponseEntity<ResponseStructure<List<ClassHourResponseDTO>>> updateClassHour(
			@RequestBody ArrayList<ClassHourRequestDTO> classHours) {
		return service.updateClassHour(classHours);
	}

	@PostMapping("/academic-program/{programId}")
	public void generateClassHourForNextWeek(@PathVariable int programId) {
		service.generateClassHourForNextWeek(programId);
	}
}
