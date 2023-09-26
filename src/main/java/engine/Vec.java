package engine;
//This can probably be replaced with a good vector library but is a good placeholder.

import javafx.geometry.Point2D;

import java.io.Serializable;

public class Vec implements Serializable {
    private float x, y;
    //, rotation, magnitude;


    public Vec(float x1, float y1) {
        x = x1;
        y = y1;
        //rotation = getAngle();
        //magnitude = abs();
    }


    public Vec(double rotation, float mag) {
        //this.rotation = (float) rotation;
        //this.magnitude = mag;
        this.x = (float) Math.sin(Math.toRadians(rotation)) * mag;
        this.y = (float) Math.cos(Math.toRadians(rotation)) * -1 * mag;
    }

    public Vec(Point2D p) {
        x = (float) p.getX();
        y = (float) p.getY();
    }

    public Vec(Vec v) {
        x = v.getX();
        y = v.getY();
    }

    public static Vec square(Vec v1) {
        return new Vec((float) Math.pow(v1.x, 2), (float) Math.pow(v1.y, 2));
    }

    //public static Vec dirP1P2(float x1, float y1, float x2, float y2) {
    //    return
    //}

    public static Vec add(Vec v1, Vec v2) {
        return new Vec(v1.x + v2.x, v1.y + v2.y);
    }

    public static Vec subtract(Vec v1, Vec v2) {
        return new Vec(v1.getX() - v2.getX(), v1.getY() - v2.getY());
    }

    public static boolean equals(Vec v1, Vec v2) {
        return (Math.abs(v1.x - v2.x) < 0.001 && Math.abs(v1.y - v2.y) < 0.001);
    }

    public static Vec scalarMult(Vec v1, float f) {
        return new Vec(v1.x * f, v1.y * f);
    }

    public static Vec vecFromMag(float mag, float direction) {
        return Vec.scalarMult(new Vec((float) Math.sin(Math.toRadians(direction)), (float) Math.cos(Math.toRadians(direction)) * -1), mag);
    }

    public static float getFastMag(Vec v1) {
        return (float) (Math.pow(v1.x, 2) + Math.pow(v1.y, 2));
    }

    private void setVars(float x, float y, float rotation, float magnitude) {
        this.x = x;
        this.y = y;
        //this.rotation = rotation;
        //this.magnitude = magnitude;
    }

    public float abs() {
        return (float) (Math.hypot(this.x, this.y));
    }

    public float getAngle() {
        float out;
        if (y != 0) out = (float) Math.toDegrees(Math.atan(x / (y * -1)));
        else {
            return (x > 0) ? 90 : -90;
        }
        if (y > 0) out += 180;

        return out;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        //rotation = getAngle();
        //magnitude = abs();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        //rotation = getAngle();
        //magnitude = abs();
    }

    public float getRotation() {
        return getAngle();
    }

    public Vec setRotation(float rotation) {
        //this.rotation = rotation;
        this.x = (float) Math.sin(Math.toRadians(rotation)) * abs();
        this.y = (float) Math.cos(Math.toRadians(rotation)) * -1 * abs();
        return this;
    }

    public float getMagnitude() {
        return abs();
    }

    public void setMagnitude(float magnitude) {
        double multiplier = magnitude / this.getMagnitude();
        //this.magnitude = magnitude;
        this.x = (float) (this.x * multiplier);
        this.y = (float) (this.y * multiplier);
    }

    public void set(Vec vec) {
        this.x = vec.x;
        this.y = vec.y;
        //this.rotation = vec.rotation;
        //this.magnitude = vec.magnitude;
    }

    @Override
    public String toString() {
        return "Vec{" +
                "x=" + x +
                ", y=" + y +
                ", rotation=" + getAngle() +
                ", magnitude=" + abs() +
                '}';
    }
}
