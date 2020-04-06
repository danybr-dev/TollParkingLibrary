package com.danybr.tollParkingLibrary;

import com.danybr.tollParkingLibrary.exception.PowerException;
import com.danybr.tollParkingLibrary.exception.WrongDateException;
import com.danybr.tollParkingLibrary.exception.WrongInputException;
import com.danybr.tollParkingLibrary.model.car.Car;
import com.danybr.tollParkingLibrary.model.car.ElectricCar;
import com.danybr.tollParkingLibrary.model.car.SedanCar;
import com.danybr.tollParkingLibrary.model.pricingPolicy.PricingPolicyFixedAndHour;
import com.danybr.tollParkingLibrary.model.pricingPolicy.PricingPolicyHour;
import com.danybr.tollParkingLibrary.model.slot.ElectricSlot;
import com.danybr.tollParkingLibrary.model.slot.SedanSlot;
import com.danybr.tollParkingLibrary.model.slot.Slot;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.danybr.tollParkingLibrary.utils.DateUtils.getHoursDifferenceToPay;
import static org.junit.Assert.*;

public class TollParkingTest {

    private TollParking parking;
    private Car cSedan, cElectric20, cElectric50;

    @Before
    public void setUp() throws PowerException, WrongInputException {
        ArrayList<Slot> slots = new ArrayList<Slot>();
        slots.add(new SedanSlot());
        slots.add(new ElectricSlot("E-20"));
        slots.add(new ElectricSlot("E-50"));

        // Base pricing policy, with cost of 2 euro per hour
        parking = new TollParking(slots, new PricingPolicyHour(2));
        cSedan = new SedanCar();
        cElectric20 = new ElectricCar(20);
        cElectric50 = new ElectricCar(50);
    }

    @Test
    public void CheckCorrectParkingInit() {
        // Check if the slots are of the correct type
        assertTrue(parking.getSlots().get(0).getType().equals("S"));
        assertTrue(parking.getSlots().get(1).getType().equals("E-20"));
        assertTrue(parking.getSlots().get(2).getType().equals("E-50"));

    }

    @Test
    public void SedanCarIntoEmptyParking() throws WrongInputException {
        //TODO: Add time check
        assertTrue(parking.insert(cSedan));
        //Check if the slot is occupied by the correct car
        assertTrue(parking.getContainCarsMap().get(cSedan).getOccupiedBy() == cSedan);
        // Check if the car is parked to the correct slot
        assertTrue(parking.getSlots().get(0) == parking.getContainCarsMap().get(cSedan));
        // Check if the state is set as OCCUPIED
        assertEquals(Slot.State.OCCUPIED, parking.getContainCarsMap().get(cSedan).getState());
    }

    @Test
    public void SedanCarIntoParkingTwice() throws WrongInputException {
        // Same car enter two times
        parking.insert(cSedan);
        assertFalse(parking.insert(cSedan));
    }


    @Test
    public void ElectricCarFullParking() throws WrongInputException {
        List<Slot> freeSedanSlots = parking.getSlotsStatusMap().get(Slot.State.FREE).get(cSedan.getType());
        List<Slot> freeElectric20Slots = parking.getSlotsStatusMap().get(Slot.State.FREE).get(cElectric20.getType());
        List<Slot> freeElectric50Slots = parking.getSlotsStatusMap().get(Slot.State.FREE).get(cElectric50.getType());

        // Get the three slots in the parking
        Slot freeSedanSlot = freeSedanSlots.remove(0);
        Slot freeSlotElectric20 = freeElectric20Slots.remove(0);
        Slot freeSlotElectric50 = freeElectric50Slots.remove(0);

        // Set occupied by cars and set status to "OCCUPIED"
        freeSedanSlot.setOccupiedBy(cSedan);
        freeSedanSlot.setState(Slot.State.OCCUPIED);

        freeSlotElectric20.setOccupiedBy(cElectric20);
        freeSlotElectric20.setState(Slot.State.OCCUPIED);

        freeSlotElectric50.setOccupiedBy(cElectric50);
        freeSlotElectric50.setState(Slot.State.OCCUPIED);

        // Set entering time
        LocalDateTime insertingTime = LocalDateTime.now();
        freeSedanSlot.setLastOccupiedTime(insertingTime);
        freeSlotElectric20.setLastOccupiedTime(insertingTime);
        freeSlotElectric50.setLastOccupiedTime(insertingTime);

        // Set slot as occupied by car
        parking.getContainCarsMap().put(cSedan, freeSedanSlot);
        parking.getContainCarsMap().put(cElectric20, freeSlotElectric20);
        parking.getContainCarsMap().put(cElectric50, freeSlotElectric50);

        // Insert a new car into full parking
        Car cSedan2 = new SedanCar();
        assertFalse(parking.insert(cSedan2));
    }



    @Test(expected = WrongInputException.class)
    public void ElectricCar20kWNotIn50kWSlot() throws WrongInputException
    {
        List<Slot> freeElectric20Slots = parking.getSlotsStatusMap().get(Slot.State.FREE).get(cElectric20.getType());
        // Get the slot in the parking
        Slot freeSlotElectric20 = freeElectric20Slots.remove(0);

        // Occupying a free slot of E-20 adapter with a Electric car of 50Kw power will throw exception
        freeSlotElectric20.setOccupiedBy(cElectric50);
    }

    @Test
    public void SedanCarOutOfParking() throws WrongInputException, WrongDateException {

        List<Slot> freeSendanSlots = parking.getSlotsStatusMap().get(Slot.State.FREE).get(cSedan.getType());
        // Get the slot in the parking
        Slot freeSendanSlot = freeSendanSlots.remove(0);
        // Set occupied
        freeSendanSlot.setOccupiedBy(cSedan);
        freeSendanSlot.setState(Slot.State.OCCUPIED);

        // Set the entering time 2 hours in the past
        LocalDateTime localDateTimeMinus2Hours = LocalDateTime.now().minusHours(2);
        freeSendanSlot.setLastOccupiedTime(localDateTimeMinus2Hours);

        // Set slot as occupied by car
        parking.getContainCarsMap().put(cSedan, freeSendanSlot);

        // Get slot occupied by car to check its state
        Slot occupiedSlot = parking.getContainCarsMap().get(cSedan);
        assertEquals(Slot.State.OCCUPIED, occupiedSlot.getState());

        assertTrue(parking.remove(cSedan));

        // Check if the slot is freeded when car leaves
        assertEquals(Slot.State.FREE, occupiedSlot.getState());
    }


    @Test
    public void Electric50CarOutOfEmptyParking() throws WrongInputException, WrongDateException {
        assertFalse(parking.remove(cElectric50));
    }

    @Test
    public void BillCustomerFor5HourPolicyHour() throws WrongDateException, WrongInputException {
        parking.insert(cElectric50);
        Slot occupiedSlot = parking.getContainCarsMap().get(cElectric50);

        // A delay triggers an additional hour
        LocalDateTime localDateTimePlus4HoursAnd20Minutes = LocalDateTime.now().plusHours(4).plusMinutes(20);
        long nHoursToPay = getHoursDifferenceToPay(occupiedSlot.getLastOccupiedTime(), localDateTimePlus4HoursAnd20Minutes);

        PricingPolicyHour pricingPolicyHour = (PricingPolicyHour) parking.getPricingPolicy();
        assertEquals(5 * pricingPolicyHour.getHourPrice(), parking.getPricingPolicy().billCustomer(nHoursToPay));
    }

    @Test
    public void BillCustomerFor7HourPolicyFixedAndHour() throws WrongDateException, WrongInputException {
        parking.insert(cElectric20);
        Slot occupiedSlot = parking.getContainCarsMap().get(cElectric20);

        LocalDateTime localDateTimePlus6HoursAnd30Minutes = LocalDateTime.now().plusHours(6).plusMinutes(30);
        long nHoursToPay = getHoursDifferenceToPay(occupiedSlot.getLastOccupiedTime(), localDateTimePlus6HoursAnd30Minutes);

        // Setting a new pricing policy with fixed amount = 5 and hour amount = 2
        parking.setPricingPolicy(new PricingPolicyFixedAndHour(5,2));

        PricingPolicyFixedAndHour pricingPolicyHour = (PricingPolicyFixedAndHour) parking.getPricingPolicy();
        assertEquals(pricingPolicyHour.getFixedAmount() + 7 * pricingPolicyHour.getHourPrice(),
                parking.getPricingPolicy().billCustomer(nHoursToPay));
    }

}