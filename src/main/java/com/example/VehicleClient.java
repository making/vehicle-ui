package com.example;


import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface VehicleClient {
	@GetExchange(url = "/vehicles")
	List<Vehicle> getVehicles(@RequestParam(required = false) String name);

	@PostExchange(url = "/vehicles")
	Vehicle postVehicles(@RequestBody Vehicle vehicle);
}
