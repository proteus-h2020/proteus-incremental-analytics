package com.treelogic.proteus.flink.incops;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;

public abstract class IncrementalOperation<IN, OUT>
    extends RichWindowFunction<IN, OUT, Tuple, GlobalWindow> {


}
