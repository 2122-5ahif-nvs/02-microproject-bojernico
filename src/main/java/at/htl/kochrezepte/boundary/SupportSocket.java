package at.htl.kochrezepte.boundary;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;

@ServerEndpoint("/api/support/{username}")
@ApplicationScoped
public class SupportSocket {

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        sessions.put(username, session);
        broadcast("Hello " +username+ ", you found the Support Team. \n " +
                "Do you need help with \n" +
                "- A: Endpoints \n" +
                "- B: Something else \n " +
                "Please answer with a single letter: ");
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessions.remove(username);
        broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        sessions.remove(username);
        broadcast("User " + username + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) {
        if (message.equalsIgnoreCase("_ready_")) {
            broadcast("User " + username + " joined");
        } else {
            broadcast(username+ ": "+message);
            if (message.length() > 1) {
                broadcast("1 letter please");
            }
            else if (!message.equals("A") && !message.equals("B")) {
                broadcast("Was that letter an option?");
            } else {
                if (message.equals("A")) {
                    broadcast("We have following endpoints: \n" +
                            "- Recipe \n" +
                            "- Ingredient \n" +
                            "- Menu");
                }
                else {
                    broadcast("Please refer to our documentation on `https://localhost:8080/q/swagger-ui`");
                }
            }
        }
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }
}
