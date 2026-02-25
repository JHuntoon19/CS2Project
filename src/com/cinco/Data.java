package com.cinco;
import java.util.UUID;
/**
 * Author: Judah Huntoon
 * Date: 2026-02-11
 * Purpose: Represents a piece of data which holds a UUID and a Name.
 * Use as a parent class to specific kinds of data
 */
abstract public class Data implements HasUUID{
	private UUID uuid;
	private String name;
	public Data(String uuid, String name) {
		super();
		this.uuid = UUID.fromString(uuid);
		this.name = name;
	}
	public UUID getUUID() {
		return this.uuid;
	}
	public String getName() {
		return this.name;
	}
}
