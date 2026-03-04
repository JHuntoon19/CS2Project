package com.cinco;

import java.util.HashMap;
import java.util.UUID;

public class InvoiceReport {

	public static void main(String[] args) {
		HashMap<UUID, Person> persons = LoadData.loadPersons("data/Persons.csv");
		HashMap<UUID, Company> companies = LoadData.loadCompanies("data/Companies.csv", persons);
		HashMap<UUID, Item> items = LoadData.loadItems("data/Items.csv");
		HashMap<UUID, Invoice> invoices = LoadData.loadInvoices("data/Invoices.csv", companies, persons);
		LoadData.loadInvoiceItems("data/InvoiceItems.csv", invoices, items, persons);
		GenerateReports.generateReports(invoices, companies);
	}

}
