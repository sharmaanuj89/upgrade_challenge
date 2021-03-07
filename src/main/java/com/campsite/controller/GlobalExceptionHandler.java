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

@ControllerAdvice
public class GlobalExceptionHandler {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(ReservationIdDoesNotExistException.class)
	public ResponseEntity handleReservationIdDoesNotExist(HttpServletRequest request, Exception ex) {
		return new ResponseEntity(ResponseCodes.RESERVATION_ID_DOES_NOT_EXIST, HttpStatus.NOT_FOUND);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(StartDateShouldBeBeforeEndDateException.class)
	public ResponseEntity handleStartDateShouldBeBeforeEndDateException(HttpServletRequest request, Exception ex) {
		return new ResponseEntity(ResponseCodes.START_DATE_SHOULD_BE_BEFORE_END_DATE, HttpStatus.BAD_REQUEST);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(DateShouldNotExceedOneMonthException.class)
	public ResponseEntity handleDateShouldNotExceedOneMonthException(HttpServletRequest request, Exception ex) {
		return new ResponseEntity(ResponseCodes.DATE_SHOULD_NOT_EXCEED_ONE_MONTH, HttpStatus.BAD_REQUEST);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(GivenDateRangeAlreadyReservedException.class)
	public ResponseEntity handleGivenDateRangeAlreadyReservedException(HttpServletRequest request, Exception ex) {
		return new ResponseEntity(ResponseCodes.GIVEN_DATE_RANGE_ALREADY_RESERVED, HttpStatus.BAD_REQUEST);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(ObjectOptimisticLockingFailureException.class)
	public ResponseEntity handleObjectOptimisticLockingFailureException(HttpServletRequest request, Exception ex) {
		return new ResponseEntity(ResponseCodes.CONFLICT, HttpStatus.CONFLICT);
	}
	

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(Exception.class)
	public ResponseEntity handleError(Exception ex) {
		ex.printStackTrace();
		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	enum ResponseCodes {
		CONFLICT,
		RESERVATION_ID_DOES_NOT_EXIST, 
		GIVEN_DATE_RANGE_ALREADY_RESERVED,
		START_DATE_SHOULD_BE_BEFORE_END_DATE, 
		DATE_SHOULD_NOT_EXCEED_ONE_MONTH;
	}

}
