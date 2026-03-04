package com.cinco;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents a purchased item and is a type of equipment. Does math
 * calculations based off of a purhcased item
 */
public class PurchaseEquipment extends Equipment {

	public PurchaseEquipment(Equipment e) {
		super(e.getUUID(), e.getName(), "e", e.getCostPerUnit());
		;
	}

	/**
	 * Returns the subtotal for the equipment: count * costPerUnit
	 */
	public BigDecimal getCost() {
		return getCostPerUnit().multiply(BigDecimal.valueOf(getCount())).setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * Returns the taxes based off of the subtotal
	 */
	public BigDecimal getTaxes() {
		return getCost().multiply(BigDecimal.valueOf(0.0525)).setScale(2, RoundingMode.HALF_UP);

	}

	/**
	 * Returns the subtotal plus taxes for full total
	 */
	public BigDecimal getTotal() {
		return getCost().add(getTaxes()).setScale(2, RoundingMode.HALF_UP);

	}

	/**
	 * Returns a string in a purchased equipment style
	 */
	@Override
	public String toString() {
		return String.format("%s (Purchase) %7s\n  %d units @ $%.2f\n%64s$%10.2f $%10.2f", getUUID(), getName(),
				getCount(), getCostPerUnit().doubleValue(), "", getTaxes(), getTotal());

	}
}
