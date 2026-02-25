package com.cinco;

import java.util.ArrayList;

/**
 * Author: Judah Huntoon Date: 2026-02-11 Purpose: Represents a person data type
 */
public class Person extends Data {
	private String firstName;
	private String lastName;
	private String phoneNumber;

	private ArrayList<String> emails;

	public Person(String UUID, String name, String firstName, String lastName, String phoneNumber,
			ArrayList<String> emails) {
		super(UUID, name);
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.emails = emails;
	}

//	Constructor which only requires a firstname and a last name and stores it as a name for the data parent class
	public Person(String UUID, String firstName, String lastName, String phoneNumber, ArrayList<String> emails) {
		super(UUID, String.format("%s,%s", firstName, lastName));
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.emails = emails;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public ArrayList<String> getEmails() {
		return new ArrayList<String>(this.emails);
	}
}
