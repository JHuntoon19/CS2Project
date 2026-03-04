package com.cinco;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents a leased item which is a type of equipment. Does calculations
 * based off of leased equipment
 */
public class LeaseEquipment extends Equipment {
	public LeaseEquipment(Equipment e) {
		super(e.getUUID(), e.getName(), "e", e.getCostPerUnit());
	}

	/**
	 * Returns the subtotal of the leased equipment over 3 years
	 */
	public BigDecimal getCost() {
		return getCostPerUnit().multiply(BigDecimal.valueOf(getCount())).multiply(BigDecimal.valueOf(1.5)).setScale(2,
				RoundingMode.HALF_UP);
	}

	/**
	 * Returns the taxes based off of the subtotals final cost and tax bracket
	 */
	public BigDecimal getTaxes() {
		if (getCost().doubleValue() < 2000) {
			return BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP);
		} else if (getCost().doubleValue() < 7000) {
			return BigDecimal.valueOf(175).setScale(2, RoundingMode.HALF_UP);
		} else {
			return BigDecimal.valueOf(350).setScale(2, RoundingMode.HALF_UP);
		}
	}

	/**
	 * Returns the total due for the month
	 */
	public BigDecimal getTotal() {
		return getCost().divide(BigDecimal.valueOf(36), 10, RoundingMode.HALF_UP).add(getTaxes()).setScale(2,
				RoundingMode.HALF_UP);
	}

	/**
	 * Formatted string of the leased equipment style
	 */
	@Override
	public String toString() {
		return String.format("%s (Lease) %7s\n  %d units\n%64s$%10.2f $%10.2f", getUUID(), getName(), getCount(), "",
				getTaxes(), getTotal());

	}
}
