package by.note.home.exeption;

/**
 * @author Yuriy Govorushkin
 */
public class UpdatedEntityIdException extends RuntimeException {

    private static String message = "Обновляемый объект не может иметь пустое поле Id";

    public UpdatedEntityIdException() {
        super(message);
    }
}
