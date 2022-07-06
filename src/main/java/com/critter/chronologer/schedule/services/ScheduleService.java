package com.critter.chronologer.schedule.services;

import com.critter.chronologer.pet.dao.entities.Pet;
import com.critter.chronologer.pet.services.PetService;
import com.critter.chronologer.schedule.dao.entities.Schedule;
import com.critter.chronologer.schedule.dao.repositories.ScheduleRepository;
import com.critter.chronologer.user.employee.dao.entities.Employee;
import com.critter.chronologer.user.employee.dao.entities.enums.EmployeeSkill;
import com.critter.chronologer.user.employee.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final EmployeeService employeeService;
    private final PetService petService;

    public Schedule save(List<Long> employeeIds, List<Long> petIds, LocalDate date, Set<EmployeeSkill> activities) {
        List<Employee> employees = employeeIds.stream().map(employeeService::getEmployeeReferenceById).toList();
        List<Pet> pets = petIds.stream().map(petService::getPetReferenceById).toList();

        return scheduleRepository.save(new Schedule(employees, pets, date, activities));
    }

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> findByPet(Long petId) {
        return scheduleRepository.findByPetsId(petId);
    }

    public List<Schedule> findByEmployee(Long employeeId) {
        return scheduleRepository.findByEmployeesId(employeeId);
    }

    public List<Schedule> findByCustomer(Long customerId) {
        return scheduleRepository.findDistinctByPetsOwnerId(customerId);
    }
}
