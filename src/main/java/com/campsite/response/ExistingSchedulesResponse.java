package com.campsite.response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ExistingSchedulesResponse {

	private List<Availablility> lAvailability = new ArrayList<Availablility>();

	@Getter
	@Setter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class Availablility {
		private LocalDate availabilityDate;
		private boolean isBooked;
	}

}
