package compile;

import java.util.HashMap;
import java.util.HashSet;

public class EventManager {

    static HashMap<Event, HashSet<Handler>> hashMap = new HashMap<Event, HashSet<Handler>>();

    public static void addListener(Event event, Handler handler) {
        hashMap.computeIfAbsent(event, k -> new HashSet<Handler>());
        hashMap.get(event).add(handler);
    }

    public static void event(Event event, Object param) {
        HashSet<Handler> handlers = hashMap.get(event);
        if (!handlers.isEmpty()) {
            for (Handler handler : handlers) {
                handler.handle(param);
            }
        }
    }

}
