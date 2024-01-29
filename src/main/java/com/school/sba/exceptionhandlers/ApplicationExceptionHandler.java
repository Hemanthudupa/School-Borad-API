package com.school.sba.exceptionhandlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.school.sba.exceptions.AcademicProgramNotFoundByIdException;
import com.school.sba.exceptions.AcademicsProgramIsNotPresentException;
import com.school.sba.exceptions.AdminCannotBeAddedToAcademicsProgramException;
import com.school.sba.exceptions.ClassRoomNotFreeException;
import com.school.sba.exceptions.ContraintsValidationException;
import com.school.sba.exceptions.ExistingAdminException;
import com.school.sba.exceptions.InvalidClassHourIdException;
import com.school.sba.exceptions.InvalidUserRoleException;
import com.school.sba.exceptions.NoSubjectFoundInAcademinException;
import com.school.sba.exceptions.ScheduleNotFoundByIDException;
import com.school.sba.exceptions.ScheduleNotFoundInSchoolException;
import com.school.sba.exceptions.ScheduledAlreadyPresentException;
import com.school.sba.exceptions.SchoolAlreadyPresentException;
import com.school.sba.exceptions.SchoolNotFound;
import com.school.sba.exceptions.SchoolNotFoundByIdException;
import com.school.sba.exceptions.SubjectNotFoundExceptionByID;
import com.school.sba.exceptions.SubjectsOnlyAddedToTeacherException;
import com.school.sba.exceptions.UnauthorizedAccessSchoolException;
import com.school.sba.exceptions.UserIsNotAnAdminException;
import com.school.sba.exceptions.UserNotFoundByIdException;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<ObjectError> allErrors = ex.getAllErrors();

		System.err.println(" entered ");
		Map<String, String> map = new HashMap<>();

		allErrors.forEach(error -> {
			FieldError fieldError = (FieldError) error;
			map.put(fieldError.getField(), fieldError.getDefaultMessage());
		});
		System.err.println(" Closed ");
		return structre(HttpStatus.BAD_REQUEST, ex.getMessage(), map);
	}

	public ResponseEntity<Object> structre(HttpStatus status, String message, Object rootCause) {
		return new ResponseEntity<Object>(Map.of("status", status.value(), "message", message, "rootcause", rootCause),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ContraintsValidationException.class)
	public ResponseEntity<Object> contraintsValidationException(ContraintsValidationException ex) {
		return structre(HttpStatus.BAD_REQUEST, ex.getMessage(), "no duplicate values for phone number and email ");
	}

	@ExceptionHandler(ExistingAdminException.class)
	public ResponseEntity<Object> existingAdminException(ExistingAdminException ex) {
		return structre(HttpStatus.BAD_REQUEST, ex.getMessage(), "Admin already present no duplicate admin ");
	}

	@ExceptionHandler(UserNotFoundByIdException.class)
	public ResponseEntity<Object> userNotFoundByIdException(UserNotFoundByIdException ex) {
		return structre(HttpStatus.NOT_FOUND, ex.getMessage(), "user not found with the specified user ID  ");
	}

	@ExceptionHandler(UnauthorizedAccessSchoolException.class)
	public ResponseEntity<Object> unauthorizedAccessSchoolException(UnauthorizedAccessSchoolException ex) {
		return structre(HttpStatus.BAD_REQUEST, "ADMIN reuired!!!! ", "only admin can regester the school");
	}

	@ExceptionHandler(SchoolAlreadyPresentException.class)
	public ResponseEntity<Object> schoolAlreadyPresentException(SchoolAlreadyPresentException ex) {
		return structre(HttpStatus.BAD_REQUEST, "school already regesterd ", "School is already regesterd by the user");
	}

	@ExceptionHandler(ScheduledAlreadyPresentException.class)
	public ResponseEntity<Object> scheduledAlreadyPresentException(ScheduledAlreadyPresentException ex) {
		return structre(HttpStatus.BAD_REQUEST, "scheduled  already present ",
				"School is already regesterd with the perticular schedule");
	}

	@ExceptionHandler(SchoolNotFoundByIdException.class)
	public ResponseEntity<Object> schoolNotFoundByIdException(SchoolNotFoundByIdException ex) {
		return structre(HttpStatus.BAD_REQUEST, "school  not found ", "School is not found by given id ");
	}

	@ExceptionHandler(ScheduleNotFoundByIDException.class)
	public ResponseEntity<Object> scheduleNotFoundByIDException(ScheduleNotFoundByIDException ex) {
		return structre(HttpStatus.NOT_FOUND, ex.getMessage(), "schedule not found by given id !!!");
	}

	@ExceptionHandler(AcademicProgramNotFoundByIdException.class)
	public ResponseEntity<Object> academicProgramNotFoundByIdException(AcademicProgramNotFoundByIdException ex) {
		return structre(HttpStatus.NOT_FOUND, ex.getMessage(), "invalid ID to find the AcademicProgram");
	}

	@ExceptionHandler(AdminCannotBeAddedToAcademicsProgramException.class)
	public ResponseEntity<Object> adminCannotBeAddedToAcademicsProgramException(
			AdminCannotBeAddedToAcademicsProgramException ex) {
		return structre(HttpStatus.BAD_REQUEST, ex.getMessage(), "Admin cannot be added to AcademicProgram ");
	}

	@ExceptionHandler(SubjectsOnlyAddedToTeacherException.class)
	public ResponseEntity<Object> subjectsOnlyAddedToTeacherException(SubjectsOnlyAddedToTeacherException ex) {
		return structre(HttpStatus.NOT_FOUND, ex.getMessage(),
				"Subjects can only added to teacher / not for ADMIN or STUDENT ");
	}

	@ExceptionHandler(UserIsNotAnAdminException.class)
	public ResponseEntity<Object> userIsNotAnAdminException(UserIsNotAnAdminException ex) {
		return structre(HttpStatus.BAD_REQUEST, ex.getMessage(), "only ADMIN is valid ");

	}

	@ExceptionHandler(SchoolNotFound.class)
	public ResponseEntity<Object> scheduleNotFoundByIDException(SchoolNotFound ex) {
		return structre(HttpStatus.NOT_FOUND, ex.getMessage(), "ADMIN not created school !!!");
	}

	@ExceptionHandler(NoSubjectFoundInAcademinException.class)
	public ResponseEntity<Object> noSubjectFoundInAcademinException(NoSubjectFoundInAcademinException ex) {
		return structre(HttpStatus.NOT_FOUND, ex.getMessage(), "subject not found in academic");
	}

	@ExceptionHandler(InvalidUserRoleException.class)
	public ResponseEntity<Object> invalidUserRoleException(InvalidUserRoleException ex) {
		return structre(HttpStatus.NOT_FOUND, ex.getMessage(), "only Teacher can be added ");
	}

	@ExceptionHandler(ScheduleNotFoundInSchoolException.class)
	public ResponseEntity<Object> scheduleNotFoundInSchoolException(ScheduleNotFoundInSchoolException ex) {
		return structre(HttpStatus.NOT_FOUND, ex.getMessage(), "schedule not found in school  ");
	}

	@ExceptionHandler(SubjectNotFoundExceptionByID.class)
	public ResponseEntity<Object> subjectNotFoundExceptionByID(SubjectNotFoundExceptionByID ex) {
		return structre(HttpStatus.NOT_FOUND, ex.getMessage(), "subject not found by ID  ");
	}

	@ExceptionHandler(AcademicsProgramIsNotPresentException.class)
	public ResponseEntity<Object> academicsProgramIsNotPresentException(AcademicsProgramIsNotPresentException ex) {
		return structre(HttpStatus.NOT_FOUND, ex.getMessage(), "Acadmics  not found in the school  ");
	}

	@ExceptionHandler(InvalidClassHourIdException.class)
	public ResponseEntity<Object> invalidClassHourIdException(InvalidClassHourIdException ex) {
		return structre(HttpStatus.BAD_REQUEST, ex.getMessage(), "invalid class hour id !!!!");
	}

	@ExceptionHandler(ClassRoomNotFreeException.class)
	public ResponseEntity<Object> classRoomNotFoundException(ClassRoomNotFreeException ex) {
		return structre(HttpStatus.BAD_REQUEST, ex.getMessage(),
				"invalid class room becuase class is not free at this time  !!!!");
	}
}
