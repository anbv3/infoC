package com.infoc.domain;

import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class User extends AbstractPersistable<Long> {
	private static final long serialVersionUID = -4548247329625710336L;
	
	private String name;
	@Override
	public void setId(Long id) {
		super.setId(id);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
