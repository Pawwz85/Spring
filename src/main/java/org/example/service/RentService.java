package org.example.service;

import lombok.AllArgsConstructor;
import org.example.dao.IUserRepository;
import org.example.dao.IVehicleRepository;
import org.example.model.User;
import org.example.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class RentService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IVehicleRepository vehicleRepository;

    public Vehicle rentVehicle(String plate, String login) {
        User user = userRepository.getUser(login);
        Vehicle vehicle = vehicleRepository.getVehicle(plate);

        if (user != null && vehicle != null && !vehicle.isRent()) {
            vehicleRepository.rentVehicle(plate,login);
            return vehicle;
        }
        return null;
    }

    public  boolean returnVehicle(String login){
        User user = userRepository.getUser(login);
        String plate = (user.getVehicle() != null)? userRepository.getUser(login).getVehicle().getPlate(): null;
        if (plate == null)
            return false;

        return vehicleRepository.returnVehicle(plate, login);
    }
}