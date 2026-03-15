package com.cinco;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents a service item to be added to an invoice. Claculate totals based
 * off of the billed hours
 */
public class InvoiceService extends Item {
	private BigDecimal costPerHour;
	private Person servicePerson;
	private double billedHours;

	/**
	 * constructor given a service. Used when adding invoice items. Also given
	 * billed hours and a service person
	 * 
	 * @param s
	 */
	public InvoiceService(Service s, double billedHours, Person servicePerson) {
		super(s.getUUID(), s.getName());
		this.costPerHour = s.getCostPerHour();
		this.billedHours = billedHours;
		this.servicePerson = servicePerson;
	}

	public BigDecimal getCostPerHour() {
		return costPerHour;
	}

	public Person getServicePerson() {
		return servicePerson;
	}

	public double getBilledHours() {
		return billedHours;
	}

	public void setServicePerson(Person person) {
		servicePerson = person;
	}

	public void setBilledHours(double billedHours) {
		this.billedHours = billedHours;
	}

	/**
	 * Returns the subtotal for the month
	 */
	@Override
	public BigDecimal getCost() {
		return costPerHour.multiply(BigDecimal.valueOf(billedHours)).add(BigDecimal.valueOf(125)).setScale(2,
				RoundingMode.HALF_UP);
	}

	/**
	 * Returns the taxes based off of the subtotal
	 */
	@Override
	public BigDecimal getTaxes() {
		return getCost().multiply(BigDecimal.valueOf(0.0315)).setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * Returns the subtotal + taxes for a full total
	 */
	@Override
	public BigDecimal getTotal() {
		return getCost().add(getTaxes()).setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * Returns a formatted String in the service item style
	 */
	@Override
	public String toString() {
		return String.format("%s (Service) %7s\n  %f hours @ $%.2f/unit\nServiced by %s\n%64s$%10.2f $%10.2f",
				getUUID(), getName(), getBilledHours(), getCostPerHour().doubleValue(), getServicePerson().getName(),
				"", getTaxes(), getCost());

	}
}
