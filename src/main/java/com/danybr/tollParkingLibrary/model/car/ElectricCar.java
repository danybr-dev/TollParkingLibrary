package com.danybr.tollParkingLibrary.model.car;

import com.danybr.tollParkingLibrary.exception.PowerException;

public class ElectricCar extends Car
{
    private int power; // Defining Electric car power in Kw

    /**
     * Creates an Electric car object from the given power.
     * Allowed values are 20 or 50 (kW)
     * @param power power of 20 or 50 as int
     * @throws PowerException Wrong power type, it must be 20 or 50 for ElectricCar
     */
    public ElectricCar(int power) throws PowerException
    {
        super("E-" + power);
        if (power == 20 || power == 50)
            this.power = power;
        else
            throw new PowerException("Wrong power type, it must be 20 or 50 for ElectricCar");
    }

    public int getPower()
    {
        return power;
    }

    /** No setter because the car power is immutable **/
}
