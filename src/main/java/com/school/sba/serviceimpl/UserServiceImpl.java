package com.school.sba.serviceimpl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;
import com.school.sba.exceptions.AcademicProgramNotFoundByIdException;
import com.school.sba.exceptions.AdminCannotBeAddedToAcademicsProgramException;
import com.school.sba.exceptions.ContraintsValidationException;
import com.school.sba.exceptions.ExistingAdminException;
import com.school.sba.exceptions.SchoolNotFound;
import com.school.sba.exceptions.SubjectNotFoundExceptionByID;
import com.school.sba.exceptions.SubjectsOnlyAddedToTeacherException;
import com.school.sba.exceptions.UserIsNotAnAdminException;
import com.school.sba.exceptions.UserNotFoundByIdException;
import com.school.sba.exceptions.UsersNotFoundInAcademicProgramException;
import com.school.sba.repository.AcademicProgramRepo;
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

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AcademicProgramRepo academicProgramRepo;

	private User mapToUser(UserRequestDTO request) {

		return User.builder().email(request.getEmail()).contactNo(request.getContactNo())
				.firstName(request.getFirstName()).lastName(request.getLastName()).password(request.getPassword())
				.userName(request.getUserName()).userRole(request.getUserRole()).isDelete(false)
				.password(passwordEncoder.encode(request.getPassword())).build();
	}

	public UserResponseDTO mapToUserResponse(User user) {
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
	public ResponseEntity<ResponseStructure<UserResponseDTO>> regesterAdmin(UserRequestDTO user) {

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
		} else {
			throw new UserIsNotAnAdminException("not an admin");
		}

//		User userEntity = null;
//		try {
//			userEntity = userRepo.save(mapToUser(user));
//		} catch (Exception e) {
//			throw new ContraintsValidationException("no duplicate values");
//
//		}
//		return new ResponseEntity<ResponseStructure<UserResponseDTO>>(new ResponseStructure<UserResponseDTO>(
//				HttpStatus.OK.value(), "user added successfully ", mapToUserResponse(userEntity)), HttpStatus.CREATED);
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

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDTO>> addOtherUsers(UserRequestDTO requestDTO) {
		String authenticatedName = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepo.findByUserName(authenticatedName).map(user -> {
			if (user.getSchool() != null) {
				if (requestDTO.getUserRole().equals(UserRole.STUDENT)
						|| requestDTO.getUserRole().equals(UserRole.TEACHER)) {
					User mapToUser = mapToUser(requestDTO);
					mapToUser.setSchool(user.getSchool());

					userRepo.save(mapToUser);

					structure.setData(mapToUserResponse(userRepo.save(mapToUser)));
					structure.setMessage("school added to this user !!!");
					structure.setStatus(HttpStatus.CREATED.value());

				}
			} else {
				throw new SchoolNotFound("school not found ");
			}
			return new ResponseEntity<ResponseStructure<UserResponseDTO>>(structure, HttpStatus.CREATED);
		}).orElseThrow(() -> new UserIsNotAnAdminException("admin required!!!"));

	}

	@Override
	public ResponseEntity<ResponseStructure<List<UserResponseDTO>>> fecthUsersByRole(int programId, String role) {
		ArrayList<UserResponseDTO> alist = new ArrayList<>();
		academicProgramRepo.findById(programId).map(program -> {
			if (program.getUsers() != null) {
				program.getUsers().forEach(user -> {
					if (!role.equalsIgnoreCase("admin")) {

						EnumSet<UserRole> enumSet = EnumSet.allOf(UserRole.class);

						if (enumSet.contains(UserRole.valueOf(role.toUpperCase()))) {
							alist.add(mapToUserResponse(user));
						} else {
							throw new UsersNotFoundInAcademicProgramException(role + " is not found in the academics ");
						}
					} else {
						throw new AdminCannotBeAddedToAcademicsProgramException(
								"admin cannot able present in the acadamics ");
					}
				});
			} else {
				throw new UsersNotFoundInAcademicProgramException("users not present in Academic program");
			}
			return alist;
		}).orElseThrow(() -> new AcademicProgramNotFoundByIdException("Invalid program ID !!"));
		ResponseStructure<List<UserResponseDTO>> structure = new ResponseStructure<>();

		structure.setData(alist);
		structure.setMessage("users fecthed successfully ");
		structure.setStatus(HttpStatus.OK.value());

		return new ResponseEntity<ResponseStructure<List<UserResponseDTO>>>(structure, HttpStatus.OK);
	}

}
