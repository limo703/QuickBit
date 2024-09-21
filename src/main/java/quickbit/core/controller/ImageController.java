package quickbit.core.controller;

import quickbit.core.service.ImageService;
import quickbit.dbcore.entity.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("image")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(
        ImageService imageService
    ) {
        this.imageService = imageService;
    }

    @GetMapping("{uuid}")
    public ResponseEntity<Resource> getImage(
        @PathVariable String uuid
    ) {
        Image image = imageService.getByUuid(uuid);
        return imageService.downloadImage(image.getId());
    }
}
