package com.campsite.request;

import java.time.LocalDate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ReservationModificationRequest {

	@NotNull
	private Long reservationId;

	@NotNull
	private LocalDate startDate;

	@NotNull
	@Min(1)
	@Max(3)
	private Integer durationInDays;

}
