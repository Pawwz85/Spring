package org.example.service;

import lombok.AllArgsConstructor;
import org.example.authenticate.Authenticator;
import org.example.dao.IUserRepository;
import org.example.dto.CreateUserDto;
import org.example.dto.UserDto;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    public Collection<UserDto> getUsers() {
        Collection<UserDto> userDtos = new ArrayList<>();
        Collection<User> users = userRepository.getUsers();
        for (User user : users) {
            UserDto userDto = new UserDto(user.getLogin(), user.getRole());
            userDtos.add(userDto);
        }
        return userDtos;
    }

    public UserDto getUser(String login) {
        System.out.println("login");
        User user = userRepository.getUser(login);
        if (user != null)
            return new UserDto(user.getLogin(),user.getRole());
        else
            return null;
    }


    public Boolean createUser(CreateUserDto createUserDto) {
        User newUser = new User();
        newUser.setLogin(createUserDto.getLogin());
        newUser.setPassword(Authenticator.hashPassword(createUserDto.getPassword()));
        newUser.setRole(User.Role.USER);
        boolean success = userRepository.getUser(newUser.getLogin()) == null;
        if(success)
            userRepository.addUser(newUser);
        return success;
    }
    public String deleteUser(String login) {
        User user = userRepository.getUser(login);
        if (user == null)
            return "not found";
        else if (user.getVehicle() != null)
            return "vehicle is not null";
        else
            userRepository.removeUser(login);
        return "deleted";
    }

    public Boolean authUser(String login, String password){
        return Authenticator.login(login, password) != null;
    }
}


