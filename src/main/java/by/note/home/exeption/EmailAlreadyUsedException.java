package by.note.home.exeption;

public class EmailAlreadyUsedException extends RuntimeException {

    public EmailAlreadyUsedException() {
        super("Email name already used!");
    }
}
