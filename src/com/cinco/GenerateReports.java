package com.cinco;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.UUID;

public class GenerateReports {
	/**
	 * This will sort the invoices to produce three reports that are outputted to
	 * the console and to a text file
	 * 
	 * @param invoices
	 */
	public static void generateReports(HashMap<UUID, Invoice> invoices, HashMap<UUID, Company> companies) {
		// String Builder to have one long string to easily print both to the standard
		// output and also to a text file
		StringBuilder sb = new StringBuilder();
		sb.append(
				"-------------------------------------------------------------------------------------------------\n");
		sb.append("Invoices By Total:\n");
		sb.append(
				"-------------------------------------------------------------------------------------------------\n");
		sb.append(
				"Invoice                              Customer                  Num Items       Tax       Total   \n");
		// Sort the invoices based on total
		ArrayList<Invoice> invoicesByTotal = new ArrayList<>(invoices.values());
		invoicesByTotal.sort(Comparator.comparing(Invoice::getTotal).reversed());
		// Goes through each invoice and adds totals to string output
		double total = 0.0;
		double tax = 0.0;
		int numItems = 0;
		for (Invoice i : invoicesByTotal) {
			total += i.getTotal();
			tax += i.getTax();
			numItems += i.getNumItems();
			sb.append(i.toString());
			sb.append("\n");
		}
		sb.append(
				"-------------------------------------------------------------------------------------------------\n");
		sb.append(String.format("%64d          $%10.2f $%10.2f\n", numItems, tax, total));
		sb.append("Invoices by Customer:\n");
		sb.append(
				"-------------------------------------------------------------------------------------------------\n");
		sb.append("Company                        # Invoices Grand Total    \n");
		// Sort the companies by name
		ArrayList<Company> companiesByName = new ArrayList<>(companies.values());
		companiesByName.sort(Comparator.comparing(i -> i.getName().toLowerCase()));
		// Creates a map of companies to their list of invoices
		HashMap<UUID, ArrayList<Invoice>> companyMap = getCompanyMap(invoices, companies);
		// Goes in order of companies and calculates totals and adds to string output
		int numInvoices = 0;
		total = 0.0;
		for (Company c : companiesByName) {
			numInvoices += companyMap.get(c.getUUID()).size();
			total += getInvoiceTotals(companyMap.get(c.getUUID()));
			sb.append(String.format("%-30s %-10d $%10.2f\n", c.getName(), companyMap.get(c.getUUID()).size(),
					getInvoiceTotals(companyMap.get(c.getUUID()))));
		}
		sb.append(
				"-------------------------------------------------------------------------------------------------\n");
		sb.append(String.format("%32d          $%10.2f\n", numInvoices, total));
		// Summary report for each invoice
		sb.append(
				"-------------------------------------------------------------------------------------------------\n");
		sb.append("Invoice Summary:\n");
		sb.append(
				"-------------------------------------------------------------------------------------------------\n");
		for (Invoice i : invoicesByTotal) {
			sb.append(String.format("Invoice#\t%s\n", i.getUUID()));
			sb.append(String.format("Date:\t%s\n", i.getDate().toString()));
			sb.append(String.format("Customer:\n%s", i.getCustomer().toString()));
			sb.append(String.format("Sales Person:\n%s", i.getSalesPerson().toString()));
			sb.append(String.format("Items (%d)                             Tax     Total\n",
					companyMap.get(i.getCustomer().getUUID()).size()));
			sb.append("===================================================\n");
			sb.append(i.toItemString());
		}
		System.out.println(sb.toString());
	}

	private static double getInvoiceTotals(ArrayList<Invoice> invoices) {
		double total = 0.0;
		for (Invoice i : invoices) {
			total += i.getTotal();
		}
		return total;
	}

	private static HashMap<UUID, ArrayList<Invoice>> getCompanyMap(HashMap<UUID, Invoice> invoices,
			HashMap<UUID, Company> companies) {
		HashMap<UUID, ArrayList<Invoice>> companyToInvoices = new HashMap<UUID, ArrayList<Invoice>>();
		for (Company c : companies.values()) {
			companyToInvoices.put(c.getUUID(), new ArrayList<Invoice>());
		}
		for (Invoice i : invoices.values()) {
			companyToInvoices.get(i.getCustomer().getUUID()).add(i);
		}

		return companyToInvoices;
	}

	private static int getNumInvoices(UUID customerUUID, ArrayList<Invoice> invoices) {
		int numOfInvoices = 0;
		for (Invoice i : invoices) {
			if (customerUUID.equals(i.getCustomer().getUUID())) {
				numOfInvoices += 1;
			}
		}
		return numOfInvoices;
	}
}
