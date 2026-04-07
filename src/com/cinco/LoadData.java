package com.cinco;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

import org.apache.logging.log4j.Logger;

/*
 * This class is used to load from either csv files or the database with data about persons, companies, and items
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
	 * Returns a man of persons loaded from the database
	 * 
	 * @param cf
	 * @return HashMap<UUID,Person>
	 */
	public static HashMap<UUID, Person> loadPersonsFromDatabase(ConnectionFactory cf, Logger logger) {
		HashMap<UUID, Person> persons = new HashMap<>();
		Connection conn = cf.getConnection();
		// Get basic person data
		String query = "select personUUID, firstName, lastName, phoneNumber from Person";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			logger.info("Creating Persons");
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String uuid = rs.getString("personUUID");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				String phoneNumber = rs.getString("phoneNumber");
				Person p = new Person(uuid, firstName, lastName, phoneNumber);
				persons.put(p.getUUID(), p);
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			logger.error("Failed to create all Persons");
			throw new RuntimeException(e);
		}

		// Add emails for all persons
		query = "select address, personUUID from Email e join Person p on p.personId = e.personId";
		try {
			logger.info("Adding emails to persons");
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String uuid = rs.getString("personUUID");
				String address = rs.getString("address");
				persons.get(UUID.fromString(uuid)).addEmail(address);
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			logger.error("Failed to add all emails");
			throw new RuntimeException(e);
		}
		cf.putConnection(conn);
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
	 * Returns a map of companies loaded from the database
	 * 
	 * @param cf
	 * @param persons
	 * @return HashMap<UUID,Company>
	 */
	public static HashMap<UUID, Company> loadCompaniesFromDatabase(ConnectionFactory cf, Logger logger,
			HashMap<UUID, Person> persons) {
		HashMap<UUID, Company> companies = new HashMap<>();
		Connection conn = cf.getConnection();
		String query = "select c.companyUUID, c.name, c.street, c.city, s.stateCode as state, z.zipcode, p.personUUId from Company c join State s on s.stateId = c.stateId join Zipcode z on z.zipcodeId = c.zipcodeId join Person p on p.personId = c.personId;\r\n";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			logger.info("Creating Companies");
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String companyUUID = rs.getString("companyUUID");
				String compName = rs.getString("name");
				String street = rs.getString("street");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String zip = rs.getString("zipcode");
				String personUUID = rs.getString("personUUID");
				Address a = new Address(street, city, state, zip);
				Company c = new Company(companyUUID, compName, persons.get(UUID.fromString(personUUID)), a);
				companies.put(c.getUUID(), c);
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			logger.error("Failed to create all companies");
			throw new RuntimeException(e);
		}
		cf.putConnection(conn);
		return companies;
	}

	/**
	 * Retruns a Map of items loaded from given file
	 * 
	 * @param fileName
	 * @return HashMap<UUID,Item>
	 */
	public static HashMap<UUID, Data> loadItems(String fileName) {
		HashMap<UUID, Data> items = new HashMap<>();
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
				Equipment e = new Equipment(uuid, name, costPerUnit);
				items.put(e.getUUID(), e);
			} else if (type.equals("S")) {
				String costPerHour = tokens[3];
				Service ser = new Service(uuid, name, costPerHour);
				items.put(ser.getUUID(), ser);
			} else if (type.equals("L")) {
				String serviceFee = tokens[3];
				String annualFee = tokens[4];
				License l = new License(uuid, name, serviceFee, annualFee);
				items.put(l.getUUID(), l);
			}
		}
		s.close();
		return items;
	}

	/**
	 * Retruns a Map of items loaded from the database
	 * 
	 * @param fileName
	 * @return HashMap<UUID,Item>
	 */
	public static HashMap<UUID, Data> loadItemsFromDatabase(ConnectionFactory cf, Logger logger) {
		HashMap<UUID, Data> items = new HashMap<>();
		Connection conn = cf.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from Item";
		try {
			logger.info("Creating items");
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String itemUUID = rs.getString("itemUUID");
				String name = rs.getString("name");
				String type = rs.getString("type");
				if (type.equals("equipment")) {
					String costPerUnit = rs.getString("costPerUnit");
					Equipment e = new Equipment(itemUUID, name, costPerUnit);
					items.put(e.getUUID(), e);
				} else if (type.equals("service")) {
					String costPerHour = rs.getString("costPerHour");
					Service s = new Service(itemUUID, name, costPerHour);
					items.put(s.getUUID(), s);
				} else if (type.equals("license")) {
					String serviceFee = rs.getString("serviceFee");
					String annualFee = rs.getString("annualFee");
					License l = new License(itemUUID, name, serviceFee, annualFee);
					items.put(l.getUUID(), l);
				}
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			logger.error("Failed to create all items");
			throw new RuntimeException(e);
		}

		cf.putConnection(conn);
		;
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
	 * This loads invoices from the database and returns a map of invoices
	 * 
	 * @param fileName
	 * @param companies
	 * @param persons
	 * @return HashMap<UUID,Invoices>
	 */
	public static HashMap<UUID, Invoice> loadInvoicesFromDatabase(ConnectionFactory cf, Logger logger,
			HashMap<UUID, Company> companies, HashMap<UUID, Person> persons) {
		HashMap<UUID, Invoice> invoices = new HashMap<>();
		Connection conn = cf.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select i.invoiceUUID, c.companyUUID, p.personUUID, date from Invoice i join Company c on c.companyId = i.companyId join Person p on p.personId = i.personId";
		try {
			logger.info("Creating invoices");
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String uuid = rs.getString("invoiceUUID");
				String customerUUID = rs.getString("companyUUID");
				String salesPersonUUID = rs.getString("personUUID");
				String date = rs.getString("date");
				Company c = companies.get(UUID.fromString(customerUUID));
				Person p = persons.get(UUID.fromString(salesPersonUUID));
				Invoice i = new Invoice(uuid, date, c, p);
				invoices.put(UUID.fromString(uuid), i);
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			logger.error("Failed to create all invoices");
			throw new RuntimeException(e);
		}
		cf.putConnection(conn);
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
	public static void loadInvoiceItems(String fileName, HashMap<UUID, Invoice> invoices, HashMap<UUID, Data> items,
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
				InvoicePurchaseEquipment pe = new InvoicePurchaseEquipment(
						(Equipment) items.get(UUID.fromString(itemUUID)), numPurchased);
				invoices.get(UUID.fromString(invoiceUUID)).addItem(pe);

				// Checks if the item is licensed equipment
			} else if (itemSpecific.toLowerCase().equals("l")) {
				// Adds an amount of leased equipment to an invoices items list
				int numLeased = Integer.parseInt(tokens[3]);
				InvoiceLeaseEquipment le = new InvoiceLeaseEquipment((Equipment) items.get(UUID.fromString(itemUUID)),
						numLeased);
				invoices.get(UUID.fromString(invoiceUUID)).addItem(le);

				// Checks if the item is a service by checking if the next field is a uuid
			} else if (itemSpecific.length() == 36) {
				// Adds a set up service to an invoices items list
				double billedHours = Double.parseDouble(tokens[3]);
				InvoiceService ser = new InvoiceService((Service) items.get(UUID.fromString(itemUUID)), billedHours,
						persons.get(UUID.fromString(itemSpecific)));
				invoices.get(UUID.fromString(invoiceUUID)).addItem(ser);

				// only other item type is a license
			} else {
				// Adds a set up License to an invoices items list
				LocalDate startDate = LocalDate.parse(tokens[2]);
				LocalDate endDate = LocalDate.parse(tokens[3]);
				InvoiceLicense l = new InvoiceLicense((License) items.get(UUID.fromString(itemUUID)), startDate,
						endDate);
				invoices.get(UUID.fromString(invoiceUUID)).addItem(l);
			}
		}
		s.close();
	}

	/**
	 * Adds invoice specific items into invoices from the database
	 * 
	 * @param fileName
	 * @param invoices
	 * @param items
	 * @param persons
	 */
	public static void loadInvoiceItemsFromDatabase(ConnectionFactory cf, Logger logger,
			HashMap<UUID, Invoice> invoices, HashMap<UUID, Data> items, HashMap<UUID, Person> persons) {
		Connection conn = cf.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select inv.invoiceUUID, i.itemUUID, ii.purchase, ii.quantity, p.personUUID, ii.billedHours, ii.startDate, ii.endDate from InvoiceItem ii join Item i on i.itemId = ii.itemId join Invoice inv on inv.invoiceId = ii.itemId left join Person p on p.personId = ii.personId";
		try {
			logger.info("Creating invoice items");
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String invoiceUUID = rs.getString("invoiceUUID");
				String itemUUID = rs.getString("itemUUID");
				String purchase = rs.getString("purchase");
				// For each item the following fields are item specific
				// Checks if the item is purchased equipment
				if (purchase != null) {
					if (purchase.equals("1")) {
						// Adds an amount of purchased equipment to an invoices items list
						int numPurchased = Integer.parseInt(rs.getString("quantity"));
						InvoicePurchaseEquipment pe = new InvoicePurchaseEquipment(
								(Equipment) items.get(UUID.fromString(itemUUID)), numPurchased);
						invoices.get(UUID.fromString(invoiceUUID)).addItem(pe);

						// Checks if the item is licensed equipment
					} else if (purchase.equals("0")) {
						// Adds an amount of leased equipment to an invoices items list
						int numLeased = Integer.parseInt(rs.getString("quantity"));
						InvoiceLeaseEquipment le = new InvoiceLeaseEquipment(
								(Equipment) items.get(UUID.fromString(itemUUID)), numLeased);
						invoices.get(UUID.fromString(invoiceUUID)).addItem(le);

						// Checks if the item is a service by checking if the next field is a uuid
					}
				} else {
					String personUUID = rs.getString("personUUID");
					if (personUUID != null) {

						// Adds a set up service to an invoices items list
						double billedHours = Double.parseDouble(rs.getString("billedHours"));
						InvoiceService ser = new InvoiceService((Service) items.get(UUID.fromString(itemUUID)),
								billedHours, persons.get(UUID.fromString(personUUID)));
						invoices.get(UUID.fromString(invoiceUUID)).addItem(ser);
						// only other item type is a license
					} else {
						// Adds a set up License to an invoices items list
						LocalDate startDate = LocalDate.parse(rs.getString("startDate"));
						LocalDate endDate = LocalDate.parse(rs.getString("endDate"));
						InvoiceLicense l = new InvoiceLicense((License) items.get(UUID.fromString(itemUUID)), startDate,
								endDate);
						invoices.get(UUID.fromString(invoiceUUID)).addItem(l);
					}

				}
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			logger.error("Failed to create all invoice items");
			throw new RuntimeException(e);
		}

		cf.putConnection(conn);
	}
}
