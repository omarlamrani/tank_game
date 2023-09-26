package engine.network.stream;

import engine.handler.Handler;
import engine.components.controllers.TankNetworkController;
import objects.RenderComponents.TankRenderComponent;
import objects.tank.Tank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread implements Runnable {

    boolean running;
    //    Socket socket;
//    Server server;
    Handler handler;
    DataInputStream in;

    public ServerThread(Socket socket, Handler handler) {
        running = true;
        this.handler = handler;
        try {
            in = new DataInputStream(socket.getInputStream());
            System.out.println("Player connecting...");
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            float hue = in.readFloat();

            Tank tank = new Tank(handler, handler.getGridWorld().getSpawn(), hue);

            out.writeInt(tank.getComponent(TankRenderComponent.class).UID);
            out.writeInt(handler.getGridWorld().dimX);
            out.writeInt(handler.getGridWorld().dimY);
            out.writeInt(handler.getGridWorld().size);


            tank.addComponent(new TankNetworkController(tank, socket));

        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("Server thread created");
    }


    @Override
    public void run() {
        System.out.println("Server thread spun up");

    }


    public void stopThread() {
        running = false;
    }

}
