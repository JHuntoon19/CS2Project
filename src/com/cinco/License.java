package com.cinco;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Author: Judah Huntoon Date: 2026-02-11 Purpose: Represents a License which is
 * a type of item
 */
public class License extends Item {
	private BigDecimal serviceFee;
	private BigDecimal annualFee;
	private LocalDate startDate;
	private LocalDate endDate;

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public void setServiceFee(BigDecimal serviceFee) {
		this.serviceFee = serviceFee;
	}

	public void setAnnualFee(BigDecimal annualFee) {
		this.annualFee = annualFee;
	}

	public License(String UUID, String name, String type, String serviceFee, String annualFee) {
		super(UUID, name, type);
		this.serviceFee = new BigDecimal(serviceFee);
		this.annualFee = new BigDecimal(annualFee);
	}

	public BigDecimal getServiceFee() {
		return serviceFee;
	}

	public BigDecimal getAnnualFee() {
		return annualFee;
	}

	public void setStartDate(String date) {
		startDate = LocalDate.parse(date);
	}

	public void setEndDate(String date) {
		endDate = LocalDate.parse(date);
	}

	private int getNumberOfDays() {
		return (int) startDate.until(endDate, ChronoUnit.DAYS);
	}

	@Override
	public BigDecimal getCost() {
		return annualFee.divide(BigDecimal.valueOf(365)).multiply(BigDecimal.valueOf(getNumberOfDays()))
				.add(BigDecimal.valueOf(25.99)).setScale(2, RoundingMode.HALF_UP);
	}

	@Override
	public BigDecimal getTaxes() {
		return BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP);
	}

	@Override
	public BigDecimal getTotal() {
		return getCost().setScale(2, RoundingMode.HALF_UP);
	}
}
