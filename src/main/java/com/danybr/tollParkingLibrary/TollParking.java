package com.danybr.tollParkingLibrary;

import com.danybr.tollParkingLibrary.exception.WrongDateException;
import com.danybr.tollParkingLibrary.exception.WrongInputException;
import com.danybr.tollParkingLibrary.model.car.Car;
import com.danybr.tollParkingLibrary.model.pricingPolicy.PricingPolicy;
import com.danybr.tollParkingLibrary.model.pricingPolicy.PricingPolicyHour;
import com.danybr.tollParkingLibrary.model.slot.Slot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.danybr.tollParkingLibrary.utils.DateUtils.getHoursDifferenceToPay;

public class TollParking
{
    private final static Logger LOGGER = Logger.getLogger(TollParking.class.getName());

    private ArrayList<Slot> slots;
    private HashMap<Car,Slot> containCarsMap; // Fast map, no order is needed
    private HashMap<Slot.State, HashMap<String, ArrayList<Slot>>> slotsStatusMap; // Map<free, Map<type,List<Slot>>>

    private PricingPolicy pricingPolicy;

    /**
     * The constructor creates a TollParking object from the slot list passed as parameter and a pricing policy
     * @param slots ArrayList of Slots
     * @param pricingPolicy A valid Pricing Policy
     * @throws WrongInputException Null value not allowed
     */
    public TollParking(ArrayList<Slot> slots, PricingPolicy pricingPolicy) throws WrongInputException
    {
        if (slots != null && pricingPolicy != null)
        {
            this.slots = slots;
            this.pricingPolicy = pricingPolicy;
        }
        else
            throw new WrongInputException("Null value not allowed");
    }

    public ArrayList<Slot> getSlots()
    {
        if (slots == null)
            slots = new ArrayList<Slot>();
        return slots;
    }

    public void setSlots(ArrayList<Slot> slots) throws WrongInputException
    {
        if (slots != null)
            this.slots = slots;
        else
            throw new WrongInputException("Null value not allowed");

    }


    /**
     * Returns a Map containing the status of the slots.
     * The Map contains free slots divided by type
     * @return the map of slots status
     */
    public HashMap<Slot.State, HashMap<String, ArrayList<Slot>>> getSlotsStatusMap()
    {
        if (slotsStatusMap == null)
        {
            slotsStatusMap = new HashMap<Slot.State, HashMap<String, ArrayList<Slot>>>();
            HashMap<String, ArrayList<Slot>> slotsStatusMapPerType = new HashMap<String, ArrayList<Slot>>();
            for (Slot slot : slots)
            {
                if (!slotsStatusMapPerType.containsKey(slot.getType()))
                    slotsStatusMapPerType.put(slot.getType(), new ArrayList<Slot>());

                slotsStatusMapPerType.get(slot.getType()).add(slot);
            }
            slotsStatusMap.put(Slot.State.FREE, slotsStatusMapPerType);
        }

        return slotsStatusMap;
    }

    /**
     * * Sets a new map of the slot status for the parking
     * @param slotsStatusMap new map of slots status
     * @throws WrongInputException Null value not allowed
     */
    public void setSlotStatusMap(HashMap<Slot.State, HashMap<String, ArrayList<Slot>>> slotsStatusMap)
            throws WrongInputException
    {
        if (slotsStatusMap != null)
            this.slotsStatusMap = slotsStatusMap;
        else
            throw new WrongInputException("Null value not allowed");
    }

    /**
     * Returns a map containing the slots occupied by the cars
     * Free slots will be not present here
     * @return a map with slots occupied by the cars
     */
    public HashMap<Car, Slot> getContainCarsMap()
    {
        if (containCarsMap == null)
        {
            containCarsMap = new HashMap<Car,Slot>();
            for (Slot slot : slots)
            {
                if (slot.getOccupiedBy() != null)
                    containCarsMap.put(slot.getOccupiedBy(), slot);
            }

        }
        return containCarsMap;


    }

    /**
     * Sets a new map of slots occupied by car
     * @param containCarsMap new map of occupied slots
     * @throws WrongInputException Null value not allowed
     */
    public void setContainCarsMap(HashMap<Car, Slot> containCarsMap) throws WrongInputException
    {
        if (containCarsMap != null)
            this.containCarsMap = containCarsMap;
        else
            throw new WrongInputException("Null value not allowed");

    }

    /**
     * Returns the current Pricing Policy
     * @return current pricing policy
     * @throws WrongInputException Invalid Pricing Policy
     */
    public PricingPolicy getPricingPolicy() throws WrongInputException {
        if (pricingPolicy == null)
            pricingPolicy = new PricingPolicyHour(1);
        return pricingPolicy;
    }

    /**
     * Sets a new Pricing Policy for the parking
     * @param pricingPolicy new Pricing Policy
     * @throws WrongInputException Null value not allowed
     */
    public void setPricingPolicy(PricingPolicy pricingPolicy) throws WrongInputException
    {
        if (pricingPolicy != null)
            this.pricingPolicy = pricingPolicy;
        else
            throw new WrongInputException("Null value not allowed");
    }

    /**
     * The function is inserting the given car into the appropriate slot, checking their type, if any is free.
     * When the car is inserted, the slot is set as OCCUPIED and the structures are set up according to the state.
     * The slots store the time of entering by using LocalDateTime now.
     * The function returns true if a slot of the same type is available
     * @param car car entering in the parking
     * @return true if the car is inserted
     * @throws WrongInputException Wrong input exception
     * @see LocalDateTime
     */
    public boolean insert(Car car) throws WrongInputException {
        if(containCarsMap == null)
            containCarsMap = new HashMap<Car, Slot>();

        if (slotsStatusMap == null)
        {
            slotsStatusMap = new HashMap<Slot.State, HashMap<String, ArrayList<Slot>>>();
            slotsStatusMap.put(Slot.State.FREE, new HashMap<String, ArrayList<Slot>>());
        }

        if (!slotsStatusMap.get(Slot.State.FREE).containsKey(car.getType()))
        {
            ArrayList<Slot> freeSlotsGivenType = new ArrayList<Slot>();
            for (Slot slot : slots)
            {
                if ((slot.getState() == Slot.State.FREE) && (slot.getType().equals(car.getType())))
                    freeSlotsGivenType.add(slot);
            }
            slotsStatusMap.put(Slot.State.FREE, new HashMap<String, ArrayList<Slot>>());
            slotsStatusMap.get(Slot.State.FREE).put(car.getType(), freeSlotsGivenType);
        }

        if (containCarsMap.containsKey(car))
        {
            LOGGER.log(Level.INFO, "Car already in the parking");
            return false;
        }

        List<Slot> freeSlots = slotsStatusMap.get(Slot.State.FREE).get(car.getType());
        if(freeSlots.size() < 1)
        {
            LOGGER.log(Level.INFO, "No free slot available for the given car type");
            return false;
        }

        // Get first free slots according to car type, removing from the list of free slots
        Slot freeSlot = freeSlots.remove(0);

        // Set occupied by given car and its status is now "OCCUPIED"
        freeSlot.setOccupiedBy(car);
        freeSlot.setState(Slot.State.OCCUPIED);

        LocalDateTime insertingTime = LocalDateTime.now();
        freeSlot.setLastOccupiedTime(insertingTime);

        // Set slot as occupied by car
        containCarsMap.put(car, freeSlot);

        LOGGER.log(Level.INFO, "The car is into the parking at " + insertingTime);
        return true;
    }

    /**
     * The function is removing the given car out of the appropriate slot and bills the customer.
     * When the car is removed, the slot is set as FREE and the structures are set up according to the state.
     * The slots store the time of exiting by using LocalDateTime now.
     * The function returns true if the car is removed correctly.
     * @param car a car
     * @return true if the car is removed and the customer is billed
     * @throws WrongDateException Wrong date exception
     * @throws WrongInputException Wrong input exception
     * @see LocalDateTime
     */
    public boolean remove (Car car) throws WrongDateException, WrongInputException {

        if(containCarsMap == null)
            containCarsMap = new HashMap<Car, Slot>();

        if (slotsStatusMap == null)
        {
            slotsStatusMap = new HashMap<Slot.State, HashMap<String, ArrayList<Slot>>>();
            slotsStatusMap.put(Slot.State.FREE, new HashMap<String, ArrayList<Slot>>());
        }

        if (!containCarsMap.containsKey(car))
        {
            LOGGER.log(Level.INFO, "Car not present in the parking");
            return false;
        }

        // If a car is present in containCarsMap, it means that the data structure is initialized
        // Remove the occupied slot from the map
        Slot occupiedSlot = containCarsMap.remove(car);
        // Insert the slot in the "FREE" list
        slotsStatusMap.get(Slot.State.FREE).get(car.getType()).add(occupiedSlot);

        // Set occupied by null and its status is now "FREE"
        occupiedSlot.setOccupiedBy(null);
        occupiedSlot.setState(Slot.State.FREE);

        LocalDateTime removingTime = LocalDateTime.now();
        // Get amount of hour to pay
        long nHoursToPay = getHoursDifferenceToPay(occupiedSlot.getLastOccupiedTime(), removingTime);

        long amount = pricingPolicy.billCustomer(nHoursToPay);

        LOGGER.log(Level.INFO, "The car is out of the parking at "
                + removingTime + " paying " + amount + " euros");
        return true;
    }

}
