package com.campsite.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import com.campsite.model.CampsiteAvailability;

@Repository
public interface AvailabilityRepository extends CrudRepository<CampsiteAvailability, Long> {

	@Query(value = "select * from campsite_availability where availability_date BETWEEN :startDate AND :endDate", nativeQuery = true)
	List<CampsiteAvailability> getAllBetweenDates(
			@Param("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@Param("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);
	
	@Query(value = "select * from campsite_availability where reservation_id is null AND availability_date between :startDate AND :endDate", nativeQuery = true)
	List<CampsiteAvailability> getAllBetweenDatesNotBindedToAnyReservation(
			@Param("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@Param("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);

}
