package com.treelogic.proteus.flink.incops;



/**
 * Operator that incrementally computes the mode of a variable. State is
 * saved using Flink's key/value store and a java.util.HashMap object. The
 * key of the map represents variable's value and the value represents the
 * repetitions of that value.
 *
 * Collects the mode element and it's value
 *
 * @param <IN> POJO that contains fields to be analyzed
 */
/*
public class IncrementalMode<IN> extends
    //IncrementalOperation<IN, List<Tuple2<Double, Integer>>> {
	IncrementalOperation<IN, Map<String, Tuple2<String, List<Tuple2<Double, Integer>>>>> {


	private static final long serialVersionUID = 1;

    private ValueStateDescriptor<Map<String, Map<Double, Integer>>> stateDescriptor;

    private int numValues, numDecimals;
    
    public IncrementalMode(IncrementalConfiguration configuration) {
    	this(configuration, 1, 0);
	}
    

    public IncrementalMode(IncrementalConfiguration configuration, int numValues, int numDecimals) {
		super(configuration);
        this.numValues = numValues > 0 ? numValues : 1;
        this.numDecimals = numDecimals >= 0 ? numDecimals : 0;
	}


    public void apply(Tuple key,
                      GlobalWindow window,
                      Iterable<IN> input,
                      Collector<Map<String, Tuple2<String, List<Tuple2<Double, Integer>>>>> out) throws Exception {

        ValueState<Map<String, Map<Double, Integer>>> state = getRuntimeContext().getState(stateDescriptor);
                
        Map<String, Map<Double, Integer>> modes = state.value();

        // Update map
        for(IN in : input){
        	for(String fieldName : this.configuration.getFields()){
        		Double fieldValue = FieldUtils.getValue(in, fieldName);   
        		double pow = Math.pow(10, numDecimals);
                Double i = Math.round((Double) fieldValue * pow) / pow;
                
                Map<Double, Integer> currentMode = modes.get(fieldName);
                if(currentMode.containsKey(i)){
                	currentMode.put(i, 1);
                }else{
                	currentMode.put(i, currentMode.get(i) + 1);
                }
        	}
        }
        state.update(modes);
        
        Map<String, Tuple2<String, List<Tuple2<Double, Integer>>>> modeTuples = new HashMap<String, Tuple2<String, List<Tuple2<Double, Integer>>>>();
        
        
        
        
        for(Entry<String, Map<Double, Integer>> entry : modes.entrySet()){
        	
        	// List<Tuple2<Double, Integer>>> {     	
        	
            TreeSet<Tuple2<Double, Integer>> set = toTreeSet(entry.getValue());
            Iterator<Tuple2<Double, Integer>> it = set.iterator();
            int i = 0;
            while (it.hasNext() && i < numValues) {
                list.add(it.next());
                i++;
            }


        }
        //Map<String, Tuple2<String, Double>>>
        
        //component {CLAVE - VALOR}
        // Return the mode element and the value of it's mode
        out.collect(list);
    }

    private TreeSet<Tuple2<Double, Integer>> toTreeSet(Map<Double, Integer> lastRecord) {
        // Order elements by number of appearances
        Comparator<Tuple2<Double, Integer>> c =
            new Comparator<Tuple2<Double, Integer>>() {
                public int compare(Tuple2<Double, Integer> o1, Tuple2<Double, Integer> o2) {
                    return o1.f1 < o2.f1 ? 1 : -1;
                }
            };

        TreeSet<Tuple2<Double, Integer>> set = new TreeSet<>(c);

        for (Map.Entry<Double, Integer> e : lastRecord.entrySet()) {
            set.add(new Tuple2<>(e.getKey(), e.getValue()));
        }

        return set;
    }

	@Override
	public void initializeDescriptor() {
        stateDescriptor = new ValueStateDescriptor<>(
                "mode-last-result",
                TypeInformation.of(new TypeHint<Map<String, Map<Double, Integer>>>() {}),
                new HashMap<String, Map<Double, Integer>>());		
	}
	
}
**/