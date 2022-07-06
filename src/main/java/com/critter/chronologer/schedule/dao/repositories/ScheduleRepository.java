package com.critter.chronologer.schedule.dao.repositories;

import com.critter.chronologer.schedule.dao.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByPetsId(Long petId);

    List<Schedule> findByEmployeesId(Long employeeId);

    List<Schedule> findDistinctByPetsOwnerId(Long ownerId);
}
