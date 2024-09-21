package quickbit.core.service;

import quickbit.dbcore.entity.Image;
import quickbit.dbcore.entity.User;
import com.sun.istack.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void uploadImage(
        @NotNull MultipartFile file,
        @NotNull User user
    );

    ResponseEntity<Resource> downloadImage(@NotNull Long imageId);

    Image getById(@NotNull Long imageId);

    Image getByUuid(@NotNull String imageUuid);

    Image generateAndSaveAvatar(@NotNull User user);

}
