package by.note.home.exeption;

/**
 * @author Yuriy Govorushkin
 */
public class CreatedEntityIdException extends RuntimeException {

    private static String message = "Создаваемый объект не может иметь заполненное поле Id";

    public CreatedEntityIdException() {
        super(message);
    }
}
