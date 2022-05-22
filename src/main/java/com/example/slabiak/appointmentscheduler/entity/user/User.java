package com.example.slabiak.appointmentscheduler.entity.user;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.slabiak.appointmentscheduler.entity.BaseEntity;
import com.example.slabiak.appointmentscheduler.entity.Notification;
import com.example.slabiak.appointmentscheduler.model.UserForm;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public class User extends BaseEntity {

	@Column(name = "username")
	private String userName;

	@Column(name = "password")
	private String password;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column(name = "mobile")
	private String mobile;

	@Column(name = "street")
	private String street;

	@Column(name = "city")
	private String city;

	@Column(name = "postcode")
	private String postcode;

	@OneToMany(mappedBy = "user")
	private List<Notification> notifications;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Collection<Role> roles;


	public User(UserForm newUserForm, String encryptedPassword, Collection<Role> roles) {
		this.setUserName(newUserForm.getUserName());
		this.setFirstName(newUserForm.getFirstName());
		this.setLastName(newUserForm.getLastName());
		this.setEmail(newUserForm.getEmail());
		this.setCity(newUserForm.getCity());
		this.setStreet(newUserForm.getStreet());
		this.setPostcode(newUserForm.getPostcode());
		this.setMobile(newUserForm.getMobile());
		this.password = encryptedPassword;
		this.roles = roles;
	}

	public void update(UserForm updateData) {
		this.setEmail(updateData.getEmail());
		this.setFirstName(updateData.getFirstName());
		this.setLastName(updateData.getLastName());
		this.setMobile(updateData.getMobile());
		this.setCity(updateData.getCity());
		this.setStreet(updateData.getStreet());
		this.setPostcode(updateData.getPostcode());
	}

	public boolean hasRole(String roleName) {
		for (Role role : roles) {
			if (role.getName().equals(roleName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof User))
			return false;
		User user = (User) o;
		return this.getId().equals(user.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.getId());
	}
}
