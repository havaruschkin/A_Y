package by.note.home.service.vm;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
public class LoginVM {

    @NotNull
    private String username;

    @NotNull
    private String password;
}
