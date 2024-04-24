package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.BooleanToShortConventer;

@Setter
@Getter
@Entity
@Table(name = "tvehicle")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicle_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Vehicle {
    private String brand;
    private String model;
    private int year;
    @Column(columnDefinition = "numeric")
    private double price;
    @Id
    private String plate;
    @Convert(converter = BooleanToShortConventer.class)
    private boolean rent;
    @OneToOne(mappedBy = "vehicle", fetch = FetchType.EAGER)
    private User user;

    public Vehicle(String brand, String model, int year, double price, String plate) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.plate = plate;
        this.rent = false;
        //this.user = null; //redundant

    }
    public Vehicle(String brand, String model, int year, double price, String plate,boolean rent) {
        this(brand,model,year,price,plate);
        this.rent = rent;
    }

    public Vehicle() {
        //IT IS MANDATORY !!
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", plate='" + plate + '\'' +
                ", rent=" + rent +
                '}';
    }

    public String toCSV() {
        return new StringBuilder()
                .append(this.getClass().getSimpleName())
                .append(";")
                .append(this.brand)
                .append(";")
                .append(this.model)
                .append(";")
                .append(this.year)
                .append(";")
                .append(this.price)
                .append(";")
                .append(this.plate)
                .append(";")
                .append(this.rent)
                .toString();
    }
}
