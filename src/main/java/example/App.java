package example;

import example.dao.IUserRepository;
import example.dao.IVehicleRepository;

import java.util.Scanner;

public class App {
    public static User user = null;
    private final Scanner scanner = new Scanner(System.in);
    private final IUserRepository iur;
    private final IVehicleRepository ivr;

    App(IVehicleRepository vehicleRepository, IUserRepository userRepository) {
        this.iur = userRepository;
        this.ivr = vehicleRepository;
        Authentication.setRepo(userRepository);
    }
    private void rent_car(){
        System.out.println("ID:");
        int id = Integer.parseInt(scanner.nextLine());
        user = iur.getUser(user.getLogin());

        if (user.getRentedVehicleID() != null){
            ivr.rentVehicle(id, user.getLogin());
            user.setRentedVehicleID(id);
        } else
            System.out.println("example.User can not rent more than 1 vehicle");

    }

    public void run() {

        System.out.println("LOG IN");
        App.user = Authentication.auth(this.scanner.nextLine(), this.scanner.nextLine());
        if (App.user != null) {
            System.out.println("logged in!!");

            String response = "";
            boolean running = true;
            while (running) {

                System.out.println("-----------MENU------------");
                System.out.println("00 - show info");
                System.out.println("0 - show cars");
                System.out.println("1 - rent car");
                System.out.println("2 - return car");
                System.out.println("6 - add car");
                System.out.println("7 - remove car");
                System.out.println("8 - add user");
                System.out.println("9 - remove user");
                response = scanner.nextLine();
                switch (response) {
                    case "00":
                        System.out.println(user);
                        break;
                    case "0":
                        for (Vehicle v : ivr.getVehicles()) {
                            System.out.println(v);
                        }
                        break;
                    case "1":
                        rent_car();
                        break;
                    case "2":
                        System.out.println("function for return car");
                        break;
                    case "6":
                        System.out.println("add car (only) separator is ; ");
                        ////example.Motorcycle(String brand, String model, int year, double price, String plate, String category)
                        String line = scanner.nextLine();
                        String[] arr = line.split(";");
                        int price = (int)(Double.parseDouble(arr[3]) * 100);
                        ivr.addVehicle(
                                new Car(arr[0],
                                        arr[1],
                                        Integer.parseInt(arr[2]),
                                        price,
                                        false,
                                        null
                                ));
                        break;
                    case "9":
                        System.out.println("remove user:");
                        String removeLogin = scanner.nextLine();
                        iur.removeUser(removeLogin);
                        break;

                    default:
                        running = false;
                }
            }
        } else {
            System.out.println("Bledne dane!");
        }
        System.exit(0);
    }
}
