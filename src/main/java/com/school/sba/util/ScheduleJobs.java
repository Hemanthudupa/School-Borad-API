package com.school.sba.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.ClassHourRepo;
import com.school.sba.repository.SchoolRepo;
import com.school.sba.repository.UserRepo;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.service.School_Service;

import jakarta.transaction.Transactional;

@Component
public class ScheduleJobs {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ClassHourRepo classHourRepo;

	@Autowired
	private AcademicProgramRepo academicProgramRepo;

	@Autowired
	private SchoolRepo schoolRepo;

	@Autowired
	private AcademicProgramService academicProgramService;

	@Autowired
	private School_Service school_Service;
	
//	@Scheduled(fixedDelay = 1000L * 60)
//	@Transactional
//	public void deleteFromUser() {
//		userRepo.findAllByIsDelete(true).forEach(user -> {
//			user.getAchaAcademicPrograms().forEach(program -> {
////				program.setUsers(null);
////				academicProgramRepo.save(program);
//			});
//			classHourRepo.findByUser(user).forEach(classHour -> {
//				classHour.setUser(null);
//				classHourRepo.save(classHour);
//			});
//			userRepo.delete(user);
//		});
//	}

//	@Transactional
//	@Scheduled(fixedDelay = 1000 * 60L)
//	public void deleteFromAcademicProgram() {
//		academicProgramService.permanentDeleteAP();
//	}
	
	@Transactional
	@Scheduled(fixedDelay = 1000*60L)
	public void deleteFromSchool()
	{
		school_Service.permanentDelete();
		
	}

}