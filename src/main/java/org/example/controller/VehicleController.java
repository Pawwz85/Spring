package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.UserDto;
import org.example.dto.VehicleDto;
import org.example.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/vehicles")
@AllArgsConstructor
public class VehicleController {
    @Autowired
    VehicleService vehicleService;

    @GetMapping("/all")
    public ResponseEntity<Collection<VehicleDto>> getVehicles(){
        Collection<VehicleDto> vehicleDtos = vehicleService.getVehicles();
        return ResponseEntity.ok(vehicleDtos);
    }

    @GetMapping("{plate}")
    public ResponseEntity<VehicleDto> getVehicle(@PathVariable String plate) {

        VehicleDto vehicleDto = vehicleService.getVehicle(plate);
        if (vehicleDto != null) {
            return ResponseEntity.ok(vehicleDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<String> createVehicle(VehicleDto vehicleDto) {
        boolean success = vehicleService.createVehicle(vehicleDto);
        if(success)
            return ResponseEntity.ok(("Vehicle created successfully"));
        else
            return ResponseEntity.badRequest().body("Vehicle already exist");
    }
}
