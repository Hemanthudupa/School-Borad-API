package com.school.sba.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;
import com.school.sba.exceptions.ContraintsValidationException;
import com.school.sba.exceptions.ExistingAdminException;
import com.school.sba.exceptions.SubjectNotFoundExceptionByID;
import com.school.sba.exceptions.SubjectsOnlyAddedToTeacherException;
import com.school.sba.exceptions.UserNotFoundByIdException;
import com.school.sba.repository.SubjectRepo;
import com.school.sba.repository.UserRepo;
import com.school.sba.requestdto.UserRequestDTO;
import com.school.sba.responnsedto.UserResponseDTO;
import com.school.sba.service.User_Service;
import com.school.sba.util.ResponseStructure;

@Service
public class UserServiceImpl implements User_Service {
	@Autowired
	private ResponseStructure<UserResponseDTO> structure;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private SubjectRepo subjectRepo;

	private User mapToUser(UserRequestDTO request) {

		return User.builder().email(request.getEmail()).contactNo(request.getContactNo())
				.firstName(request.getFirstName()).lastName(request.getLastName()).password(request.getPassword())
				.userName(request.getUserName()).userRole(request.getUserRole()).isDelete(false).build();
	}

	private UserResponseDTO mapToUserResponse(User user) {
		return UserResponseDTO.builder().contactNo(user.getContactNo()).email(user.getEmail())
				.firstName(user.getFirstName()).lastName(user.getLastName()).userId(user.getUserId())
				.userName(user.getUserName()).userRole(user.getUserRole()).build();
	}

	public ResponseEntity<ResponseStructure<UserResponseDTO>> getStructure(HttpStatus status, String message,
			Object data) {
		structure.setData(mapToUserResponse((User) data));
		structure.setMessage("user added successfully ");
		structure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> regesterUser(UserRequestDTO user) {

		if (user.getUserRole() == UserRole.ADMIN) {
			if (userRepo.existsByUserRole(user.getUserRole()) != true) {
				User userEntity = null;
				try {
					userEntity = userRepo.save(mapToUser(user));
				} catch (Throwable e) {
					throw new ContraintsValidationException("no duplicate values");
				}
				return new ResponseEntity<ResponseStructure<UserResponseDTO>>(
						new ResponseStructure<UserResponseDTO>(HttpStatus.OK.value(), "user added successfully ",
								mapToUserResponse(userEntity)),
						HttpStatus.OK);
			} else {
				throw new ExistingAdminException("ADMIN alredy existed ");
			}
		}
		User userEntity = null;
		try {
			userEntity = userRepo.save(mapToUser(user));
		} catch (Exception e) {
			throw new ContraintsValidationException("no duplicate values");

		}
		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(new ResponseStructure<UserResponseDTO>(
				HttpStatus.OK.value(), "user added successfully ", mapToUserResponse(userEntity)), HttpStatus.CREATED);

	}

	private User getById(int userId) {
		return userRepo.findById(userId).map(user2 -> {
			return user2;
		}).orElseThrow(() -> new UserNotFoundByIdException("user not found "));
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> findUserById(int userId) {
//		User user = repo.findById(userId).map(user2 -> {
//			return user2;
//		}).orElseThrow(() -> new UserNotFoundByIdException("user not found "));
		User user = getById(userId);
		structure.setData(mapToUserResponse(user));
		structure.setMessage("USER FOUND ");
		structure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> deleteByUserId(int userId) {
//		User user1 = repo.findById(userId).map(user -> {
//			user.setDelete(true);
//			return user;
//		}).orElseThrow(() -> new UserNotFoundByIdException("user not found by ID "));
		User user1 = getById(userId);
		if (user1.isDelete() != true) {
			user1.setDelete(true);
			userRepo.save(user1);
			structure.setData(mapToUserResponse(user1));
			structure.setMessage("user updated ");
			structure.setStatus(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.OK);
		} else {
			structure.setData(null);
			structure.setMessage("user is already deleted ");
			structure.setStatus(HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> addSubject(int subjectId, int userId) {
		return userRepo.findById(userId).map(user -> {
			System.out.println(user.getUserRole());
			if (user.getUserRole().toString().equals("TEACHER")) {
				System.out.println(user.getUserRole());

				subjectRepo.findById(subjectId).map(subject -> {
					user.setSubject(subject);
					userRepo.save(user);
					return user;
				}).orElseThrow(() -> new SubjectNotFoundExceptionByID("invalid Id "));
			} else {
				throw new SubjectsOnlyAddedToTeacherException("subjects only added to Teachers");
			}
			structure.setData(mapToUserResponse(user));
			structure.setMessage(" Subject successfully added to Teacher ");
			structure.setStatus(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.OK);
		}).orElseThrow(() -> new UserNotFoundByIdException("invalid Id "));

	}
}
