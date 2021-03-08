package com.campsite.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.campsite.controller.CampsiteController;
import com.campsite.exception.DateShouldNotExceedOneMonthException;
import com.campsite.exception.GivenDateRangeAlreadyReservedException;
import com.campsite.exception.ReservationIdDoesNotExistException;
import com.campsite.exception.StartDateShouldBeBeforeEndDateException;
import com.campsite.model.Reservation;
import com.campsite.response.ExistingSchedulesResponse;
import com.campsite.response.ExistingSchedulesResponse.Availablility;
import com.campsite.service.CampsiteService;

@WebMvcTest(controllers = CampsiteController.class)
@ActiveProfiles("test")
public class CampsiteControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CampsiteService campsiteService;

	@Test
	public void testSuccessfulForFetchAvailabilitySchedule() throws Exception {
		ExistingSchedulesResponse response = new ExistingSchedulesResponse();
		List<Availablility> l = new ArrayList<Availablility>();
		l.add(new Availablility(LocalDate.now(), false));
		l.add(new Availablility(LocalDate.now().plusDays(1), false));
		l.add(new Availablility(LocalDate.now().plusDays(2), false));
		response.setLAvailability(l);
		Mockito.when(campsiteService.findExistingScehdules(Mockito.anyObject(), Mockito.anyObject()))
				.thenReturn(response);

		this.mockMvc.perform(get("/getAvailabilitySchedule")).andExpect(status().isOk());
	}

	@Test
	public void testFailureStartDateShouldBeBeforeEndDateExceptionForFetchAvailabilitySchedule() throws Exception {
		Mockito.when(campsiteService.findExistingScehdules(Mockito.anyObject(), Mockito.anyObject()))
				.thenThrow(StartDateShouldBeBeforeEndDateException.class);

		this.mockMvc.perform(get("/getAvailabilitySchedule")).andExpect(status().isBadRequest());
	}

	@Test
	public void testFailureDateShouldNotExceedOneMonthExceptionForFetchAvailabilitySchedule() throws Exception {
		Mockito.when(campsiteService.findExistingScehdules(Mockito.anyObject(), Mockito.anyObject()))
				.thenThrow(DateShouldNotExceedOneMonthException.class);

		this.mockMvc.perform(get("/getAvailabilitySchedule")).andExpect(status().isBadRequest());
	}

	@Test
	public void testSuccessfulForCreateReservation() throws Exception {
		Reservation reservation = new Reservation();
		reservation.setId(1L);
		Mockito.when(campsiteService.createReservation(Mockito.anyObject())).thenReturn(reservation);

		this.mockMvc.perform(post("/createReservation").contentType(MediaType.APPLICATION_JSON_VALUE).content(
				"{\"firstName\" : \"anuj\", \"lastName\" : \"sharma\", \"email\" : \"anuj@gmail.com\", \"startDate\" : \"2021-03-26\", \"durationInDays\": 2}"))
				.andExpect(status().isOk());
	}

	@Test
	public void testFailureGivenDateRangeAlreadyReservedExceptionForCreateReservation() throws Exception {
		Mockito.when(campsiteService.createReservation(Mockito.anyObject()))
				.thenThrow(GivenDateRangeAlreadyReservedException.class);

		this.mockMvc.perform(post("/createReservation").contentType(MediaType.APPLICATION_JSON_VALUE).content(
				"{\"firstName\" : \"anuj\", \"lastName\" : \"sharma\", \"email\" : \"anuj@gmail.com\", \"startDate\" : \"2021-03-26\", \"durationInDays\": 2}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testFailureObjectOptimisticLockingFailureExceptionForCreateReservation() throws Exception {
		Mockito.when(campsiteService.createReservation(Mockito.anyObject()))
				.thenThrow(ObjectOptimisticLockingFailureException.class);

		this.mockMvc.perform(post("/createReservation").contentType(MediaType.APPLICATION_JSON_VALUE).content(
				"{\"firstName\" : \"anuj\", \"lastName\" : \"sharma\", \"email\" : \"anuj@gmail.com\", \"startDate\" : \"2021-03-26\", \"durationInDays\": 2}"))
				.andExpect(status().isConflict());
	}

	@Test
	public void testFailureReservationIdDoesNotExistExceptionForCancelReservation() throws Exception {
		Mockito.doThrow(new ReservationIdDoesNotExistException(1L)).when(campsiteService)
				.cancelReservation(Mockito.anyLong());

		this.mockMvc.perform(post("/cancelReservation").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content("{\"reservationId\" : 3}")).andExpect(status().isNotFound());
	}

}
