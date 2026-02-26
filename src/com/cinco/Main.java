package com.cinco;

import java.util.HashMap;
import java.util.UUID;

public class Main {

	public static void main(String[] args) {
		HashMap<UUID, Person> persons = LoadData.loadPersons("data/Persons.csv");
		HashMap<UUID, Company> companies = LoadData.loadCompanies("data/Companies.csv", persons);
		HashMap<UUID, Item> items = LoadData.loadItems("data/Items.csv");
		DataConverter.printPersonToXML(persons);
		DataConverter.printPersonToJSON(persons);
		DataConverter.printCompaniesToXML(companies);
		DataConverter.printCompaniesToJSON(companies);
		DataConverter.printItemsToXML(items);
		DataConverter.printItemsToJSON(items);

	}

}
