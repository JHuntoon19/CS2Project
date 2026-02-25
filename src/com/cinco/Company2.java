/**
 * Author: Judah Huntoon
 * Date: 2026-02-11
 * Purpose: represents a Company which is a type of Data
 */
package com.cinco;

public class Company2 extends Data{
	private String contactUUID;
	private Address address;
	public Company2(String UUID, String name, String contactUUID, Address address) {
		super(UUID, name);
		this.contactUUID = contactUUID;
		this.address = address;
	}
	public String getContactUUID() {
		return contactUUID;
	}
	public String getAddress() {
		return address.toString();
	}

}
