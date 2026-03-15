package com.cinco;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Equipment that is purchased and placed on an invoice. Calculates totals based
 * off of a count
 */
public class InvoicePurchaseEquipment extends Equipment {
	public InvoicePurchaseEquipment(Equipment e, int count) {
		super(e.getUUID(), e.getName(), e.getCostPerUnit());
		this.setCount(count);
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
				getCount(), getCostPerUnit().doubleValue(), "", getTaxes(), getCost());

	}

}
