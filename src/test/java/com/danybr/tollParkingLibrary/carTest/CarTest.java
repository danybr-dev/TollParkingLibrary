package com.danybr.tollParkingLibrary.carTest;

import com.danybr.tollParkingLibrary.exception.PowerException;
import com.danybr.tollParkingLibrary.exception.WrongInputException;
import com.danybr.tollParkingLibrary.model.car.Car;
import com.danybr.tollParkingLibrary.model.car.ElectricCar;
import com.danybr.tollParkingLibrary.model.car.SedanCar;
import org.junit.Test;

import static org.junit.Assert.*;

public class CarTest {

    private Car car;

    @Test
    public void SedanCarInit()
    {
        car = new SedanCar();
        assertEquals("S", car.getType());
    }

    @Test(expected = PowerException.class)
    public void ElecticCarInitThrowsPowerException() throws PowerException
    {
        // Values must be 20 or 50.
        car = new ElectricCar(30);
    }

    @Test
    public void ElecticCarInit() throws PowerException {
        car = new ElectricCar(20);
        assertEquals("E-20", car.getType());
        car = new ElectricCar(50);
        assertEquals("E-50", car.getType());
    }



}