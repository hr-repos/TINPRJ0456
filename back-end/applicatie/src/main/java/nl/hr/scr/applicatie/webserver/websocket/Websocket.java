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
    private final Main main;
    private final Set<WsContext> connected = new HashSet<>();

    public Websocket(Webserver webserver, Main main) {
        this.main = main;

        webserver.http().ws("api/socket", context -> {
            context.onConnect(this::onConnect);
            context.onError(WsContext::closeSession);
            context.onClose(this::onClose);
        });
    }

    public void broadcast(Map<String, ?> message) {
        System.out.println("Connected: " + this.connected.size());
        this.connected.forEach(ws -> {
            ws.send(message);
            System.out.println("Sent message: " + message);
        });
    }

    private static final Map<String, Boolean> CONNECTED = Map.of("connected", true);

    private void onConnect(WsConnectContext ws) {
        this.connected.add(ws);
        ws.send(CONNECTED);
    }

    private void onClose(WsCloseContext ws) {
        this.connected.remove(ws);
    }
}
