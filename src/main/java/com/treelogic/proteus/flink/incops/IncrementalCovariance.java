package com.treelogic.proteus.flink.incops;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import com.treelogic.proteus.flink.incops.util.MeanTuple;

public class IncrementalCovariance<IN>
    extends IncrementalOperation<IN, Double> {

	private static final long serialVersionUID = 1L;
    private final String fieldX, fieldY;

    // Tuple4 - xMean, yMean, Sum of deviation products, Element count
    private ValueStateDescriptor<Tuple4<MeanTuple, MeanTuple, Double, Double>> descriptor;

    public IncrementalCovariance(String fieldX, String fieldY) {
        if (fieldX == null || fieldX.equals("")
            || fieldY == null || fieldY.equals("")) {
            throw new IllegalArgumentException("Field cannot be empty");
        }

        this.fieldX = fieldX;
        this.fieldY = fieldY;

        descriptor = new ValueStateDescriptor<>(
            "incremental-variance-descriptor",
            TypeInformation.of(
                new TypeHint<Tuple4<MeanTuple, MeanTuple, Double, Double>>() {}),
            new Tuple4<>(new MeanTuple(), new MeanTuple(), 0d, 0d));
    }

    @Override
    public void apply(Tuple tuple,
                      GlobalWindow window,
                      Iterable<IN> input,
                      Collector<Double> out) throws Exception {

        ValueState<Tuple4<MeanTuple, MeanTuple, Double, Double>> state =
            getRuntimeContext().getState(descriptor);

        // TODO Set initial size equals to window size?
        List<Tuple2<Double, Double>> elems = new ArrayList<>();

        for (IN in : input) {
            Field fieldX = in.getClass().getDeclaredField(this.fieldX);
            Field fieldY = in.getClass().getDeclaredField(this.fieldY);
            fieldX.setAccessible(true);
            fieldY.setAccessible(true);
            elems.add(new Tuple2<>(
                (Double) fieldX.get(in), (Double)fieldY.get(in)));
        }

        // Compute elems mean
        double xMean2sum = 0, yMean2sum = 0, xMean2, yMean2;
        for (Tuple2<Double, Double> t : elems) {
            xMean2sum += t.f0;
            yMean2sum += t.f1;
        }

        xMean2 = xMean2sum / elems.size();
        yMean2 = yMean2sum / elems.size();

        // currentSDP = current sum of deviation products
        double currentSDP = 0;
        for(Tuple2<Double, Double> t : elems) {
            currentSDP += (t.f0 - xMean2) * (t.f1 - yMean2);
        }

        // Merge (if first iteration just save)
        double elemsProcessed = state.value().f3;

        if(elemsProcessed == 0) {
            elemsProcessed = elems.size();
            MeanTuple xmt = new MeanTuple(sumX(elems), elems.size());
            MeanTuple ymt = new MeanTuple(sumY(elems), elems.size());
            state.update(new Tuple4<>(xmt, ymt, currentSDP, (double) elems.size()));

            // Naive covariance formula
            out.collect(currentSDP / ( elems.size() - 1) );
        } else {
            double lastSDP = state.value().f2;
            MeanTuple lastXMean = state.value().f0;
            MeanTuple lastYMean = state.value().f1;
            double xMeanDiff = xMean2 - lastXMean.mean();
            double yMeanDiff = yMean2 - lastYMean.mean();
            double sizeSum = elemsProcessed + elems.size();
            double newSDP = lastSDP + currentSDP
                + (elemsProcessed * elems.size() * xMeanDiff * yMeanDiff / sizeSum);
            
            lastXMean.inc(sumX(elems), elems.size());
            lastYMean.inc(sumY(elems), elems.size());

            state.update(new Tuple4<>(
                lastXMean, lastYMean, newSDP, elemsProcessed + elems.size()));

            out.collect(newSDP / (sizeSum - 1));
        }
    }

    private double sumX(List<Tuple2<Double, Double>> list) {
        double sum = 0;
        for(Tuple2<Double, Double> t : list) {
            sum += t.f0;
        }
        return sum;
    }

    private double sumY(List<Tuple2<Double, Double>> list) {
        double sum = 0;
        for(Tuple2<Double, Double> t : list) {
            sum += t.f1;
        }
        return sum;
    }
}
