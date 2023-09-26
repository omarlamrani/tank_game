package engine.components;

import engine.Vec;
import engine.wrappers.FloatWrapper;

import java.io.Serializable;


public class PositionComponent implements Component, Serializable {

    //Coords x, y and rotation r
    public FloatWrapper x, y, r;

    public PositionComponent(Vec pos) {
        this.x = new FloatWrapper(pos.getX());
        this.y = new FloatWrapper(pos.getY());
        this.r = new FloatWrapper(0F);
    }

    public PositionComponent(float x, float y, float r) {
        this.x = new FloatWrapper(x);
        this.y = new FloatWrapper(y);
        this.r = new FloatWrapper(r);
    }

    public PositionComponent(PositionComponent copy) {
        this.x = copy.getWrappedX();
        this.y = copy.getWrappedY();
        this.r = copy.getWrappedR();
    }

    public PositionComponent(FloatWrapper x, FloatWrapper y, FloatWrapper r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public FloatWrapper getWrappedX() {
        return this.x;
    }

    public FloatWrapper getWrappedY() {
        return this.y;
    }

    public FloatWrapper getWrappedR() {
        return this.r;
    }

    public int getIntX() {
        return (int) x.get();
    }

    public void setIntX(int x) {
        this.x.set(x);
    }

    public int getIntY() {
        return (int) y.get();
    }

    public void setIntY(int y) {
        this.y.set(y);
    }

    public float getX() {
        return x.get();
    }

    public void setX(float n) {
        x.set(n);
    }

    public float getY() {
        return y.get();
    }

    public void setY(float n) {
        y.set(n);
    }

    public float getR() {
        return r.get();
    }

    public void setR(Float r) { //Enforce rotation rules.
        r = r % 360;
        if (r < 0) r += 360;
        this.r.set(r);
    }

    public Vec get() {
        return new Vec(x.get(), y.get());
    }

    //returns a unit vector in the direction.
    public Vec getRotation() {
        return new Vec((double) r.get(), 1.0F);
    }

    //Returns a unit vector of the direction the object is facing.
    public Vec getRVec() {
        return new Vec((float) Math.sin(Math.toRadians(r.get())), (float) Math.cos(Math.toRadians(r.get())) * -1);
    }

    public void setPos(Vec pos) {
        this.x.set(pos.getX());
        this.y.set(pos.getY());
    }

    public ComponentType getType() {
        return ComponentType.SIMPLE;
    }

}
