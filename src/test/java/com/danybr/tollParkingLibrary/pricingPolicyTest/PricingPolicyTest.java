package com.danybr.tollParkingLibrary.pricingPolicyTest;

import com.danybr.tollParkingLibrary.exception.PowerException;
import com.danybr.tollParkingLibrary.exception.WrongInputException;
import com.danybr.tollParkingLibrary.model.pricingPolicy.PricingPolicy;
import com.danybr.tollParkingLibrary.model.pricingPolicy.PricingPolicyFixedAndHour;
import com.danybr.tollParkingLibrary.model.pricingPolicy.PricingPolicyHour;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PricingPolicyTest {
    private PricingPolicy pricingPolicy;

    @Test
    public void PricingPolicyHourInit() throws WrongInputException {
        pricingPolicy = new PricingPolicyHour(2);
        // Bill customer for 5 hours
        assertEquals(2*5, pricingPolicy.billCustomer(5));

        PricingPolicyHour pricingPolicyHour = (PricingPolicyHour) pricingPolicy;
        // Changing price per hour from 2 to 3
        pricingPolicyHour.setHourPrice(3);
        // Bill customer for 5 hours
        assertEquals(3*5, pricingPolicyHour.billCustomer(5));

    }

    @Test(expected = WrongInputException.class)
    public void PricingPolicyWithPriceZero() throws  WrongInputException {
        // Values must be greater then 0
        pricingPolicy = new PricingPolicyHour(0);
    }

    @Test
    public void PricingPolicyFixedAndHourInit() throws WrongInputException {
        // Creating a pricing policy with fixed amount of 5 euros and 1 euro per hour
        pricingPolicy = new PricingPolicyFixedAndHour(5, 1);
        // Bill customer for 5 hours
        assertEquals(5 + 1*5, pricingPolicy.billCustomer(5));

        PricingPolicyFixedAndHour pricingPolicyFixedAndHour = (PricingPolicyFixedAndHour) pricingPolicy;
        // Changing price per hour from 1 to 3
        pricingPolicyFixedAndHour.setHourPrice(3);
        // Bill customer for 5 hours
        assertEquals(5 + 3*5, pricingPolicyFixedAndHour.billCustomer(5));
    }

}
