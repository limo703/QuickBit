package quickbit.notification.observer;

import quickbit.core.model.UserModel;
import quickbit.notification.util.EventType;

public interface EventObserver {
    void sendEvent(EventType event, UserModel user);
}
