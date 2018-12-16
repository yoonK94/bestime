package com.yoonkim.bestime.Visualizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.yoonkim.bestime.R;

/*
    Implementation used for Algorithm final project.
    Please refer to https://github.com/yoonK94/sorting.show for more information
 */

public class SortingVisualizer extends AlgorithmVisualizer {

    Paint paint;
    Paint highlightPaintSwap;
    Paint highlightPaintTrace;
    Paint textPaint;
    int[] array;
    String[] labels;
    double zoom = 3000;
    int margins = 30;

    int highlightPositionOne = -1, highlightPositionTwo = -1;
    int highlightPosition = -1;
    int lineStrokeWidth = getDimensionInPixel(10);

    public SortingVisualizer(Context context) {
        super(context);
        initialise();
    }

    public SortingVisualizer(Context context, AttributeSet atrrs) {
        super(context, atrrs);
        initialise();
    }

    private void initialise() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#009688"));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(lineStrokeWidth);

        highlightPaintSwap = new Paint(paint);
        highlightPaintSwap.setColor(Color.RED);

        highlightPaintTrace = new Paint(paint);
        highlightPaintTrace.setColor(Color.BLUE);

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(getDimensionInPixelFromSP(5));
    }

    public void setFontSize(int x){
        textPaint.setTextSize(getDimensionInPixelFromSP(x));
    }
    public void setMargin(int x){
        margins = x;
    }
    public void setZoom(double x){
        zoom = x;
    }
    public void setBarWidth(int x){
        lineStrokeWidth = getDimensionInPixel(x);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (array != null) {
            int numberOfLines = array.length;

            int min = Integer.MAX_VALUE;

            float margin = (getWidth() - (margins * numberOfLines)) / (numberOfLines + 1);

            float xPos = margin + getDimensionInPixel(lineStrokeWidth) + 5;

            //finding the cheapest price to highlight
            for(int i = 0; i <array.length;i++){
                if(array[i] < min){
                    min = array[i];
                    highlightPositionOne = i;
                }
            }
            for (int i = 0; i < array.length; i++) {
                if (i == highlightPositionOne || i == highlightPositionTwo)
                    canvas.drawLine(xPos, getHeight() - (float) ((array[i] / zoom) * getHeight()), xPos, getHeight(), highlightPaintSwap);
                else {
                    canvas.drawLine(xPos, getHeight() - (float) ((array[i] / zoom) * getHeight()), xPos, getHeight(), paint);
                }

                canvas.drawText(labels[i], xPos - lineStrokeWidth - margin / 5, getHeight() - (float) ((array[i] / zoom) * getHeight()) - 50, textPaint);
                canvas.drawText("$" + String.valueOf(array[i]), xPos - lineStrokeWidth * 2, getHeight() - (float) ((array[i] / zoom) * getHeight()) - 10, textPaint);

                xPos += margin + lineStrokeWidth;
            }
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setData(DataSet ds) {
        this.array = ds.arr;
        this.labels = ds.labels;
        invalidate();
    }
    public void highlightSwap(int one, int two) {
        this.highlightPositionOne = one;
        this.highlightPositionTwo = two;
        invalidate();
    }

    public void highlightTrace(int position) {
        this.highlightPosition = position;
        invalidate();
    }


    private boolean contains(int[] array, int index) {
        for(int i = 0; i < array.length; i++) {
            if(array[i] == index)
                return true;
        }
        return false;
    }

}