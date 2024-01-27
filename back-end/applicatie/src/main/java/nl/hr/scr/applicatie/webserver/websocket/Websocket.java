package nl.hr.scr.applicatie.webserver.websocket;

import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsContext;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* Eli @ October 20, 2023 (nl.hr.scr.applicatie.webserver.websocket) */
public final class Websocket {
    private final Set<WsContext> connected = new HashSet<>();

    // websocket endpoint to broadcast sensor data to all connected dashboards
    public Websocket(Webserver webserver) {
        webserver.http().ws("api/socket", context -> {
            context.onConnect(this::onConnect);
            context.onError(WsContext::closeSession);
            context.onClose(this::onClose);
        });
    }

    // broadcast to all connected dashboards through websocket
    public void broadcast(Map<String, ?> message) {
        this.connected.forEach(ws -> ws.send(message));
    }

    private static final Map<String, Boolean> CONNECTED = Map.of("connected", true);

    // send connected message to dashboard
    private void onConnect(WsConnectContext ws) {
        this.connected.add(ws);
        ws.send(CONNECTED);
    }

    // remove dashboard from connected list
    private void onClose(WsCloseContext ws) {
        this.connected.remove(ws);
    }
}
