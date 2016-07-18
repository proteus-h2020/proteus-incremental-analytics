package com.treelogic.proteus.examples;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.io.PojoCsvInputFormat;
import org.apache.flink.api.java.typeutils.PojoField;
import org.apache.flink.api.java.typeutils.PojoTypeInfo;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.treelogic.proteus.core.configuration.IncrementalConfiguration;
import com.treelogic.proteus.core.configuration.OpParameter;
import com.treelogic.proteus.core.incops.statistics.IncrementalVariance;
import com.treelogic.proteus.core.pojos.AirRegister;

public class VarianceExample {
	public static final int WINDOW_SIZE = 2;

	public static final String FILE = "./src/main/resources/datasets/smallDataset.csv";
	public static final String OUTPUT = "./OUTPUT";

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

        
        IncrementalConfiguration configuration = new IncrementalConfiguration();
        configuration.fields(
    			new OpParameter("o3"), 
    			new OpParameter("co"),
    			new OpParameter("so2"),
    			new OpParameter("pm10")
        );
        stream
            .keyBy("station")
            .countWindow(WINDOW_SIZE)
            .apply(new IncrementalVariance<AirRegister>(configuration))            
            .print();


        streamingEnv.execute("AirRegisters");
    }
}
