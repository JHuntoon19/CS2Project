package com.cinco;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Author: Judah Huntoon Date: 2026-02-11 Purpose: Represents a piece of
 * Equipment which is a type of item
 */
public class Equipment extends Item {
	private BigDecimal costPerUnit;
	private int count;

// Constructors given either a UUID or a String for UUID
	public Equipment(String UUID, String name, String type, String costPerUnit) {
		super(UUID, name, type);
		this.costPerUnit = new BigDecimal(costPerUnit);
	}

	public Equipment(UUID uuid, String name, String type, BigDecimal costPerUnit) {
		super(uuid, name, type);
		this.costPerUnit = costPerUnit;
	}

	public BigDecimal getCostPerUnit() {
		return costPerUnit;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int c) {
		count = c;
	}

	// These are to be handled by the classes PurchaseEquipment and LeaseEquipment
	@Override
	public BigDecimal getCost() {
		return BigDecimal.ZERO;
	};

	@Override
	public String toString() {
		return String.format("%s ( ) %7s\n  %d units @ $%f", getUUID(), getName(), getCount(),
				getCostPerUnit().doubleValue());

	}

	@Override
	public BigDecimal getTaxes() {
		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal getTotal() {
		return BigDecimal.ZERO;
	}

}
