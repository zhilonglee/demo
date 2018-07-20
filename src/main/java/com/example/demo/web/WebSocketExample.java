package com.example.demo.web;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
  * @ServerEndpoint Annotations are class-level annotations that define the current class as a websocket server.
  * The value of the annotation will be used to listen to
 *  the user's connection terminal to access the URL address
 *  through which the client can connect to the WebSocket server
 */
@ServerEndpoint("/websocket")
@Component
public class WebSocketExample {
	
	private static int onlineCount = 0;
	
	//concurrent package thread-safe Set.To enable a server to communicate with a single client,
    // can use a Map to store the Key, which can be identified by the user
	private static CopyOnWriteArraySet<WebSocketExample> webSocketSet = new CopyOnWriteArraySet<WebSocketExample>();
	
	//A connection session with a client requires it to send data to the client
	private Session session;


	@OnOpen
	public void onOpen(Session session) {
		List<String> list = session.getRequestParameterMap().get("name");
		setSession(session);
		System.out.println(list.toString());
		webSocketSet.add(this);
		addOnlineCount();
		System.out.println("A new connection has joined! Session is :" + session.getId());
		System.out.println("Current online :" + getOnlineCount());
		
	}

	@OnClose
	public void onClose(Session session) {
		webSocketSet.remove(this);
		subOnlineCount();
		System.out.println("A connection has disconnected! Session is :" + session.getId());
		System.out.println("Current online :" + getOnlineCount());
		
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		try {
			throw throwable;
		} catch (Throwable t) {
			System.out.println("The new connection encounters something unknown error ! Message is :" + t.getMessage());
		}
		
	}

	@OnMessage
	 public void onMessage(String message, Session session) {
		System.out.println("Message from session " + session.getRequestParameterMap().get("name").get(0) + ":" + message);
		//Group-sent message
		for(WebSocketExample item: webSocketSet){
			 try {
				 item.sendMessage(session.getRequestParameterMap().get("name").get(0)+":"+message);
			} catch (Exception e) {
				System.out.println(session.getId() + " encountered an unknown error when send a message. Error is : " + e.getMessage());
			}
		}
	}

	public void sendMessage(String message) throws Exception{
		this.session.getBasicRemote().sendText(message);
	}
	
	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		WebSocketExample.onlineCount++;
	}
		
	public static synchronized void subOnlineCount() {
		WebSocketExample.onlineCount--;
    }
		
	public Session getSession() {
		return session;
	}


	public void setSession(Session session) {
		this.session = session;
	}
    
	
}
