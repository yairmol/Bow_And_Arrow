package com.example.bow_and_arrow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;

public class Bow implements GameObject {
    private Bitmap image;
    private Matrix matrix;
    private Point center;
    private Point top_left;
    private Point bottom_left;
    private Point pullBackPoint;
    private int angle;
    private int center_toTop_angle;
    private int center_toTop_distance;
    private final double RADIANS_TO_DEGREES = 57.295;
    private final double DEGREES_TO_RADIANS = 0.01745;
    private int scaleY;
    private int scaleX;

    public Bow(Bitmap image, Point center) {
        this.image = image;
        this.center = center;
        this.matrix = new Matrix();
        this.top_left = new Point(center.x - image.getWidth()/2, center.y - image.getHeight()/2);
        this.bottom_left = new Point(center.x - image.getWidth()/2, center.y + image.getHeight()/2);
        this.pullBackPoint = new Point(center.x - image.getWidth()/2, center.y);
        angle = 0;
        center_toTop_angle = (int)(RADIANS_TO_DEGREES*Math.atan(((double)image.getWidth()/2)/(image.getHeight()/2)));
        center_toTop_distance = (int)Math.sqrt(image.getHeight()*image.getHeight()/4 + image.getWidth()*image.getWidth()/4);
        scaleY = (int)(center_toTop_distance*(Math.sin(DEGREES_TO_RADIANS*(45 + center_toTop_angle))));
        scaleX = (image.getWidth() + image.getHeight())/2;
        System.out.println(center_toTop_distance);
    }

    public void setPullBackPoint(int x) {
        // REMINDER: need to implement in such a way that when the x gets smaller the bow gets pulled back more.
        //x = Math.max(x,)
    }

    public void setAngle(int y) {
        if (y <= center.y - scaleY)
            angle = 45;
        else {
            angle = (int)(45*((double)(center.y - y)/(scaleY)));
            System.out.println("smaller: " + y + "; " + angle);
        }
        if (y >= center.y + scaleY)
            angle = -45;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, matrix, null);
        canvas.drawLine(bottom_left.x,bottom_left.y,pullBackPoint.x,pullBackPoint.y, new Paint());
        canvas.drawLine(top_left.x,top_left.y,pullBackPoint.x,pullBackPoint.y, new Paint());
    }

    @Override
    public void update() {
        matrix.setRotate(angle,center.x,center.y);
        matrix.preTranslate(center.x - image.getWidth()/2,center.y - (image.getHeight()/2));
        int x = center.x + (int)(center_toTop_distance*(Math.cos(DEGREES_TO_RADIANS*(90 - angle + center_toTop_angle))));
        int y = center.y - (int)(center_toTop_distance*(Math.sin(DEGREES_TO_RADIANS*(90 - angle + center_toTop_angle))));
        top_left.set(x,y);
        x = center.x + (int)(center_toTop_distance*(Math.cos(DEGREES_TO_RADIANS*(270 - angle - center_toTop_angle))));
        y = center.y - (int)(center_toTop_distance*(Math.sin(DEGREES_TO_RADIANS*(270 - angle - center_toTop_angle))));
        bottom_left.set(x,y);
        x = (bottom_left.x + top_left.x)/2;
        y = (bottom_left.y + top_left.y)/2;
        pullBackPoint.set(x,y);

    }
}
