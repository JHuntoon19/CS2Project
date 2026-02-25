package com.cinco;

/**
 * Author: Judah Huntoon Date: 2026-02-11 Purpose: Represents a generic item Use
 * this as a parent to Equipment, Service, and License
 */
public class Item extends Data {
	private String type;

	public Item(String UUID, String name, String type) {
		super(UUID, name);
		this.type = type;
	}
}
