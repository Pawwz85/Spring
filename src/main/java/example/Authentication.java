package example;

import example.dao.IUserRepository;
import org.apache.commons.codec.digest.DigestUtils;

public class Authentication {

    public static String hashPassword(String plainText){
        return DigestUtils.sha3_256Hex(plainText);
    }

    public static void setRepo(IUserRepository repo) {
        Authentication.repo = repo;
    }

    private static IUserRepository repo;

    public static User auth(String login, String plainTextPassword){
        User account = repo.getUser(login);

        if(account == null)
            return null;

        return (account.getPassword().equals(hashPassword(plainTextPassword)))?account:null;
    }

}
