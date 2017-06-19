package com.treelogic.proteus.core.sinks;

import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.configuration.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.treelogic.proteus.connectors.ProteusConnector;
import com.treelogic.proteus.resources.model.IncrementalWindowResult;
import com.treelogic.proteus.visualization.server.WebsocketServer;

public class WebsocketSink extends RichSinkFunction<IncrementalWindowResult<?>> {

	/**
	 * Default generated serial version UID
	 */
	private static final long serialVersionUID = -5478444596730684432L;

	/**
	 * Default Logger
	 */
	private static Logger log = LoggerFactory.getLogger(WebsocketSink.class);

	/**
	 * This connector is used to transform the window outcomes into a readable format for the visualization library.
	 */
	private ProteusConnector connector;

	/**
	 * When JVM loads this class, this static block is  implicitily executed 
	 */
	static {
		log.debug("Initializing websocket server");
		WebsocketServer.start();
	}

	public WebsocketSink(ProteusConnector connector) {
		this.connector = connector;
	}

	@Override
	public void invoke(IncrementalWindowResult<?> data) throws Exception {
		String json = connector.apply(data).output();
		send(json);
	}

	private void send(String json) {
		WebsocketServer.sendAll(json);
		log.debug("Sending message to websocket: " + json);
	}

	@Override
	public void open(Configuration parameters) throws Exception {
		log.debug("Opening sink with parameters: " + parameters);
	}

	@Override
	public void close() throws Exception {
		log.debug("Closing Sink" + this);
		if (WebsocketServer.isRunning()) {
			WebsocketServer.stop();
		}
	}

}
