package quickbit.core.service;

import quickbit.core.exception.FileNotFoundException;
import quickbit.dbcore.entity.Image;
import quickbit.dbcore.entity.User;
import quickbit.dbcore.repositories.ImageRepository;
import quickbit.util.FileConstrants;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;


@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final String imagePath;
    private final String defaultImage;

    @Autowired
    public ImageServiceImpl(
        ImageRepository imageRepository,
        @Value("${image.path}") String imagePath,
        @Value("${image.default}") String defaultImage
    ) {
        this.imageRepository = imageRepository;
        this.imagePath = imagePath;
        this.defaultImage = defaultImage;
    }

    @PostConstruct
    public void initImagePath() throws IOException {
        Path folder = Paths.get(imagePath);
        Files.createDirectories(folder);
    }

    @Override
    public Image getById(@NotNull Long imageId) {
        return imageRepository.findById(imageId)
            .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Image getByUuid(@NotNull String imageUuid) {
        return imageRepository.findByUuid(imageUuid)
            .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void uploadImage(
        @NotNull MultipartFile file,
        @NotNull User user
    ) {
        String fileName;
        Path filePath;
        try {
            fileName = FileConstrants.generateFileName(user.getUuid());
            filePath = Paths.get(imagePath, fileName);

            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            System.out.print("произошла ошибка при загрузке image");
            throw new RuntimeException(e);
        }
        Image image = getById(user.getAvatarId());
        image
            .setFilePath(filePath.toString())
            .setOriginalFileName(fileName);

        imageRepository.save(image);
    }

    @Override
    public ResponseEntity<Resource> downloadImage(@NotNull Long imageId) {
        Image image = getById(imageId);
        try {
            Path filePath = Paths.get(image.getFilePath());
            if (!Files.exists(filePath)) {
                throw new FileNotFoundException("Файл изображения не найден");
            }

            Resource fileResource = new UrlResource(filePath.toUri());

            String contentType = Files.probeContentType(filePath);
            if (Objects.isNull(contentType)) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "inline; filename=\"" + image.getOriginalFileName() + "\""
                )
                .body(fileResource);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке файла", e);
        }
    }

    @Override
    public Image generateAndSaveAvatar(@NotNull User user) {
        Path filePath = Paths.get(imagePath, defaultImage);
        Image image = new Image()
            .setFilePath(filePath.toString())
            .setOriginalFileName(defaultImage)
            .setUser(user)
            .setUserId(user.getId());

        return imageRepository.save(image);
    }
}
