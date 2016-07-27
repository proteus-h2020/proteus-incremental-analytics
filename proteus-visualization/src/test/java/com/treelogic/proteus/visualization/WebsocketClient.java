package com.treelogic.proteus.visualization;

import java.io.IOException;
import java.net.URI;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.treelogic.proteus.visualization.server.WebsocketServer;


public class WebsocketClient {
	

	private Session session;
	/**
	 * Initialize a connection between this client and server
	 * @param stringUri server uri
	 * @throws DeploymentException
	 * @throws IOException
	 */
	public WebsocketClient(String stringUri) throws DeploymentException, IOException{//ws://localhost:8080/events/
		URI uri = URI.create(stringUri);
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		this.session = container.connectToServer(WebsocketServer.WebsocketEndpoint.class,uri);
	}
	/**
	 * Send a message to the connected server
	 * @param textMessage text messages
	 * @throws IOException
	 */
	public void send(String textMessage) throws IOException{
		if(session != null && session.isOpen()){
			session.getBasicRemote().sendText("Hello");
		}
	}
	
	/**
	 * Close the connection
	 * @throws IOException
	 */
	public void close() throws IOException{
		if(session != null && session.isOpen()){
			session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "Nomal close"));
		}
	}
}