package com.example.justin.donutorcookie;

import android.util.Log;

import org.ojalgo.matrix.BasicMatrix;
import org.ojalgo.matrix.PrimitiveMatrix;

// ojalgo stuff

public class NeuralNet {

    // weights
    PrimitiveMatrix syn0;
    PrimitiveMatrix syn1;

    // ret
    String ret = "";

    public PrimitiveMatrix sigmoid(PrimitiveMatrix x)
    {
        double[][] dummy = new double[(int)x.countColumns()][(int)x.countRows()];
        PrimitiveMatrix ret = x;
        for (int i = 0; i < x.countRows(); i++)
        {
            for (int j = 0; j < x.countColumns(); j++)
            {
                dummy[j][i] = 1 / (1 + Math.exp(-x.doubleValue(i,j)));
            }
        }
        BasicMatrix.Factory<PrimitiveMatrix> matrixFactory = PrimitiveMatrix.FACTORY;
        ret = matrixFactory.rows(dummy);
        return ret;
    }
    public double[] forwardFeed(double[] data)
    {

        BasicMatrix.Factory<PrimitiveMatrix> matrixFactory = PrimitiveMatrix.FACTORY;
        PrimitiveMatrix l0 = matrixFactory.rows(data);
        PrimitiveMatrix b0 = matrixFactory.makeEye(1,1);
        PrimitiveMatrix b1 = matrixFactory.makeEye(1,1);
        Log.i("n0WIDTH", Long.toString(l0.countColumns()));
        Log.i("n0HEIGHT", Long.toString(l0.countRows()));
        l0 = l0.transpose();
        l0 = l0.mergeColumns(b0);
        l0 = l0.transpose();
        PrimitiveMatrix l1 = l0.multiply(syn0);
        l1 = sigmoid(l1);
        l1 = l1.mergeColumns(b1);
        l1 = l1.transpose();
        PrimitiveMatrix l2 = l1.multiply(syn1);
        l2 = sigmoid(l2);
        double maxl2 = 0;
        int maxl2_index = 0;
        double[] retu = new double[3];
        for(int i = 0; i < (int)(l2.count()); i++) {
            // maxl2 = Math.max(maxl2, l2.doubleValue(0, i));
            double current = l2.doubleValue(i, 0);
            Log.d("tag",Double.toString(current));
            if (maxl2 < current) {
                maxl2 = current;
                maxl2_index = i;
                retu[i] = l2.doubleValue(i,0)*100;
            }
        }

        if (maxl2_index == 1){
            ret = "Cookie";
        }
        if (maxl2_index == 0){
            ret = "Donut";
        }
        if (maxl2_index == 2){
            ret = "Neither";
        }
        return retu;

    }

}
