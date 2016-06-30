package com.treelogic.proteus.flink.incops.util;

import java.util.List;

/**
 * Tuple that contains sum and count of elements. Used to compute mean
 * values associatively. 
 *
 */
public class StatefulAverage extends Stateful<Number>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6953929745457825750L;
	private double sum , count;

	/**
	 * Sum and count = 0
	 */
    public StatefulAverage() { 
    	
    }

    public StatefulAverage(double sum, double count) {
    	checkCountParameter(count);
    	
        this.sum = sum;
        this.count = count;
    }

    /**
     * Increments sum by value and count by 1. Same as inc(value, 1).
     * 
     * @param value
     */
    private void inc(Number value) {
    	inc(value, 1);
    }
    
    /**
     * Used to increment both sum and count by the specified values.
     * 
     * @param sum
     * @param count
     */
    private void inc(Number sum, int count) {
    	checkCountParameter(count);
    	this.sum += sum.doubleValue();
    	this.count += count;
    	calculate();
    }
    
    private void checkCountParameter(double count) {
    	if(count < 1) {
    		throw new IllegalArgumentException(
    				"MeanTuple count cannot be less than one");
    	}
    }
    

	@Override
	public Number value() {
		return this.value;
	}

	@Override
	public void calculate() {
		this.value  = (count == 0 ? 0 : sum / count);
	}

	@Override
	public void add(Number[] values) {
		for(Number v: values)	{
			inc(v);
		}
	}

	@Override
	public void add(Number v0) {
		inc(v0);	
	}

	@Override
	public void add(List<Number> values) {
		for(Number number : values){
			inc(number);
		}
		
	}


}