import org.apache.commons.codec.digest.DigestUtils;

public class Authentication {

    public static String hashPassword(String plainText){
        return DigestUtils.sha3_256Hex(plainText);
    }

    private final IUserRepository repo;
    Authentication (IUserRepository repo){
        this.repo = repo;
    }

    public User auth(String login, String plainTextPassword){
        User account = repo.getUser(login);

        if(account == null)
            return null;

        return (account.getPassword().equals(hashPassword(plainTextPassword)))?account:null;
    }

}
