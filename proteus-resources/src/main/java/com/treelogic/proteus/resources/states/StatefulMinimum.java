package com.treelogic.proteus.resources.states;

import com.treelogic.proteus.resources.model.DataSerie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pablo.mesa on 20/09/16.
 */
public class StatefulMinimum extends Stateful {


    private static final long serialVersionUID = 6953929745457825750L;

    private double min = Double.MAX_VALUE, count;

    private DataSerie serie;

    public StatefulMinimum() { this.serie = new DataSerie().values(new ArrayList<Double>());}

    // Buscar donde inicializar el Maximo

    public StatefulMinimum(double max, double count) {
        checkCountParameter(count);
        this.min = Double.MAX_VALUE;
        System.out.println("Valor MIN:" + this.min);
        this.count = count;
    }

    private void checkCountParameter(double count) {
        if (count < 1) {
            throw new IllegalArgumentException("MinimumTuple count cannot be less than one");
        }
    }

    @Override
    public Double value() {
        this.value = this.min;
        return  this.value;
    }

    @Override
    public void apply(List<DataSerie> series) {
        this.serie = series.get(0);
        List<Double> values = serie.values();
        minimum(values);

    }

    private void minimum(List<Double> values){
        int serieSize = values.size();
        double currentMinimum = searchMinimumWindow(values);

        if ( currentMinimum < this.min ) this.min = currentMinimum;

        this.count += serieSize;
    }

    private double searchMinimumWindow(List<Double> values){
        double res = Double.MAX_VALUE;
        for ( double temp : values){
            if ( temp < res ) res = temp;
        }
        return res;

    }


    public void inc(List<Double> values) {
        this.serie.values().addAll(values);
        searchMinimumWindow(this.serie.values());
    }

}

