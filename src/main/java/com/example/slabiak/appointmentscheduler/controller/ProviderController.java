package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.entity.WorkingPlan;
import com.example.slabiak.appointmentscheduler.model.ChangePasswordForm;
import com.example.slabiak.appointmentscheduler.model.TimePeroid;
import com.example.slabiak.appointmentscheduler.model.UserForm;
import com.example.slabiak.appointmentscheduler.security.CustomUserDetails;
import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import com.example.slabiak.appointmentscheduler.service.UserService;
import com.example.slabiak.appointmentscheduler.service.WorkService;
import com.example.slabiak.appointmentscheduler.service.WorkingPlanService;
import com.example.slabiak.appointmentscheduler.validation.groups.CreateProvider;
import com.example.slabiak.appointmentscheduler.validation.groups.CreateUser;
import com.example.slabiak.appointmentscheduler.validation.groups.UpdateProvider;
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
@RequestMapping("/providers")
public class ProviderController {

	private final UserService userService;
	private final WorkService workService;
	private final WorkingPlanService workingPlanService;
	private final AppointmentService appointmentService;
	private static final String PASSWORD_CHANGE = "passwordChange";
	private static final String REDIRECT_PROVIDERS = "redirect:/providers/";
	private static final String REDIRECT_PROVIDERS_AVAILABILITY = "redirect:/providers/availability";
	
	public ProviderController(UserService userService, WorkService workService, WorkingPlanService workingPlanService, AppointmentService appointmentService) {
		this.userService = userService;
		this.workService = workService;
		this.workingPlanService = workingPlanService;
		this.appointmentService = appointmentService;
	}

	@GetMapping("/all")
	public String showAllProviders(Model model) {
		model.addAttribute("providers", userService.getAllProviders());
		return "users/listProviders";
	}

	@GetMapping("/{id}")
	public String showProviderDetails(@PathVariable("id") int providerId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
		if (currentUser.getId() == providerId || currentUser.hasRole("ROLE_ADMIN")) {
			if (!model.containsAttribute("user")) {
				model.addAttribute("user", new UserForm(userService.getProviderById(providerId)));
			}
			if (!model.containsAttribute(PASSWORD_CHANGE)) {
				model.addAttribute(PASSWORD_CHANGE, new ChangePasswordForm(providerId));
			}
			model.addAttribute("account_type", "provider");
			model.addAttribute("formActionProfile", "/providers/update/profile");
			model.addAttribute("formActionPassword", "/providers/update/password");
			model.addAttribute("allWorks", workService.getAllWorks());
			model.addAttribute("numberOfScheduledAppointments", appointmentService.getNumberOfScheduledAppointmentsForUser(providerId));
			model.addAttribute("numberOfCanceledAppointments", appointmentService.getNumberOfCanceledAppointmentsForUser(providerId));
			return "users/updateUserForm";

		} else {
			throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
		}
	}

	@PostMapping("/update/profile")
	public String processProviderUpdate(@Validated({ UpdateUser.class, UpdateProvider.class }) @ModelAttribute("user") UserForm userUpdateData, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
			redirectAttributes.addFlashAttribute("user", userUpdateData);
			return REDIRECT_PROVIDERS + userUpdateData.getId();
		}
		userService.updateProviderProfile(userUpdateData);
		return REDIRECT_PROVIDERS + userUpdateData.getId();
	}

	@GetMapping("/new")
	public String showProviderRegistrationForm(Model model) {
		if (!model.containsAttribute("user"))
			model.addAttribute("user", new UserForm());
		model.addAttribute("account_type", "provider");
		model.addAttribute("registerAction", "/providers/new");
		model.addAttribute("allWorks", workService.getAllWorks());
		return "users/createUserForm";
	}

	@PostMapping("/new")
	public String processProviderRegistrationForm(@Validated({ CreateUser.class, CreateProvider.class }) @ModelAttribute("user") UserForm userForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
			redirectAttributes.addFlashAttribute("user", userForm);
			return "redirect:/providers/new";
		}
		userService.saveNewProvider(userForm);
		return "redirect:/providers/all";
	}

	@PostMapping("/delete")
	public String processDeleteProviderRequest(@RequestParam("providerId") int providerId) {
		userService.deleteUserById(providerId);
		return "redirect:/providers/all";
	}

	@GetMapping("/availability")
	public String showProviderAvailability(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
		model.addAttribute("plan", workingPlanService.getWorkingPlanByProviderId(currentUser.getId()));
		model.addAttribute("breakModel", new TimePeroid());
		return "users/showOrUpdateProviderAvailability";
	}

	@PostMapping("/availability")
	public String processProviderWorkingPlanUpdate(@ModelAttribute("plan") WorkingPlan plan) {
		workingPlanService.updateWorkingPlan(plan);
		return REDIRECT_PROVIDERS_AVAILABILITY;
	}

	@PostMapping("/availability/breakes/add")
	public String processProviderAddBreak(@ModelAttribute("breakModel") TimePeroid breakToAdd, @RequestParam("planId") int planId, @RequestParam("dayOfWeek") String dayOfWeek) {
		workingPlanService.addBreakToWorkingPlan(breakToAdd, planId, dayOfWeek);
		return REDIRECT_PROVIDERS_AVAILABILITY;
	}

	@PostMapping("/availability/breakes/delete")
	public String processProviderDeleteBreak(@ModelAttribute("breakModel") TimePeroid breakToDelete, @RequestParam("planId") int planId, @RequestParam("dayOfWeek") String dayOfWeek) {
		workingPlanService.deleteBreakFromWorkingPlan(breakToDelete, planId, dayOfWeek);
		return REDIRECT_PROVIDERS_AVAILABILITY;
	}

	@PostMapping("/update/password")
	public String processProviderPasswordUpate(@Valid @ModelAttribute(PASSWORD_CHANGE) ChangePasswordForm passwordChange, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passwordChange", bindingResult);
			redirectAttributes.addFlashAttribute(PASSWORD_CHANGE, passwordChange);
			return REDIRECT_PROVIDERS + passwordChange.getId();
		}
		userService.updateUserPassword(passwordChange);
		return REDIRECT_PROVIDERS + passwordChange.getId();
	}

}
