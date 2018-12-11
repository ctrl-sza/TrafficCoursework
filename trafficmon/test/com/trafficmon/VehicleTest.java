package com.trafficmon;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


class VehicleTest {

    @Test
    void checkToStringMethod() {
        Vehicle vehicle = Vehicle.withRegistration("A123 XYZ");
        assertThat(vehicle.toString(), is("Vehicle [A123 XYZ]"));
    }


    @Test
    void checkEqualsMethod() {
        Vehicle vehicle1 = Vehicle.withRegistration("C783 4TT");
        Vehicle vehicle2 = Vehicle.withRegistration("C783 4TT");
        Vehicle vehicle3 = Vehicle.withRegistration("F773 3RE");

        Assert.assertTrue(vehicle1.equals(vehicle1));
        Assert.assertFalse(vehicle1.equals(vehicle3));
        Assert.assertTrue(vehicle2.equals(vehicle1));
    }


    @Test
    void checkHashCodeMethod() {

    Vehicle vehicle1 = Vehicle.withRegistration(null);
    Assert.assertEquals(0, vehicle1.hashCode());
    Vehicle vehicle2 = Vehicle.withRegistration("C783 4TT");
    Assert.assertEquals(vehicle2.hashCode(), vehicle2.getRegistration().hashCode());
    }


}