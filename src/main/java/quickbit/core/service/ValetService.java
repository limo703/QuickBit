package quickbit.core.service;

import quickbit.dbcore.entity.Valet;
import com.sun.istack.NotNull;

public interface ValetService {
    Valet getByUserId(@NotNull Long userId);

    Valet save(@NotNull Valet valet);

    @NotNull
    Valet getById(Long valetId);
}
