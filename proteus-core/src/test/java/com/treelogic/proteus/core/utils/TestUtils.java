package com.treelogic.proteus.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

import com.treelogic.proteus.core.pojos.IncResult;
import com.treelogic.proteus.core.states.Stateful;

public class TestUtils {

	public static class Tuple2ToDouble implements MapFunction<Tuple2<String, Double>, Double> {

		private static final long serialVersionUID = 1L;

		@Override
		public Double map(Tuple2<String, Double> arg0) throws Exception {
			return arg0.f1;
		}

	}

	public static class Tuple2ToListDouble implements MapFunction<Map<String, Tuple2<String, Double>>, List<Double>> {

		private static final long serialVersionUID = 1L;

		@Override
		public List<Double> map(Map<String, Tuple2<String, Double>> params) throws Exception {
			List<Double> values = new ArrayList<Double>();
			for (Entry<String, Tuple2<String, Double>> e : params.entrySet()) {
				values.add(e.getValue().f1);
			}
			return values;
		}
	}
	
	public static class IncResult2ToDouble<T extends Stateful<S>, S> implements MapFunction<IncResult<T>, List<S>> {

		private static final long serialVersionUID = 1L;

		@Override
		public List<S> map(IncResult<T> r) throws Exception {
			List<S> results = new ArrayList<S>();
			for(Entry<String, Tuple2<String, T>> e : r.entrySet()){
				results.add(e.getValue().f1.value());
			}
			return results;
		}

	}

}