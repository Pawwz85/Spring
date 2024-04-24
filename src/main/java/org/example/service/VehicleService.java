package org.example.service;

import lombok.AllArgsConstructor;
import org.example.dao.IVehicleRepository;
import org.example.dto.UserDto;
import org.example.dto.VehicleDto;
import org.example.model.Car;
import org.example.model.Motorcycle;
import org.example.model.User;
import org.example.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@AllArgsConstructor
public class VehicleService {

    @Autowired
    IVehicleRepository vehicleRepository;

    public Collection<VehicleDto> getVehicles() {
        Collection<VehicleDto> vehicleDtos = new ArrayList<>();
        Collection<Vehicle> vehicles = vehicleRepository.getVehicles();
        for (Vehicle vehicle : vehicles) {
            String category = (vehicle instanceof Motorcycle)? ((Motorcycle) vehicle).getCategory(): null;
            VehicleDto vehicleDto = new VehicleDto(vehicle.getBrand(), vehicle.getModel(), vehicle.getYear(),
                    vehicle.getPrice(), vehicle.getPlate(), vehicle.isRent(), category );
            vehicleDtos.add(vehicleDto);
        }
        return vehicleDtos;
    }

    public VehicleDto getVehicle(String plate){
        Vehicle vehicle = vehicleRepository.getVehicle(plate);
        String category = (vehicle instanceof Motorcycle)? ((Motorcycle) vehicle).getCategory(): null;

        return  new VehicleDto(vehicle.getBrand(), vehicle.getModel(), vehicle.getYear(),
                vehicle.getPrice(), vehicle.getPlate(), vehicle.isRent(), category );
    }

    public Boolean createVehicle(VehicleDto vehicleDto){
        Vehicle vehicle = null;
        if (vehicleDto.getCategory() == null){
            vehicle = new Car(vehicleDto.getBrand(), vehicleDto.getModel(), vehicleDto.getYear(),
                    vehicleDto.getPrice(), vehicleDto.getPlate());
        } else {
            vehicle = new Motorcycle(vehicleDto.getBrand(), vehicleDto.getModel(), vehicleDto.getYear(),
                    vehicleDto.getPrice(), vehicleDto.getPlate(), vehicleDto.getCategory());
        }

        boolean success = vehicleRepository.getVehicle(vehicle.getPlate()) == null;

        if(success){
            if(vehicleDto.getCategory() == null)
                vehicle = new Car(vehicleDto.getBrand(), vehicleDto.getModel(), vehicleDto.getYear(), vehicleDto.getPrice(), vehicleDto.getPlate());
            else
                vehicle = new Motorcycle(vehicleDto.getBrand(), vehicleDto.getModel(), vehicleDto.getYear(), vehicleDto.getPrice(), vehicleDto.getPlate(), vehicleDto.getCategory());
        }

        return success;
    }

    public Boolean deleteVehicle(String plate){
        if (vehicleRepository.getVehicle(plate) == null)
            return false;
        return vehicleRepository.removeVehicle(plate);
    }
}
