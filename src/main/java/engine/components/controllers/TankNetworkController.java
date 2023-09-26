package engine.components.controllers;

import engine.Vec;
import engine.components.ComponentType;
import engine.components.TickingInterface;
import objects.tank.Tank;
import objects.turret.Turret;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class TankNetworkController implements TickingInterface {

    Tank tank;
    Turret turret;
    DataInputStream in;
    Socket socket;

    int timeout = 600;

    //This class is where the netcode will interface with the tank
    //This controller can be applied to any tank.
    //A previously AI driven tank could be taken over by a player for example.

    public TankNetworkController(Tank tank, Socket socket) throws IOException {
        this.tank = tank;
        this.turret = tank.turret;
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void tick() {
        timeout--;

        if (timeout < 0) {
            System.out.println("Player timed out");
            tank.remove();
        }

        try {
            while (in.available() > 0) {
                switch (in.readShort()) {
                    case 0:
                        tank.moveComp.moveForward();
                        //System.out.println("read f");
                        break;
                    case 1:
                        tank.moveComp.moveBackward();
                        //System.out.println("read c");

                        break;
                    case 2:
                        tank.moveComp.turnClockwise();
                        //System.out.println("read cw");

                        break;
                    case 3:
                        tank.moveComp.turnAntiClockwise();
                        //System.out.println("read acw");

                        break;

                    case 4:
                        turret.fire();
                        //System.out.println("read fire");

                        break;

                    case 5:
                        float mx = in.readFloat();
                        float my = in.readFloat();
                        turret.turnTo(new Vec(mx, my));
                }
                timeout = 600;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize() {

    }

    @Override
    public ComponentType getType() {
        return ComponentType.TICKING;
    }
}
