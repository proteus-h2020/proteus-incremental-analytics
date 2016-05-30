package com.treelogic.proteus.websocket;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.websocket.DeploymentException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.treelogic.proteus.network.WebsocketServer;

public class WebsocketMessagesTest {

	@BeforeClass
	public static void startServer(){
		WebsocketServer.start();
	}
	
	@Test(timeout = 0)
	public void testSessions() throws DeploymentException, IOException, InterruptedException{
		Thread.sleep(500);
		assertTrue(WebsocketServer.isRunning());
		WebsocketClient client = new WebsocketClient("ws://localhost:8787/websocket");
		WebsocketClient client2 = new WebsocketClient("ws://localhost:8787/websocket");
		WebsocketClient client3 = new WebsocketClient("ws://localhost:8787/websocket");
		WebsocketClient client4 = new WebsocketClient("ws://localhost:8787/websocket");
		int actualClients = WebsocketServer.getSessions().size();
		assertSame(actualClients, 4 * 2); //why *2? every client is added twice to session list (server and client connection).
		
		client.close();
		client2.close();
		client3.close();
		client4.close();
		Thread.sleep(200);
		actualClients = WebsocketServer.getSessions().size();
		assertSame(actualClients, 0);
	}
	
	@AfterClass
	public static void stopServer(){
		WebsocketServer.stop();
	}

}
