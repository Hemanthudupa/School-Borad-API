package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.requestdto.ScheduleRequestDTO;
import com.school.sba.responnsedto.ScheduleResponseDTO;
import com.school.sba.service.ScheduleService;
import com.school.sba.util.ResponseStructure;

@RestController
public class ScheduleController {

	@Autowired
	ScheduleService scheduleService;

	@PostMapping("/schools/{schoolId}/schedules")
	public ResponseEntity<ResponseStructure<ScheduleResponseDTO>> saveSchedule(@PathVariable int schoolId,
			@RequestBody ScheduleRequestDTO scheduleRequestDTO) {
		return scheduleService.saveSchedule(schoolId, scheduleRequestDTO);
	}

	@GetMapping("/schools/{schoolId}/schedules")
	public ResponseEntity<ResponseStructure<ScheduleResponseDTO>> findScheduleBySchool(@PathVariable int schoolId) {
		return scheduleService.findScheduleBySchool(schoolId);
	}

	@PutMapping("/schedules/{scheduleId}")
	public ResponseEntity<ResponseStructure<ScheduleResponseDTO>> updateSchedule(@PathVariable int scheduleId,
			@RequestBody ScheduleRequestDTO scheduleRequestDTO) {
		return scheduleService.updateSchedule(scheduleId, scheduleRequestDTO);
	}
}
