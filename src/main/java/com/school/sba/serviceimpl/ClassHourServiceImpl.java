package com.school.sba.serviceimpl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.ClassHour;
import com.school.sba.entity.Schedule;
import com.school.sba.entity.School;
import com.school.sba.enums.ClassStatus;
import com.school.sba.exceptions.AcademicProgramNotFoundByIdException;
import com.school.sba.exceptions.ClassHoursNotPresentException;
import com.school.sba.exceptions.ClassRoomNotFreeException;
import com.school.sba.exceptions.DuplicateClassHoursException;
import com.school.sba.exceptions.InvalidClassHourIdException;
import com.school.sba.exceptions.InvalidUserRoleException;
import com.school.sba.exceptions.ScheduleNotFoundInSchoolException;
import com.school.sba.exceptions.SchoolNotAddedToAcademicProgramException;
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

	public ClassHour mapToClassHour(ClassHour classHour) {
		return ClassHour.builder().academicProgram(classHour.getAcademicProgram())
				.beginsAtLocalTime(classHour.getBeginsAtLocalTime().plusWeeks(1))
				.classStatus(classHour.getClassStatus()).endsALocalTime(classHour.getEndsALocalTime().plusWeeks(1))
				.roomNo(classHour.getRoomNo()).subject(classHour.getSubject()).user(classHour.getUser()).build();
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

//	public ClassHourResponseDTO mapToResponseDTO(ClassHour classHour) {
//		return ClassHourResponseDTO.builder().beginsAtLocalTime(classHour.getBeginsAtLocalTime())
//				.classHourId(classHour.getClassHourID()).classStatus(classHour.getClassStatus())
//				.endsALocalTime(classHour.getEndsALocalTime()).roomNumber(classHour.getRoomNo()).build();
//	}

	@Override
	public ResponseEntity<ResponseStructure<List<ClassHourResponseDTO>>> generateClassHour(int programID) {

		return academicProgramRepo.findById(programID).map(program -> {
			School school = program.getSchool();
			if (school == null)
				throw new SchoolNotAddedToAcademicProgramException("school not yet added ");
			Schedule schedule = school.getSchedule();
			List<ClassHourResponseDTO> responses = new ArrayList<>();

			List<ClassHour> classHours = new ArrayList<>();
			if (program.getClassHours().isEmpty()) {
				if (schedule != null) {
					long classHoursInMinutes = schedule.getClassHourLength().toMinutes();
					int classHoursPerDay = schedule.getClassHoursPerDay();

					LocalTime closesAt = schedule.getClosesAt();

					LocalDateTime currentTime = LocalDateTime.now().with(schedule.getOpensAt());

					LocalTime lunchTimeStart = schedule.getLunchTime();
					LocalTime lunchTimeEnd = lunchTimeStart.plusMinutes(schedule.getLunchLength().toMinutes());

					LocalTime breakTimestart = schedule.getBreakTime();

					LocalTime breakTimeEnd = breakTimestart.plusMinutes(schedule.getBreakLength().toMinutes());

					int days = 7 - currentTime.getDayOfWeek().getValue();

					System.out.println(days + " is the number of days ");

					for (int day = 1; day <= 7 + days; day++) {
						System.out.println(currentTime.getDayOfWeek().name() + " is the day of the week");

						if (!currentTime.getDayOfWeek().equals(school.getHolidayOfTheWeek())) {
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
											classHour.setBeginsAtLocalTime(
													breakTimestart.atDate(currentTime.toLocalDate()));
											classHour.setEndsALocalTime(breakTimeEnd.atDate(currentTime.toLocalDate()));
											currentTime = breakTimeEnd.atDate(currentTime.toLocalDate());

											classHour.setClassStatus(ClassStatus.BREAK_TIME);

										}
									} else {
										classHour
												.setBeginsAtLocalTime(lunchTimeStart.atDate(currentTime.toLocalDate()));
										classHour.setEndsALocalTime(lunchTimeEnd.atDate(currentTime.toLocalDate()));
										currentTime = lunchTimeEnd.atDate(currentTime.toLocalDate());

										classHour.setClassStatus(ClassStatus.LUNCH_TIME);
									}
									classHour.setAcademicProgram(program);

									ClassHour savedClassHour = classHourRepo.save(classHour);

									classHours.add(savedClassHour);

									responses.add(mapToClassHourResponse(savedClassHour));

								}
							}
							currentTime = currentTime.plusDays(1).with(schedule.getOpensAt());

						} else {
							currentTime = currentTime.plusDays(1).with(schedule.getOpensAt());
						}

					}
				} else {
					throw new ScheduleNotFoundInSchoolException("schedule not found !!!");
				}
			} else {
				throw new DuplicateClassHoursException(" class hours already present !!!");
			}

			program.setClassHours(classHours);
			academicProgramRepo.save(program);
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

	public ResponseEntity<ResponseStructure<List<ClassHourResponseDTO>>> deleteClassHour(List<ClassHour> classHours) {
		ArrayList<ClassHourResponseDTO> aList = new ArrayList<>();
		classHours.forEach(classHour -> {
			classHourRepo.delete(classHour);
			aList.add(mapToClassHourResponse(classHour));
		});
		responseStructure.setData(aList);
		responseStructure.setMessage("deleted successfully");
		responseStructure.setStatus(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<ResponseStructure<List<ClassHourResponseDTO>>>(responseStructure, HttpStatus.OK);
	}

//	@Override
//	public ResponseEntity<ResponseStructure<List<ClassHourResponseDTO>>> generateClassHourForNextWeek(int programId) {
//		academicProgramRepo.findById(programId).map(program -> {
//			LocalDateTime endsALocalTime = program.getClassHours().getLast().getEndsALocalTime();
//
//			LocalDateTime monday = endsALocalTime.minusDays(5).minusHours(8).minusMinutes(30);
//			LocalDateTime saturday = endsALocalTime;
//
//			List<ClassHour> findByEndsALocalTimeBetween = classHourRepo.findByEndsALocalTimeBetween(monday, saturday);
//
//			ArrayList<ClassHourResponseDTO> alist = new ArrayList<>();
//
//			findByEndsALocalTimeBetween.forEach(find -> {
//				alist.add(mapToClassHourResponse(classHourRepo.save(mapToClassHour(find))));
//			});
//			responseStructure.setData(alist);
//			responseStructure.setMessage("next week data classhour !!!");
//			responseStructure.setStatus(HttpStatus.CREATED.value());
//			return new ResponseEntity<ResponseStructure<List<ClassHourResponseDTO>>>(responseStructure,
//					HttpStatus.CREATED);
//
//		});
//		return new ResponseEntity<ResponseStructure<List<ClassHourResponseDTO>>>(responseStructure, HttpStatus.CREATED);
//
//	}

	@Override
	public void generateClassHourForNextWeek(int programId) throws ClassHoursNotPresentException {
		ArrayList<ClassHour> alist = new ArrayList<>();
		academicProgramRepo.findById(programId).map(program -> {
			if (!program.getClassHours().isEmpty()) {
				LocalDateTime endsALocalTime = program.getClassHours().getLast().getEndsALocalTime();

				LocalDateTime monday = endsALocalTime.minusDays(5).minusHours(8).minusMinutes(30);
				LocalDateTime saturday = endsALocalTime;

				List<ClassHour> findByEndsALocalTimeBetween = classHourRepo.findByEndsALocalTimeBetween(monday,
						saturday);

				findByEndsALocalTimeBetween.forEach(classHour -> {

					alist.add(mapToClassHour(classHour));
				});
			} else {
				throw new ClassHoursNotPresentException("class hours is Empty");
			}
			classHourRepo.saveAll(alist);

			return program;
		}).orElseThrow(() -> new AcademicProgramNotFoundByIdException("Invalid ID!!!!"));

	}
}
