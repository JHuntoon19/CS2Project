package com.cinco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * JUnit test suite for the invoice system.
 */
public class EntityTests {

	public static final double TOLERANCE = 0.001;

	/**
	 * Creates an instance of purchased equipment and tests if its cost and tax
	 * calculations are correct.
	 */
	@Test
	public void testEquipmentPurchase() {

		// data values
		UUID uuid = UUID.fromString("dca4b8e6-9c13-4b1d-aeef-1e3f237f9a9a");
		String name = "Monitor";
		double pricePerUnit = 199.99;

		// Creates an instance of equipment with the data values
		Equipment e = new Equipment(uuid.toString(), name, "e", Double.toString(pricePerUnit));
		PurchaseEquipment pe = new PurchaseEquipment(e);
		pe.setCount(25);
		// Establishes the expected cost and tax (rounded to nearest cent)
		double expectedCost = 4999.75;
		double expectedTax = 262.49;
		double expectedTotal = 5262.24;

		// Invokes methods to determine the cost/tax:
		double actualCost = pe.getCost().doubleValue();
		double actualTax = pe.getTaxes().doubleValue();
		double actualTotal = pe.getTotal().doubleValue();
		String s = pe.toString();

		// Determines if methods return the expected results
		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		assertEquals(expectedTotal, actualTotal, TOLERANCE);
		// ensures that the string representation contains necessary elements
		assertTrue(s.contains("Purchase"));
		assertTrue(s.contains("Monitor"));
		assertTrue(s.contains(Double.toString(pricePerUnit)));

	}

	/**
	 * * Creates an instance of leased equipment and tests if its cost and tax
	 * calculations are correct.
	 */
	@Test
	public void testEquipmentLease() {
		// data values
		UUID uuid = UUID.fromString("dca4b8e6-9c13-4b1d-aeef-1e3f237f9a9a");
		String name = "Monitor";
		double pricePerUnit = 199.99;

		// Creates an instance of equipment with the data values
		Equipment e = new Equipment(uuid.toString(), name, "E", Double.toString(pricePerUnit));
		LeaseEquipment le = new LeaseEquipment(e);
		le.setCount(25);
		// Establishes the expected cost and tax (rounded to nearest cent)
		double expectedCost = 7499.63;
		double expectedTax = 350;
		double expectedTotal = 558.32;

		// Invokes methods to determine the cost/tax:
		double actualCost = le.getCost().doubleValue();
		double actualTax = le.getTaxes().doubleValue();
		double actualTotal = le.getTotal().doubleValue();
		String s = le.toString();

		// Determines if methods return the expected results
		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		assertEquals(expectedTotal, actualTotal, TOLERANCE);
		// ensure that the string representation contains necessary elements
		assertTrue(s.contains("Lease"));
		assertTrue(s.contains("Monitor"));
	}

	@Test
	public void testService() {
		// data values
		UUID uuid = UUID.fromString("864c15b2-c21f-4cb9-95de-97fd245fa473");
		String name = "Delivery";
		double costPerHour = 50;
		// Filler person to test the toString method on a service
		Person servicePerson = new Person("1f3a9c2e-6c4e-4c8b-9a2a-1c9c6f5b7e21", "Frodo", "Baggins", "",
				new ArrayList<String>());
		double billedHours = 3.5;
		// Creates an instance of service with the data values
		Service ser = new Service(uuid.toString(), name, "S", Double.toString(costPerHour));
		ser.setBilledHours(billedHours);
		ser.setServicePerson(servicePerson);
		// Establishes the expected cost and tax (rounded to nearest cent)
		double expectedCost = 300;
		double expectedTax = 9.45;
		double expectedTotal = 309.45;

		// Invokes methods to determine the cost/tax:
		double actualCost = ser.getCost().doubleValue();
		double actualTax = ser.getTaxes().doubleValue();
		double actualTotal = ser.getTotal().doubleValue();
		String s = ser.toString();

		// Determines if methods return the expected results
		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		assertEquals(expectedTotal, actualTotal, TOLERANCE);
		// ensures that the string representation contains necessary elements
		assertTrue(s.contains("Service"));
		assertTrue(s.contains(servicePerson.getName()));
	}

	@Test
	public void testLicense() {
		// data values
		UUID uuid = UUID.fromString("b4f02d75-d29b-468d-a244-1a1e3d5d92a6");
		String name = "SM Office";
		String serviceFee = "25.99";
		String annualFee = "1212.12";
		// Creates an instance of license with the data values
		License l = new License(uuid.toString(), name, "L", serviceFee, annualFee);
		l.setStartDate("2026-01-01");
		l.setEndDate("2026-06-30");
		// Establishes the expected cost and tax (rounded to nearest cent)
		double expectedCost = 627.07;
		double expectedTax = 0;
		double expectedTotal = 627.07;

		// Invokes methods to determine the cost/tax:
		double actualCost = l.getCost().doubleValue();
		double actualTax = l.getTaxes().doubleValue();
		double actualTotal = l.getTotal().doubleValue();
		String s = l.toString();

		// Determines if methods return the expected results
		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		assertEquals(expectedTotal, actualTotal, TOLERANCE);
		// ensures that the string representation contains necessary elements
		assertTrue(s.contains("License"));
		assertTrue(s.contains(annualFee));
		assertTrue(s.contains(serviceFee));
		assertTrue(s.contains(l.getStartDate().toString()));
	}

}
