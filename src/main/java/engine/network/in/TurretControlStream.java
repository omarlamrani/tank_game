package engine.network.in;

import engine.Entity;
import engine.Vec;
import engine.components.controllers.TurretControls;

import java.io.DataOutputStream;
import java.io.IOException;

public class TurretControlStream implements TurretControls {

    DataOutputStream out;
    Entity toClose;

    public TurretControlStream(DataOutputStream out, Entity toClose) {
        this.out = out;
        this.toClose = toClose;
    }

    public void close() {
        System.out.println("Removing turret");
        toClose.remove();
    }


    @Override
    public float turnTo(Vec t) {
        try {
            out.writeShort(5);
            out.writeFloat(t.getX());
            out.writeFloat(t.getY());
            out.flush();
        } catch (IOException e) {
            close();
        }
        return 0;
    }

    @Override
    public void fire() {
        try {
            out.writeShort(4);
            out.flush();
        } catch (IOException e) {
            close();
        }
    }
}
