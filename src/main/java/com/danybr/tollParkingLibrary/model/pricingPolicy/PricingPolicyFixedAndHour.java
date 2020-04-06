package com.danybr.tollParkingLibrary.model.pricingPolicy;

import com.danybr.tollParkingLibrary.exception.WrongInputException;

public class PricingPolicyFixedAndHour implements PricingPolicy
{
    private int fixedAmount;
    private int hourPrice;

    /**
     * Creates an PricingPolicyFixedAndHour object, passing a fixed amount and hour price to be paid by customer.
     * @param fixedAmount a fixed amount as int
     * @param hourPrice an amount per hour greater then zero as int
     * @throws WrongInputException hour price must be greater then 0
     */
    public PricingPolicyFixedAndHour(int fixedAmount, int hourPrice) throws WrongInputException {
        if(hourPrice <= 0)
            throw new WrongInputException("hour price must be greater then 0");

        this.fixedAmount = fixedAmount;
        this.hourPrice = hourPrice;
    }

    public int getFixedAmount()
    {
        return fixedAmount;
    }

    public void setFixedAmount(int fixedAmount)
    {
        this.fixedAmount = fixedAmount;
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
        return fixedAmount + nHours * hourPrice;
    }
}
