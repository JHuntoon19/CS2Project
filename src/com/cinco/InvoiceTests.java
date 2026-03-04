package com.cinco;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * JUnit test suite for the invoice system.
 */
public class InvoiceTests {

	public static final double TOLERANCE = 0.001;

	/**
	 * Tests the subtotal, tax total and grand total values of an invoice in the
	 * system.
	 */
	@Test
	public void testInvoice01() {

		// Creates 25 purchased equipment (Monitors)
		UUID uuid = UUID.fromString("dca4b8e6-9c13-4b1d-aeef-1e3f237f9a9a");
		String name = "Monitor";
		double pricePerUnit = 199.99;
		Equipment e = new Equipment(uuid.toString(), name, "e", Double.toString(pricePerUnit));
		PurchaseEquipment pe = new PurchaseEquipment(e);
		pe.setCount(25);

		// Creates a delivery service
		uuid = UUID.fromString("864c15b2-c21f-4cb9-95de-97fd245fa473");
		name = "Delivery";
		double costPerHour = 50;

		// Filler person to test the invoice
		Person servicePerson = new Person("1f3a9c2e-6c4e-4c8b-9a2a-1c9c6f5b7e21", "Frodo", "Baggins", "",
				new ArrayList<String>());
		double billedHours = 3.5;
		Service ser = new Service(uuid.toString(), name, "S", Double.toString(costPerHour));
		ser.setBilledHours(billedHours);
		ser.setServicePerson(servicePerson);

		// Creates a license for SM office
		uuid = UUID.fromString("b4f02d75-d29b-468d-a244-1a1e3d5d92a6");
		name = "SM Office";
		String serviceFee = "25.99";
		String annualFee = "1212.12";
		License l = new License(uuid.toString(), name, "L", serviceFee, annualFee);
		l.setStartDate("2026-01-01");
		l.setEndDate("2026-06-30");

		// Creates an instance of invoice and adds these 3 items to it
		ArrayList<Item> items = new ArrayList<>();
		items.add(pe);
		items.add(ser);
		items.add(l);
		Address a = new Address("12 Bagshot Row", "Hobbiton", "CA", "95001");
		Company c = new Company("9a1f6c2d-4e7b-4a1e-8d3b-5f2c9e7a1b44", "Shire Agricultural", servicePerson, a);
		UUID iuuid = UUID.fromString("e6c9d8e0-bb5a-4b5f-b1b6-d07a547fcf25");
		Invoice invoice = new Invoice(iuuid, LocalDate.now(), c, servicePerson, items);

		// Expected results

		double expectedSubtotal = 5926.82;
		double expectedTaxTotal = 271.94;
		double expectedGrandTotal = 6198.76;

		// Invoice's real results
		double actualSubtotal = invoice.getSubtotal();
		double actualTaxTotal = invoice.getTax();
		double actualGrandTotal = invoice.getTotal();

		// compares the expected to the actual
		assertEquals(expectedSubtotal, actualSubtotal, TOLERANCE);
		assertEquals(expectedTaxTotal, actualTaxTotal, TOLERANCE);
		assertEquals(expectedGrandTotal, actualGrandTotal, TOLERANCE);

	}

	/**
	 * Tests the subtotal, tax total and grand total values of an invoice in the VGB
	 * system.
	 */
	@Test
	public void testInvoice02() {

		// Create an instance of leased equipment with the data values
		UUID uuid = UUID.fromString("dca4b8e6-9c13-4b1d-aeef-1e3f237f9a9a");
		String name = "Monitor";
		double pricePerUnit = 199.99;
		Equipment e = new Equipment(uuid.toString(), name, "E", Double.toString(pricePerUnit));
		LeaseEquipment le = new LeaseEquipment(e);
		le.setCount(25);

		// Creates a license for MiddleEarth Hosting
		uuid = UUID.fromString("2c3d4e5f-6a7b-48c9-0d1e-2f3a4b5c6d7e");
		name = "MiddleEarth Hosting";
		String serviceFee = "120";
		String annualFee = "2400";
		License l = new License(uuid.toString(), name, "L", serviceFee, annualFee);
		l.setStartDate("2026-01-01");
		l.setEndDate("2026-07-31");

		// Filler person for the Invoice
		Person servicePerson = new Person("1f3a9c2e-6c4e-4c8b-9a2a-1c9c6f5b7e21", "Frodo", "Baggins", "",
				new ArrayList<String>());

		// Creates an instance of invoice and adds these 2 items to it
		ArrayList<Item> items = new ArrayList<>();
		items.add(le);
		items.add(l);
		Address a = new Address("77 Woodland Path", "Greenwood", "OR", "97035");
		Company c = new Company("5c2d7a9e-1b4f-4e8c-a3d2-7f9b6c1e2a66", "Mirkwood Archery Supply", servicePerson, a);
		UUID iuuid = UUID.fromString("d4f1c30d-0427-4d92-86d5-931865cd73f4");
		Invoice invoice = new Invoice(iuuid, LocalDate.now(), c, servicePerson, items);

		// Expected results
		double expectedSubtotal = 9013.60;
		double expectedTaxTotal = 350;
		double expectedGrandTotal = 2072.29;

		// Invoice's real results
		double actualSubtotal = invoice.getSubtotal();
		double actualTaxTotal = invoice.getTax();
		double actualGrandTotal = invoice.getTotal();
		// Compares the Invoice's actual to expected
		assertEquals(expectedSubtotal, actualSubtotal, TOLERANCE);
		assertEquals(expectedTaxTotal, actualTaxTotal, TOLERANCE);
		assertEquals(expectedGrandTotal, actualGrandTotal, TOLERANCE);

	}

}
