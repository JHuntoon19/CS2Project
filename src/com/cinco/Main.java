package com.cinco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
public class Main {

	public static void main(String[] args) {
		ArrayList<Person> persons = LoadData.loadPersons("data/Persons.csv");
		HashMap<UUID, Person> personsMap= new HashMap<>();
		for(int i = 0; i < persons.size(); i ++) {
			personsMap.put(persons.get(i).getUUID(), persons.get(i));
		}
		System.out.println(persons.get(0).getUUID());
		ArrayList<Company> companies = LoadData.loadCompanies("data/Companies.csv",personsMap);
		ArrayList<Item> items = LoadData.loadItems("data/Items.csv");
		System.out.println(companies.get(0).getUUID());
		DataConverter.printPersonToXML(persons);
		DataConverter.printPersonToJSON(persons);
		DataConverter.printCompaniesToXML(companies);
		DataConverter.printCompaniesToJSON(companies);
		DataConverter.printItemsToXML(items);
		DataConverter.printItemsToJSON(items);
	}

}
