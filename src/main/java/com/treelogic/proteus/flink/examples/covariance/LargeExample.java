package com.treelogic.proteus.flink.examples.covariance;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.io.PojoCsvInputFormat;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.typeutils.PojoField;
import org.apache.flink.api.java.typeutils.PojoTypeInfo;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.treelogic.proteus.flink.examples.airquality.AirRegister;
import com.treelogic.proteus.flink.incops.IncrementalCovariance;

public class LargeExample {
	
	public static final int WINDOW_SIZE = 2;
	public static final String FILE = "./src/main/resources/datasets/smallDataset.csv";

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment streamingEnv =
            StreamExecutionEnvironment.getExecutionEnvironment();

        List<PojoField> fields = new LinkedList<>();
        List<String> fieldNames = new LinkedList<>();

        for (Field field : AirRegister.class.getDeclaredFields()) {
            TypeInformation<?> typeInfo = BasicTypeInfo.of(field.getType());
            fields.add(new PojoField(field, typeInfo));
            fieldNames.add(field.getName());
        }

        PojoTypeInfo<AirRegister> typeInfo =
            new PojoTypeInfo<>(AirRegister.class, fields);

        String[] fieldNamesArray = fieldNames.toArray(new String[0]);

        PojoCsvInputFormat<AirRegister> format =
            new PojoCsvInputFormat<>(new Path(FILE), typeInfo, fieldNamesArray);

        DataStream<AirRegister> stream =
            streamingEnv.createInput(format, typeInfo);

        stream
            .keyBy("station")
            .countWindow(WINDOW_SIZE)
            .apply(new IncrementalCovariance<AirRegister>("o3", "co"))
            .map(new MapFunction<Tuple2<String,Double>, Tuple3<String, Double, Long>>() {

				private static final long serialVersionUID = 1L;

				@Override
				public Tuple3<String, Double, Long> map(Tuple2<String, Double> arg0) throws Exception {
					return new Tuple3<>(arg0.f0, arg0.f1, new Date().getTime());
				}
			})
            .writeAsCsv("/home/ezequiel.cimadevilla/tests/flinkOutput");

        streamingEnv.execute("AirRegisters");
    }

}
