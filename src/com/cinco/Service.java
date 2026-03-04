package com.cinco;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Author: Judah Huntoon Date: 2026-02-11 Purpose: Represents a service which is
 * a type of item
 */
public class Service extends Item {
	private BigDecimal costPerHour;
	private Person servicePerson;
	private double billedHours;

	public Service(String UUID, String name, String type, String costPerHour) {
		super(UUID, name, type);
		this.costPerHour = new BigDecimal(costPerHour);
	}

	/**
	 * constructor given a service. Used when adding invoice items
	 * 
	 * @param s
	 */
	public Service(Service s) {
		super(s.getUUID(), s.getName(), "s");
		this.costPerHour = s.getCostPerHour();
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
				"", getTaxes(), getTotal());

	}
}
