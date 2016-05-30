package com.treelogic.proteus.network;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

public class WebsocketServer {

	/**
	 * Common logger
	 */
	private static Log logger = LogFactory.getLog(WebsocketServer.class);

	/**
	 * Server instance
	 */
	private static Server server;

	/**
	 * Server connector instance
	 */
	private static ServerConnector connector;

	/**
	 * Initialize a websocket server
	 */
	public static void start() {
		if(isRunning()){
			return;
		}
		server = new Server();
		connector = new ServerConnector(server);
		connector.setPort(8787);
		server.addConnector(connector);

		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);

		try {
			ServerContainer wscontainer = WebSocketServerContainerInitializer
					.configureContext(context);
			wscontainer.addEndpoint(WebsocketEndpoint.class);
			synchronized (server) {
				server.start();
			}
		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}
	}
	/**
	 * Return the list of connected client. Just used for testing purposes
	 * @return List of connected users
	 */
	public static Collection<Session> getSessions(){
		return Collections.unmodifiableCollection(WebsocketEndpoint.clients);
	}
	/**
	 * Check if server is running
	 * 
	 * @return server is runnning
	 */
	public static boolean isRunning() {
		return server != null && server.isRunning();
	}

	/**
	 * Stop tje websocket server instance
	 */
	public static void stop() {
		if (server != null) {
			try {
				synchronized (server) {
					server.stop();
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	/**
	 * Send a message to all the connected clients
	 * @param message text message
	 */
	public static void sendAll(String message){
		for(Session session: WebsocketEndpoint.clients){
			send(message, session);
		}
		
	}
	
	/**
	 * Send a text message to a remote client
	 * @param message textmessage
	 * @param client client session
	 */
	public static void send(String message, Session client){
		client.getAsyncRemote().sendText(message);
	}
	
	/**
	 * Internal class to handle websocket messages
	 */
	@ClientEndpoint
	@ServerEndpoint("/websocket")
	public static class WebsocketEndpoint {

		/**
		 * Common logger
		 */
		protected Log logger = LogFactory.getLog(this.getClass());

		/**
		 * Synchronized list of connected clients to websocket server
		 */
		private static Set<Session> clients = Collections
				.synchronizedSet(new HashSet<Session>());

		/**
		 * This method is invoked when a new message is received
		 * 
		 * @param message Received string message
		 * @param session  Client reference
		 * @throws IOException
		 */
		@OnMessage
		public void onMessage(String message, Session session)
				throws IOException {
			logger.info("New message: " + message + " from client: " + session);
			synchronized (clients) {
				for (Session client : clients) {
					if (!client.equals(session)) {
						client.getBasicRemote().sendText(message);
					}
				}
			}
		}

		/**
		 * This method is invoked when a new client is connected
		 * @param session New client session
		 */
		@OnOpen
		public void onOpen(Session session) {
			logger.info("New connection to websocket endpoint: " + session);
			clients.add(session);
		}

		/**
		 * This method is invoked when a client is disconnected or an error
		 * occurs.
		 * 
		 * @param session session of the affected user
		 */
		@OnClose
		public void onClose(Session session, CloseReason reason) {
			logger.info("Disconnected client, reason "+ reason +" session:" + session);
			clients.remove(session);
		}

	}

}
