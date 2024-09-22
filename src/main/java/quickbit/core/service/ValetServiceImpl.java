package quickbit.core.service;

import quickbit.core.exception.ValetNotFoundException;
import quickbit.core.form.DepositUserForm;
import quickbit.dbcore.entity.User;
import quickbit.dbcore.entity.Valet;
import quickbit.dbcore.repositories.ValetRepository;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValetServiceImpl implements ValetService {

    private final ValetRepository valetRepository;

    @Autowired
    public ValetServiceImpl(ValetRepository valetRepository) {
        this.valetRepository = valetRepository;
    }

    @Override
    public Valet getByUserId(@NotNull Long userId) {
        return valetRepository.findByUserId(userId)
            .orElseThrow(ValetNotFoundException::new);
    }

    @Override
    public Valet save(@NotNull Valet valet) {
        return valetRepository.save(valet);
    }

    @NotNull
    @Override
    public Valet getById(Long valetId) {
        return valetRepository.findById(valetId)
            .orElseThrow(ValetNotFoundException::new);
    }

    @Override
    public Valet deposit(
        @NotNull DepositUserForm form,
        @NotNull User user
    ) {
        Valet valet = getById(user.getValetId());

        return valet;
    }
}
