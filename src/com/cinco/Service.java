package com.cinco;

import java.math.BigDecimal;

/**
 * Author: Judah Huntoon Date: 2026-02-11 Purpose: Represents a service which is
 * a type of item
 */
public class Service extends Data {
	private BigDecimal costPerHour;

	public Service(String UUID, String name, String costPerHour) {
		super(UUID, name);
		this.costPerHour = new BigDecimal(costPerHour);
	}

	public BigDecimal getCostPerHour() {
		return costPerHour;
	}

	/**
	 * Returns a formatted String in the service item style
	 */
	@Override
	public String toString() {
		return String.format("%s (Service) %7s\n @ $%.2f/unit", getUUID(), getName(), getCostPerHour().doubleValue());

	}
}
