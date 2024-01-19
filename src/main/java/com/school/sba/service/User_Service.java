package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.UserRequestDTO;
import com.school.sba.responnsedto.UserResponseDTO;
import com.school.sba.util.ResponseStructure;

public interface User_Service {

	ResponseEntity<ResponseStructure<UserResponseDTO>> regesterUser(UserRequestDTO user);

	ResponseEntity<ResponseStructure<UserResponseDTO>> findUserById(int userId);

	ResponseEntity<ResponseStructure<UserResponseDTO>> deleteByUserId(int userId);

	ResponseEntity<ResponseStructure<UserResponseDTO>> addSubject(int subjectId, int userId);

}
