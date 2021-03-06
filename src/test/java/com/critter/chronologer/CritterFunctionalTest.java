package com.critter.chronologer;

import com.critter.chronologer.pet.controllers.PetController;
import com.critter.chronologer.pet.dao.entities.enms.PetType;
import com.critter.chronologer.pet.dto.PetCreationRequest;
import com.critter.chronologer.pet.dto.PetDTO;
import com.critter.chronologer.schedule.dto.ScheduleCreationRequest;
import com.critter.chronologer.user.customer.controllers.CustomerController;
import com.critter.chronologer.user.customer.dto.CustomerCreationRequest;
import com.critter.chronologer.user.employee.controllers.EmployeeController;
import com.critter.chronologer.user.employee.dao.entities.enums.EmployeeSkill;
import com.critter.chronologer.user.employee.dto.EmployeeCreationRequest;
import com.critter.chronologer.user.employee.dto.EmployeeDTO;
import com.critter.chronologer.user.employee.dto.FindEmployeesRequest;
import com.google.common.collect.Sets;
import com.critter.chronologer.schedule.controllers.ScheduleController;
import com.critter.chronologer.schedule.dto.ScheduleDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a set of functional tests to validate the basic capabilities desired for this application.
 */
@Transactional
@SpringBootTest(classes = CritterApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CritterFunctionalTest {

    @Autowired
    private EmployeeController employeeController;

    @Autowired
    private CustomerController customerController;

    @Autowired
    private PetController petController;

    @Autowired
    private ScheduleController scheduleController;

    @Test
    public void testCreateCustomer(){
        var request = getCustomerCreationRequest();
        var newCustomer = customerController.saveCustomer(request);
        var retrievedCustomer = customerController.getAllCustomers().get(0);

        assertEquals(newCustomer.getName(), request.name());
        assertEquals(newCustomer.getId(), retrievedCustomer.getId());
        assertTrue(retrievedCustomer.getId() > 0);
    }

    @Test
    public void testCreateEmployee(){
        var request = getEmployeeCreationRequest(Collections.emptySet(), Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.PETTING));
        var newEmployee = employeeController.saveEmployee(request);
        var retrievedEmployee = employeeController.getEmployee(newEmployee.getId());

        assertEquals(request.skills(), newEmployee.getSkills());
        assertEquals(newEmployee.getId(), retrievedEmployee.getId());
        assertTrue(retrievedEmployee.getId() > 0);
    }

    @Test
    public void testAddPetsToCustomer() {
        var customerCreationRequest = getCustomerCreationRequest();
        var newCustomer = customerController.saveCustomer(customerCreationRequest);

        var petCreationRequest = getPetCreationRequest(newCustomer.getId());
        var newPet = petController.savePet(petCreationRequest);

        //make sure pet contains customer id
        var retrievedPet = petController.getPet(newPet.getId());
        assertEquals(retrievedPet.getId(), newPet.getId());
        assertEquals(retrievedPet.getOwnerId(), newCustomer.getId());

        //make sure you can retrieve pets by owner
        List<PetDTO> pets = petController.getPetsByOwner(newCustomer.getId());
        assertEquals(newPet.getId(), pets.get(0).getId());
        assertEquals(newPet.getName(), pets.get(0).getName());

        //check to make sure customer now also contains pet
        var retrievedCustomer = customerController.getAllCustomers().get(0);
        assertTrue(retrievedCustomer.getPetIds() != null && retrievedCustomer.getPetIds().size() > 0);
        assertEquals(retrievedCustomer.getPetIds().get(0), retrievedPet.getId());
    }

    @Test
    public void testFindPetsByOwner() {
        var customerCreationRequest = getCustomerCreationRequest();
        var newCustomer = customerController.saveCustomer(customerCreationRequest);

        var petCreationRequest = getPetCreationRequest(newCustomer.getId());
        PetDTO newPet1 = petController.savePet(petCreationRequest);
        PetDTO newPet2 = petController.savePet(new PetCreationRequest(PetType.DOG, "DogName", newCustomer.getId(),
                LocalDate.of(2019, 11, 2), "note"));

        List<PetDTO> pets = petController.getPetsByOwner(newCustomer.getId());
        assertEquals(pets.size(), 2);
        assertEquals(pets.get(0).getOwnerId(), newCustomer.getId());
        assertEquals(pets.get(0).getId(), newPet1.getId());
    }

    @Test
    public void testFindOwnerByPet() {
        var customerCreationRequest = getCustomerCreationRequest();
        var newCustomer = customerController.saveCustomer(customerCreationRequest);

        var petCreationRequest = getPetCreationRequest(newCustomer.getId());
        PetDTO newPet = petController.savePet(petCreationRequest);

        var owner = customerController.getOwnerByPet(newPet.getId());
        assertEquals(owner.getId(), newCustomer.getId());
        assertEquals(owner.getPetIds().get(0), newPet.getId());
    }

    @Test
    public void testChangeEmployeeAvailability() {
        var employeeCreationRequest = getEmployeeCreationRequest(Sets.newHashSet(), Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.PETTING));
        var emp1 = employeeController.saveEmployee(employeeCreationRequest);
        assertTrue(emp1.getDaysAvailable() != null && emp1.getDaysAvailable().isEmpty());

        Set<DayOfWeek> availability = Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY);
        employeeController.setAvailability(availability, emp1.getId());

        var emp2 = employeeController.getEmployee(emp1.getId());
        assertEquals(availability, emp2.getDaysAvailable());
    }

    @Test
    public void testFindEmployeesByServiceAndTime() {
        var emp1 = getEmployeeCreationRequest(Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY), Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.PETTING));
        var emp2 = getEmployeeCreationRequest(Sets.newHashSet(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), Sets.newHashSet(EmployeeSkill.PETTING, EmployeeSkill.WALKING));
        var emp3 = getEmployeeCreationRequest(Sets.newHashSet(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY), Sets.newHashSet(EmployeeSkill.WALKING, EmployeeSkill.SHAVING));

        var emp1n = employeeController.saveEmployee(emp1);
        var emp2n = employeeController.saveEmployee(emp2);
        var emp3n = employeeController.saveEmployee(emp3);

        //make a request that matches employee 1 or 2
        FindEmployeesRequest findEmployeesRequest1 = new FindEmployeesRequest(Sets.newHashSet(EmployeeSkill.PETTING), LocalDate.of(2019, 12, 25));

        Set<Long> eIds1 = employeeController.findEmployeesForService(findEmployeesRequest1).stream().map(EmployeeDTO::getId).collect(Collectors.toSet());
        Set<Long> eIds1expected = Sets.newHashSet(emp1n.getId(), emp2n.getId());
        assertEquals(eIds1expected, eIds1);

        //make a request that matches only employee 3
        FindEmployeesRequest findEmployeesRequest2 = new FindEmployeesRequest(Sets.newHashSet(EmployeeSkill.WALKING, EmployeeSkill.SHAVING), LocalDate.of(2019, 12, 27));

        Set<Long> eIds2 = employeeController.findEmployeesForService(findEmployeesRequest2).stream().map(EmployeeDTO::getId).collect(Collectors.toSet());
        Set<Long> eIds2expected = Sets.newHashSet(emp3n.getId());
        assertEquals(eIds2expected, eIds2);
    }

    @Test
    public void testSchedulePetsForServiceWithEmployee() {
        var employeeCreationRequest = getEmployeeCreationRequest(Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY), Collections.emptySet());

        EmployeeDTO employee = employeeController.saveEmployee(employeeCreationRequest);
        var customer = customerController.saveCustomer(getCustomerCreationRequest());
        var petCreationRequest = getPetCreationRequest(customer.getId());
        PetDTO pet = petController.savePet(petCreationRequest);

        LocalDate date = LocalDate.of(2019, 12, 25);
        List<Long> petsList = List.of(pet.getId());
        List<Long> employeesList = List.of(employee.getId());
        Set<EmployeeSkill> skillsSet = Sets.newHashSet(EmployeeSkill.PETTING);

        scheduleController.createSchedule(getScheduleCreationRequest(petsList, employeesList, date, skillsSet));
        ScheduleDTO schedule = scheduleController.getAllSchedules().get(0);

        assertEquals(schedule.getActivities(), skillsSet);
        assertEquals(schedule.getDate(), date);
        assertEquals(schedule.getEmployeeIds(), employeesList);
        assertEquals(schedule.getPetIds(), petsList);
    }

    @Test
    public void testFindScheduleByEntities() {
        ScheduleDTO schedule1 = populateSchedule(1, 2, LocalDate.of(2019, 12, 25), Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.WALKING));
        ScheduleDTO schedule2 = populateSchedule(3, 1, LocalDate.of(2019, 12, 26), Sets.newHashSet(EmployeeSkill.PETTING));

        //add a third schedule that shares some employees and pets with the other schedules
        var scheduleCreationRequest = new ScheduleCreationRequest(schedule1.getEmployeeIds(), schedule2.getPetIds(), LocalDate.of(2020, 3, 23), Sets.newHashSet(EmployeeSkill.SHAVING, EmployeeSkill.PETTING));

        ScheduleDTO schedule3 = scheduleController.createSchedule(scheduleCreationRequest);

        /*
            We now have 3 schedule entries. The third schedule entry has the same employees as the 1st schedule
            and the same pets/owners as the second schedule. So if we look up schedule entries for the employee from
            schedule 1, we should get both the first and third schedule as our result.
         */

        //Employee 1 in is both schedule 1 and 3
        List<ScheduleDTO> scheds1e = scheduleController.getScheduleForEmployee(schedule1.getEmployeeIds().get(0));
        compareSchedules(schedule1, scheds1e.get(0));
        compareSchedules(schedule3, scheds1e.get(1));

        //Employee 2 is only in schedule 2
        List<ScheduleDTO> scheds2e = scheduleController.getScheduleForEmployee(schedule2.getEmployeeIds().get(0));
        compareSchedules(schedule2, scheds2e.get(0));

        //Pet 1 is only in schedule 1
        List<ScheduleDTO> scheds1p = scheduleController.getScheduleForPet(schedule1.getPetIds().get(0));
        compareSchedules(schedule1, scheds1p.get(0));

        //Pet from schedule 2 is in both schedules 2 and 3
        List<ScheduleDTO> scheds2p = scheduleController.getScheduleForPet(schedule2.getPetIds().get(0));
        compareSchedules(schedule2, scheds2p.get(0));
        compareSchedules(schedule3, scheds2p.get(1));

        //Owner of the first pet will only be in schedule 1
        List<ScheduleDTO> scheds1c = scheduleController.getScheduleForCustomer(customerController.getOwnerByPet(schedule1.getPetIds().get(0)).getId());
        compareSchedules(schedule1, scheds1c.get(0));

        //Owner of pet from schedule 2 will be in both schedules 2 and 3
        List<ScheduleDTO> scheds2c = scheduleController.getScheduleForCustomer(customerController.getOwnerByPet(schedule2.getPetIds().get(0)).getId());
        compareSchedules(schedule2, scheds2c.get(0));
        compareSchedules(schedule3, scheds2c.get(1));
    }


    private static EmployeeCreationRequest getEmployeeCreationRequest(Set<DayOfWeek> daysAvailable, Set<EmployeeSkill> skills) {
        return new EmployeeCreationRequest("TestEmployee", skills, daysAvailable);
    }

    private static CustomerCreationRequest getCustomerCreationRequest() {
         return new CustomerCreationRequest("TestEmployee", "123-456-789", "notes");
    }

    private static PetCreationRequest getPetCreationRequest(Long ownerId) {
        return new PetCreationRequest(PetType.CAT, "TestPet", ownerId,
                                      LocalDate.of(2019, 10, 1), "note");
    }

    private static FindEmployeesRequest getFindEmployeesRequest() {
        return new FindEmployeesRequest(Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.WALKING),
                                        LocalDate.of(2019, 12, 25));
    }

    private static ScheduleCreationRequest getScheduleCreationRequest(List<Long> petIds, List<Long> employeeIds, LocalDate date, Set<EmployeeSkill> activities) {
        return new ScheduleCreationRequest(employeeIds, petIds, date, activities);
    }

    private ScheduleDTO populateSchedule(int numEmployees, int numPets, LocalDate date, Set<EmployeeSkill> activities) {
        List<Long> employeeIds = IntStream.range(0, numEmployees)
                .mapToObj(i -> getEmployeeCreationRequest(Sets.newHashSet(date.getDayOfWeek()), activities))
                .map(e -> employeeController.saveEmployee(e).getId())
                .toList();

        var customer = customerController.saveCustomer(getCustomerCreationRequest());

        List<Long> petIds = IntStream.range(0, numPets)
                .mapToObj(i -> getPetCreationRequest(customer.getId()))
                .map(p -> petController.savePet(p).getId())
                .toList();

        return scheduleController.createSchedule(getScheduleCreationRequest(petIds, employeeIds, date, activities));
    }

    private static void compareSchedules(ScheduleDTO sched1, ScheduleDTO sched2) {
        assertEquals(sched1.getPetIds(), sched2.getPetIds());
        assertEquals(sched1.getActivities(), sched2.getActivities());
        assertEquals(sched1.getEmployeeIds(), sched2.getEmployeeIds());
        assertEquals(sched1.getDate(), sched2.getDate());
    }
}
