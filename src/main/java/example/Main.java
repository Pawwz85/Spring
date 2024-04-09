package example;

import example.Car;
import example.dao.jdbc.JdbcUserRepository;
import example.dao.jdbc.JdbcVehicleRepository;

import java.util.Random;

public class Main {
    static Random RNG = new Random();
    public static Car generateRandomCar(){
        String[] brands = {"Toyota", "Ford", "Honda", "Nissan", "BMW"};
        Integer[] years = {2015, 2016, 2017, 2018, 2019};
        Integer[] prices = {15000, 20000, 30000, 40000, 50000};

        String brand = (String) randomChoice(brands);
        String model = "prototype";
        return new Car(brand, model, (Integer) randomChoice(years), (Integer) randomChoice(prices), false, null);
    }
    public static Object randomChoice(Object[] array){
        return array[RNG.nextInt(0, array.length)];
    }
    public static void main(String[] args) {
        App app = new App(JdbcVehicleRepository.getInstance(), JdbcUserRepository.getInstance());
        app.run();
    }
}