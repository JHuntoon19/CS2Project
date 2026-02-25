/**
 * Author: Judah Huntoon
 * Date: 2026-02-11
 * Purpose: loads data from files and outputs data into both 
 * xml and json for storage. Saves file in Lists of Persons, Companies, and Items
 */
package com.cinco;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;


public class DataConverter2 {

	public static void main(String[] args) {
		ArrayList<Person> persons = loadPersons("data/Persons.csv", new ArrayList<Person>());
		ArrayList<Company> companies = loadCompanies("data/Companies.csv", new ArrayList<Company>());
		ArrayList<Item> items = loadItems("data/Items.csv",new ArrayList<Item>());
		XStream xstream = new XStream();
		xstream.alias("Person", Person.class);
		xstream.alias("Company", Company.class);
		xstream.alias("Equipment", Equipment.class);
		xstream.alias("Service", Service.class);
		xstream.alias("License", License.class);
		xstream.alias("Address", Address.class);
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.create();
		printPersonsToFiles(persons,xstream,gson);
		printCompaniesToFiles(companies,xstream,gson);
		printItemsToFiles(items,xstream,gson);
	}
	private static ArrayList<Person> loadPersons(String fileName, ArrayList<Person> persons) {
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
	private static ArrayList<Company> loadCompanies(String fileName, ArrayList<Company> companies) {
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
			String contactUUID = tokens[1];
			String name = tokens[2];
			String street = tokens[3];
			String city = tokens[4];
			String state = tokens[5];
			int zip = Integer.parseInt(tokens[6]);
			Address a = new Address(street,city,state,zip);
			Company c = new Company(uuid,name,contactUUID,a);
			companies.add(c);
		}
		s.close();
		return companies;
	}
	private static ArrayList<Item> loadItems(String fileName, ArrayList<Item> items) {
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
	private static void printPersonsToFiles(ArrayList<Person> persons, XStream xstream, Gson gson) {
		try {
			File f = new File("data/PersonsReal.xml");
			PrintWriter pw = new PrintWriter(f);
			pw.println("<Persons>");
			for(Person p : persons) {
				String xml = xstream.toXML(p);
				pw.println(xml); 
				
			}
			pw.println("</Persons>");
			pw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try {
			File f = new File("data/PersonsReal.json");
			PrintWriter pw = new PrintWriter(f);
			Map<String, Object> wrapper = new HashMap<>();
		    wrapper.put("persons", persons);
		    String json = gson.toJson(wrapper);
		    pw.println(json);
			pw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	private static void printCompaniesToFiles(ArrayList<Company> companies, XStream xstream, Gson gson) {
		try {
			File f = new File("data/CompaniesReal.xml");
			PrintWriter pw = new PrintWriter(f);
			pw.println("<Companies>");
			for(Company c : companies) {
				String xml = xstream.toXML(c);
				pw.println(xml); 
				
			}
			pw.println("</Companies>");
			pw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try {
			File f = new File("data/CompaniesReal.json");
			PrintWriter pw = new PrintWriter(f);
			Map<String, Object> wrapper = new HashMap<>();
		    wrapper.put("companies", companies);
		    String json = gson.toJson(wrapper);
		    pw.println(json);
			pw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	private static void printItemsToFiles(ArrayList<Item> items, XStream xstream, Gson gson) {
		try {
			File f = new File("data/ItemsReal.xml");
			PrintWriter pw = new PrintWriter(f);
			pw.println("<Items>");
			for(Item i : items) {
				String xml = xstream.toXML(i);
				pw.println(xml); 
				
			}
			pw.println("</Items>");
			pw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try {
			File f = new File("data/ItemsReal.json");
			PrintWriter pw = new PrintWriter(f);
			Map<String, Object> wrapper = new HashMap<>();
		    wrapper.put("items", items);
		    String json = gson.toJson(wrapper);
		    pw.println(json);
			pw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
}
