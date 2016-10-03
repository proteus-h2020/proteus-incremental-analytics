package com.treelogic.proteus.resources.utils;

import java.io.Serializable;

/**
 * Created by pablo.mesa on 3/10/16.
 */
public class Quantile implements Serializable {

    public final double quantile;
    public final double error;
    public final double u;
    public final double v;

    public Quantile(double quantile, double error) {
        this.quantile = quantile;
        this.error = error;
        u = 2.0 * error / (1.0 - quantile);
        v = 2.0 * error / quantile;
    }

    @Override
    public String toString() {
        return String.format("Q{q=%.3f, eps=%.3f})", quantile, error);
    }

}
