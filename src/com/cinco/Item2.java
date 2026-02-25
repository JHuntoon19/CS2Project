/**
 * Author: Judah Huntoon
 * Date: 2026-02-11
 * Purpose: Represents a generic item
 * Use this as a parent to Equipment, Service, and License
 */
package com.cinco;

public class Item2 extends Data{
	private String type;

	public Item2(String UUID, String name, String type) {
		super(UUID, name);
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
