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
import java.util.stream.Collectors;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, UserModel> {

    private final WalletService walletService;
    private final ValetModelAssembler valetModelAssembler;
    private final ImageService imageService;

    @Autowired
    public UserModelAssembler(
        WalletService walletService,
        ValetModelAssembler valetModelAssembler,
        ImageService imageService
    ) {
        this.walletService = walletService;
        this.valetModelAssembler = valetModelAssembler;
        this.imageService = imageService;
    }

    @NotNull
    @Override
    public UserModel toModel(@NotNull User entity) {
        UserModel userModel = new UserModel();

        Wallet wallet = walletService.getByUserId(entity.getId());

        Image avatar = imageService.getById(entity.getAvatarId());

        userModel
            .setFirstName(entity.getFirstName())
            .setLastName(entity.getLastName())
            .setUsername(entity.getUsername())
            .setUuid(entity.getUuid())
            .setAvatar(avatar.getUuid())
            .setValet(
                valetModelAssembler.toModel(wallet)
            );

        return userModel;
    }

    public Page<UserModel> toPagedModel(Page<? extends User> entities) {
        List<UserModel> userModels = entities.stream()
            .map(this::toModel)
            .collect(Collectors.toList());

        return new PageImpl<>(userModels, entities.getPageable(), entities.getTotalElements());
    }
}
