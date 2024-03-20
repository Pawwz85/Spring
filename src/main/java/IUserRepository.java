import java.util.ArrayList;
import java.util.List;

public interface IUserRepository {
    User getUser(String Login);
    List<User> getUsers();
    void save(String path);

}
