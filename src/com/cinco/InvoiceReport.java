package com.cinco;

import java.util.HashMap;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Contains main method for program. Creates and stores the maps of persons,
 * companies, items, and invoices Prints out the report
 */
public class InvoiceReport {

	public static void main(String[] args) {
		final Logger logger = LogManager.getLogger(InvoiceReport.class);
		ConnectionFactory cf = new ConnectionFactory(logger);
		HashMap<UUID, Person> persons = InvoiceData.loadPersonsFromDatabase();
		HashMap<UUID, Company> companies = InvoiceData.loadCompaniesFromDatabase(persons);
		HashMap<UUID, Data> items = InvoiceData.loadItemsFromDatabase();
		HashMap<UUID, Invoice> invoices = InvoiceData.loadInvoicesFromDatabase(companies, persons);
		InvoiceData.loadInvoiceItemsFromDatabase(invoices, items, persons);
		String report = GenerateReports.generateReportString(invoices, companies);
		System.out.print(report);
	}

}
