package edu.unc.epeden.sensorproject;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by epeden on 2/19/16.
 */
public class PlotView extends View {

    float last_val = 0;
    List<Float> points = new ArrayList<Float>();    // Sensor values to be plotted
    List<Float> means = new ArrayList<Float>();     // The means of the data at each time interval.
    List<Float> stds = new ArrayList<Float>();      // The standard deviation of the data at each time interval.
    float max_val = 0;          // Max value in the list of points to be plotted. Used to determine axis-label intervals.
    int time_value = 0;         // Current time value (seconds).
    float last_mean = 0;        // The mean of the data values after the last addition to the list.
    boolean t_switch = true;
    ImageView ani;

    public PlotView(Context context) {
        super(context);
    }
    public PlotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlotView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public PlotView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Initialize Paint objects.
        Paint p = new Paint();              // White brush for axis
        p.setColor(Color.WHITE);
        p.setStrokeWidth(3);

        Paint p_text = new Paint();          // Blue brush for text
        p_text.setColor(Color.BLUE);
        p_text.setStrokeWidth(10);
        p_text.setTextSize(40);

        Paint p_val = new Paint();          // Blue brush for values
        p_val.setColor(Color.BLUE);
        p_val.setStrokeWidth(10);

        Paint p_mean = new Paint();         // Green brush for means
        p_mean.setColor(Color.GREEN);
        p_mean.setStrokeWidth(10);

        Paint p_std = new Paint();          // Yellow brush for standard deviation
        p_std.setColor(Color.YELLOW);
        p_std.setStrokeWidth(10);

        // Offset chosen to allow space for axis labels and height/width partition values.
        int offset = 80;                        // 80 pixels for label space
        int h_offset = getHeight() - offset;    // height of graph including offset
        int w_part = (getWidth() - offset) / 6; // partition of graph width (1/6 of graph)
        int h_part = h_offset / 6;              // partition of graph height (1/6 of graph)

        // Draw axis and axis labels.
        for (int k = 0; k < 7; k++) {
            canvas.drawLine(offset, k * h_part, getWidth(), k * h_part, p);
            canvas.drawLine(offset + k * w_part, 0, offset + k * w_part, h_offset, p);

            canvas.drawText(Integer.toString(Math.round(k * ((max_val+10) / 6))), 0, (6 - k) * h_part + 20, p_text);
            if (time_value > 6) {
                canvas.drawText(Integer.toString(time_value - 6 + k), k * w_part + offset, getHeight() - 10, p_text);
            } else {
                canvas.drawText(Integer.toString(k), k * w_part + offset, getHeight() - 10, p_text);
            }
        }

        // Add point to be plotted to ArrayList, "points".
        addPlotPoint(last_val);

        // Calculate the mean and standard deviation.
        calcMean();
        calcStd();

        // Draw each plot point on the canvas.  "/2" because 500 milliseconds
        for (int i = 0; i < points.size(); i++) {
            canvas.drawCircle(offset + w_part / 2 * i, h_offset - means.get(i)*((h_offset) / (max_val+10)), 10, p_mean);
            canvas.drawCircle(offset + w_part / 2 * i, h_offset - points.get(i)*((h_offset) / (max_val+10)), 8, p_val);
            canvas.drawCircle(offset + w_part / 2 * i, h_offset - stds.get(i)*((h_offset) / (max_val+10)), 10, p_std);
        }

         // TEST
        if (points.size() > 0) {
            Log.v("point plotted", "" + time_value + ", " + points.get(0));
        }

        // Connect the points with appropriate line.
        connectPoints(canvas,means);
        connectPoints(canvas,points);
        connectPoints(canvas,stds);


        // Sleep for .5 second and then increment the time value by 1.0 after every other thread-sleep.
        try {
            Thread.sleep(500);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        if (t_switch) {
            time_value++;
            t_switch = false;
        } else {
            t_switch = true;
        }
    }

    /* Clear the list of sensor data. */
    public void clearList() {
        points.clear();
    }

    /* Called from sensor listener to add point to ArrayList, "floats". */
    public void addPoint(float f) {

        last_val = f;
    }

    /* Called locally to add another value to the list that will be plotted, "points". */
    public void addPlotPoint(float f) {
        if (points.size() > 12) {
            points.remove(0);
        }
        points.add(f);

        max_val = Collections.max(points);
    }

    /* Calculate the mean of the values in "points" at the current time and store
        the value in an ArrayList to record mean at each time. */
    public void calcMean() {
        float total= 0;
        for (int i = 0; i < points.size(); i++) {
            total += points.get(i);
        }
        if (means.size() > 12) {
            means.remove(0);
        }
        means.add(total/points.size());
        last_mean = total/points.size();

    }

    /* Calculate the standard deviation of the values in "points" at the current time value
        and store the value in an ArrayList to record std at each time. */
    public void calcStd() {
        float temp= 0;
        int i;
        for (i = 0; i < points.size(); i++) {
            temp += (last_mean - points.get(i))*(last_mean - points.get(i));
        }
        if (stds.size() > 12) {
            stds.remove(0);
        }
        float f = (float) Math.sqrt(temp/points.size());

        stds.add(f);
    }

    /* Connect each plotted point with a line. */
    public void connectPoints(Canvas canvas, List<Float> list) {

        int offset = 80;                        // 80 pixels for axis-label space
        int h_offset = getHeight() - offset;    // height of graph including offset
        int w_part = (getWidth() - offset) / 6; // partition of graph width (1/6 of graph)

        Paint p = new Paint();
        p.setStrokeWidth(9);
        if (list == points) {
            p.setColor(Color.BLUE);
            p.setStrokeWidth(6);
        } else if (list == means) {
            p.setColor(Color.GREEN);
        } else {
            p.setColor(Color.YELLOW);
        }

        for (int i = 0; i < list.size()-1; i++) {
            float startX = i * w_part / 2 + 80;
            float startY = h_offset - list.get(i) * (h_offset / (max_val + 10));
            float endX = (i + 1) * w_part / 2 + 80;
            float endY = h_offset - list.get(i + 1) * (h_offset / (max_val + 10));

            canvas.drawLine(startX, startY, endX, endY, p);
        }
    }

    public double getLastMean() {
        return (double) last_mean;
    }
}
