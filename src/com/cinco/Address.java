package com.cinco;

/**
 * Author: Judah Huntoon
 * Date: 2026-02-11
 * Purpose: Represents an address by strings of street, city, state, and zip
 */
public class Address {
	private String street;
	private String city;
	private String state;
	private String zip;
	public Address(String street, String city, String state, String zip) {
		super();
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
	public String getStreet() {
		return street;
	}
	public String getCity() {
		return city;
	}
	public String getState() {
		return state;
	}
	public String getZip() {
		return zip;
	}
	@Override
	public String toString() {
		return String.format("%s,%s,%s,%d",this.getStreet(),this.getCity(),this.getState(),this.getZip());
	}
}
