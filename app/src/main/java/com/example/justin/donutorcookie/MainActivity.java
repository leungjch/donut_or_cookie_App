package com.example.justin.donutorcookie;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.ojalgo.matrix.BasicMatrix;
import org.ojalgo.matrix.PrimitiveMatrix;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    public void loadMatrix(NeuralNet nn)
    {
        try{
            double[][] t_syn0;
            double[][] t_syn1;
            double[][] t_b0;
            double[][] t_b1;


//            FileInputStream fileIn = new FileInputStream(new File("/src/main/res/raw/syn0.data"));
//            ObjectInputStream in = new ObjectInputStream(fileIn);
            InputStream in = getResources().openRawResource(R.raw.syn0);
            InputStream buffer = new BufferedInputStream(in);
            ObjectInput input = new ObjectInputStream (buffer);
            t_syn0 = (double[][]) input.readObject();

            in = getResources().openRawResource(R.raw.syn1);
            buffer = new BufferedInputStream(in);
            input = new ObjectInputStream (buffer);
            t_syn1 = (double[][]) input.readObject();


            // convert the regular 2D arrays into PrimitiveMatrix
            BasicMatrix.Factory<PrimitiveMatrix> matrixFactory = PrimitiveMatrix.FACTORY;
            nn.syn0 = matrixFactory.rows(t_syn0);
            nn.syn1 = matrixFactory.rows(t_syn1);
            Log.i("HIIIIIIIII", "HIII");
            Log.i("LENGJHTHJE", Long.toString(nn.syn0.count()));
            Log.i("WIDTH", Long.toString(nn.syn0.countColumns()));
            Log.i("HEIGHT", Long.toString(nn.syn0.countRows()));
        }
        catch(FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            Log.i("HIIIIIIIII", "HI1II");
        }
        catch(java.io.IOException ioe)
        {
            System.out.println(ioe.getMessage());
            Log.i("HIIIIIIIII", "HI2II");
        }
        catch(java.lang.ClassNotFoundException cnf)
        {
            System.out.println(cnf.getMessage());
            Log.i("HIIIIIIIII", "HI3II");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NeuralNet nn = new NeuralNet();
        String workDir = getApplicationInfo().dataDir;
        Log.i("i","WORKDIR IS : " + workDir);

        loadMatrix(nn);

        // Initialize draw view
        final DrawView drawView = (DrawView) findViewById(R.id.draw_view);



        // Button listener
        Button button = (Button) findViewById(R.id.submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bitmap unresizedBitmap = drawView.mBitmap;
                Bitmap drawBitmap = Bitmap.createScaledBitmap(unresizedBitmap,28,28,false);
                int[] pixels = new int[784];
                drawBitmap.getPixels(pixels, 0,drawBitmap.getWidth(),0,0,drawBitmap.getWidth(),drawBitmap.getHeight());
                String stringArray = Arrays.toString(pixels);
                Log.i("bitmap",stringArray);
                double[] newPixels = new double[784];
                // normalize array
                int min = Arrays.stream(pixels).min().getAsInt();
                int max = Arrays.stream(pixels).max().getAsInt();

                double count = pixels.length;
                double sum1 = 0.0;
                double sum2 = 0.0;
                // convert to 0-255
                for (int i=0; i < drawBitmap.getHeight(); i++){
                    for (int j = 0; j < drawBitmap.getWidth(); j++)
                    pixels[(i*drawBitmap.getHeight())+j] = Color.red(drawBitmap.getPixel(j,i));

                }
                for (int i=0; i < pixels.length; i++){
                    double n = pixels[i];
                    sum1 += n;
                    sum2 += n * n;
                }
                double average = sum1 / count;
                double variance = (count * sum2 - sum1 * sum1) / (count * count);
                double stdev = Math.sqrt(variance);


                for (int i = 0; i < pixels.length; i++){
                    newPixels[i] = (double)(pixels[i]-average)/(stdev);
                    System.out.println(Double.toString(newPixels[i]));
                }
                stringArray = Arrays.toString(newPixels);
                double[] guess;
                guess = nn.forwardFeed(newPixels);
                Toast.makeText(MainActivity.this, "Is this a " + nn.ret + "?\n" + "I am " + Double.toString(Math.floor(guess[0]))+ "% confident that it's a Donut.\n" +
                        "I am " + Double.toString(Math.floor(guess[1])) + "% confident that it's a Cookie.\n" +
                        "I am " + Double.toString(Math.floor(guess[2])) + "% confident that it's Neither.",
                        Toast.LENGTH_LONG).show();
            }
        });


    }
}
