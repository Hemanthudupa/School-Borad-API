package com.school.sba.serviceimpl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.ClassHour;
import com.school.sba.entity.Schedule;
import com.school.sba.exceptions.ScheduleNotFoundByIDException;
import com.school.sba.exceptions.ScheduledAlreadyPresentException;
import com.school.sba.exceptions.SchoolNotFoundByIdException;
import com.school.sba.repository.ScheduleRepo;
import com.school.sba.repository.SchoolRepo;
import com.school.sba.requestdto.ScheduleRequestDTO;
import com.school.sba.responnsedto.ClassHourResponseDTO;
import com.school.sba.responnsedto.ScheduleResponseDTO;
import com.school.sba.service.ScheduleService;
import com.school.sba.util.ResponseStructure;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	private ScheduleRepo scheduleRepo;

	@Autowired
	private SchoolRepo schoolRepo;

	@Autowired
	private ResponseStructure<ScheduleResponseDTO> structure;

	public Schedule mapToSchedule(ScheduleRequestDTO request) {
		return Schedule.builder().opensAt(request.getOpensAt()).closesAt(request.getClosesAt())
				.classHoursPerDay(request.getClassHoursPerDay())
				.classHourLength(Duration.ofMinutes(request.getClassHourLengthInMinutes()))
				.breakTime(request.getBreakTime()).breakLength(Duration.ofMinutes(request.getBreakLengthInMinutes()))
				.lunchTime(request.getLunchTime()).lunchLength(Duration.ofMinutes(request.getLunchLengthInMinutes()))
				.build();
	}

	public ScheduleResponseDTO mapToScheduleResponse(Schedule schedule) {

		return ScheduleResponseDTO.builder().scheduleId(schedule.getScheduleId()).opensAt(schedule.getOpensAt())
				.closesAt(schedule.getClosesAt()).classHoursPerDay(schedule.getClassHoursPerDay())
				.classHourLengthInMinutes((int) schedule.getClassHourLength().toMinutes())
				.breakLengthInMinutes((int) schedule.getBreakLength().toMinutes())
				.breakLengthInMinutes((int) schedule.getBreakLength().toMinutes())
				.lunchLengthInMinutes((int) schedule.getLunchLength().toMinutes()).lunchTime(schedule.getLunchTime())
				.breakTime(schedule.getBreakTime()).build();

	}

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponseDTO>> saveSchedule(int schoolId,
			ScheduleRequestDTO scheduleRequestDTO) {
		return schoolRepo.findById(schoolId).map(school -> {
			if (school.getSchedule() == null) {
				Schedule schedule = mapToSchedule(scheduleRequestDTO);
				Schedule save = scheduleRepo.save(schedule);
				school.setSchedule(schedule);
				schoolRepo.save(school);
				structure.setData(mapToScheduleResponse(save));
				structure.setMessage("successfully schedule added ");
				structure.setStatus(HttpStatus.CREATED.value());
				return new ResponseEntity<ResponseStructure<ScheduleResponseDTO>>(structure, HttpStatus.CREATED);
			} else {
				throw new ScheduledAlreadyPresentException("Scheduled alredy present in the school");
			}
		}).orElseThrow(() -> new SchoolNotFoundByIdException("School not found "));
	}

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponseDTO>> findScheduleBySchool(int schoolId) {
		return schoolRepo.findById(schoolId).map(school -> {
			structure.setData(mapToScheduleResponse(school.getSchedule()));
			structure.setMessage("schedule find!!!");
			structure.setStatus(HttpStatus.FOUND.value());
			return new ResponseEntity<ResponseStructure<ScheduleResponseDTO>>(structure, HttpStatus.FOUND);
		}).orElseThrow(() -> new SchoolNotFoundByIdException("invalid id !!! school is not there "));
	}

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponseDTO>> updateSchedule(int scheduleId,
			ScheduleRequestDTO scheduleRequestDTO) {
		return scheduleRepo.findById(scheduleId).map(schedule -> {

			Schedule scheduleObj = mapToSchedule(scheduleRequestDTO);

			schedule.setBreakLength(scheduleObj.getBreakLength());
			schedule.setBreakTime(scheduleObj.getBreakTime());
			schedule.setClassHourLength(scheduleObj.getClassHourLength());
			schedule.setClassHoursPerDay(scheduleObj.getClassHoursPerDay());
			schedule.setClosesAt(scheduleObj.getClosesAt());
			schedule.setOpensAt(scheduleObj.getOpensAt());
			schedule.setLunchLength(scheduleObj.getLunchLength());
			schedule.setLunchTime(scheduleObj.getLunchTime());

			structure.setData(mapToScheduleResponse(scheduleRepo.save(schedule)));
			structure.setMessage("schedule updated !!!");
			structure.setStatus(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<ScheduleResponseDTO>>(structure, HttpStatus.OK);
		}).orElseThrow(() -> new ScheduleNotFoundByIDException(" invalid id !!!"));
	}

	public ResponseEntity<ResponseStructure<List<ScheduleResponseDTO>>> deleteClassHour(List<Schedule> schedules) {
		ArrayList<ScheduleResponseDTO> aList = new ArrayList<>();
		schedules.forEach(schedule -> {
			scheduleRepo.delete(schedule);
			aList.add(mapToScheduleResponse(schedule));
		});
		ResponseStructure<List<ScheduleResponseDTO>> structure = new ResponseStructure<>();
		structure.setData(aList);
		structure.setMessage("deleted successfully");
		structure.setStatus(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<ResponseStructure<List<ScheduleResponseDTO>>>(structure, HttpStatus.OK);
	}

}
