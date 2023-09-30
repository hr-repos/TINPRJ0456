package nl.hr.scr.applicatie.util;

import io.javalin.http.Context;
import io.javalin.websocket.WsContext;

/* Eli @ April 28, 2023 (org.eu.nl.gug.bankserver.util) */
public final class NetworkUtil
{
    // ip-adres verkrijgen van webserver context
    public static String ip (Context context) {
        return context.headerMap().computeIfAbsent(
            "X-Forwarded-For",
            empty -> context.ip()
        );
    }
}
