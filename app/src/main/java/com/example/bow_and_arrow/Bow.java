package com.example.bow_and_arrow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;

public class Bow implements GameObject {
    private Bitmap image;
    private Arrow arrow;
    private Matrix matrix;
    private Point center;
    private Point top_left;
    private Point bottom_left;
    private Point pullBackPoint;
    private int angle;
    private int pullBack;
    private int scaleY;
    private int scaleX;
    private int center_toTop_angle;
    private int center_toTop_distance;
    private boolean isShooting;
    private final double RADIANS_TO_DEGREES = 57.295;
    private final double DEGREES_TO_RADIANS = 0.01745;

    public Bow(Bitmap image, Point center) {
        this.image = image;
        this.center = center;
        this.matrix = new Matrix();
        this.top_left = new Point(center.x - image.getWidth()/2, center.y - image.getHeight()/2);
        this.bottom_left = new Point(center.x - image.getWidth()/2, center.y + image.getHeight()/2);
        this.pullBackPoint = new Point(center.x - image.getWidth()/2, center.y);
        angle = 0;
        pullBack = image.getWidth()/2;
        center_toTop_angle = (int)(RADIANS_TO_DEGREES*Math.atan(((double)image.getWidth()/2)/(image.getHeight()/2)));
        center_toTop_distance = (int)Math.sqrt(image.getHeight()*image.getHeight()/4 + image.getWidth()*image.getWidth()/4);
        scaleY = (int)(center_toTop_distance*(Math.sin(DEGREES_TO_RADIANS*(45 + center_toTop_angle))));
        scaleX = (image.getWidth() + image.getHeight())/2;
        isShooting = false;
        arrow = new Arrow(pullBackPoint,scaleX + image.getWidth());
    }

    public void setPullBack(int x) {
        // REMINDER: need to implement in such a way that when the x gets smaller the bow gets pulled back more.
        if (x <= center.x - scaleX) {
            pullBack = scaleX;
        }
        else {
            pullBack = center.x - x;
        }
        if (x > center.x - image.getWidth()/2) {
            pullBack = image.getWidth()/2;
        }
    }

    public void setAngle(int y) {
        if (y <= center.y - scaleY)
            angle = 45;
        else {
            angle = (int)(45*((double)(center.y - y)/(scaleY)));
        }
        if (y >= center.y + scaleY)
            angle = -45;
    }

    public  void setShooting() {
        isShooting = true;
        arrow.setShooting();
        arrow.setForce(((double)pullBack)/scaleX);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, matrix, null);
        canvas.drawLine(bottom_left.x,bottom_left.y,pullBackPoint.x,pullBackPoint.y, new Paint());
        canvas.drawLine(top_left.x,top_left.y,pullBackPoint.x,pullBackPoint.y, new Paint());
        arrow.draw(canvas);
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
        x = center.x - (int)(pullBack*(Math.cos(DEGREES_TO_RADIANS*(-angle))));
        y = center.y + (int)(pullBack*(Math.sin(DEGREES_TO_RADIANS*(-angle))));
        pullBackPoint.set(x,y);
        if (isShooting) {
            pullBack = Math.max(image.getWidth()/2,pullBack-40);
            if (pullBack == image.getWidth()/2) {
                isShooting = false;
            }
        }
        if (!arrow.isShooting()) {
            arrow.setAngle(-angle);
            arrow.setStart(pullBackPoint.x, pullBackPoint.y);
        }
        arrow.update();
    }
}
