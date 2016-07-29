package com.treelogic.proteus.core.sinks;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.treelogic.proteus.resources.model.IncrementalWindowValue;


public class ProteusConnectorSink extends RichSinkFunction<IncrementalWindowValue>{

	/**
	 * Default generated serial version UID
	 */
	private static final long serialVersionUID = -5478444596730684432L;
	
	/**
	 * Default Logger
	 */
	private Logger log = LoggerFactory.getLogger(this.getClass());
	

	@Override
	public void invoke(IncrementalWindowValue data) throws Exception {
		StringBuilder message = new StringBuilder();
		//log.info("\n----Invoking Sink ----\n");
	/**	for(Entry<String, Pair<String, Double>> e : data.entrySet()){
			message.append(e.getKey())
					.append("   ->   ")
					.append(e.getValue().f0)
					.append(" - ")
					.append(e.getValue().f1)
					.append("\n");
		}
		log.info(message.toString());
		//log.info("Message: " + message.toString());
		 * **/
	}

}
