package com.cinco;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Author: Judah Huntoon Date: 2026-02-11 Purpose: Represents a piece of
 * Equipment which is a type of item
 */
public class Equipment extends Item {
	private BigDecimal costPerUnit;
	private Boolean purchased;
	private int count;

	public Equipment(String UUID, String name, String type, String costPerUnit) {
		super(UUID, name, type);
		this.costPerUnit = new BigDecimal(costPerUnit);
	}

	public BigDecimal getCostPerUnit() {
		return costPerUnit;
	}

	public void setCount(int c) {
		count = c;
	}

	public void setPurchased(Boolean p) {
		purchased = p;
	}

	@Override
	public BigDecimal getCost() {
		if (purchased) {
			return getCostPerUnit().multiply(BigDecimal.valueOf(count)).setScale(2, RoundingMode.HALF_UP);
		} else {
			return getCostPerUnit().multiply(BigDecimal.valueOf(count)).multiply(BigDecimal.valueOf(1.5)).setScale(2,
					RoundingMode.HALF_UP);
		}
	}

	public BigDecimal getTaxes() {
		if (purchased) {
			return getCost().multiply(BigDecimal.valueOf(0.0525)).setScale(2, RoundingMode.HALF_UP);
		} else {
			if (getCost().doubleValue() < 2000) {
				return BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP);
			} else if (getCost().doubleValue() < 7000) {
				return BigDecimal.valueOf(175).setScale(2, RoundingMode.HALF_UP);
			} else {
				return BigDecimal.valueOf(350).setScale(2, RoundingMode.HALF_UP);
			}
		}
	}

	public BigDecimal getTotal() {
		if (purchased) {
			return getCost().add(getTaxes()).setScale(2, RoundingMode.HALF_UP);
		} else {
			return getCost().divide(BigDecimal.valueOf(36)).add(getTaxes()).setScale(2, RoundingMode.HALF_UP);
		}
	}

}
