package com.example.bow_and_arrow;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Arrow implements GameObject {
    private Point start;
    private Point end;
    private Point side1;
    private Point side2;
    private int length;
    private int angle;
    private int force;
    private boolean isShooting;
    private final double RADIANS_TO_DEGREES = 57.295;
    private final double DEGREES_TO_RADIANS = 0.01745;

    public Arrow(Point start, int length) {
        this.start = new Point(start.x,start.y);
        this.length = length;
        this.end = new Point(start.x + length, start.y);
        angle = 0;
        this.side1 = new Point();
        this.side2 = new Point();
        isShooting = false;
        force = 0;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public void setStart(int x, int y) {
        start.set(x,y);
    }

    public void setShooting() {
        isShooting = true;
    }

    public void setForce(double force) {
        System.out.println(force);
        this.force = (int)(75*force);
    }

    public boolean isShooting() {return isShooting;}

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        canvas.drawLine(start.x,start.y,end.x,end.y, paint);
        canvas.drawLine(end.x,end.y,side1.x,side1.y,paint);
        canvas.drawLine(end.x,end.y,side2.x,side2.y,paint);
    }

    @Override
    public void update() {
        if (isShooting) {
            start.set(start.x + (int)(force*Math.cos(angle*DEGREES_TO_RADIANS)), start.y - (int)(force*Math.sin(angle*DEGREES_TO_RADIANS)));
            if (start.x > Resources.getSystem().getDisplayMetrics().widthPixels || start.y < 0 || start.y > Resources.getSystem().getDisplayMetrics().heightPixels) {
                isShooting = false;
            }
        }
        end.set(start.x + (int)(length*Math.cos(angle*DEGREES_TO_RADIANS)), start.y - (int)(length*Math.sin(angle*DEGREES_TO_RADIANS)));
        side1.set(end.x + (int)(30*Math.cos((180+angle+25)*DEGREES_TO_RADIANS)), end.y - (int)(30*Math.sin((180+angle+25)*DEGREES_TO_RADIANS)));
        side2.set(end.x + (int)(30*Math.cos((180+angle-25)*DEGREES_TO_RADIANS)), end.y - (int)(30*Math.sin((180+angle-25)*DEGREES_TO_RADIANS)));
    }
}
