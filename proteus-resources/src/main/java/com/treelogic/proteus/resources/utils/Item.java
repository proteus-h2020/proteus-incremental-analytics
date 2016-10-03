package com.treelogic.proteus.resources.utils;

import java.io.Serializable;

/**
 * Created by pablo.mesa on 3/10/16.
 */
public class Item implements Serializable {

    public final Double value;
    public int g;
    public final int delta;

    public Item(Double value, int lower_delta, int delta) {
        this.value = value;
        this.g = lower_delta;
        this.delta = delta;
    }

    @Override
    public String toString() {
        //System.out.println("%d, %d, %d" + value +  g + delta);
        return String.format("%f, %d, %d", value, g, delta);
    }

}
