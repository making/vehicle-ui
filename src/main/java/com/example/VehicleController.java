package com.example;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VehicleController {

	private final VehicleClient vehicleClient;

	private final Logger logger = LoggerFactory.getLogger(VehicleController.class);

	public VehicleController(VehicleClient vehicleClient) {
		this.vehicleClient = vehicleClient;
	}

	@GetMapping(path = "/")
	public String index(@RequestParam Optional<String> name, Model model) {
		final List<Vehicle> vehicles = name.map(this.vehicleClient::getVehicles).orElse(List.of());
		model.addAttribute("vehicles", vehicles);
		return "index";
	}

	@PostMapping(path = "/")
	public String add(Vehicle vehicle, RedirectAttributes attributes) {
		final Vehicle created = this.vehicleClient.postVehicles(vehicle);
		logger.info("Created {}", created);
		attributes.addAttribute("name", vehicle.name());
		return "redirect:/";
	}
}
