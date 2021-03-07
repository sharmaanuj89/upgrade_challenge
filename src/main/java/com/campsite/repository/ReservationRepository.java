package com.campsite.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.campsite.model.Reservation;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

}
