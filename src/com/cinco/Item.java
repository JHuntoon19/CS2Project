package com.cinco;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Author: Judah Huntoon Date: 2026-02-11 Purpose: Represents a generic item Use
 * this as a parent to Equipment, Service, and License
 */
public abstract class Item extends Data {
	private String type;

	public Item(String UUID, String name, String type) {
		super(UUID, name);
		this.type = type;
	}

	public Item(UUID uuid, String name, String type) {
		super(uuid, name);
		this.type = type;
	}

	public abstract BigDecimal getCost();

	public String getType() {
		return type;
	}

	public abstract BigDecimal getTaxes();

	public abstract BigDecimal getTotal();
}
