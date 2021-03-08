package com.campsite.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.campsite.model.Reservation;
import com.campsite.request.ReservationCancellationRequest;
import com.campsite.request.ReservationCreationRequest;
import com.campsite.request.ReservationModificationRequest;
import com.campsite.response.CreateReservationResponse;
import com.campsite.response.ExistingSchedulesResponse;
import com.campsite.service.CampsiteService;

@RestController
public class CampsiteController {

	@Autowired
	private CampsiteService campsiteService;

	@RequestMapping(method = RequestMethod.GET, value = "/getAvailabilitySchedule")
	@ResponseBody
	public ResponseEntity<ExistingSchedulesResponse> getScehdules(@RequestParam(required = false) LocalDate startDate,
			@RequestParam(required = false) LocalDate endDate) {
		return ResponseEntity.ok().body(campsiteService.findExistingScehdules(startDate, endDate));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/createReservation", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreateReservationResponse> createReservation(
			@RequestBody ReservationCreationRequest reservationCreationRequest) {
		Reservation reservation = campsiteService.createReservation(reservationCreationRequest);
		CreateReservationResponse response = new CreateReservationResponse();
		response.setReservationId(reservation.getId());
		return new ResponseEntity<CreateReservationResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/modifyReservation")
	public ResponseEntity<String> modifyExistingReservation(
			@RequestBody ReservationModificationRequest reservationModificationRequest) {
		campsiteService.modifyReservation(reservationModificationRequest);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/cancelReservation")
	public ResponseEntity<String> cancelExistingReservation(
			@RequestBody ReservationCancellationRequest reservationCancellationRequest) {
		campsiteService.cancelReservation(reservationCancellationRequest.getReservationId());
		return ResponseEntity.ok().build();
	}

}
