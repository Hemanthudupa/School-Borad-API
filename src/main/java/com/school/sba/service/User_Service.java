package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.RequestDTO;
import com.school.sba.requestdto.ResponseDTO;
import com.school.sba.util.ResponseStructure;


public interface User_Service {

	ResponseEntity<ResponseStructure<ResponseDTO>> regesterUser(RequestDTO user);

	ResponseEntity<ResponseStructure<ResponseDTO>> findUserById(int userId);

	ResponseEntity<ResponseStructure<ResponseDTO>> deleteByUserId(int userId);

}
