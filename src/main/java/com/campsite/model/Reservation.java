package com.campsite.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	@Column(name = "email")
	private String email;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "start_date")
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Column(name = "end_date")
	@Temporal(TemporalType.DATE)
	private Date endDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "reservation_status")
	private ReservationStatus reservationStatus;

	@OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
	private List<CampsiteAvailability> lCampsiteAvailability = new ArrayList<CampsiteAvailability>();
	
	public void addCampsiteAvailability(CampsiteAvailability campsiteAvailability) {
		lCampsiteAvailability.add(campsiteAvailability);
		campsiteAvailability.setReservation(this);
    }
	
	public void removeCampsiteAvailability(CampsiteAvailability campsiteAvailability) {
		lCampsiteAvailability.remove(campsiteAvailability);
		campsiteAvailability.setReservation(null);
    }
	
	public void removeAllAvailability() {
		java.util.Iterator<CampsiteAvailability> it = lCampsiteAvailability.iterator();
		while(it.hasNext()) {
			CampsiteAvailability c = it.next();
			it.remove();
			c.setReservation(null);
		}
	}
}
