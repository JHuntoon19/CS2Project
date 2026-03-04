package com.cinco;

import java.util.UUID;

/**
 * Author: Judah Huntoon Date: 2026-02-11 Purpose: represents a Company which is
 * a type of Data
 */
public class Company extends Data {
	private UUID contactUUID;
	private Address address;
	private Person contactPerson;

	// Constructors based off of either given UUID or String for UUID
	public Company(String UUID, String name, Person contactPerson, Address address) {
		super(UUID, name);
		this.contactPerson = contactPerson;
		this.contactUUID = contactPerson.getUUID();
		this.address = address;
	}

	public Company(String UUID, String name, UUID contactUUID, Address address) {
		super(UUID, name);
		this.contactUUID = contactUUID;
		this.address = address;
	}

	public UUID getContactUUID() {
		return contactUUID;
	}

	public String getAddress() {
		return address.toString();
	}

	public Person getContactPerson() {
		return contactPerson;
	}

	@Override
	public String toString() {
		return String.format("%S  (%s)\n%s\nAddress: %s\n", getName(), getUUID(), getContactPerson().toString(),
				getAddress());
	}

}
