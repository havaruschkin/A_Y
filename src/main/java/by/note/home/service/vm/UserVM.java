package by.note.home.service.vm;

import by.note.home.service.dto.UserDTO;

public class UserVM extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 1;
    public static final int PASSWORD_MAX_LENGTH = 20;

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
