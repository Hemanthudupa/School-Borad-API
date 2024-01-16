package com.school.sba.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;
import com.school.sba.exceptions.ContraintsValidationException;
import com.school.sba.exceptions.ExistingAdminException;
import com.school.sba.exceptions.UserNotFoundByIdException;
import com.school.sba.repository.User_Repo;
import com.school.sba.requestdto.RequestDTO;
import com.school.sba.requestdto.ResponseDTO;
import com.school.sba.service.User_Service;
import com.school.sba.util.ResponseStructure;

@Service
public class User_ServiceImpl implements User_Service {
	@Autowired
	private ResponseStructure<ResponseDTO> structure;

	@Autowired
	private User_Repo repo;

	static int count = 0;

	private User mapToUser(RequestDTO request) {

		return User.builder().email(request.getEmail()).contactNo(request.getContactNo())
				.firstName(request.getFirstName()).lastName(request.getLastName()).password(request.getPassword())
				.userName(request.getUserName()).userRole(request.getUserRole()).isDelete(false).build();
	}

	private ResponseDTO mapToUserResponse(User user) {
		return ResponseDTO.builder().contactNo(user.getContactNo()).email(user.getEmail())
				.firstName(user.getFirstName()).lastName(user.getLastName()).userId(user.getUserId())
				.userName(user.getUserName()).userRole(user.getUserRole()).build();
	}

	public ResponseEntity<ResponseStructure<ResponseDTO>> getStructure(HttpStatus status, String message, Object data) {
		structure.setData(mapToUserResponse((User) data));
		structure.setMessage("user added successfully ");
		structure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<ResponseDTO>>(structure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<ResponseDTO>> regesterUser(RequestDTO user) {
		try {
			if (user.getUserRole() == UserRole.ADMIN) {
				if (repo.existsByUserRole(user.getUserRole()) != true) {
					User userEntity = repo.save(mapToUser(user));
					count++;

					return new ResponseEntity<ResponseStructure<ResponseDTO>>(
							new ResponseStructure<ResponseDTO>(HttpStatus.OK.value(), "user added successfully ",
									mapToUserResponse(userEntity)),
							HttpStatus.OK);
				} else {
					throw new ExistingAdminException("ADMIN alredy existed ");
				}
			}
		} catch (Exception e) {
			throw new ContraintsValidationException("no duplicate values");
		}
		User userEntity = repo.save(mapToUser(user));
		return new ResponseEntity<ResponseStructure<ResponseDTO>>(new ResponseStructure<ResponseDTO>(
				HttpStatus.OK.value(), "user added successfully ", mapToUserResponse(userEntity)), HttpStatus.CREATED);

	}

	@Override
	public ResponseEntity<ResponseStructure<ResponseDTO>> findUserById(int userId) {
		User user = repo.findById(userId).map(user2 -> {
			return user2;
		}).orElseThrow(() -> new UserNotFoundByIdException("user not found "));
		structure.setData(mapToUserResponse(user));
		structure.setMessage("USER FOUND ");
		structure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<ResponseDTO>> deleteByUserId(int userId) {
		User user1 = repo.findById(userId).map(user -> {
			user.setDelete(true);
			return user;
		}).orElseThrow(() -> new UserNotFoundByIdException("user not found by ID "));
		repo.save(user1);
		structure.setData(mapToUserResponse(user1));
		structure.setMessage("user updated ");
		structure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<ResponseDTO>>(structure, HttpStatus.OK);
	}

}
