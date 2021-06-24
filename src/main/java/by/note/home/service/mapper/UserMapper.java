package by.note.home.service.mapper;

import by.note.home.model.User;
import by.note.home.service.dto.UserDTO;
import java.util.List;

public interface UserMapper {

    List<UserDTO> toUserDTOs(List<User> users);

    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDto);
    }
