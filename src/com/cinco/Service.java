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

	public BigDecimal getCostPerHour() {
		return costPerHour;
	}

	public void setServicePerson(Person person) {
		servicePerson = person;
	}

	public void setBilledHours(double billedHours) {
		this.billedHours = billedHours;
	}

	@Override
	public BigDecimal getCost() {
		return costPerHour.multiply(BigDecimal.valueOf(billedHours)).add(BigDecimal.valueOf(125)).setScale(2,
				RoundingMode.HALF_UP);
	}

	@Override
	public BigDecimal getTaxes() {
		return getCost().multiply(BigDecimal.valueOf(0.0315)).setScale(2, RoundingMode.HALF_UP);
	}

	@Override
	public BigDecimal getTotal() {
		return getCost().add(getTaxes()).setScale(2, RoundingMode.HALF_UP);
	}

	public Person getServicePerson() {
		return servicePerson;
	}

	public double getBilledHours() {
		return billedHours;
	}

	@Override
	public String toString() {
		return String.format("%s (Service) %7s\n  %f hours @ $%f/unit\nServiced by %s", getUUID(), getName(),
				getBilledHours(), getCostPerHour().doubleValue(), getServicePerson().getName());

	}
}
