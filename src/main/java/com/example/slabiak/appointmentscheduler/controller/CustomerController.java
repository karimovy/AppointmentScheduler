package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.entity.user.customer.Customer;
import com.example.slabiak.appointmentscheduler.model.ChangePasswordForm;
import com.example.slabiak.appointmentscheduler.model.UserForm;
import com.example.slabiak.appointmentscheduler.security.CustomUserDetails;
import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import com.example.slabiak.appointmentscheduler.service.UserService;
import com.example.slabiak.appointmentscheduler.validation.groups.CreateCorporateCustomer;
import com.example.slabiak.appointmentscheduler.validation.groups.CreateUser;
import com.example.slabiak.appointmentscheduler.validation.groups.UpdateCorporateCustomer;
import com.example.slabiak.appointmentscheduler.validation.groups.UpdateUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/customers")
public class CustomerController {

	private final UserService userService;
	private final AppointmentService appointmentService;
	private static final String ACCOUNT_TYPE = "account_type";
	private static final String CUSTOMER_CORPORATE = "customer_corporate";
	private static final String CUSTOMER_RETAIL = "customer_retail";
	private static final String PASSWORD_CHANGE = "passwordChange";
	private static final String REDIRECT_CUSTOMERS = "redirect:/customers/";
	private static final String USERS_CREATE_USER_FORM = "users/createUserForm";
	private static final String REGISTER_ACTION = "registerAction";

	public CustomerController(UserService userService, AppointmentService appointmentService) {
		this.userService = userService;
		this.appointmentService = appointmentService;
	}

	@GetMapping("/all")
	public String showAllCustomers(Model model) {
		model.addAttribute("customers", userService.getAllCustomers());
		return "users/listCustomers";
	}

	@GetMapping("/{id}")
	public String showCustomerDetails(@PathVariable int id, Model model) {
		Customer customer = userService.getCustomerById(id);
		if (customer.hasRole("ROLE_CUSTOMER_CORPORATE")) {
			if (!model.containsAttribute("user")) {
				model.addAttribute("user", new UserForm(userService.getCorporateCustomerById(id)));
			}
			model.addAttribute(ACCOUNT_TYPE, CUSTOMER_CORPORATE);
			model.addAttribute("formActionProfile", "/customers/corporate/update/profile");
		} else if (customer.hasRole("ROLE_CUSTOMER_RETAIL")) {
			if (!model.containsAttribute("user")) {
				model.addAttribute("user", new UserForm(userService.getRetailCustomerById(id)));
			}
			model.addAttribute(ACCOUNT_TYPE, CUSTOMER_RETAIL);
			model.addAttribute("formActionProfile", "/customers/retail/update/profile");
		}
		if (!model.containsAttribute(PASSWORD_CHANGE)) {
			model.addAttribute(PASSWORD_CHANGE, new ChangePasswordForm(id));
		}
		model.addAttribute("formActionPassword", "/customers/update/password");
		model.addAttribute("numberOfScheduledAppointments", appointmentService.getNumberOfScheduledAppointmentsForUser(id));
		model.addAttribute("numberOfCanceledAppointments", appointmentService.getNumberOfCanceledAppointmentsForUser(id));
		return "users/updateUserForm";
	}

	@PostMapping("/corporate/update/profile")
	public String processCorporateCustomerProfileUpdate(@Validated({ UpdateUser.class, UpdateCorporateCustomer.class }) @ModelAttribute("user") UserForm user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
			redirectAttributes.addFlashAttribute("user", user);
			return REDIRECT_CUSTOMERS + user.getId();
		}
		userService.updateCorporateCustomerProfile(user);
		return REDIRECT_CUSTOMERS + user.getId();
	}

	@PostMapping("/retail/update/profile")
	public String processRetailCustomerProfileUpdate(@Validated({ UpdateUser.class }) @ModelAttribute("user") UserForm user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
			redirectAttributes.addFlashAttribute("user", user);
			return REDIRECT_CUSTOMERS + user.getId();
		}
		userService.updateRetailCustomerProfile(user);
		return REDIRECT_CUSTOMERS + user.getId();
	}

	@GetMapping("/new/{customer_type}")
	public String showCustomerRegistrationForm(@PathVariable("customer_type") String customerType, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
		if (currentUser != null) {
			return "redirect:/";
		}
		if (customerType.equals("corporate")) {
			model.addAttribute(ACCOUNT_TYPE, CUSTOMER_CORPORATE);
			model.addAttribute(REGISTER_ACTION, "/customers/new/corporate");
		} else if (customerType.equals("retail")) {
			model.addAttribute(ACCOUNT_TYPE, CUSTOMER_RETAIL);
			model.addAttribute(REGISTER_ACTION, "/customers/new/retail");
		} else {
			throw new RuntimeException();
		}
		model.addAttribute("user", new UserForm());
		return USERS_CREATE_USER_FORM;
	}

	@PostMapping("/new/retail")
	public String processReatilCustomerRegistration(@Validated({ CreateUser.class }) @ModelAttribute("user") UserForm userForm, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			populateModel(model, userForm, CUSTOMER_RETAIL, "/customers/new/retail", null);
			return USERS_CREATE_USER_FORM;
		}
		userService.saveNewRetailCustomer(userForm);
		model.addAttribute("createdUserName", userForm.getUserName());
		return "users/login";
	}

	@PostMapping("/new/corporate")
	public String processCorporateCustomerRegistration(@Validated({ CreateUser.class, CreateCorporateCustomer.class }) @ModelAttribute("user") UserForm userForm, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			populateModel(model, userForm, CUSTOMER_CORPORATE, "/customers/new/corporate", null);
			return USERS_CREATE_USER_FORM;
		}
		userService.saveNewCorporateCustomer(userForm);
		model.addAttribute("createdUserName", userForm.getUserName());
		return "users/login";
	}

	@PostMapping("/update/password")
	public String processCustomerPasswordUpate(@Valid @ModelAttribute(PASSWORD_CHANGE) ChangePasswordForm passwordChange, BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails currentUser, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passwordChange", bindingResult);
			redirectAttributes.addFlashAttribute(PASSWORD_CHANGE, passwordChange);
			return REDIRECT_CUSTOMERS + currentUser.getId();
		}
		userService.updateUserPassword(passwordChange);
		return REDIRECT_CUSTOMERS + currentUser.getId();
	}

	@PostMapping("/delete")
	public String processDeleteCustomerRequest(@RequestParam("customerId") int customerId) {
		userService.deleteUserById(customerId);
		return "redirect:/customers/all";
	}

	public Model populateModel(Model model, UserForm user, String account_type, String action, String error) {
		model.addAttribute("user", user);
		model.addAttribute(ACCOUNT_TYPE, account_type);
		model.addAttribute(REGISTER_ACTION, action);
		model.addAttribute("registrationError", error);
		return model;
	}

}
