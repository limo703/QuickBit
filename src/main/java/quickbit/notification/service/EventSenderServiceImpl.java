package quickbit.notification.service;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import quickbit.core.model.UserModel;
import quickbit.notification.observer.EventObserver;
import quickbit.notification.util.EventType;

import java.util.Set;

@Service
public class EventSenderServiceImpl implements EventSenderService {

    private final Set<EventObserver> observers;

    @Autowired
    public EventSenderServiceImpl(
        @Qualifier("basicObservers") Set<EventObserver> observers
    ) {
        this.observers = observers;
    }

    @Override
    public void sendNotification(@NotNull EventType eventType, @NotNull UserModel user) {
        for (EventObserver observer : observers) {
            observer.sendEvent(eventType, user);
        }
    }
}
