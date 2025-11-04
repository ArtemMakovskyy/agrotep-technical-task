package technikal.task.fishmarket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import technikal.task.fishmarket.exception.FishNotFoundException;
import technikal.task.fishmarket.exception.ImageStorageException;
import technikal.task.fishmarket.exception.InvalidFishDataException;
import technikal.task.fishmarket.model.Fish;
import technikal.task.fishmarket.model.FishDto;
import technikal.task.fishmarket.repository.FishRepository;

import java.io.InputStream;
import java.nio.file.*;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FishService {

    private static final String IMAGE_DIR = "public/images/";

    private final FishRepository fishRepository;

    public List<Fish> getAllFish() {
        return fishRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public void addFish(FishDto fishDto) {
        MultipartFile image = fishDto.getImageFile();
        if (image == null || image.isEmpty()) {
            throw new InvalidFishDataException("Фото рибки обов'язкове");
        }

        Date catchDate = new Date();
        String storageFileName = catchDate.getTime() + "_" + image.getOriginalFilename();

        saveImage(image, storageFileName);

        Fish fish = new Fish();
        fish.setCatchDate(catchDate);
        fish.setImageFileName(storageFileName);
        fish.setName(fishDto.getName());
        fish.setPrice(fishDto.getPrice());

        fishRepository.save(fish);
    }

    public void deleteFish(int id) {
        Fish fish = fishRepository.findById(id)
                .orElseThrow(() -> new FishNotFoundException(id));

        deleteImage(fish.getImageFileName());
        fishRepository.delete(fish);
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

    private void deleteImage(String fileName) {
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
