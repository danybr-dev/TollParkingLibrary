package com.danybr.tollParkingLibrary.model.slot;

import com.danybr.tollParkingLibrary.exception.WrongInputException;
import com.danybr.tollParkingLibrary.model.car.Car;
import java.time.LocalDateTime;

public abstract class Slot
{
    public enum State {
        FREE,
        OCCUPIED
    }

    private String type; // S, E-20, E-50
    private State state;
    private Car occupiedBy;
    private LocalDateTime lastOccupiedTime;

    /**
     * Creates a Slot object.
     * Allowed types are: "S", "E-20" or "E-50"
     * @param type a string with one of the values: "S", "E-20" or "E-50"
     * @throws WrongInputException type not allowed
     */
    public Slot(String type) throws WrongInputException
    {
        if (type.equals("S") || type.equals("E-20") || type.equals("E-50"))
            this.type = type;
        else
            throw new WrongInputException(type + " type is not allowed");
        state = State.FREE;
        occupiedBy = null;
        lastOccupiedTime = null;
    }

    public String getType()
    {
        return type;
    }

    /** No setter for type, Car type is immutable **/


    public State getState() {
        return state;
    }


    public void setState(State state) {
        this.state = state;
    }

    public Car getOccupiedBy()
    {
        return occupiedBy;
    }

    public void setOccupiedBy(Car occupiedBy) throws WrongInputException
    {
        if ((occupiedBy != null) && (!occupiedBy.getType().equals(type)))
            throw new WrongInputException("Car of type " + occupiedBy.getType()
                    + " can not occupy slot of type " + type);

        // occupiedBy by can be null
        this.occupiedBy = occupiedBy;
    }

    public LocalDateTime getLastOccupiedTime() {
        return lastOccupiedTime;
    }

    public void setLastOccupiedTime(LocalDateTime lastOccupiedTime){
        this.lastOccupiedTime = lastOccupiedTime;
    }

}
