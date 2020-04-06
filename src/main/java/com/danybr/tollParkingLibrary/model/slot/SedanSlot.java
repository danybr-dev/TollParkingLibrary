package com.danybr.tollParkingLibrary.model.slot;

import com.danybr.tollParkingLibrary.exception.WrongInputException;
import com.danybr.tollParkingLibrary.model.car.SedanCar;

public class SedanSlot extends Slot
{
    private SedanCar occupiedBy;

    public SedanSlot() throws WrongInputException {
        super("S");
        occupiedBy = null;
    }

}
