package com.cinco;

/**
 * Author: Judah Huntoon Date: 2026-02-11 Purpose: Represents a License which is
 * a type of item
 */
public class License extends Item {
	private double serviceFee;
	private double annualFee;

	public License(String UUID, String name, String type, double serviceFee, double annualFee) {
		super(UUID, name, type);
		this.serviceFee = serviceFee;
		this.annualFee = annualFee;
	}

	public double getServiceFee() {
		return serviceFee;
	}

	public double getAnnualFee() {
		return annualFee;
	}
}
