package com.treelogic.proteus.resources.states;

import com.treelogic.proteus.resources.model.DataSerie;
import com.treelogic.proteus.resources.utils.MathUtils;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 * Created by pablo.mesa on 20/09/16.
 */
public class StatefulMaximum extends Stateful {


    private static final long serialVersionUID = 6953929745457825750L;

    private double max = Double.MIN_VALUE, count;

    private DataSerie serie;

    public StatefulMaximum() { this.serie = new DataSerie().values(new ArrayList<Double>());}

    // Buscar donde inicializar el Maximo

    public StatefulMaximum(double max, double count) {
        checkCountParameter(count);
        this.max = Double.MIN_VALUE;
        this.count = count;
    }

    private void checkCountParameter(double count) {
        if (count < 1) {
            throw new IllegalArgumentException("MaximumTuple count cannot be less than one");
        }
    }

    @Override
    public Double value() {
        this.value = this.max;
        return  this.value;
    }

    @Override
    public void apply(List<DataSerie> series) {
        this.serie = series.get(0);
        List<Double> values = serie.values();
        maximun(values);

    }

    private void maximun(List<Double> values){
        int serieSize = values.size();
        double currentMaximum = searchMaximumWindow(values);

        if ( currentMaximum > max ) this.max = currentMaximum;

        this.count += serieSize;
    }

    private double searchMaximumWindow(List<Double> values){
        double res = Double.MIN_VALUE;
        for ( double temp : values){
            if ( temp > res ) res = temp;
        }
        return res;
    }


    public void inc(List<Double> values) {
        this.serie.values().addAll(values);
        searchMaximumWindow(this.serie.values());
    }

}

