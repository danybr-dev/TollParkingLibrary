package com.danybr.tollParkingLibrary.model.slot;

import com.danybr.tollParkingLibrary.exception.WrongInputException;
import com.danybr.tollParkingLibrary.model.car.ElectricCar;

public class ElectricSlot extends Slot
{
    private int adapterPower;
    private ElectricCar occupiedBy;

    /**
     * Creates an Electric Slot of the given type.
     * The type allowed are "E-20" or "E-50"
     * @param type slot type as string, "E-20" or "E-50"
     * @throws WrongInputException wrong input exception
     */
    public ElectricSlot(String type) throws WrongInputException {
        super(type);
        if (type.equals("E-20"))
            this.adapterPower = 20;
        else if (type.equals("E-50"))
            this.adapterPower = 50;
        occupiedBy = null;
    }

    public int getAdapterPower()
    {
        return adapterPower;
    }

    /** No setter because the slot adapter is immutable **/


}
