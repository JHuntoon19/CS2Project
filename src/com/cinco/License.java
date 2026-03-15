package com.cinco;

import java.math.BigDecimal;

/**
 * Author: Judah Huntoon Date: 2026-02-11 Purpose: Represents a License which is
 * a type of item
 */
public class License extends Data {
	private BigDecimal serviceFee;
	private BigDecimal annualFee;

	public License(String UUID, String name, String serviceFee, String annualFee) {
		super(UUID, name);
		this.serviceFee = new BigDecimal(serviceFee);
		this.annualFee = new BigDecimal(annualFee);
	}

	public void setServiceFee(BigDecimal serviceFee) {
		this.serviceFee = serviceFee;
	}

	public void setAnnualFee(BigDecimal annualFee) {
		this.annualFee = annualFee;
	}

	public BigDecimal getServiceFee() {
		return serviceFee;
	}

	public BigDecimal getAnnualFee() {
		return annualFee;
	}

	/**
	 * Returrns a formatted String in the Liicense item style
	 */
	@Override
	public String toString() {
		return String.format("%s (License) %7s\n  %s\n $%.2f /year\nService Fee: $%.2f", getUUID(), getName(),
				getAnnualFee(), getServiceFee());

	}
}
