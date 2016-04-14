package com.treelogic.proteus.flink.sink;

import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import com.treelogic.proteus.network.WebsocketServer;

public class WebsocketSink<IN> extends RichSinkFunction<IN> {
	
	/**
	 * Common logger
	 */
	private static Log logger = LogFactory.getLog(WebsocketSink.class);

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = -1330993740891863057L;

	/**
	 * When JVM loads this class, static block is executed implicitily
	 */
	static {
		logger.info("Initializing websocket server");
		WebsocketServer.start();
	}

	/**
	 * It is called when a new sink is opened
	 * 
	 * @param parameters
	 *            Configuration parameters
	 */
	@Override
	public void open(Configuration parameters) throws Exception {
		logger.debug("Opening sink with parameters: " + parameters);
	}

	/**
	 * Invoke the current sink. Its mission is to send the previously calculated
	 * value to websocket clients.
	 * 
	 * @param value Result from previous operations. It will be send to all the websocket clients.
	 */
	@Override
	public void invoke(final IN value) throws Exception {
		logger.info("Sending value: " + value);
	}

	/**
	 * Close a sink
	 */
	@Override
	public void close() throws Exception {
	}
}