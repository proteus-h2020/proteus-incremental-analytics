package com.treelogic.proteus.core.sinks;

import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.treelogic.proteus.resources.model.IncResult;


public class ProteusConnectorSink extends RichSinkFunction<IncResult>{

	/**
	 * Default generated serial version UID
	 */
	private static final long serialVersionUID = -5478444596730684432L;
	
	/**
	 * Default Logger
	 */
	private Logger log = LoggerFactory.getLogger(this.getClass());
	

	@Override
	public void invoke(IncResult data) throws Exception {
		log.info("----Invoking Sink ----");
		log.info("data" + data.toString());
	}

}
