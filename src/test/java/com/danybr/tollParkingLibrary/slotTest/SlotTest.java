package com.danybr.tollParkingLibrary.slotTest;

import com.danybr.tollParkingLibrary.exception.WrongInputException;
import com.danybr.tollParkingLibrary.model.slot.ElectricSlot;
import com.danybr.tollParkingLibrary.model.slot.SedanSlot;
import com.danybr.tollParkingLibrary.model.slot.Slot;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SlotTest {

    private Slot slot;

    @Test
    public void SedanSlotInit() throws WrongInputException {
        slot = new SedanSlot();
        assertEquals("S", slot.getType());
    }

    @Test(expected = WrongInputException.class)
    public void ElecticSlotInitThrowsPowerException() throws WrongInputException
    {
        // Values must be 20 or 50.
        slot = new ElectricSlot("E-30");
    }

    @Test
    public void ElecticSlotInit() throws WrongInputException {
        slot = new ElectricSlot("E-20");

        assertEquals("E-20", slot.getType());
        assertEquals(Slot.State.FREE, slot.getState());
        assertNull(slot.getLastOccupiedTime());
        assertNull(slot.getOccupiedBy());
        ElectricSlot eSlot = (ElectricSlot) slot;
        assertEquals(20, eSlot.getAdapterPower());

        slot = new ElectricSlot("E-50");
        assertEquals("E-50", slot.getType());

    }

}

