package example;

public class User {
    private String login;
    private String hashedPassword;

    public void setRole(UserRole role) {
        Role = role;
    }

    private UserRole Role;
    private Integer rentedVehicleID;
    private void init(String login, String hashedPassword, UserRole role, Integer id){
        this.login = login;
        this.Role = role;
        this.hashedPassword = hashedPassword;
        this.rentedVehicleID = id;
    }
    public User(String login, String hashedPassword, UserRole role){
       init(login, hashedPassword, role, null);
    }

    public User(String login, String hashedPassword, UserRole role, Integer id){
        init(login, hashedPassword, role, id);
    }

    User(String[] csvLine){
        UserRole role = (csvLine[2].equals("admin"))? UserRole.administrator: UserRole.user;
        Integer rentedVehicleID = (csvLine[3].equals("null"))?null:Integer.parseInt(csvLine[3]);
        init(csvLine[0], csvLine[1], role, rentedVehicleID);
    }

    public String toCSV(){
        String role = (this.Role == UserRole.administrator)? "admin" : "user";
        if(rentedVehicleID == null)
            return String.format("%s;%s;%s;null", login, hashedPassword, role);
        else
            return String.format("%s;%s;%s;%d", login, hashedPassword, role, rentedVehicleID);
    }

    public UserRole getRole() {
        return Role;
    }

    public String getLogin() {
        return login;
    }

    public String  getPassword() {
        return hashedPassword;
    }

    public Integer getRentedVehicleID() {
        return rentedVehicleID;
    }
    public void setRentedVehicleID(Integer rentedVehicleID){
        this.rentedVehicleID = rentedVehicleID;
    }

}
