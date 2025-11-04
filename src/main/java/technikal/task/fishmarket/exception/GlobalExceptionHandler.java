package technikal.task.fishmarket.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FishNotFoundException.class)
    public String handleFishNotFound(FishNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(ImageStorageException.class)
    public String handleImageStorageError(ImageStorageException ex, Model model) {
        model.addAttribute("errorMessage", "Помилка роботи із зображеннями: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(InvalidFishDataException.class)
    public String handleInvalidData(InvalidFishDataException ex, Model model) {
        model.addAttribute("errorMessage", "Неправильні дані: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model) {
        model.addAttribute("errorMessage", "Невідома помилка: " + ex.getMessage());
        return "error";
    }
}
