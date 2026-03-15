package com.cinco;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * License Item to be added to an Invoice and calculates totals based off of a
 * start date and an end date
 */
public class InvoiceLicense extends Item {
	private BigDecimal serviceFee;
	private BigDecimal annualFee;
	private LocalDate startDate;
	private LocalDate endDate;

	/**
	 * Constructor given a license and a start + end date invoice items
	 * 
	 * @param l
	 */
	public InvoiceLicense(License l, LocalDate startDate, LocalDate endDate) {
		super(l.getUUID(), l.getName());
		this.serviceFee = l.getServiceFee();
		this.annualFee = l.getAnnualFee();
		this.startDate = startDate;
		this.endDate = endDate;
	}

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

	public int getNumberOfDays() {
		return (int) startDate.until(endDate, ChronoUnit.DAYS) + 1;
	}

	/**
	 * Returns the subtotal of the license
	 */
	@Override
	public BigDecimal getCost() {
		return annualFee.divide(BigDecimal.valueOf(365), 10, RoundingMode.HALF_UP)
				.multiply(BigDecimal.valueOf(getNumberOfDays())).add(serviceFee).setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * Returns 0 because Licenses have no taxes
	 */
	@Override
	public BigDecimal getTaxes() {
		return BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * Returns the subtotal calulated by getCost(); because taxes are 0
	 */
	@Override
	public BigDecimal getTotal() {
		return getCost();
	}

	/**
	 * Returrns a formatted String in the Liicense item style
	 */
	@Override
	public String toString() {
		return String.format(
				"%s (License) %7s\n  %d days (%s -> %s) @ $%.2f /year\nService Fee: $%.2f\n%64s$%10.2f $%10.2f",
				getUUID(), getName(), getNumberOfDays(), getStartDate().toString(), getEndDate().toString(),
				getAnnualFee(), getServiceFee(), "", getTaxes(), getCost());

	}
}
