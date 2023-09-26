package engine.network.in;

import engine.Entity;
import engine.components.TickingInterface;
import engine.components.controllers.TankControls;

import java.io.DataOutputStream;
import java.io.IOException;

public class TankControlStream extends TankControls implements TickingInterface {

    int keepAlive = 300;
    DataOutputStream out;
    Entity toClose;

    public TankControlStream(DataOutputStream out, Entity toClose) {
        this.out = out;
        this.toClose = toClose;
    }

    public void close() {
        toClose.remove();
    }

    //On event send data to server
    @Override
    public boolean moveForward() {
        try {
            out.writeShort(0);
            out.flush();
        } catch (IOException e) {
            close();
        }
        return false;
    }

    @Override
    public boolean moveBackward() {
        try {
            out.writeShort(1);
            out.flush();
        } catch (IOException e) {
            close();

        }
        return false;
    }

    @Override
    public void turnClockwise() {
        try {
            out.writeShort(2);
            out.flush();
        } catch (IOException e) {
            close();
        }
    }

    @Override
    public void turnAntiClockwise() {
        try {
            out.writeShort(3);
            out.flush();
        } catch (IOException e) {
            close();
        }
    }

    @Override
    public void tick() {
        try {
            keepAlive--;
            out.writeShort(9);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize() {

    }
}
