package engine.network.stream;

import engine.Entity;
import engine.handler.Handler;
import engine.Vec;
import engine.components.PositionComponent;
import engine.components.RenderComponent;
import engine.components.TickingComponent;
import objects.RenderComponents.*;
import org.nustaq.serialization.simpleapi.DefaultCoder;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class Server extends Entity implements Runnable {

    public static DefaultCoder coder = new DefaultCoder(true, RenderComponent.class,
            Vec.class, PositionComponent.class, ProjectileRenderComponent.class, TurretRenderComponent.class,
            TankRenderComponent.class, WallRenderComponent.class, BackgroundRenderComponent.class, HealthBarRenderComponent.class);
    private final Thread thread = new Thread(this);
    private final ArrayList<ServerThread> threads = new ArrayList<>();
    public int PORT = 25565;
    public transient MulticastSocket multicastSocket;
    public transient ServerSocket serverSocket;
    byte[] bytes;
    private Handler handler;
    private boolean running = true;


    public Server(Handler handler) {
        super(handler);

        this.addComponent(new TickingComponent(this::tick));

        try {
            serverSocket = new ServerSocket(PORT);
            multicastSocket = new MulticastSocket(PORT);
            multicastSocket.joinGroup(InetAddress.getByName("228.5.6.7"));

            this.handler = handler;

            bytes = coder.toByteArray(handler.getRenderComponents());

        } catch (IOException e) {
            e.printStackTrace();
        }

        thread.start();


        this.initComponents();
    }

    public byte[] getBytes() {
        return bytes;
    }


    public void tick() {
        long start = System.nanoTime();

        byte[] encoded = coder.toByteArray(new ArrayList(handler.getRenderComponents()));

        if (encoded.length < 65508) {
            bytes = encoded;
        } else {
            System.out.println("Too large: " + encoded.length);
        }

        //System.out.println("Encoded " + bytes.length + " bytes in: " + (System.nanoTime() - start) + "ns");

        try {
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, InetAddress.getByName("228.5.6.7"), PORT);
            multicastSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stopServer() {
        this.running = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (ServerThread thread : threads) {
            thread.stopThread();
        }
    }


    @Override
    public void run() {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                ServerThread thread = new ServerThread(socket, handler);
                threads.add(thread);
                thread.run();

            } catch (IOException e) {
                System.out.println("Server died");
            }
        }
    }
}


