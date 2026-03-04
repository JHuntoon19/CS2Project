package com.cinco;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

/*
 * This class is used to load from csv files data about persons, companies, and items
 * Each method returns a map of the data's UUID to itself
 */
public class LoadData {

	/**
	 * Returns a map of persons loaded from the given file
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
	 * Returns a map of companies loaded from the given file
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
	 * Retruns a Map of items loaded from given file
	 * 
	 * @param fileName
	 * @return HashMap<UUID,Item>
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

	/**
	 * This loads invoices from a file and returns a map of invoices
	 * 
	 * @param fileName
	 * @param companies
	 * @param persons
	 * @return HashMap<UUID,Invoices>
	 */
	public static HashMap<UUID, Invoice> loadInvoices(String fileName, HashMap<UUID, Company> companies,
			HashMap<UUID, Person> persons) {
		HashMap<UUID, Invoice> invoices = new HashMap<>();
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
			String customerUUID = tokens[1];
			String salesPersonUUID = tokens[2];
			String date = tokens[3];
			Company c = companies.get(UUID.fromString(customerUUID));
			Person p = persons.get(UUID.fromString(salesPersonUUID));
			Invoice i = new Invoice(uuid, date, c, p);
			invoices.put(UUID.fromString(uuid), i);
		}
		s.close();
		return invoices;
	}

	/**
	 * Adds invoice specific items into invoices from an invoice item text file
	 * 
	 * @param fileName
	 * @param invoices
	 * @param items
	 * @param persons
	 */
	public static void loadInvoiceItems(String fileName, HashMap<UUID, Invoice> invoices, HashMap<UUID, Item> items,
			HashMap<UUID, Person> persons) {
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
			String invoiceUUID = tokens[0];
			String itemUUID = tokens[1];
			String itemSpecific = tokens[2];
			// For each item the following fields are item specific
			// Checks if the item is purchased equipment
			if (itemSpecific.toLowerCase().equals("p")) {
				// Adds an amount of purchased equipment to an invoices items list
				int numPurchased = Integer.parseInt(tokens[3]);
				PurchaseEquipment pe = new PurchaseEquipment((Equipment) items.get(UUID.fromString(itemUUID)));
				pe.setCount(numPurchased);
				invoices.get(UUID.fromString(invoiceUUID)).addItem(pe);

				// Checks if the item is licensed equipment
			} else if (itemSpecific.toLowerCase().equals("l")) {
				// Adds an amount of leased equipment to an invoices items list
				int numLeased = Integer.parseInt(tokens[3]);
				LeaseEquipment le = new LeaseEquipment((Equipment) items.get(UUID.fromString(itemUUID)));
				le.setCount(numLeased);
				invoices.get(UUID.fromString(invoiceUUID)).addItem(le);

				// Checks if the item is a service by checking if the next field is a uuid
			} else if (itemSpecific.length() == 36) {
				// Adds a set up service to an invoices items list
				double billedHours = Double.parseDouble(tokens[3]);
				Service ser = new Service((Service) items.get(UUID.fromString(itemUUID)));
				ser.setBilledHours(billedHours);
				ser.setServicePerson(persons.get(UUID.fromString(itemSpecific)));
				invoices.get(UUID.fromString(invoiceUUID)).addItem(ser);

				// only other item type is a license
			} else {
				// Adds a set up License to an invoices items list
				LocalDate startDate = LocalDate.parse(tokens[2]);
				LocalDate endDate = LocalDate.parse(tokens[3]);
				License l = new License((License) items.get(UUID.fromString(itemUUID)));
				l.setStartDate(startDate);
				l.setEndDate(endDate);
				invoices.get(UUID.fromString(invoiceUUID)).addItem(l);
			}
		}
		s.close();
	}
}
