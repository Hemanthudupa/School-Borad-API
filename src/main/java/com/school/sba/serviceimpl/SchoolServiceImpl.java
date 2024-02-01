package com.school.sba.serviceimpl;

import java.util.List;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.School;
import com.school.sba.enums.UserRole;
import com.school.sba.exceptions.SchoolAlreadyPresentException;
import com.school.sba.exceptions.SchoolNotFoundByIdException;
import com.school.sba.exceptions.UnauthorizedAccessSchoolException;
import com.school.sba.exceptions.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.SchoolRepo;
import com.school.sba.repository.UserRepo;
import com.school.sba.requestdto.SchoolRequestDTO;
import com.school.sba.responnsedto.SchoolResponseDTO;
import com.school.sba.service.School_Service;
import com.school.sba.util.ResponseStructure;

@Service
public class SchoolServiceImpl implements School_Service {
	@Autowired
	SchoolRepo schoolRepo;

	@Autowired
	UserRepo userRepo;

	@Autowired
	ResponseStructure<SchoolResponseDTO> structure;

	@Autowired
	AcademicProgramRepo academicProgramRepo;

	public School mapToSchool(SchoolRequestDTO requestDTO) {
		return School.builder().address(requestDTO.getAddress()).schoolName(requestDTO.getSchoolName())
				.contactNo(requestDTO.getContactNo()).emailId(requestDTO.getEmailId()).build();
	}

	public SchoolResponseDTO mapToresponse(School school) {
		return SchoolResponseDTO.builder().address(school.getAddress()).contactNo(school.getContactNo())
				.emailId(school.getEmailId()).shoolName(school.getSchoolName()).isDeleted(school.isDeleted())
				.schoolId(school.getSchoolId()).build();

	}

	public ResponseStructure<SchoolResponseDTO> getStructure(School school) {
		structure.setData(mapToresponse(school));
		structure.setMessage("school sussessfully added ");
		structure.setStatus(HttpStatus.CREATED.value());
		return structure;
	}

	public ResponseEntity<ResponseStructure<SchoolResponseDTO>> saveSchool(int userID, SchoolRequestDTO requestDTO) {
		return userRepo.findById(userID).map(user -> {
			if (user.getUserRole().equals(UserRole.ADMIN)) {
				if (user.getSchool() == null) {
					School mapToSchool = mapToSchool(requestDTO);
					School savedSchool = schoolRepo.save(mapToSchool);
					structure.setData(mapToresponse(savedSchool));
					user.setSchool(savedSchool);
					userRepo.save(user);
					structure.setMessage("school sussessfully added ");
					structure.setStatus(HttpStatus.CREATED.value());
					return new ResponseEntity<ResponseStructure<SchoolResponseDTO>>(structure, HttpStatus.CREATED);
				} else {
					throw new SchoolAlreadyPresentException("school already present in the given user");
				}
			} else {
				throw new UnauthorizedAccessSchoolException("Only Admin can create school");
			}
		}).orElseThrow(() -> new UserNotFoundByIdException("user not found with given ID"));

	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponseDTO>> delete(int id) {
		return schoolRepo.findById(id).map(school -> {
			if (school.isDeleted()==false) {
				school.setDeleted(true);
				schoolRepo.save(school);
			}

			structure.setData(mapToresponse(school));
			structure.setMessage("deleted successfully !!!");
			structure.setStatus(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<SchoolResponseDTO>>(structure, HttpStatus.OK);
		}).orElseThrow(() -> new SchoolNotFoundByIdException("Invalid school ID!!!!"));
	}

	@Override
	public void permanentDelete() {
		schoolRepo.findByIsDeleted(true).forEach(school -> {
			school.getAcademicProgram().forEach(program -> {
				program.setSchool(null);
				academicProgramRepo.save(program);
			});
			userRepo.findBySchool(school).forEach(user -> {
				user.setSchool(null);
				userRepo.save(user);
			});

			schoolRepo.delete(school);
		});
	}

}