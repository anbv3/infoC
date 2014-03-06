package com.infoc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.google.common.base.Objects;

@Entity
public class User extends AbstractPersistable<Long> {
	private static final long serialVersionUID = -4548247329625710336L;
	
	@Column
	private String email;
	
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public void setId(Long id) {
		super.setId(id);
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("id", super.getId())
			.add("email", this.email)
			.toString();
	}
}
