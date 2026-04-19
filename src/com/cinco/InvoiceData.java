package com.cinco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: Judah Huntoon Date: 04/18/2026 This is a collection of utility
 * methods that define a general API for interacting with the database
 * supporting this application.
 *
 */
public class InvoiceData {

	/**
	 * Removes all records from all tables in the database.
	 */
	public static void clearDatabase() {
		final Logger logger = LogManager.getLogger(InvoiceData.class);
		ConnectionFactory cf = new ConnectionFactory(logger);
		Connection conn = cf.getConnection();
		String[] queries = { "delete from InvoiceItem", "delete from Invoice", "delete from Email",
				"delete from Company", "delete from Item", "delete from Zipcode", "delete from State",
				"delete from Person" };
		PreparedStatement ps = null;
		try {
			logger.info("Clearing database");
			for (String q : queries) {
				ps = conn.prepareStatement(q);
				ps.executeUpdate();
			}
			ps.close();
		} catch (SQLException e) {
			logger.error("Failed to delete database");
			throw new RuntimeException(e);
		}
		cf.putConnection(conn);
	}

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personUuid
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public static void addPerson(UUID personUuid, String firstName, String lastName, String phone) {
		if (!personExists(personUuid.toString())) {
			final Logger logger = LogManager.getLogger(InvoiceData.class);
			ConnectionFactory cf = new ConnectionFactory(logger);
			Connection conn = cf.getConnection();
			String query = "insert into Person (personUUID, firstName, lastName, phoneNumber) values (?, ?, ?, ?)";
			PreparedStatement ps = null;
			try {
				logger.info("Adding person");
				ps = conn.prepareStatement(query);
				ps.setString(1, personUuid.toString());
				ps.setString(2, firstName);
				ps.setString(3, lastName);
				ps.setString(4, phone);
				ps.executeUpdate();
				ps.close();

			} catch (SQLException e) {
				logger.error("Failed to add a person");
				throw new RuntimeException(e);
			}
			cf.putConnection(conn);
		}

	}

	/**
	 * Checks if person already exists in the database returns boolean
	 */
	private static boolean personExists(String personUUID) {
		final Logger logger = LogManager.getLogger(InvoiceData.class);
		ConnectionFactory cf = new ConnectionFactory(logger);
		Connection conn = cf.getConnection();
		String query = "select personId from Person where personUUID = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exists;
		try {
			logger.info("Checking if person exists");
			ps = conn.prepareStatement(query);
			ps.setString(1, personUUID);
			rs = ps.executeQuery();
			exists = rs.next();
			ps.close();
			rs.close();

		} catch (SQLException e) {
			logger.error("Failed to check person");
			throw new RuntimeException(e);
		}
		cf.putConnection(conn);
		return exists;
	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personUuid</code>
	 *
	 * @param personUuid
	 * @param email
	 */
	public static void addEmail(UUID personUuid, String email) {
		if (!emailExists(email)) {
			final Logger logger = LogManager.getLogger(InvoiceData.class);
			ConnectionFactory cf = new ConnectionFactory(logger);
			Connection conn = cf.getConnection();
			String query = "insert into Email (address, personId) values(?,(select personId from Person where personUUID = ?))";
			PreparedStatement ps = null;
			try {
				logger.info("Adding email");
				ps = conn.prepareStatement(query);
				ps.setString(1, email);
				ps.setString(2, personUuid.toString());
				ps.executeUpdate();
				ps.close();
			} catch (SQLException e) {
				logger.error("Failed to add email");
				throw new RuntimeException(e);
			}
			cf.putConnection(conn);
		}

	}

	/**
	 * Checks if the email already exists in the database returns boolean
	 */
	private static boolean emailExists(String address) {
		final Logger logger = LogManager.getLogger(InvoiceData.class);
		ConnectionFactory cf = new ConnectionFactory(logger);
		Connection conn = cf.getConnection();
		String query = "select emailId from Email where address like ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exists;
		try {
			logger.info("Checking if email exists");
			ps = conn.prepareStatement(query);
			ps.setString(1, "%" + address + "%");
			rs = ps.executeQuery();
			exists = rs.next();
			ps.close();
			rs.close();

		} catch (SQLException e) {
			logger.error("Failed to check email");
			throw new RuntimeException(e);
		}
		cf.putConnection(conn);
		return exists;
	}

	/**
	 * Adds a company record to the database with the primary contact person
	 * identified by the given code.
	 *
	 * @param companyUuid
	 * @param name
	 * @param contactUuid
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public static void addCompany(UUID companyUuid, UUID contactUuid, String name, String street, String city,
			String state, String zip) {
		if (!companyExists(companyUuid.toString())) {
			addState(state);
			addZipcode(zip);
			final Logger logger = LogManager.getLogger(InvoiceData.class);
			ConnectionFactory cf = new ConnectionFactory(logger);
			Connection conn = cf.getConnection();
			if (personExists(contactUuid.toString())) {
				String query = "insert into Company (companyUUID, name, street, city, stateId, zipcodeId, personId) values(?, ?, ?,?,(select stateId from State where state like ?),(select zipcodeId from Zipcode where zipcode = ?),(select personId from Person where personUUID = ?))";
				PreparedStatement ps = null;
				try {
					logger.info("Adding company");
					ps = conn.prepareStatement(query);
					ps.setString(1, companyUuid.toString());
					ps.setString(2, name);
					ps.setString(3, street);
					ps.setString(4, city);
					ps.setString(5, state);
					ps.setString(6, zip);
					ps.setString(7, contactUuid.toString());
					ps.executeUpdate();
					ps.close();
				} catch (SQLException e) {
					logger.error("Failed to add company");
					throw new RuntimeException(e);
				}
				cf.putConnection(conn);
			} else {
				logger.error("Tried to add company but invalid contact data was given");
			}

		}

	}

	/**
	 * Checks if company exists in the database returns boolean
	 */
	private static boolean companyExists(String companyUUID) {
		final Logger logger = LogManager.getLogger(InvoiceData.class);
		ConnectionFactory cf = new ConnectionFactory(logger);
		Connection conn = cf.getConnection();
		String query = "select companyId from Company where companyUUID = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exists;
		try {
			logger.info("Checking if company exists");
			ps = conn.prepareStatement(query);
			ps.setString(1, companyUUID);
			rs = ps.executeQuery();
			exists = rs.next();
			ps.close();
			rs.close();

		} catch (SQLException e) {
			logger.error("Failed to check company");
			throw new RuntimeException(e);
		}
		cf.putConnection(conn);
		return exists;
	}

	/**
	 * Checks if state exists in database returns boolean
	 */
	private static boolean stateExists(String state) {
		final Logger logger = LogManager.getLogger(InvoiceData.class);
		ConnectionFactory cf = new ConnectionFactory(logger);
		Connection conn = cf.getConnection();
		String query = "select stateId from State where state like ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exists;
		try {
			logger.info("Checking if state exists");
			ps = conn.prepareStatement(query);
			ps.setString(1, "%" + state + "%");
			rs = ps.executeQuery();
			exists = rs.next();
			ps.close();
			rs.close();

		} catch (SQLException e) {
			logger.error("Failed to check state");
			throw new RuntimeException(e);
		}
		cf.putConnection(conn);
		return exists;
	}

	/**
	 * Checks if zipcode exists in database returns boolean
	 */
	private static boolean zipcodeExists(String zipcode) {
		final Logger logger = LogManager.getLogger(InvoiceData.class);
		ConnectionFactory cf = new ConnectionFactory(logger);
		Connection conn = cf.getConnection();
		String query = "select zipcodeId from Zipcode where zipcode = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exists;
		try {
			logger.info("Checking if zipcode exists");
			ps = conn.prepareStatement(query);
			ps.setString(1, zipcode);
			rs = ps.executeQuery();
			exists = rs.next();
			ps.close();
			rs.close();

		} catch (SQLException e) {
			logger.error("Failed to check zipcode");
			throw new RuntimeException(e);
		}
		cf.putConnection(conn);
		return exists;
	}

	/**
	 * adds a state to database returns void
	 */

	public static void addState(String state) {
		if (!stateExists(state)) {
			final Logger logger = LogManager.getLogger(InvoiceData.class);
			ConnectionFactory cf = new ConnectionFactory(logger);
			Connection conn = cf.getConnection();
			String query = "insert into State (state) values(?)";
			PreparedStatement ps = null;
			try {
				logger.info("Adding state");
				ps = conn.prepareStatement(query);
				ps.setString(1, state);
				ps.executeUpdate();
				ps.close();
			} catch (SQLException e) {
				logger.error("Failed to add state");
				throw new RuntimeException(e);
			}
			cf.putConnection(conn);
		}

	}

	/**
	 * adds a state to database returns void
	 */

	public static void addZipcode(String zipcode) {
		if (!zipcodeExists(zipcode)) {
			final Logger logger = LogManager.getLogger(InvoiceData.class);
			ConnectionFactory cf = new ConnectionFactory(logger);
			Connection conn = cf.getConnection();
			String query = "insert into Zipcode (zipcode) values(?)";
			PreparedStatement ps = null;
			try {
				logger.info("Adding zipcode");
				ps = conn.prepareStatement(query);
				ps.setString(1, zipcode);
				ps.executeUpdate();
				ps.close();
			} catch (SQLException e) {
				logger.error("Failed to add zipcode");
				throw new RuntimeException(e);
			}
			cf.putConnection(conn);
		}

	}

	/**
	 * Adds an equipment record to the database of the given values.
	 *
	 * @param equipmentUuid
	 * @param name
	 * @param modelNumber
	 * @param retailPrice
	 */
	public static void addEquipment(UUID equipmentUuid, String name, double pricePerUnit) {
		if (!itemExists(equipmentUuid.toString())) {
			final Logger logger = LogManager.getLogger(InvoiceData.class);
			ConnectionFactory cf = new ConnectionFactory(logger);
			Connection conn = cf.getConnection();
			String query = "insert into Item (itemUUID, name, type, costPerUnit) values(?,?, 'equipment', ?)";
			PreparedStatement ps = null;
			try {
				logger.info("Adding equipment");
				ps = conn.prepareStatement(query);
				ps.setString(1, equipmentUuid.toString());
				ps.setString(2, name);
				ps.setDouble(3, pricePerUnit);
				ps.executeUpdate();
				ps.close();
			} catch (SQLException e) {
				logger.error("Failed to add equipment");
				throw new RuntimeException(e);
			}
			cf.putConnection(conn);
		}
	}

	/**
	 * Checks if item exists in database returns boolean
	 */
	private static boolean itemExists(String itemUUID) {
		final Logger logger = LogManager.getLogger(InvoiceData.class);
		ConnectionFactory cf = new ConnectionFactory(logger);
		Connection conn = cf.getConnection();
		String query = "select itemId from Item where itemUUID = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exists;
		try {
			logger.info("Checking if item exists");
			ps = conn.prepareStatement(query);
			ps.setString(1, itemUUID);
			rs = ps.executeQuery();
			exists = rs.next();
			ps.close();
			rs.close();

		} catch (SQLException e) {
			logger.error("Failed to check item");
			throw new RuntimeException(e);
		}
		cf.putConnection(conn);
		return exists;
	}

	/**
	 * Adds a service record to the database of the given values.
	 *
	 * @param equipmentUuid
	 * @param name
	 * @param costPerHour
	 */
	public static void addService(UUID equipmentUuid, String name, double costPerHour) {
		if (!itemExists(equipmentUuid.toString())) {
			final Logger logger = LogManager.getLogger(InvoiceData.class);
			ConnectionFactory cf = new ConnectionFactory(logger);
			Connection conn = cf.getConnection();
			String query = "insert into Item (itemUUID, name, type, costPerHour) values(?,?, 'service', ?)";
			PreparedStatement ps = null;
			try {
				logger.info("Adding service");
				ps = conn.prepareStatement(query);
				ps.setString(1, equipmentUuid.toString());
				ps.setString(2, name);
				ps.setDouble(3, costPerHour);
				ps.executeUpdate();
				ps.close();
			} catch (SQLException e) {
				logger.error("Failed to add service");
				throw new RuntimeException(e);
			}
			cf.putConnection(conn);
		}
	}

	/**
	 * Adds a license record to the database of the given values.
	 *
	 * @param equipmentUuid
	 * @param name
	 * @param serviceFee
	 * @param annualFee
	 */
	public static void addLicense(UUID equipmentUuid, String name, double serviceFee, double annualFee) {
		if (!itemExists(equipmentUuid.toString())) {
			final Logger logger = LogManager.getLogger(InvoiceData.class);
			ConnectionFactory cf = new ConnectionFactory(logger);
			Connection conn = cf.getConnection();
			String query = "insert into Item (itemUUID, name, type, serviceFee, annualFee) values(?,?, 'license', ?, ?)";
			PreparedStatement ps = null;
			try {
				logger.info("Adding license");
				ps = conn.prepareStatement(query);
				ps.setString(1, equipmentUuid.toString());
				ps.setString(2, name);
				ps.setDouble(3, serviceFee);
				ps.setDouble(4, annualFee);
				ps.executeUpdate();
				ps.close();
			} catch (SQLException e) {
				logger.error("Failed to add license");
				throw new RuntimeException(e);
			}
			cf.putConnection(conn);
		}
	}

	/**
	 * Adds an Invoice record to the database with the given data.
	 *
	 * @param invoiceUuid
	 * @param customerUuid
	 * @param salesPersonUuid
	 * @param date
	 */
	public static void addInvoice(UUID invoiceUuid, UUID customerUuid, UUID salesPersonUuid, LocalDate date) {
		if (!invoiceExists(invoiceUuid.toString())) {
			final Logger logger = LogManager.getLogger(InvoiceData.class);
			if (companyExists(customerUuid.toString())) {
				if (personExists(salesPersonUuid.toString())) {
					ConnectionFactory cf = new ConnectionFactory(logger);
					Connection conn = cf.getConnection();
					String query = "insert into Invoice (invoiceUUID,personId, date, companyId) values(?,(select personId from Person where personUUID = ?),?,(select companyId from Company where companyUUID = ?))";
					PreparedStatement ps = null;
					try {
						logger.info("Adding invoice");
						ps = conn.prepareStatement(query);
						ps.setString(1, invoiceUuid.toString());
						ps.setString(2, salesPersonUuid.toString());
						ps.setString(3, date.toString());
						ps.setString(4, customerUuid.toString());
						ps.executeUpdate();
						ps.close();
					} catch (SQLException e) {
						logger.error("Failed to add invoice");
						throw new RuntimeException(e);
					}
					cf.putConnection(conn);
				} else {
					logger.error("Tried to add invoice but invalid sales person data was given");
				}
			} else {
				logger.error("Tried to add invoice but invalid customer data was given");
			}
		}
	}

	/**
	 * Checks if Invoice exists in database returns boolean
	 */
	private static boolean invoiceExists(String invoiceUUID) {
		final Logger logger = LogManager.getLogger(InvoiceData.class);
		ConnectionFactory cf = new ConnectionFactory(logger);
		Connection conn = cf.getConnection();
		String query = "select invoiceId from Invoice where invoiceUUID = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exists;
		try {
			logger.info("Checking if invoice exists");
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceUUID);
			rs = ps.executeQuery();
			exists = rs.next();
			ps.close();
			rs.close();

		} catch (SQLException e) {
			logger.error("Failed to check invoice");
			throw new RuntimeException(e);
		}
		cf.putConnection(conn);
		return exists;
	}

	/**
	 * Adds an Equipment purchase record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 */
	public static void addEquipmentPurchaseToInvoice(UUID invoiceUuid, UUID itemUuid, int numberOfUnits) {
		final Logger logger = LogManager.getLogger(InvoiceData.class);
		if (invoiceExists(invoiceUuid.toString())) {
			if (itemExists(itemUuid.toString())) {
				ConnectionFactory cf = new ConnectionFactory(logger);
				Connection conn = cf.getConnection();
				String query = "insert into InvoiceItem (invoiceId,itemId, purchase, quantity) values((select invoiceId from Invoice where invoiceUUID = ?),(select itemId from Item where itemUUID = ?),true,?)";
				PreparedStatement ps = null;
				try {
					logger.info("Adding equipment purchase");
					ps = conn.prepareStatement(query);
					ps.setString(1, invoiceUuid.toString());
					ps.setString(2, itemUuid.toString());
					ps.setDouble(3, numberOfUnits);
					ps.executeUpdate();
					ps.close();
				} catch (SQLException e) {
					logger.error("Failed to add equipment purchase");
					throw new RuntimeException(e);
				}
				cf.putConnection(conn);
			} else {
				logger.error("Tried to add invoice equipment purchase but invalid item data was given");
			}
		} else {
			logger.error("Tried to add invoice equipment purchase but invalid invoice data was given");
		}
	}

	/**
	 * Adds an Equipment lease record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param start
	 * @param end
	 */
	public static void addEquipmentLeaseToInvoice(UUID invoiceUuid, UUID itemUuid, int numberOfUnits) {
		final Logger logger = LogManager.getLogger(InvoiceData.class);
		if (invoiceExists(invoiceUuid.toString())) {
			if (itemExists(itemUuid.toString())) {
				ConnectionFactory cf = new ConnectionFactory(logger);
				Connection conn = cf.getConnection();
				String query = "insert into InvoiceItem (invoiceId,itemId, purchase, quantity) values((select invoiceId from Invoice where invoiceUUID = ?),(select itemId from Item where itemUUID = ?),false,?)";
				PreparedStatement ps = null;
				try {
					logger.info("Adding equipment lease");
					ps = conn.prepareStatement(query);
					ps.setString(1, invoiceUuid.toString());
					ps.setString(2, itemUuid.toString());
					ps.setDouble(3, numberOfUnits);
					ps.executeUpdate();
					ps.close();
				} catch (SQLException e) {
					logger.error("Failed to add equipment lease");
					throw new RuntimeException(e);
				}
				cf.putConnection(conn);
			} else {
				logger.error("Tried to add invoice equipment lease but invalid item data was given");
			}
		} else {
			logger.error("Tried to add invoice equipment lease but invalid invoice data was given");
		}
	}

	/**
	 * Adds a service record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param servicePersonUuid
	 * @param numberOfHours
	 */
	public static void addServiceToInvoice(UUID invoiceUuid, UUID itemUuid, UUID servicePersonUuid,
			double numberOfHours) {
		final Logger logger = LogManager.getLogger(InvoiceData.class);
		if (invoiceExists(invoiceUuid.toString())) {
			if (itemExists(itemUuid.toString())) {
				if (personExists(servicePersonUuid.toString())) {
					ConnectionFactory cf = new ConnectionFactory(logger);
					Connection conn = cf.getConnection();
					String query = "insert into InvoiceItem (invoiceId,itemId,personId,billedHours) values((select invoiceId from Invoice where invoiceUUID = ?),(select itemId from Item where itemUUID = ?),(select personId from Person where personUUID = ?),?)";
					PreparedStatement ps = null;
					try {
						logger.info("Adding invoice service");
						ps = conn.prepareStatement(query);
						ps.setString(1, invoiceUuid.toString());
						ps.setString(2, itemUuid.toString());
						ps.setString(3, servicePersonUuid.toString());
						ps.setDouble(4, numberOfHours);
						ps.executeUpdate();
						ps.close();
					} catch (SQLException e) {
						logger.error("Failed to add invoice service");
						throw new RuntimeException(e);
					}
					cf.putConnection(conn);
				} else {
					logger.error("Tried to add invoice service but invalid service person data was given");
				}

			} else {
				logger.error("Tried to add invoice invalid service but invalid item data was given");
			}
		} else {
			logger.error("Tried to add invoice invalid service but invalid invoice data was given");
		}
	}

	/**
	 * Adds a license record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param beginDate
	 * @param endDate
	 */
	public static void addLicenseToInvoice(UUID invoiceUuid, UUID itemUuid, LocalDate beginDate, LocalDate endDate) {
		final Logger logger = LogManager.getLogger(InvoiceData.class);
		if (invoiceExists(invoiceUuid.toString())) {
			if (itemExists(itemUuid.toString())) {
				ConnectionFactory cf = new ConnectionFactory(logger);
				Connection conn = cf.getConnection();
				String query = "insert into InvoiceItem (invoiceId,itemId, startDate, endDate) values((select invoiceId from Invoice where invoiceUUID = ?),(select itemId from Item where itemUUID = ?),?,?)";
				PreparedStatement ps = null;
				try {
					logger.info("Adding invoice license");
					ps = conn.prepareStatement(query);
					ps.setString(1, invoiceUuid.toString());
					ps.setString(2, itemUuid.toString());
					ps.setString(3, beginDate.toString());
					ps.setString(4, endDate.toString());
					ps.executeUpdate();
					ps.close();
				} catch (SQLException e) {
					logger.error("Failed to add invoice license");
					throw new RuntimeException(e);
				}
				cf.putConnection(conn);
			} else {
				logger.error("Tried to add invoice invoice license but invalid item data was given");
			}
		} else {
			logger.error("Tried to add invoice invoice license but invalid invoice data was given");
		}
	}

}
