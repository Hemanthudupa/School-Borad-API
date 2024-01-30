package com.school.sba.serviceimpl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.ClassHour;
import com.school.sba.entity.Schedule;
import com.school.sba.enums.ClassStatus;
import com.school.sba.exceptions.AcademicProgramNotFoundByIdException;
import com.school.sba.exceptions.ClassRoomNotFreeException;
import com.school.sba.exceptions.InvalidClassHourIdException;
import com.school.sba.exceptions.InvalidUserRoleException;
import com.school.sba.exceptions.ScheduleNotFoundInSchoolException;
import com.school.sba.exceptions.SubjectNotFoundExceptionByID;
import com.school.sba.exceptions.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.ClassHourRepo;
import com.school.sba.repository.SubjectRepo;
import com.school.sba.repository.UserRepo;
import com.school.sba.requestdto.ClassHourRequestDTO;
import com.school.sba.responnsedto.ClassHourResponseDTO;
import com.school.sba.service.ClassHourService;
import com.school.sba.util.ResponseStructure;

@Service
public class ClassHourServiceImpl implements ClassHourService {

	@Autowired
	private AcademicProgramRepo academicProgramRepo;

	@Autowired
	private ClassHourRepo classHourRepo;

	@Autowired
	private SubjectRepo subjectRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ResponseStructure<List<ClassHourResponseDTO>> responseStructure;

	public ClassHourResponseDTO mapToClassHourResponse(ClassHour classHour) {
		return ClassHourResponseDTO.builder().beginsAtLocalTime(classHour.getBeginsAtLocalTime())
				.classHourId(classHour.getClassHourID()).classStatus(classHour.getClassStatus())
				.endsALocalTime(classHour.getEndsALocalTime()).roomNumber(classHour.getRoomNo())
				.subject(classHour.getSubject()).build();

	}

	public boolean isLunchTime(LocalDateTime currentTime, Schedule schedule) {
		LocalTime lunchTimeStart = schedule.getLunchTime();
		LocalTime lunchTimeEnd = lunchTimeStart.plusMinutes(schedule.getLunchLength().toMinutes());
		return currentTime.toLocalTime().isAfter(lunchTimeStart) && currentTime.toLocalTime().isBefore(lunchTimeEnd);
	}

	public boolean isBreakTime(LocalDateTime currentTime, Schedule schedule) {

		LocalTime breakTimeStart = schedule.getBreakTime();
		LocalTime breakTimeEnd = breakTimeStart.plusMinutes(schedule.getBreakLength().toMinutes());

		return currentTime.toLocalTime().isAfter(breakTimeStart) && currentTime.toLocalTime().isBefore(breakTimeEnd);
	}

	public ClassHourResponseDTO mapToResponseDTO(ClassHour classHour) {
		return ClassHourResponseDTO.builder().beginsAtLocalTime(classHour.getBeginsAtLocalTime())
				.classHourId(classHour.getClassHourID()).classStatus(classHour.getClassStatus())
				.endsALocalTime(classHour.getEndsALocalTime()).roomNumber(classHour.getRoomNo()).build();
	}

	@Override
	public ResponseEntity<ResponseStructure<List<ClassHourResponseDTO>>> generateClassHour(int programID) {

		return academicProgramRepo.findById(programID).map(program -> {
			Schedule schedule = program.getSchool().getSchedule();
			List<ClassHourResponseDTO> responses = new ArrayList<>();

			if (schedule != null) {
				long classHoursInMinutes = schedule.getClassHourLength().toMinutes();
				int classHoursPerDay = schedule.getClassHoursPerDay();

				LocalTime closesAt = schedule.getClosesAt();

				LocalDateTime currentTime = LocalDateTime.now().with(schedule.getOpensAt());

				LocalTime lunchTimeStart = schedule.getLunchTime();
				LocalTime lunchTimeEnd = lunchTimeStart.plusMinutes(schedule.getLunchLength().toMinutes());

				LocalTime breakTimestart = schedule.getBreakTime();

				LocalTime breakTimeEnd = breakTimestart.plusMinutes(schedule.getBreakLength().toMinutes());

				for (int day = 1; day <= 6; day++) {
					for (int hour = 0; hour <= classHoursPerDay + 2; hour++) {
						ClassHour classHour = new ClassHour();
						if (currentTime.toLocalTime().isBefore(closesAt)
								&& !currentTime.toLocalTime().equals(closesAt)) {
							LocalDateTime beginsAt = currentTime;
							LocalDateTime endsAt = currentTime.plusMinutes(classHoursInMinutes);

							if (!currentTime.toLocalTime().equals(lunchTimeStart)
									&& !isLunchTime(currentTime, schedule)) {
								if (!currentTime.toLocalTime().equals(breakTimestart)
										&& !isBreakTime(currentTime, schedule)) {
									classHour.setBeginsAtLocalTime(beginsAt);
									classHour.setEndsALocalTime(endsAt);
									classHour.setClassStatus(ClassStatus.NOT_SCHEDULED);

									currentTime = endsAt;

								} else {
									classHour.setBeginsAtLocalTime(breakTimestart.atDate(currentTime.toLocalDate()));
									classHour.setEndsALocalTime(breakTimeEnd.atDate(currentTime.toLocalDate()));
									currentTime = breakTimeEnd.atDate(currentTime.toLocalDate());

									classHour.setClassStatus(ClassStatus.BREAK_TIME);

								}
							} else {
								classHour.setBeginsAtLocalTime(lunchTimeStart.atDate(currentTime.toLocalDate()));
								classHour.setEndsALocalTime(lunchTimeEnd.atDate(currentTime.toLocalDate()));
								currentTime = lunchTimeEnd.atDate(currentTime.toLocalDate());

								classHour.setClassStatus(ClassStatus.LUNCH_TIME);
							}
							classHour.setAcademicProgram(program);

							responses.add(mapToResponseDTO(classHourRepo.save(classHour)));

						}
					}
					currentTime = currentTime.plusDays(1).with(schedule.getOpensAt());

				}
			} else {
				throw new ScheduleNotFoundInSchoolException("schedule not found !!!");
			}
			responseStructure.setData(responses);
			responseStructure.setMessage("Added Successfully !!!");
			responseStructure.setStatus(HttpStatus.OK.value());

			return new ResponseEntity<ResponseStructure<List<ClassHourResponseDTO>>>(responseStructure, HttpStatus.OK);
		}).orElseThrow(() -> new AcademicProgramNotFoundByIdException("Academic Program Not Found !!!"));
	}

	@Override
	public ResponseEntity<ResponseStructure<List<ClassHourResponseDTO>>> updateClassHour(
			ArrayList<ClassHourRequestDTO> classHours) {
		ArrayList<ClassHourResponseDTO> alist = new ArrayList<>();
		if (!classHours.isEmpty()) {
			classHours.forEach(classHour -> {
				classHourRepo.findById(classHour.getClassHourId()).map(classHourdb -> {
					if (!classHourRepo.existsByBeginsAtLocalTimeAndRoomNo(classHourdb.getBeginsAtLocalTime(),
							classHour.getRoomNo())) {
						subjectRepo.findById(classHour.getSubjectId()).map(subject -> {

							if (LocalDateTime.now().isBefore(classHourdb.getEndsALocalTime())
									&& LocalDateTime.now().isAfter(classHourdb.getBeginsAtLocalTime())) {
								classHourdb.setRoomNo(classHour.getRoomNo());
								classHourdb.setSubject(subject);
								classHourdb.setClassStatus(ClassStatus.ONGOING);
							} else if (classHourdb.getBeginsAtLocalTime().toLocalTime().equals(userRepo
									.findById(classHour.getUserId()).get().getSchool().getSchedule().getLunchTime())) {
								classHourdb.setClassStatus(ClassStatus.LUNCH_TIME);
								classHourdb.setSubject(null);

							} else if (classHourdb.getBeginsAtLocalTime().toLocalTime().equals(userRepo
									.findById(classHour.getUserId()).get().getSchool().getSchedule().getBreakTime())) {
								classHourdb.setClassStatus(ClassStatus.BREAK_TIME);
								classHourdb.setSubject(null);

							} else if (classHourdb.getBeginsAtLocalTime().isBefore(LocalDateTime.now())) {
								classHourdb.setRoomNo(classHour.getRoomNo());
								classHourdb.setSubject(subject);
								classHourdb.setClassStatus(ClassStatus.COMPLETED);
							} else if (classHourdb.getBeginsAtLocalTime().isAfter(LocalDateTime.now())) {
								classHourdb.setRoomNo(classHour.getRoomNo());
								classHourdb.setSubject(subject);
								classHourdb.setClassStatus(ClassStatus.UPCOMING);
							}
							return classHourdb;
						}).orElseThrow(() -> new SubjectNotFoundExceptionByID("invalid Subject ID  !!!"));
					} else {
						throw new ClassRoomNotFreeException(" not free !!!");
					}

					userRepo.findById(classHour.getUserId()).map(user -> {

						if (user.getUserRole().toString().equals("TEACHER")) {

							classHourdb.setUser(user);
							return classHourdb;
						} else {
							throw new InvalidUserRoleException("invalid User role !!!TEACHER required ");
						}
					}).orElseThrow(() -> new UserNotFoundByIdException("invalid ID!!!"));
					alist.add(mapToClassHourResponse(classHourRepo.save(classHourdb)));
					responseStructure.setData(alist);
					responseStructure.setMessage("updated successuflly ");
					responseStructure.setStatus(HttpStatus.ACCEPTED.value());
					return classHourdb;
				}).orElseThrow(() -> new InvalidClassHourIdException("invalid ID"));
			});
		}
		return new ResponseEntity<ResponseStructure<List<ClassHourResponseDTO>>>(responseStructure, HttpStatus.OK);
	}
}
