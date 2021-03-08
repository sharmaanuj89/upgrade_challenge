package com.campsite.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campsite.exception.CannotModifyAlreadyCancelledReservation;
import com.campsite.exception.DateShouldNotExceedOneMonthException;
import com.campsite.exception.GivenDateRangeAlreadyReservedException;
import com.campsite.exception.ReservationIdDoesNotExistException;
import com.campsite.exception.StartDateShouldBeBeforeEndDateException;
import com.campsite.model.CampsiteAvailability;
import com.campsite.model.Reservation;
import com.campsite.model.ReservationStatus;
import com.campsite.repository.AvailabilityRepository;
import com.campsite.repository.ReservationRepository;
import com.campsite.request.ReservationCreationRequest;
import com.campsite.request.ReservationModificationRequest;
import com.campsite.response.ExistingSchedulesResponse;
import com.campsite.response.ExistingSchedulesResponse.Availablility;

@Service
public class CampsiteService {

	private Logger logger = LoggerFactory.getLogger(CampsiteService.class);

	@Autowired
	private AvailabilityRepository availabilityRepository;

	@Autowired
	private ReservationRepository reservationRepository;
	
	@Transactional
	public ExistingSchedulesResponse findExistingScehdules(LocalDate startDate, LocalDate endDate) {
		if (startDate != null && endDate != null) {
			if (startDate.atStartOfDay().isAfter(endDate.atStartOfDay())) {
				throw new StartDateShouldBeBeforeEndDateException();
			}

			if (liesWithinNext1Month(startDate) && liesWithinNext1Month(endDate)) {
				throw new DateShouldNotExceedOneMonthException();
			}
		}

		startDate = startDate != null ? startDate : LocalDate.now().plusDays(1);
		endDate = endDate != null ? endDate : LocalDate.now().plusDays(30);
		ExistingSchedulesResponse response = new ExistingSchedulesResponse();
		List<CampsiteAvailability> lCampsiteAvailability = availabilityRepository.getAllBetweenDates(startDate,
				endDate);
		List<Availablility> lAvailability = lCampsiteAvailability.stream()
				.map(t -> new Availablility(convert(t.getAvailabilityDate()), t.getReservation() != null))
				.collect(Collectors.toList());
		response.setLAvailability(lAvailability);
		return response;
	}

	@Transactional
	public Reservation createReservation(ReservationCreationRequest reservationCreationRequest) {
		LocalDate startDate = reservationCreationRequest.getStartDate();
		LocalDate endDate = reservationCreationRequest.getStartDate()
				.plusDays(reservationCreationRequest.getDurationInDays());
		List<CampsiteAvailability> lCampsiteAvailability = availabilityRepository
				.getAllBetweenDatesNotBindedToAnyReservation(startDate, endDate);
		if (lCampsiteAvailability.size() != (reservationCreationRequest.getDurationInDays() + 1)) {
			throw new GivenDateRangeAlreadyReservedException();
		}
		Reservation reservation = new Reservation();
		reservation.setEmail(reservationCreationRequest.getEmail());
		reservation.setEndDate(convert(endDate));
		reservation.setFirstName(reservationCreationRequest.getFirstName());
		reservation.setLastName(reservationCreationRequest.getLastName());
		for (CampsiteAvailability campsiteAvailability : lCampsiteAvailability) {
			reservation.addCampsiteAvailability(campsiteAvailability);
		}
		reservation.setReservationStatus(ReservationStatus.BOOKED);
		reservation.setStartDate(convert(startDate));
		return reservationRepository.save(reservation);
	}

	@Transactional
	public void modifyReservation(ReservationModificationRequest reservationModificationRequest) {
		Optional<Reservation> oReservation = reservationRepository
				.findById(reservationModificationRequest.getReservationId());
		if (!oReservation.isPresent()) {
			throw new ReservationIdDoesNotExistException(reservationModificationRequest.getReservationId());
		}
		Reservation reservation = oReservation.get();
		if (reservation.getReservationStatus() == ReservationStatus.CANCELLED) {
			throw new CannotModifyAlreadyCancelledReservation();
		}
		LocalDate startDate = reservationModificationRequest.getStartDate();
		LocalDate endDate = reservationModificationRequest.getStartDate()
				.plusDays(reservationModificationRequest.getDurationInDays());
		Map<Long, CampsiteAvailability> reservedCampsite = reservation.getLCampsiteAvailability().stream().collect(Collectors.toMap(CampsiteAvailability::getId, Function.identity()));
		List<CampsiteAvailability> lCampsiteAvailability = availabilityRepository.getAllBetweenDates(startDate,
				endDate);
		for (CampsiteAvailability campsiteAvailability : lCampsiteAvailability) {
			if (campsiteAvailability.getReservation() != null) {
				if (!campsiteAvailability.getReservation().getId().equals(reservation.getId())) {
					throw new GivenDateRangeAlreadyReservedException();
				}else {
					reservedCampsite.remove(campsiteAvailability.getId());
				}
			} else {
				reservation.addCampsiteAvailability(campsiteAvailability);
			}
		}
		for (Entry<Long, CampsiteAvailability> entry : reservedCampsite.entrySet()) {
		   reservation.removeCampsiteAvailability(entry.getValue());
		}
		reservation.setStartDate(convert(startDate));
		reservation.setEndDate(convert(endDate));
		reservationRepository.save(reservation);
	}

	@Transactional
	public void cancelReservation(Long reservationId) {
		Optional<Reservation> oReservation = reservationRepository.findById(reservationId);
		if (!oReservation.isPresent()) {
			throw new ReservationIdDoesNotExistException(reservationId);
		}
		Reservation reservation = oReservation.get();
		reservation.setReservationStatus(ReservationStatus.CANCELLED);
		reservation.removeAllAvailability();
		reservationRepository.save(reservation);
	}

	private Date convert(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	private LocalDate convert(Date date) {
		return ((java.sql.Date) date).toLocalDate();
	}

	private boolean liesWithinNext1Month(LocalDate localDate) {
		LocalDate today = LocalDate.now();
		LocalDate oneMonthFromTomorrow = LocalDate.now().plusDays(31);
		return localDate.isAfter(today) && localDate.isBefore(oneMonthFromTomorrow);

	}

}
