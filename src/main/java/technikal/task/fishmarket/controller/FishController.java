package technikal.task.fishmarket.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import technikal.task.fishmarket.model.FishDto;
import technikal.task.fishmarket.service.FishService;

@Controller
@RequestMapping(FishController.FISH_BASE_PATH)
@RequiredArgsConstructor
public class FishController {

    public static final String FISH_BASE_PATH = "/fish";
    private static final String REDIRECT_FISH = "redirect:/fish";
    private static final String CREATE_FISH_PAGE = "createFish";
    private static final String MODEL_FISH_LIST = "fishlist";
    private static final String MODEL_FISH_DTO = "fishDto";
    private static final String FIELD_IMAGE_FILE = "imageFiles";

    private final FishService fishService;

    @GetMapping({"", "/"})
    public String showFishList(Model model) {
        model.addAttribute(MODEL_FISH_LIST, fishService.getAllFish());
        return "index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute(MODEL_FISH_DTO, new FishDto());
        return CREATE_FISH_PAGE;
    }

    @PostMapping("/create")
    public String addFish(@Valid @ModelAttribute FishDto fishDto, BindingResult result) {
        if (fishDto.getImageFiles() == null || fishDto.getImageFiles().isEmpty()) {
            result.addError(new FieldError(MODEL_FISH_DTO, FIELD_IMAGE_FILE, "Потрібне хоча б одне фото рибки"));
        }

        if (result.hasErrors()) {
            return CREATE_FISH_PAGE;
        }

        try {
            fishService.addFish(fishDto);
        } catch (Exception e) {
            result.addError(new FieldError(MODEL_FISH_DTO, FIELD_IMAGE_FILE, "Помилка при збереженні файлу"));
            return CREATE_FISH_PAGE;
        }

        return REDIRECT_FISH;
    }

    @DeleteMapping("/delete")
    public String deleteFish(@RequestParam int id) {
        fishService.deleteFish(id);
        return REDIRECT_FISH;
    }
}
