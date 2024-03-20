import java.util.Random;

public class Main {
    static Random RNG = new Random();
    public static Car generateRandomCar(){
        String[] brands = {"Toyota", "Ford", "Honda", "Nissan", "BMW"};
        Integer[] years = {2015, 2016, 2017, 2018, 2019};
        Integer[] prices = {15000, 20000, 30000, 40000, 50000};

        String brand = (String) randomChoice(brands);
        String model = "prototype";
        return new Car(brand, model, (Integer) randomChoice(years), (Integer) randomChoice(prices), false);
    }
    public static Object randomChoice(Object[] array){
        return array[RNG.nextInt(0, array.length)];
    }
    public static void main(String[] args) {
        VehicleRepository test = new VehicleRepository("repo.csv");
        for(int i = 0; i<20; ++i)
            test.addToRepository(generateRandomCar());
    }
}