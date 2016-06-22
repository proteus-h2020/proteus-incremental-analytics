package com.treelogic.proteus.flink.incops.util;

/**
 * Tuple that contains sum and count of elements. Used to compute mean
 * values associatively. 
 *
 */
public class MeanTuple {
	
	// TODO Count should be long
	private double sum , count;

	/**
	 * Sum and count = 0
	 */
    public MeanTuple() { 
    	sum = 0;
    	count = 0;
    }

    public MeanTuple(double sum, double count) {
    	checkCountParameter(count);
    	
        this.sum = sum;
        this.count = count;
    }

    /**
     * @return sum / count
     */
    public double mean() {
    	return count == 0 ? 0 : sum / count;
    }

    /**
     * Increments sum by value and count by 1. Same as inc(value, 1).
     * 
     * @param value
     */
    public void inc(double value) {
    	inc(value, 1);
    }
    
    /**
     * Used to increment both sum and count by the specified values.
     * 
     * @param sum
     * @param count
     */
    public void inc(double sum, double count) {
    	checkCountParameter(count);
    	
    	this.sum += sum;
    	this.count += count;
    }
    
    private void checkCountParameter(double count) {
    	if(count < 1) {
    		throw new IllegalArgumentException(
    				"MeanTuple count cannot be less than one");
    	}
    }
}