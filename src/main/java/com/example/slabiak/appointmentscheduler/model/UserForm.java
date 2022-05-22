package com.example.slabiak.appointmentscheduler.model;

import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.entity.user.customer.CorporateCustomer;
import com.example.slabiak.appointmentscheduler.entity.user.customer.RetailCustomer;
import com.example.slabiak.appointmentscheduler.entity.user.provider.Provider;
import com.example.slabiak.appointmentscheduler.validation.FieldsMatches;
import com.example.slabiak.appointmentscheduler.validation.UniqueUsername;
import com.example.slabiak.appointmentscheduler.validation.groups.*;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

@FieldsMatches(field = "password", matchingField = "matchingPassword", groups = { CreateUser.class })
@Data
public class UserForm {

	@NotNull(groups = { UpdateUser.class })
	@Min(	value = 1, groups = { UpdateUser.class })
	private int id;

	@UniqueUsername(groups = { CreateUser.class })
	@Size(	min = 5, max = 15, 
			groups = { CreateUser.class }, 
			message = "Username should have 5-15 letters")
	@NotBlank(groups = { CreateUser.class })
	private String userName;

	@Size(	min = 5, max = 15, 
			groups = { CreateUser.class }, 
			message = "Password should have 5-15 letters")
	@NotBlank(groups = { CreateUser.class })
	private String password;

	@Size(	min = 5, max = 15, 
			groups = { CreateUser.class }, 
			message = "Password should have 5-15 letters")
	@NotBlank(groups = { CreateUser.class })
	private String matchingPassword;

	@NotBlank(	groups = { CreateUser.class, UpdateUser.class },
				message = "First name cannot be empty")
	private String firstName;

	@NotBlank(	groups = { CreateUser.class, UpdateUser.class }, 
				message = "Last name cannot be empty")
	private String lastName;

	@Email(groups = { CreateUser.class, UpdateUser.class }, message = "Email not valid!")
	@NotBlank(groups = { CreateUser.class, UpdateUser.class }, message = "Email cannot be empty")
	private String email;

	@Pattern(	groups = { CreateUser.class, UpdateUser.class }, 
				regexp = "[0-9]{10}", 
				message = "Please enter valid mobile phone with 10 digits")
	@NotBlank(groups = { CreateUser.class, UpdateUser.class },
	message = "Mobile phone cannot be empty")
	private String mobile;

	@Size(	groups = { CreateUser.class, UpdateUser.class }, 
			min = 5, max = 30, 
			message = "Wrong street!")
	@NotBlank(	groups = { CreateUser.class, UpdateUser.class }, 
				message = "Street cannot be empty")
	private String street;

	@Pattern(	groups = { CreateUser.class, UpdateUser.class }, 
				regexp = "[0-9]{2}-[0-9]{3}", 
				message = "Please enter valid postcode. Format 00-000")
	@NotBlank(	groups = { CreateUser.class, UpdateUser.class }, 
				message = "Post code cannot be empty")
	private String postcode;

	@NotBlank(	groups = { CreateUser.class, UpdateUser.class }, 
				message = "City cannot be empty")
	private String city;

	/*
	 * CorporateCustomer only:
	 */
	@NotBlank(	groups = { CreateCorporateCustomer.class, UpdateCorporateCustomer.class }, 
				message = "Company cannot be empty")
	private String companyName;

	@Pattern(	groups = { CreateCorporateCustomer.class, UpdateCorporateCustomer.class }, 
				regexp = "[0-9]{10}", 
				message = "Please enter valid Polish VAT number with 10 digits")
	@NotBlank(	groups = { CreateCorporateCustomer.class, UpdateCorporateCustomer.class }, 
				message = "VAT number cannot be empty")
	private String vatNumber;

	/*
	 * Provider only:
	 */
	@NotNull(groups = { CreateProvider.class, UpdateProvider.class })
	private List<Work> works;

	public UserForm() {
		// default constructor
	}

	public UserForm(User user) {
		this.setId(user.getId());
		this.setUserName(user.getUserName());
		this.setFirstName(user.getFirstName());
		this.setLastName(user.getLastName());
		this.setEmail(user.getEmail());
		this.setCity(user.getCity());
		this.setStreet(user.getStreet());
		this.setPostcode(user.getPostcode());
		this.setMobile(user.getMobile());
	}

	public UserForm(Provider provider) {
		this((User) provider);
		this.setWorks(provider.getWorks());
	}

	public UserForm(RetailCustomer retailCustomer) {
		this((User) retailCustomer);
	}
	public UserForm(CorporateCustomer corporateCustomer) {
		this((User) corporateCustomer);
		this.setCompanyName(corporateCustomer.getCompanyName());
		this.setVatNumber(corporateCustomer.getVatNumber());
	}
}