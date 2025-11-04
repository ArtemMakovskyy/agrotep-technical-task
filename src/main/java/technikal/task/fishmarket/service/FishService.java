package technikal.task.fishmarket.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import technikal.task.fishmarket.exception.FishNotFoundException;
import technikal.task.fishmarket.exception.ImageStorageException;
import technikal.task.fishmarket.exception.InvalidFishDataException;
import technikal.task.fishmarket.model.Fish;
import technikal.task.fishmarket.model.FishDto;
import technikal.task.fishmarket.repository.FishRepository;

@Service
@RequiredArgsConstructor
public class FishService {

    private static final String IMAGE_DIR = "public/images/";
    private final FishRepository fishRepository;

    public List<Fish> getAllFish() {
        return fishRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public void addFish(FishDto fishDto) {
        List<MultipartFile> images = fishDto.getImageFiles();
        if (images == null || images.isEmpty() || images.size() > 3) {
            throw new InvalidFishDataException("Кількість зображень має бути від 1 до 3");
        }

        List<String> storedFilenames = new ArrayList<>();
        Date catchDate = new Date();

        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            if (image == null || image.isEmpty()) {
                throw new InvalidFishDataException("Порожнє зображення не дозволено");
            }
            String storageFileName = catchDate.getTime() + "_" + i + "_" + image.getOriginalFilename();
            saveImage(image, storageFileName);
            storedFilenames.add(storageFileName);
        }

        Fish fish = new Fish();
        fish.setCatchDate(catchDate);
        fish.setImageFileNames(String.join(",", storedFilenames));
        fish.setName(fishDto.getName());
        fish.setPrice(fishDto.getPrice());
        fishRepository.save(fish);
    }

    public void deleteFish(Long id) {
        Fish fish = fishRepository.findById(id)
                .orElseThrow(() -> new FishNotFoundException(id));

        deleteImages(fish.getImageFileNames());
        fishRepository.delete(fish);
    }

    private void deleteImages(String fileNames) {
        if (fileNames == null || fileNames.isEmpty()) return;

        for (String fileName : fileNames.split("\\s*,\\s*")) {
            Path imagePath = Paths.get(IMAGE_DIR + fileName);
            try {
                if (Files.exists(imagePath)) {
                    Files.delete(imagePath);
                }
            } catch (Exception ex) {
                throw new ImageStorageException("Не вдалося видалити зображення " + fileName, ex);
            }
        }
    }

    private void saveImage(MultipartFile image, String fileName) {
        Path uploadPath = Paths.get(IMAGE_DIR);
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
            throw new ImageStorageException("Помилка при збереженні зображення", ex);
        }
    }
}
