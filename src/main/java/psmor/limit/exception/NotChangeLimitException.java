package psmor.limit.exception;

public class NotChangeLimitException extends RuntimeException {
    public NotChangeLimitException() {

        super("Не найден запрос на изменение лимита");
    }
}
