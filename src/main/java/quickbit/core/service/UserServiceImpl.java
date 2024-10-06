package quickbit.core.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import quickbit.core.form.CreateUserForm;
import quickbit.core.form.EditUserForm;
import quickbit.core.model.AuthUser;
import quickbit.core.service.security.SecurityService;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.Image;
import quickbit.dbcore.entity.User;
import quickbit.dbcore.entity.Wallet;
import quickbit.dbcore.repositories.UserRepository;
import quickbit.core.exception.UserNotFoundException;
import quickbit.core.util.UserRole;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import quickbit.dbcore.repositories.WalletRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final WalletRepository walletRepository;
    private final ImageService imageService;
    private final CurrencyService currencyService;
    private final SecurityService securityService;

    @Autowired
    public UserServiceImpl(
        UserRepository repository,
        PasswordEncoder passwordEncoder,
        WalletRepository walletRepository,
        ImageService imageService,
        CurrencyService currencyService,
        SecurityService securityService
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.walletRepository = walletRepository;
        this.imageService = imageService;
        this.currencyService = currencyService;
        this.securityService = securityService;
    }

    @NotNull
    @Override
    public User getById(@NotNull Long id) {
        return repository.findById(id)
            .orElseThrow(UserNotFoundException::new);
    }

    @NotNull
    @Override
    public User editUser(
        @NotNull EditUserForm form,
        @NotNull Long userId
    ) {
        User user = getById(userId);
        user
            .setFirstName(form.getFirstName())
            .setLastName(form.getLastName())
        ;

        securityService.changeContextUser(user);
        return repository.save(user);
    }

    @Nullable
    @Override
    public User getByUsername(@NotNull String username) {
        return repository.findByUsername(username).orElse(null);
    }

    @Nullable
    @Override
    public User getByEmail(@NotNull String email) {
        return repository.findByEmail(email).orElse(null);
    }

    @Override
    public Optional<User> findByUsername(@NotNull String username) {
        return repository.findByUsername(username);
    }

    @NotNull
    @Override
    @Transactional
    public User create(@NotNull CreateUserForm form) {
        User newUser = new User();
        newUser
            .setEmail(form.getEmail())
            .setUsername(form.getUsername())
            .setRole(UserRole.USER)
            .setFirstName("")
            .setLastName("")
            .setPassword(
                passwordEncoder.encode(form.getPassword())
            );

        newUser = repository.save(newUser);

        Wallet wallet = initializeWallets(newUser);
        newUser.setDefaultWalletId(wallet.getId());

        Image image = imageService.generateAndSaveAvatar(newUser);
        newUser.setAvatarId(image.getId());

        return newUser;
    }

    private Wallet initializeWallets(@NotNull User user) {
        List<Currency> currencies = currencyService.findAll();

        Set<Wallet> newWallets = new HashSet<>();
        for (Currency currency : currencies) {
            newWallets.add(
                new Wallet()
                    .setUser(user)
                    .setAmount(BigDecimal.ZERO)
                    .setCurrency(currency)
            );
        }
        walletRepository.saveAll(newWallets);

        Currency defaultCurrency = currencyService.getDefault();
        return newWallets
            .stream()
            .filter(wallet -> wallet.getCurrency().equals(defaultCurrency))
            .findFirst()
            .orElse(null);
    }

    @NotNull
    @Override
    public Page<User> findAll(@NotNull Pageable pageable) {
        return repository.findAll(pageable);
    }

    @NotNull
    @Override
    public User getByUuid(String uuid) {
        return repository.findByUuid(uuid)
            .orElseThrow(UserNotFoundException::new);
    }
}
