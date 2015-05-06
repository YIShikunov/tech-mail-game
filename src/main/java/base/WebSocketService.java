package base;

import org.json.simple.JSONObject;

public interface WebSocketService {
    public void send(Boolean isFirstPlayer, JSONObject packet);
}
