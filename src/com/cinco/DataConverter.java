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


/**
 * Author: Judah Huntoon
 * Date: 2026-02-11
 * Purpose: loads data from files and outputs data into both 
 * xml and json for storage. Saves file in Lists of Persons, Companies, and Items
 */
public class DataConverter {
	/**
	 * Prints a list of persons as xml to data/PersonsReal.xml
	 * @param persons
	 */
	public static void printPersonToXML(ArrayList<Person> persons) {
		XStream xstream = new XStream();
		xstream.alias("Person", Person.class);
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
	}
	/**
	 * Prints a list of persons as json to data/PersonsReal.json
	 * @param persons
	 */
	public static void printPersonToJSON(ArrayList<Person> persons) {
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.create();
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
	/**
	 * Prints a list of companies as xml to data/CompaniesReal.xml
	 * @param companies
	 */
	public static void printCompaniesToXML(ArrayList<Company> companies) {
		XStream xstream = new XStream();
		xstream.alias("Company", Company.class);
		xstream.alias("Address", Address.class);
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
	}
	/**
	 * Prints a list of companies as json to data/CompaniesReal.json
	 * @param companies
	 */
	public static void printCompaniesToJSON(ArrayList<Company> companies) {
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.create();
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
	/**
	 * Prints a list of items as xml to data/ItemsReal.xml
	 * @param items
	 */
	public static void printItemsToXML(ArrayList<Item> items) {
		XStream xstream = new XStream();
		xstream.alias("Equipment", Equipment.class);
		xstream.alias("Service", Service.class);
		xstream.alias("License", License.class);
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
	}
	/**
	 * Prints a list of items as json to data/ItemsReal.json
	 * @param items
	 */
	public static void printItemsToJSON(ArrayList<Item> items) {
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.create();
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
