package com.cinco;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Represents an Invoice that hold different Items be used to generate outputs
 */
public class Invoice implements HasUUID {
	private UUID uuid;
	private LocalDate date;
	private Company customer;
	private Person salesPerson;
	private ArrayList<Item> items;

	public Invoice(UUID uuid, LocalDate date, Company customer, Person salesPerson, ArrayList<Item> items) {
		super();
		this.uuid = uuid;
		this.date = date;
		this.customer = customer;
		this.salesPerson = salesPerson;
		this.items = items;
	}

	public Invoice(String uuid, String date, Company customer, Person salesPerson) {
		this.uuid = UUID.fromString(uuid);
		this.date = LocalDate.parse(date);
		this.customer = customer;
		this.salesPerson = salesPerson;
		this.items = new ArrayList<Item>();
	}

	public LocalDate getDate() {
		return date;
	}

	public Company getCustomer() {
		return customer;
	}

	public Person getSalesPerson() {
		return salesPerson;
	}

	/**
	 * returtns the number of items on invoice
	 */
	public int getNumItems() {
		return items.size();
	}

	/**
	 * adds a given item to the arrayList of invoice items
	 * 
	 * @param i
	 */
	public void addItem(Item i) {
		this.items.add(i);
	}

	/**
	 * Returns the sum of all subtotals calculated by each item
	 * 
	 * @return double
	 */
	public double getSubtotal() {
		double subtotal = 0;
		for (Item item : items) {
			subtotal += item.getCost().doubleValue();
		}
		return subtotal;
	}

	/**
	 * Returns the sum of all taxes calculated by each item
	 * 
	 * @return double
	 */
	public double getTax() {
		double taxes = 0;
		for (Item item : items) {
			taxes += item.getTaxes().doubleValue();
		}
		return taxes;
	}

	/**
	 * Returns the sum of all the final totals calculated by each item
	 * 
	 * @return double
	 */
	public double getTotal() {
		double total = 0;
		for (Item item : items) {
			total += item.getTotal().doubleValue();
		}
		return total;
	}

	public String toItemString() {
		String s = "";
		for (Item i : items) {
			s = s + i.toString() + "\n";
		}
		return s;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public String toString() {
		return String.format("%-25s %-25s %-10d $%10.2f $%10.2f", getUUID().toString(), getCustomer().getName(),
				items.size(), getTax(), getTotal());
	}

}
