package com.danybr.tollParkingLibrary.model.pricingPolicy;

import com.danybr.tollParkingLibrary.exception.WrongInputException;

public class PricingPolicyHour implements PricingPolicy
{
    private int hourPrice;

    /**
     * Creates an PricingPolicyHour object, passing a hour price to be paid by customer.
     * @param hourPrice an amount per hour greater then zero as int
     * @throws WrongInputException hour price must be greater then 0
     */
    public PricingPolicyHour(int hourPrice) throws WrongInputException
    {
        if(hourPrice <= 0)
            throw new WrongInputException("hour price must be greater then 0");
        this.hourPrice = hourPrice;
    }

    public int getHourPrice()
    {
        return hourPrice;
    }

    public void setHourPrice(int hourPrice) throws WrongInputException
    {
        if(hourPrice <= 0)
            throw new WrongInputException("hour price must be greater then 0");
        this.hourPrice = hourPrice;
    }

    public long billCustomer(long nHours) {
        return nHours * hourPrice;
    }
}
