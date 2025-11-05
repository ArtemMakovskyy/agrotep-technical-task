package technikal.task.fishmarket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;
import technikal.task.fishmarket.model.FishDto;
import technikal.task.fishmarket.model.Fish;
import technikal.task.fishmarket.service.FishService;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

@Timeout(10)
class FishControllerBaseRockGeneratedTest {

    @Mock
    private FishService fishService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    private FishController fishController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fishController = new FishController(fishService);
    }

    @Test
    void testShowFishList() {
        List<Fish> expectedFishList = new ArrayList<>();
        Fish fish = new Fish();
        fish.setName("Test Fish");
        fish.setPrice(10.50);
        expectedFishList.add(fish);
        when(fishService.getAllFish()).thenReturn(expectedFishList);
        String result = fishController.showFishList(model);
        assertThat(result, is("index"));
        verify(model, atLeast(1)).addAttribute("fishlist", expectedFishList);
        verify(fishService, atLeast(1)).getAllFish();
    }

    @Test
    void testShowFishListWithEmptyList() {
        List<Fish> emptyFishList = new ArrayList<>();
        when(fishService.getAllFish()).thenReturn(emptyFishList);
        String result = fishController.showFishList(model);
        assertThat(result, is("index"));
        verify(model, atLeast(1)).addAttribute("fishlist", emptyFishList);
        verify(fishService, atLeast(1)).getAllFish();
    }

    @Test
    void testShowCreatePage() {
        String result = fishController.showCreatePage(model);
        assertThat(result, is("createFish"));
        verify(model, atLeast(1)).addAttribute(eq("fishDto"), any(FishDto.class));
    }

    @Test
    void testAddFishWithValidDataAndImageFiles() {
        FishDto fishDto = new FishDto();
        fishDto.setName("Test Fish");
        fishDto.setPrice(15.0);
        List<MultipartFile> imageFiles = new ArrayList<>();
        MultipartFile mockFile = mock(MultipartFile.class);
        imageFiles.add(mockFile);
        fishDto.setImageFiles(imageFiles);
        when(bindingResult.hasErrors()).thenReturn(false);
        String result = fishController.addFish(fishDto, bindingResult);
        assertThat(result, is("redirect:/fish"));
        verify(fishService, atLeast(1)).addFish(fishDto);
    }

    @Test
    void testAddFishWithNullImageFiles() {
        FishDto fishDto = new FishDto();
        fishDto.setName("Test Fish");
        fishDto.setPrice(15.0);
        fishDto.setImageFiles(null);
        when(bindingResult.hasErrors()).thenReturn(true);
        String result = fishController.addFish(fishDto, bindingResult);
        assertThat(result, is("createFish"));
        verify(bindingResult, atLeast(1)).addError(any(FieldError.class));
    }

    @Test
    void testAddFishWithEmptyImageFiles() {
        FishDto fishDto = new FishDto();
        fishDto.setName("Test Fish");
        fishDto.setPrice(15.0);
        fishDto.setImageFiles(new ArrayList<>());
        when(bindingResult.hasErrors()).thenReturn(true);
        String result = fishController.addFish(fishDto, bindingResult);
        assertThat(result, is("createFish"));
        verify(bindingResult, atLeast(1)).addError(any(FieldError.class));
    }

    @Test
    void testAddFishWithValidationErrors() {
        FishDto fishDto = new FishDto();
        fishDto.setName("Test Fish");
        fishDto.setPrice(15.0);
        List<MultipartFile> imageFiles = new ArrayList<>();
        MultipartFile mockFile = mock(MultipartFile.class);
        imageFiles.add(mockFile);
        fishDto.setImageFiles(imageFiles);
        when(bindingResult.hasErrors()).thenReturn(true);
        String result = fishController.addFish(fishDto, bindingResult);
        assertThat(result, is("createFish"));
    }

    @Test
    void testAddFishWithServiceException() throws Exception {
        FishDto fishDto = new FishDto();
        fishDto.setName("Test Fish");
        fishDto.setPrice(15.0);
        List<MultipartFile> imageFiles = new ArrayList<>();
        MultipartFile mockFile = mock(MultipartFile.class);
        imageFiles.add(mockFile);
        fishDto.setImageFiles(imageFiles);
        when(bindingResult.hasErrors()).thenReturn(false).thenReturn(true);
        doThrow(new RuntimeException("Service error")).when(fishService).addFish(fishDto);
        String result = fishController.addFish(fishDto, bindingResult);
        assertThat(result, is("createFish"));
        verify(bindingResult, atLeast(1)).addError(any(FieldError.class));
        verify(fishService, atLeast(1)).addFish(fishDto);
    }

    @Test
    void testDeleteFish() {
        Long fishId = 1L;
        String result = fishController.deleteFish(fishId);
        assertThat(result, is("redirect:/fish"));
        verify(fishService, atLeast(1)).deleteFish(fishId);
    }

    @Test
    void testDeleteFishWithZeroId() {
        Long fishId = 0L;
        String result = fishController.deleteFish(fishId);
        assertThat(result, is("redirect:/fish"));
        verify(fishService, atLeast(1)).deleteFish(fishId);
    }

    @Test
    void testDeleteFishWithNegativeId() {
        Long fishId = -1L;
        String result = fishController.deleteFish(fishId);
        assertThat(result, is("redirect:/fish"));
        verify(fishService, atLeast(1)).deleteFish(fishId);
    }

    @Test
    void testConstructorCreatesInstance() {
        FishController controller = new FishController(fishService);
        assertThat(controller, is(notNullValue()));
    }

    @Test
    void testFishBasePathConstant() {
        assertThat(FishController.FISH_BASE_PATH, is("/fish"));
    }
}
