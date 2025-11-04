package technikal.task.fishmarket.exception;

public class FishNotFoundException extends RuntimeException {
    public FishNotFoundException(Long id) {
        super("Рибка з id " + id + " не знайдена");
    }
}
