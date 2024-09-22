package quickbit.core.util;


import com.sun.istack.NotNull;

import java.util.UUID;

public class FileConstraints {

    public static String generateFileName(String userUuid) {
        return String.format("image_user_%s_%s", userUuid, UUID.randomUUID());
    }

    public static String generateImageFileName(@NotNull String userUuid) {
        return String.format("image_user_%s_%s.png", userUuid, UUID.randomUUID());
    }
}
