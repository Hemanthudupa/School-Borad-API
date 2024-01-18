package com.school.sba.serviceimpl;

import java.util.List;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.School;
import com.school.sba.enums.UserRole;
import com.school.sba.exceptions.SchoolAlreadyPresentException;
import com.school.sba.exceptions.UnauthorizedAccessSchoolException;
import com.school.sba.exceptions.UserNotFoundByIdException;
import com.school.sba.repository.School_Repo;
import com.school.sba.repository.User_Repo;
import com.school.sba.requestdto.SchoolRequestDTO;
import com.school.sba.responnsedto.SchoolResponseDTO;
import com.school.sba.service.School_Service;
import com.school.sba.util.ResponseStructure;

@Service
public class School_ServiceImpl implements School_Service {
	@Autowired
	School_Repo repo;

	@Autowired
	User_Repo userRepo;

	@Autowired	
	ResponseStructure<SchoolResponseDTO> structure;

	public School mapToSchool(SchoolRequestDTO requestDTO) {
		return School.builder().address(requestDTO.getAddress()).schoolName(requestDTO.getSchoolName())
				.contactNo(requestDTO.getContactNo()).emailId(requestDTO.getEmailId()).build();
	}

	public SchoolResponseDTO mapToresponse(School school) {
		return SchoolResponseDTO.builder().address(school.getAddress()).contactNo(school.getContactNo())
				.emailId(school.getEmailId()).shoolName(school.getSchoolName()).schoolId(school.getSchoolId()).build();

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
					School savedSchool = repo.save(mapToSchool);
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

	public School update(School school) {
		School orElseThrow = repo.findById(school.getSchoolId())
				.orElseThrow(() -> new ObjectNotFoundException("Object", school));
		orElseThrow.setAddress(school.getAddress());
		orElseThrow.setEmailId(school.getEmailId());
		orElseThrow.setContactNo(school.getContactNo());
		orElseThrow.setSchoolName(school.getSchoolName());
		return repo.save(orElseThrow);
	}

	public String delete(int id) {
		School deleteEntity = repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("not found ", repo));
		repo.delete(deleteEntity);
		return "Object delete successfully ";
	}

	public List<School> getAll() {
		return repo.findAll();
	}

	public School getSchoolByID(int id) {
		return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException(repo, null));
	}
}