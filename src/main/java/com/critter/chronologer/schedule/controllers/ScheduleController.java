package com.critter.chronologer.schedule.controllers;

import com.critter.chronologer.schedule.dao.entities.Schedule;
import com.critter.chronologer.schedule.dto.ScheduleCreationRequest;
import com.critter.chronologer.schedule.dto.ScheduleDTO;
import com.critter.chronologer.schedule.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final SpelAwareProxyProjectionFactory projectionFactory;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleCreationRequest request) {
        Schedule schedule = scheduleService.save(request.employeeIds(), request.petIds(), request.date(), request.activities());
        return toScheduleDTO(schedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleService.findAll().stream().map(this::toScheduleDTO).toList();
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return scheduleService.findByPet(petId).stream().map(this::toScheduleDTO).toList();
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return scheduleService.findByEmployee(employeeId).stream().map(this::toScheduleDTO).toList();
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        return scheduleService.findByCustomer(customerId).stream().map(this::toScheduleDTO).toList();
    }

    private ScheduleDTO toScheduleDTO(Schedule schedule){
        return projectionFactory.createProjection(ScheduleDTO.class, schedule);
    }
}
