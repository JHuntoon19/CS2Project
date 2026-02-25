package com.cinco;

/**
 * Author: Judah Huntoon Date: 2026-02-11 Purpose: Represents a piece of
 * Equipment which is a type of item
 */
public class Equipment extends Item {
	private double costPerUnit;

	public Equipment(String UUID, String name, String type, double costPerUnit) {
		super(UUID, name, type);
		this.costPerUnit = costPerUnit;
	}

	public double getCostPerUnit() {
		return costPerUnit;
	}

}
