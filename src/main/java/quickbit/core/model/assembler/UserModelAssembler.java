package quickbit.core.model.assembler;

import quickbit.core.model.UserModel;
import quickbit.core.service.ImageService;
import quickbit.core.service.WalletService;
import quickbit.dbcore.entity.Image;
import quickbit.dbcore.entity.User;
import quickbit.dbcore.entity.Wallet;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, UserModel> {

    private final WalletService walletService;
    private final WalletModelAssembler walletModelAssembler;
    private final ImageService imageService;

    @Autowired
    public UserModelAssembler(
        WalletService walletService,
        WalletModelAssembler walletModelAssembler,
        ImageService imageService
    ) {
        this.walletService = walletService;
        this.walletModelAssembler = walletModelAssembler;
        this.imageService = imageService;
    }

    @NotNull
    @Override
    public UserModel toModel(@NotNull User entity) {
        UserModel userModel = new UserModel();

        Wallet wallet = walletService.getById(entity.getDefaultWalletId());

        Optional<Image> avatar = imageService.findById(entity.getAvatarId());

        userModel
            .setFirstName(entity.getFirstName())
            .setLastName(entity.getLastName())
            .setUsername(entity.getUsername())
            .setUuid(entity.getUuid())
            .setEmail(entity.getEmail())
            .setWallet(
                walletModelAssembler.toModel(wallet)
            );
        avatar.ifPresent(image -> userModel.setAvatar(image.getUuid()));

        return userModel;
    }

    public Page<UserModel> toModels(Page<? extends User> entities) {
        List<UserModel> userModels = entities.stream()
            .map(this::toModel)
            .collect(Collectors.toList());

        return new PageImpl<>(userModels, entities.getPageable(), entities.getTotalElements());
    }
}
