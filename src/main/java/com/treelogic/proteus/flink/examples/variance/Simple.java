package com.treelogic.proteus.flink.examples.variance;

import com.treelogic.proteus.flink.examples.airquality.AirRegister;
import com.treelogic.proteus.flink.incops.IncrementalVariance;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.io.PojoCsvInputFormat;
import org.apache.flink.api.java.typeutils.PojoField;
import org.apache.flink.api.java.typeutils.PojoTypeInfo;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class Simple {

    public static final int WINDOW_SIZE = 2;

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment streamingEnv =
            StreamExecutionEnvironment.getExecutionEnvironment();
        //final String hdfs = "hdfs://192.168.4.245:8020/bigdata/datasets/aire.csv";
        final String hdfs =
            "./src/main/resources/datasets/smallDataset.csv";

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
            new PojoCsvInputFormat<>(new Path(hdfs), typeInfo, fieldNamesArray);

        DataStream<AirRegister> stream =
            streamingEnv.createInput(format, typeInfo);

        stream
            .keyBy("station")
            .countWindow(WINDOW_SIZE)
            .apply(new IncrementalVariance<AirRegister>("o3"))
            .print();

        streamingEnv.execute("AirRegisters");
    }

}

