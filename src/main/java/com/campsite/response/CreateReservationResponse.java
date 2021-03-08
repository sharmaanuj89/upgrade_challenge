package com.campsite.response;

import java.io.Serializable;

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
public class CreateReservationResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8153258328077449621L;
	private Long reservationId;

}
