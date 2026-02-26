package com.cinco;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.thoughtworks.xstream.XStream;

/**
 * Author: Judah Huntoon Date: 2026-02-11 Purpose: loads data from files and
 * outputs data into both xml and json for storage. Saves file in Lists of
 * Persons, Companies, and Items
 */
public class DataConverter {
	/**
	 * Prints a map of persons as xml to data/PersonsReal.xml
	 * 
	 * @param persons
	 */
	public static void printPersonToXML(HashMap<UUID, Person> persons) {
		ArrayList<Person> people = new ArrayList<>(persons.values());
		XStream xstream = new XStream();
		xstream.alias("Person", Person.class);
		try {
			File f = new File("data/PersonsReal.xml");
			PrintWriter pw = new PrintWriter(f);
			pw.println("<Persons>");
			for (Person p : people) {
				String xml = xstream.toXML(p);
				pw.println(xml);

			}
			pw.println("</Persons>");
			pw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Prints a map of persons as json to data/PersonsReal.json
	 * 
	 * @param persons
	 */
	public static void printPersonToJSON(HashMap<UUID, Person> persons) {
		ArrayList<Person> people = new ArrayList<>(persons.values());
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
			File f = new File("data/PersonsReal.json");
			PrintWriter pw = new PrintWriter(f);
			Map<String, Object> wrapper = new HashMap<>();
			wrapper.put("persons", people);
			String json = gson.toJson(wrapper);
			pw.println(json);
			pw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Prints a Map of companies as xml to data/CompaniesReal.xml
	 * 
	 * @param companies
	 */
	public static void printCompaniesToXML(HashMap<UUID, Company> companies) {
		ArrayList<Company> companiesList = new ArrayList<>(companies.values());
		XStream xstream = new XStream();
		xstream.alias("Company", Company.class);
		xstream.alias("Address", Address.class);
		try {
			File f = new File("data/CompaniesReal.xml");
			PrintWriter pw = new PrintWriter(f);
			pw.println("<Companies>");
			for (Company c : companiesList) {
				String xml = xstream.toXML(c);
				pw.println(xml);

			}
			pw.println("</Companies>");
			pw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Prints a Map of companies as json to data/CompaniesReal.json
	 * 
	 * @param companies
	 */
	public static void printCompaniesToJSON(HashMap<UUID, Company> companies) {
		ArrayList<Company> companiesList = new ArrayList<>(companies.values());
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
			File f = new File("data/CompaniesReal.json");
			PrintWriter pw = new PrintWriter(f);
			Map<String, Object> wrapper = new HashMap<>();
			wrapper.put("companies", companiesList);
			String json = gson.toJson(wrapper);
			pw.println(json);
			pw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Prints a map of items as xml to data/ItemsReal.xml
	 * 
	 * @param items
	 */
	public static void printItemsToXML(HashMap<UUID, Item> items) {
		ArrayList<Item> itemsList = new ArrayList<>(items.values());
		XStream xstream = new XStream();
		xstream.alias("Equipment", Equipment.class);
		xstream.alias("Service", Service.class);
		xstream.alias("License", License.class);
		try {
			File f = new File("data/ItemsReal.xml");
			PrintWriter pw = new PrintWriter(f);
			pw.println("<Items>");
			for (Item i : itemsList) {
				String xml = xstream.toXML(i);
				pw.println(xml);

			}
			pw.println("</Items>");
			pw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Prints a map of items as json to data/ItemsReal.json
	 * 
	 * @param items
	 */
	public static void printItemsToJSON(HashMap<UUID, Item> items) {
		ArrayList<Item> itemsList = new ArrayList<>(items.values());
		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
			@Override
			public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
				return new JsonPrimitive(src.toString());
			}
		}).setPrettyPrinting().create();
		try {
			File f = new File("data/ItemsReal.json");
			PrintWriter pw = new PrintWriter(f);
			Map<String, Object> wrapper = new HashMap<>();
			wrapper.put("items", itemsList);
			String json = gson.toJson(wrapper);
			pw.println(json);
			pw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
