package by.note.home.service.dto;

import by.note.home.model.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserDTO {

    private Long id;
    private String login;
    private String email;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdTs;

    private boolean activated = false;
    private Set<String> authorities;
    private Status status;
    private String language;
    private String theme;
}
