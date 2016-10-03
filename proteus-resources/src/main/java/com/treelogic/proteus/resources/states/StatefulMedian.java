package com.treelogic.proteus.resources.states;

import com.treelogic.proteus.resources.model.DataSerie;

import java.io.IOException;
import java.util.*;

import com.treelogic.proteus.resources.utils.Item;
import com.treelogic.proteus.resources.utils.Quantile;


/**
 *  CKSM Algorithm
 *
 *  Effective Computation of Biased Quantiles over Data Streams
 *  http://www.cs.rutgers.edu/~muthu/bquant.pdf
 *
 */

public class StatefulMedian extends Stateful {

	private static final long serialVersionUID = 6953929745457825750L;

	private double median, count;

    // Tracking Incremental Compression
    private int compressIdx = 0;

    // Lista de elementos muestreados, mantenida en orden con cotas de error
    LinkedList<Item> sample;

    // Buffers de los elementos que llegan insertados en batch
    Double[] buffer = new Double[1];
    int bufferCount = 0;

    //Array de Quantiles que queremos calcular con los errores deseados
    // Quantil Mediana
    Quantile mediana = new Quantile(0.50,0.10);
    final Quantile quantiles[] = {mediana};


	private DataSerie serie;

	public StatefulMedian() {

		// Datos de la ventana
        this.serie = new DataSerie().values(new ArrayList<Double>());
        // Quantil que nos interesa
        // this.quantiles = lista/array que recibieramos con los cuantiles que nos interesen
        // Estructura de datos final
        this.sample = new LinkedList<Item>();
        // Datos de la ventana copiados al Buffer
        // buffer
	}

	public StatefulMedian(double median, double count) {
		checkCountParameter(count);

		this.median = median;
		this.count = count;
	}

    @Override
    public void apply(List<DataSerie> series) {
        this.serie = series.get(0);
        List<Double> values = serie.values();

        for ( Double valor : values){
            insert(valor);
        }

    }

    /*
    * 30/09 - Wrong buffer initialization
    * 03/09 - Fixed
    *
    *
    */

    public void insert(Double v) {

        System.out.println(" ** V **"  + v);

        buffer[bufferCount] = v;
        bufferCount++;
        // printBuffer();

        if (bufferCount == buffer.length) {
            insertBatch();
            compress();
        }

        try {
            median = query(0.50);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	private void checkCountParameter(double count) {
		if (count < 1) {
			throw new IllegalArgumentException("MedianTuple count cannot be less than one");
		}
	}

	// MEDIANA - FUNCION ERROR PERMITIDO
    /**
     * Specifies the allowable error for this rank, depending on which quantiles
     * are being targeted.
     *
     * This is the f(r_i, n) function from the CKMS paper. It's basically how wide
     * the range of this rank can be.
     *
     * @param rank
     *          the index in the list of samples
     */
    private double allowableError(int rank) {
        // NOTE: according to CKMS, this should be count, not size, but this leads
        // to error larger than the error bounds. Leaving it like this is
        // essentially a HACK, and blows up memory, but does "work".
        //int size = count;
        int size = sample.size();
        double minError = size + 1;
        for (Quantile q : quantiles) {
            double error;
            if (rank <= q.quantile * size) {
                error = q.u * (size - rank);
            } else {
                error = q.v * rank;
            }
            if (error < minError) {
                minError = error;
            }
        }

        return minError;
    }

    // MEDIANA - PRINTLIST
    private void printList() {

        StringBuffer buf = new StringBuffer("sample = ");
        for (Item i : sample) {
            buf.append(String.format("(%s),", i));
        }

    }

    // MEDIANA - PRINTBUFFER - SOBRA
    /*
    private void printBuffer() {

        StringBuffer buf = new StringBuffer("buffer = [");
        for (int i = 0; i < bufferCount; i++) {
            buf.append(buffer[i] + ", ");
        }
        buf.append("]");

    }
    */


    // MEDIANA - AÑADO EL SIGUIENTE VALOR DEL STREAM DE DATOS
    /**
     * Add a new value from the stream.
     *
     * @param v
     */




    // MEDIANA - INSERTO

    private void insertBatch() {

        if (bufferCount == 0) {
            return;
        }
        printList();

        Arrays.sort(buffer, 0, bufferCount);
        //printBuffer();

        // Base case: no samples
        int start = 0;
        if (sample.size() == 0) {
            Item newItem = new Item(buffer[0], 1, 0);
            sample.add(newItem);
            start++;
            count++;
        }

        ListIterator<Item> it = sample.listIterator();
        Item item = it.next();
        for (int i = start; i < bufferCount; i++) {
            Double v = buffer[i];
            while (it.nextIndex() < sample.size() && item.value < v) {
                item = it.next();
            }
            // If we found that bigger item, back up so we insert ourselves before it
            if (item.value > v) {
                it.previous();
            }
            // We use different indexes for the edge comparisons, because of the above
            // if statement that adjusts the iterator
            int delta;
            if (it.previousIndex() == 0 || it.nextIndex() == sample.size()) {
                delta = 0;
            } else {
                delta = ((int) Math.floor(allowableError(it.nextIndex()))) - 1;
            }
            Item newItem = new Item(v, 1, delta);
            it.add(newItem);
            count++;
            item = newItem;
            printList();
        }

        bufferCount = 0;
        printList();
    }


    // MEDIANA - COMPRIMO DATOS

    /**
     * Try to remove extraneous items from the set of sampled items. This checks
     * if an item is unnecessary based on the desired error bounds, and merges it
     * with the adjacent item if it is.
     */
    public void compress() {

        if (sample.size() < 2) {
            return;
        }

        ListIterator<Item> it = sample.listIterator();
        int removed = 0;

        Item prev = null;
        Item next = it.next();
        while (it.hasNext()) {
            prev = next;
            next = it.next();

            if (prev.g + next.g + next.delta <= allowableError(it.previousIndex())) {
                next.g += prev.g;
                // Remove prev. it.remove() kills the last thing returned.
                it.previous();
                it.previous();
                it.remove();
                // it.next() is now equal to next, skip it back forward again
                it.next();
                removed++;
            }
        }

    }

    // MEDIANA - RETORNO VALOR ESTIMADO DEL QUANTIL
    /**
     * Get the estimated value at the specified quantile.
     *
     * @param quantile
     *          Queried quantile, e.g. 0.50 or 0.99.
     * @return Estimated value at that quantile.
     */
    public Double query(double quantile) throws IOException {

        // clear the buffer
        insertBatch();
        compress();

        if (sample.size() == 0) {
            throw new IOException("No samples present");
        }

        int rankMin = 0;
        int desired = (int) (quantile * count);

        ListIterator<Item> it = sample.listIterator();
        Item prev, cur;
        cur = it.next();
        while (it.hasNext()) {
            prev = cur;
            cur = it.next();

            rankMin += prev.g;

            if (rankMin + cur.g + cur.delta > desired + (allowableError(desired) / 2)) {
                return prev.value;
            }
        }

        // edge case of wanting max value
        return sample.getLast().value;
    }







    // Operacion Media
	@Override
	public Double value() {
		this.value = this.median;
		return this.value;
	}




}

