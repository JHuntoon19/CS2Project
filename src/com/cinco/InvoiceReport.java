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
		HashMap<UUID, Person> persons = LoadData.loadPersonsFromDatabase(cf, logger);
		HashMap<UUID, Company> companies = LoadData.loadCompaniesFromDatabase(cf, logger, persons);
		HashMap<UUID, Data> items = LoadData.loadItemsFromDatabase(cf, logger);
		HashMap<UUID, Invoice> invoices = LoadData.loadInvoicesFromDatabase(cf, logger, companies, persons);
		LoadData.loadInvoiceItemsFromDatabase(cf, logger, invoices, items, persons);
		String report = GenerateReports.generateReportString(invoices, companies);
		System.out.print(report);
		DataConverter.printToFile(report);
		DataConverter.printItemsToXML(items);
		DataConverter.printItemsToJSON(items);
	}

}
