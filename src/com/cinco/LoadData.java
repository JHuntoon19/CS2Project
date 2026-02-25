package com.cinco;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

public class LoadData {

	/**
	 * Returns a list of persons loaded from the given file
	 * 
	 * @param fileName
	 * @return HashMap<UUID,Person>
	 */
	public static HashMap<UUID, Person> loadPersons(String fileName) {
		HashMap<UUID, Person> persons = new HashMap<>();
		Scanner s = null;
		try {
			s = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		s.nextLine();
		while (s.hasNext()) {
			String line = s.nextLine();
			String tokens[] = line.split(",");
			String uuid = tokens[0];
			String firstName = tokens[1];
			String lastName = tokens[2];
			String phoneNumber = tokens[3];
			ArrayList<String> emails = new ArrayList<String>();
			int n = tokens.length;
			for (int i = 4; i < n; i++) {
				emails.add(tokens[i]);
			}
			Person p = new Person(uuid, firstName, lastName, phoneNumber, emails);
			persons.put(p.getUUID(), p);
		}
		s.close();
		return persons;
	}

	/**
	 * Returns a list of companies loaded from the given file
	 * 
	 * @param fileName
	 * @return HashMap<UUID,Company>
	 */
	public static HashMap<UUID, Company> loadCompanies(String fileName, HashMap<UUID, Person> persons) {
		HashMap<UUID, Company> companies = new HashMap<>();
		Scanner s = null;
		try {
			s = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		s.nextLine();
		while (s.hasNext()) {
			String line = s.nextLine();
			String tokens[] = line.split(",");
			String uuid = tokens[0];
			String cUUID = tokens[1];
			UUID contactUUID = UUID.fromString(cUUID);
			String name = tokens[2];
			String street = tokens[3];
			String city = tokens[4];
			String state = tokens[5];
			String zip = tokens[6];
			Address a = new Address(street, city, state, zip);
			Company c;
			if (persons.get(contactUUID) != null) {
				c = new Company(uuid, name, persons.get(contactUUID), a);
			} else {
				c = new Company(uuid, name, contactUUID, a);
			}
			companies.put(c.getUUID(), c);
		}
		s.close();
		return companies;
	}

	/**
	 * Retruns a list of items loaded from given file
	 * 
	 * @param fileName
	 * @return ArrayList<Item>
	 */
	public static HashMap<UUID, Item> loadItems(String fileName) {
		HashMap<UUID, Item> items = new HashMap<>();
		Scanner s = null;
		try {
			s = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		s.nextLine();
		while (s.hasNext()) {
			String line = s.nextLine();
			String tokens[] = line.split(",");
			String uuid = tokens[0];
			String type = tokens[1];
			String name = tokens[2];

			if (type.equals("E")) {
				String costPerUnit = tokens[3];
				Equipment e = new Equipment(uuid, name, type, costPerUnit);
				items.put(e.getUUID(), e);
			} else if (type.equals("S")) {
				String costPerHour = tokens[3];
				Service ser = new Service(uuid, name, type, costPerHour);
				items.put(ser.getUUID(), ser);
			} else if (type.equals("L")) {
				String serviceFee = tokens[3];
				String annualFee = tokens[4];
				License l = new License(uuid, name, type, serviceFee, annualFee);
				items.put(l.getUUID(), l);
			}
		}
		s.close();
		return items;
	}
}
