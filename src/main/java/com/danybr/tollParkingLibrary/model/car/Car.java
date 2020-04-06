package com.danybr.tollParkingLibrary.model.car;

// Abstract parent class
public abstract class Car
{
    private String type; //It could be S or E

    public Car(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    /** No setter for type, Car type is immutable **/
}
