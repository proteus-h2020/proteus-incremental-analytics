package com.treelogic.proteus.visualization;

import org.junit.Test;

import com.treelogic.proteus.visualization.server.WebsocketServer;

import static org.junit.Assert.*;


public class WebsocketConnectionTest {

	@Test
	public void testStartStop() {
		//start
		WebsocketServer.start();
		boolean isRunning = WebsocketServer.isRunning();
		boolean expected = true;
		assertTrue(isRunning == expected);
		//stop
		WebsocketServer.stop();
		isRunning = WebsocketServer.isRunning();
		expected = false;
		assertTrue(isRunning == expected);
	}

}
