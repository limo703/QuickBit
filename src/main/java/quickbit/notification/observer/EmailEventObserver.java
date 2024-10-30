package quickbit.notification.observer;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quickbit.core.model.UserModel;
import quickbit.notification.configuration.NotificationGateway;
import quickbit.notification.util.EventType;

@Service
public class EmailEventObserver implements EventObserver {

    private final NotificationGateway notificationGateway;

    @Autowired
    public EmailEventObserver(NotificationGateway notificationGateway) {
        this.notificationGateway = notificationGateway;
    }

    @Override
    public void sendEvent(@NotNull EventType event, @NotNull UserModel user) {



    }
}
