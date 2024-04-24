package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.RentCarDto;
import org.example.dto.UserDto;
import org.example.model.Vehicle;
import org.example.service.RentService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rents")
@AllArgsConstructor
public class RentController {

    @Autowired
    private RentService rentService;

    @Autowired
    private UserService userService;

    @PostMapping("/rent")
    public ResponseEntity<String> rentVehicle(@RequestBody RentCarDto request) {

        if (!userService.authUser(request.getLogin(), request.getPassword()))
            return ResponseEntity.badRequest().body("Bad credentials");

        Vehicle vehicle = rentService.rentVehicle(request.getPlate(),request.getLogin());

        if (vehicle != null)
            return ResponseEntity.ok(vehicle.toCSV());
         else
            return ResponseEntity.badRequest().body("Failed");

    }

    @PostMapping("/return")
    public ResponseEntity<String> returnVehicle(@RequestBody RentCarDto request){

        if (!userService.authUser(request.getLogin(), request.getPassword()))
            return ResponseEntity.badRequest().body("Bad credentials");

        boolean success = rentService.returnVehicle(request.getLogin());
        if (success)
            return ResponseEntity.ok("ok");
        else
            return ResponseEntity.badRequest().body("Failed");
    }

}