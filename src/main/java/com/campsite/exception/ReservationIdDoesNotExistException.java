package com.campsite.exception;

public class ReservationIdDoesNotExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7664798344782817972L;

	public ReservationIdDoesNotExistException(Long reservationId) {
		super("ReservationIdDoesNotExistException with id=" + reservationId);
	}

}
