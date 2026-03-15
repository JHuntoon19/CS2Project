package com.cinco;

import java.util.HashMap;
import java.util.UUID;

/**
 * Contains main method for program. Creates and stores the maps of persons,
 * companies, items, and invoices Prints out the report
 */
public class InvoiceReport {

	public static void main(String[] args) {
		HashMap<UUID, Person> persons = LoadData.loadPersons("data/Persons.csv");
		HashMap<UUID, Company> companies = LoadData.loadCompanies("data/Companies.csv", persons);
		HashMap<UUID, Data> items = LoadData.loadItems("data/Items.csv");
		HashMap<UUID, Invoice> invoices = LoadData.loadInvoices("data/Invoices.csv", companies, persons);
		LoadData.loadInvoiceItems("data/InvoiceItems.csv", invoices, items, persons);
		String report = GenerateReports.generateReportString(invoices, companies);
		System.out.print(report);
		DataConverter.printToFile(report);
		DataConverter.printItemsToXML(items);
		DataConverter.printItemsToJSON(items);
		System.out.println("Converted");
	}

}
