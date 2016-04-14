package com.treelogic.proteus.websocket;

import java.io.IOException;
import java.net.URI;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.treelogic.proteus.network.WebsocketServer;

public class WebsocketClient {
	
	private static Log logger = LogFactory.getLog(WebsocketClient.class);

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
		}else{
			logger.warn("Attemping to send message when it is not open");
		}
	}
	
	/**
	 * Close the connection
	 * @throws IOException
	 */
	public void close() throws IOException{
		if(session != null && session.isOpen()){
			session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "Nomal close"));
		}else{
			logger.warn("Attemping to close connection when it is not open");
		}
	}
}