package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {
    private String brand;
    private String model;
    private int year;
    private double price;
    private String plate;
    private boolean rent;
    private String category;
}

