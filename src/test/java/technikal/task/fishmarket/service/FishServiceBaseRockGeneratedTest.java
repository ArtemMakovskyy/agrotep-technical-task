package technikal.task.fishmarket.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;
import technikal.task.fishmarket.exception.FishNotFoundException;
import technikal.task.fishmarket.exception.ImageStorageException;
import technikal.task.fishmarket.exception.InvalidFishDataException;
import technikal.task.fishmarket.model.Fish;
import technikal.task.fishmarket.model.FishDto;
import technikal.task.fishmarket.repository.FishRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Timeout(10)
public class FishServiceBaseRockGeneratedTest {

    @Mock
    private FishRepository fishRepository;

    private FishService fishService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fishService = new FishService(fishRepository);
    }

    @Test
    @DisplayName("getAllFish returns a list of fish sorted by id descending")
    void getAllFishReturnsListOfFishSortedByIdDescending() {
        List<Fish> expectedFish = new ArrayList<>();
        Fish fish1 = new Fish();
        Fish fish2 = new Fish();
        expectedFish.add(fish1);
        expectedFish.add(fish2);

        when(fishRepository.findAll(any(Sort.class))).thenReturn(expectedFish);

        List<Fish> result = fishService.getAllFish();

        assertThat(result, hasSize(2));
        verify(fishRepository, atLeast(1)).findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Test
    @DisplayName("addFish successfully adds a fish with one image")
    void addFishSuccessfullyWithOneImage() throws IOException {
        FishDto fishDto = new FishDto();
        fishDto.setName("Salmon");
        fishDto.setPrice(25.99);
        MultipartFile mockImage = mock(MultipartFile.class);
        when(mockImage.isEmpty()).thenReturn(false);
        when(mockImage.getOriginalFilename()).thenReturn("fish.jpg");
        when(mockImage.getInputStream()).thenReturn(new ByteArrayInputStream("image data".getBytes()));

        List<MultipartFile> images = new ArrayList<>();
        images.add(mockImage);
        fishDto.setImageFiles(images);

        try (var mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            mockedFiles.when(() -> Files.createDirectories(any(Path.class))).thenReturn(null);
            mockedFiles.when(() -> Files.copy(any(InputStream.class), any(Path.class), eq(StandardCopyOption.REPLACE_EXISTING))).thenReturn(0L);

            fishService.addFish(fishDto);
        }

        verify(fishRepository, atLeast(1)).save(any(Fish.class));
    }

    @Test
    @DisplayName("addFish successfully adds a fish with three images")
    void addFishSuccessfullyWithThreeImages() throws IOException {
        FishDto fishDto = new FishDto();
        fishDto.setName("Tuna");
        fishDto.setPrice(45.99);

        List<MultipartFile> images = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            MultipartFile mockImage = mock(MultipartFile.class);
            when(mockImage.isEmpty()).thenReturn(false);
            when(mockImage.getOriginalFilename()).thenReturn("fish" + i + ".jpg");
            when(mockImage.getInputStream()).thenReturn(new ByteArrayInputStream("image data".getBytes()));
            images.add(mockImage);
        }
        fishDto.setImageFiles(images);

        try (var mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            mockedFiles.when(() -> Files.createDirectories(any(Path.class))).thenReturn(null);
            mockedFiles.when(() -> Files.copy(any(InputStream.class), any(Path.class), eq(StandardCopyOption.REPLACE_EXISTING))).thenReturn(0L);

            fishService.addFish(fishDto);
        }

        verify(fishRepository, atLeast(1)).save(any(Fish.class));
    }

    @Test
    @DisplayName("addFish throws InvalidFishDataException when images list is null")
    void addFishThrowsExceptionWhenImagesAreNull() {
        FishDto fishDto = new FishDto();
        fishDto.setName("Bass");
        fishDto.setPrice(15.99);
        fishDto.setImageFiles(null);

        InvalidFishDataException exception = assertThrows(InvalidFishDataException.class, () -> fishService.addFish(fishDto));
        assertThat(exception.getMessage(), is("Кількість зображень має бути від 1 до 3"));
    }

    @Test
    @DisplayName("addFish throws InvalidFishDataException when images list is empty")
    void addFishThrowsExceptionWhenImagesAreEmpty() {
        FishDto fishDto = new FishDto();
        fishDto.setName("Cod");
        fishDto.setPrice(20.99);
        fishDto.setImageFiles(new ArrayList<>());

        InvalidFishDataException exception = assertThrows(InvalidFishDataException.class, () -> fishService.addFish(fishDto));
        assertThat(exception.getMessage(), is("Кількість зображень має бути від 1 до 3"));
    }

    @Test
    @DisplayName("addFish throws InvalidFishDataException when images list has more than 3 images")
    void addFishThrowsExceptionWhenTooManyImages() {
        FishDto fishDto = new FishDto();
        fishDto.setName("Trout");
        fishDto.setPrice(18.99);

        List<MultipartFile> images = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            images.add(mock(MultipartFile.class));
        }
        fishDto.setImageFiles(images);

        InvalidFishDataException exception = assertThrows(InvalidFishDataException.class, () -> fishService.addFish(fishDto));
        assertThat(exception.getMessage(), is("Кількість зображень має бути від 1 до 3"));
    }

    @Test
    @DisplayName("addFish throws InvalidFishDataException when image is empty")
    void addFishThrowsExceptionWhenImageIsEmpty() {
        FishDto fishDto = new FishDto();
        fishDto.setName("Pike");
        fishDto.setPrice(22.99);

        MultipartFile mockImage = mock(MultipartFile.class);
        when(mockImage.isEmpty()).thenReturn(true);

        List<MultipartFile> images = new ArrayList<>();
        images.add(mockImage);
        fishDto.setImageFiles(images);

        InvalidFishDataException exception = assertThrows(InvalidFishDataException.class, () -> fishService.addFish(fishDto));
        assertThat(exception.getMessage(), is("Порожнє зображення не дозволено"));
    }

    @Test
    @DisplayName("addFish throws InvalidFishDataException when image is null")
    void addFishThrowsExceptionWhenImageIsNull() {
        FishDto fishDto = new FishDto();
        fishDto.setName("Mackerel");
        fishDto.setPrice(12.99);

        List<MultipartFile> images = new ArrayList<>();
        images.add(null);
        fishDto.setImageFiles(images);

        InvalidFishDataException exception = assertThrows(InvalidFishDataException.class, () -> fishService.addFish(fishDto));
        assertThat(exception.getMessage(), is("Порожнє зображення не дозволено"));
    }

    @Test
    @DisplayName("addFish throws ImageStorageException when saving image fails")
    void addFishThrowsImageStorageExceptionWhenSaveImageFails() throws IOException {
        FishDto fishDto = new FishDto();
        fishDto.setName("Herring");
        fishDto.setPrice(8.99);

        MultipartFile mockImage = mock(MultipartFile.class);
        when(mockImage.isEmpty()).thenReturn(false);
        when(mockImage.getOriginalFilename()).thenReturn("fish.jpg");
        when(mockImage.getInputStream()).thenReturn(new ByteArrayInputStream("image data".getBytes()));

        List<MultipartFile> images = new ArrayList<>();
        images.add(mockImage);
        fishDto.setImageFiles(images);

        try (var mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(false);
            mockedFiles.when(() -> Files.createDirectories(any(Path.class))).thenAnswer(invocation -> {
                throw new IOException("Cannot create directory");
            });

            ImageStorageException exception = assertThrows(ImageStorageException.class, () -> fishService.addFish(fishDto));
            assertThat(exception.getMessage(), is("Помилка при збереженні зображення"));
        }
    }

    @Test
    @DisplayName("deleteFish successfully deletes a fish with existing images")
    void deleteFishSuccessfullyWithExistingImages() {
        Long fishId = 1L;
        Fish fish = new Fish();
        fish.setImageFileNames("image1.jpg,image2.jpg");

        when(fishRepository.findById(fishId)).thenReturn(Optional.of(fish));

        try (var mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            mockedFiles.when(() -> Files.delete(any(Path.class))).thenAnswer(invocation -> null);

            fishService.deleteFish(fishId);
        }

        verify(fishRepository, atLeast(1)).delete(fish);
    }

    @Test
    @DisplayName("deleteFish successfully deletes a fish with null imageFileNames")
    void deleteFishSuccessfullyWithNullImageFileNames() {
        Long fishId = 2L;
        Fish fish = new Fish();
        fish.setImageFileNames(null);

        when(fishRepository.findById(fishId)).thenReturn(Optional.of(fish));

        try (var mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            mockedFiles.when(() -> Files.delete(any(Path.class))).thenAnswer(invocation -> null);

            fishService.deleteFish(fishId);
        }

        verify(fishRepository, atLeast(1)).delete(fish);
    }

    @Test
    @DisplayName("deleteFish throws FishNotFoundException when fish does not exist")
    void deleteFishThrowsExceptionWhenFishNotFound() {
        Long fishId = 999L;
        when(fishRepository.findById(fishId)).thenReturn(Optional.empty());

        FishNotFoundException exception = assertThrows(FishNotFoundException.class, () -> fishService.deleteFish(fishId));
        assertThat(exception.getMessage(), is("Рибка з id " + fishId + " не знайдена"));
    }

    @Test
    @DisplayName("deleteFish throws ImageStorageException when deleting image fails")
    void deleteFishThrowsImageStorageExceptionWhenDeleteImagesFails() {
        Long fishId = 3L;
        Fish fish = new Fish();
        fish.setImageFileNames("image1.jpg");

        when(fishRepository.findById(fishId)).thenReturn(Optional.of(fish));

        try (var mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            mockedFiles.when(() -> Files.delete(any(Path.class))).thenAnswer(invocation -> {
                throw new IOException("Cannot delete file");
            });

            ImageStorageException exception = assertThrows(ImageStorageException.class, () -> fishService.deleteFish(fishId));
            assertThat(exception.getMessage(), is("Не вдалося видалити зображення image1.jpg"));
        }
    }
}
