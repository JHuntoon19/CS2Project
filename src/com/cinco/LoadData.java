package com.cinco;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

public class LoadData {

	/**
	 * Returns a list of persons loaded from the given file
	 * @param fileName
	 * @return ArrayList<Person>
	 */
	public static ArrayList<Person> loadPersons(String fileName) {
		ArrayList<Person> persons = new ArrayList<Person>();
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
			for(int i = 4; i < n; i++) {
				emails.add(tokens[i]);
			}
			Person p = new Person(uuid,firstName,lastName,phoneNumber,emails);
			persons.add(p);
		}
		s.close();
		return persons;
	}
	/**
	 * Returns a list of companies loaded from the given file
	 * @param fileName
	 * @return ArrayList<Company>
	 */
	public static ArrayList<Company> loadCompanies(String fileName, HashMap<UUID, Person> persons) {
		ArrayList<Company> companies = new ArrayList<Company>();
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
			Address a = new Address(street,city,state,zip);
			Company c;
			if(persons.get(contactUUID) != null) {
				c = new Company(uuid,name,persons.get(contactUUID),a);
				System.out.println("Person Found");
			}
			else {
				c = new Company(uuid,name,contactUUID,a);
			}
			companies.add(c);
		}
		s.close();
		return companies;
	}
	/**
	 * Retruns a list of items loaded from given file
	 * @param fileName
	 * @return ArrayList<Item>
	 */
	public static ArrayList<Item> loadItems(String fileName) {
		ArrayList<Item> items = new ArrayList<Item>();
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
			
			if(type.equals("E")) {
				double costPerUnit = Double.parseDouble(tokens[3]);
				Equipment e = new Equipment(uuid,name,type,costPerUnit);
				items.add(e);
			}
			else if(type.equals("S")) {
				double costPerHour = Double.parseDouble(tokens[3]);
				Service ser = new Service(uuid,name,type,costPerHour);
				items.add(ser);
			}
			else if(type.equals("L")) {
				double serviceFee = Double.parseDouble(tokens[3]);
				double annualFee = Double.parseDouble(tokens[4]);
				License l = new License(uuid,name,type,serviceFee,annualFee);
				items.add(l);
			}	
		}
		s.close();
		return items;
	}
}
