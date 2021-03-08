package com.campsite.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.campsite.exception.DateShouldNotExceedOneMonthException;
import com.campsite.exception.GivenDateRangeAlreadyReservedException;
import com.campsite.exception.ReservationIdDoesNotExistException;
import com.campsite.exception.StartDateShouldBeBeforeEndDateException;
import com.campsite.response.CampsiteError;

@ControllerAdvice
public class GlobalExceptionHandler {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(ReservationIdDoesNotExistException.class)
	public ResponseEntity<CampsiteError> handleReservationIdDoesNotExist(HttpServletRequest request, Exception ex) {
		CampsiteError error = new CampsiteError(ResponseCodes.RESERVATION_ID_DOES_NOT_EXIST.errorCode,
				ResponseCodes.RESERVATION_ID_DOES_NOT_EXIST.errorDescription);
		return new ResponseEntity(error, HttpStatus.NOT_FOUND);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(StartDateShouldBeBeforeEndDateException.class)
	public ResponseEntity<CampsiteError> handleStartDateShouldBeBeforeEndDateException(HttpServletRequest request, Exception ex) {
		CampsiteError error = new CampsiteError(ResponseCodes.START_DATE_SHOULD_BE_BEFORE_END_DATE.errorCode,
				ResponseCodes.START_DATE_SHOULD_BE_BEFORE_END_DATE.errorDescription);
		return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(DateShouldNotExceedOneMonthException.class)
	public ResponseEntity<CampsiteError> handleDateShouldNotExceedOneMonthException(HttpServletRequest request, Exception ex) {
		CampsiteError error = new CampsiteError(ResponseCodes.DATE_SHOULD_NOT_EXCEED_ONE_MONTH.errorCode,
				ResponseCodes.DATE_SHOULD_NOT_EXCEED_ONE_MONTH.errorDescription);
		return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(GivenDateRangeAlreadyReservedException.class)
	public ResponseEntity<CampsiteError> handleGivenDateRangeAlreadyReservedException(HttpServletRequest request, Exception ex) {
		CampsiteError error = new CampsiteError(ResponseCodes.GIVEN_DATE_RANGE_ALREADY_RESERVED.errorCode,
				ResponseCodes.GIVEN_DATE_RANGE_ALREADY_RESERVED.errorDescription);
		return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(ObjectOptimisticLockingFailureException.class)
	public ResponseEntity<CampsiteError> handleObjectOptimisticLockingFailureException(HttpServletRequest request, Exception ex) {
		CampsiteError error = new CampsiteError(ResponseCodes.CONFLICT.errorCode,
				ResponseCodes.CONFLICT.errorDescription);
		return new ResponseEntity(error, HttpStatus.CONFLICT);
	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(Exception.class)
	public ResponseEntity handleError(Exception ex) {
		ex.printStackTrace();
		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	enum ResponseCodes {
		CONFLICT(100, "CONFLICT"), RESERVATION_ID_DOES_NOT_EXIST(101, "RESERVATION_ID_DOES_NOT_EXIST"),
		GIVEN_DATE_RANGE_ALREADY_RESERVED(102, "GIVEN_DATE_RANGE_ALREADY_RESERVED"),
		START_DATE_SHOULD_BE_BEFORE_END_DATE(103, "START_DATE_SHOULD_BE_BEFORE_END_DATE"),
		DATE_SHOULD_NOT_EXCEED_ONE_MONTH(104, "DATE_SHOULD_NOT_EXCEED_ONE_MONTH");

		private Integer errorCode;
		private String errorDescription;

		ResponseCodes(Integer errorCode, String errorDescription) {
			this.errorCode = errorCode;
			this.errorDescription = errorDescription;
		}
	}

}
