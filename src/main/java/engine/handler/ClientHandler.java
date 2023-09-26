package engine.handler;

import engine.Entity;
import engine.components.RenderComponent;
import engine.components.controllers.TankKeyboardController;
import engine.components.controllers.TurretMouseController;
import engine.network.in.TankControlStream;
import engine.network.in.TurretControlStream;
import engine.network.stream.Server;
import engine.world.GridWorld;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import org.nustaq.serialization.simpleapi.DefaultCoder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Handler {

    //todo link these to GUI
    private final int PORT = 25565;
    DefaultCoder coder = Server.coder;
    private int packetSize = 65508;
    private transient MulticastSocket multicastSocket;
    private transient Socket socket;

    public ClientHandler(GraphicsContext gc, Scene scene, Float hue, String IP) {
        super(gc, scene);
        try {
            //todo change from multicast to just UDP
            multicastSocket = new MulticastSocket(PORT);
            multicastSocket.setReceiveBufferSize(100);
            multicastSocket.joinGroup(InetAddress.getByName("228.5.6.7"));

            socket = new Socket(IP, PORT);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            out.writeFloat(hue);
            setTackingID(in.readInt());
            int dimX = in.readInt();
            int dimY = in.readInt();
            int size = in.readInt();

            super.gridWorld = new GridWorld(this, dimX, dimY, size, 0, 0);
            out.flush();

            Entity dummy = new Entity(this);
            dummy.addComponent(new TankKeyboardController(new TankControlStream(out, dummy), getKeyboardTracker()));
            dummy.addComponent(new TurretMouseController(new TurretControlStream(out, dummy), getMouseTracker()));


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void stop() {
        if (socket != null && socket.isConnected()) {
            try {
                socket.close();
                System.out.println("Socket closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void renderList() {
        try {
            byte[] input = new byte[packetSize];
            DatagramPacket packet = new DatagramPacket(input, input.length);
            multicastSocket.receive(packet);
            packetSize = packet.getLength() + 1000;

            Object in = coder.toObject(input);

            if (in != null) {
                this.renderComponents = (ArrayList<RenderComponent>) in;
            } else {
                System.out.println("in null");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        super.renderList();
    }

}
