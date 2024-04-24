package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//todo: przy zwracaniu dodac informacje na temat wypozyczonego pojazdu
public class UserDto {

    private String login;
    private User.Role role;
}