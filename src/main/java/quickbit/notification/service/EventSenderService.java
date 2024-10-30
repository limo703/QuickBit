package quickbit.notification.service;

import com.sun.istack.NotNull;
import quickbit.core.model.UserModel;
import quickbit.notification.util.EventType;

public interface EventSenderService {
    void sendNotification(@NotNull EventType eventType, @NotNull UserModel user);
}
