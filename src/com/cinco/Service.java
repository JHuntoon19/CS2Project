package com.cinco;

/**
 * Author: Judah Huntoon Date: 2026-02-11 Purpose: Represents a service which is
 * a type of item
 */
public class Service extends Item {
	private double costPerHour;

	public Service(String UUID, String name, String type, double costPerHour) {
		super(UUID, name, type);
		this.costPerHour = costPerHour;
	}

	public double getCostPerHour() {
		return costPerHour;
	}

}
