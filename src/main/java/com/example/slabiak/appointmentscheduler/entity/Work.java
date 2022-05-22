package com.example.slabiak.appointmentscheduler.entity;

import com.example.slabiak.appointmentscheduler.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "WORKS")
@Data
@NoArgsConstructor @AllArgsConstructor
public class Work extends BaseEntity {

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "price")
	private double price;

	@Column(name = "duration")
	private int duration;

	@Column(name = "editable")
	private boolean editable;

	@Column(name = "target")
	private String targetCustomer;

	@ManyToMany
	@JoinTable(name = "works_providers", joinColumns = @JoinColumn(name = "id_work"), inverseJoinColumns = @JoinColumn(name = "id_user"))
	private List<User> providers;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Work))
			return false;
		Work work = (Work) o;
		return super.getId().equals(work.getId());
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}
}
