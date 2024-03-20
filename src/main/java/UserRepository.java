import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserRepository implements IUserRepository{

    private final Map<String, User> Users = new HashMap<>();
    @Override
    public User getUser(String Login) {
        return Users.get(Login);
    }

    @Override
    public List<User> getUsers() {
       return Users.values().stream().toList();
    }

    @Override
    public void save(String path) {
        {
            File f = new File(path);

            try{

                if(!f.exists())
                    f.createNewFile();

                FileWriter fileWriter = new FileWriter(f);

                for (var u : Users.values()){
                    fileWriter.write(STR."\{u.toCSV()}\n");
                }

                fileWriter.close();
            } catch (IOException e){
                throw new RuntimeException();
            }


        }
    }

    public void load(String path){
        File file = new File(path);

        if(!file.exists())
            return;


        Scanner scanner;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNext()){
                User user = new User(scanner.nextLine().split(";"));
                Users.put(user.getLogin(), user);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
