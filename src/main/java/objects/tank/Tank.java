package objects.tank;

import engine.components.Component;
import engine.components.ComponentType;
import engine.handler.Handler;
import engine.Vec;
import engine.components.FreeCollider;
import engine.components.GridPositionComponent;
import engine.world.AI.PathFinder;
import engine.world.Node;
import objects.GameObject;
import objects.RenderComponents.HealthBarRenderComponent;
import objects.RenderComponents.TankRenderComponent;
import objects.grid.Wall;
import objects.health.HealthComponent;
import objects.turret.Turret;

import java.util.ArrayList;
import java.util.Comparator;


/**
 * Tank is pretty self-explanatory as a class, it's a Tank. The player(s) control one and some enemies
 * are also Tank objects. It moves around the map and shoots stuff.<br>
 * <br>
 * MAIN METHODS:<br>
 * Tank - the constructor, spawns a tank at some coordinates and creates other necessary objects
 * for the tank to function<br>
 * tick - updates the position of the tank and stops it leaving the game area.<br><br>
 * ATTRIBUTES:<br>
 * SCENE_WIDTH - width of the stage
 * SCENE_HEIGHT - height of the stage
 * collides - list of classes the Tank object can collide with
 * projectiles - list of classes the Tank can 'fire' or be hit by
 * tankList - [DON'T KNOW]
 * previousPos - the previous position of the Tank
 * vel - the velocity of the Tank
 * turret - the rotating turret that shoots. it is a separate class but is 'attached' to Tank
 * hue - the colour of the tank
 * MAX_HEALTH - the maximum health of the Tank
 * healthPoints - the current health of the Tank
 */
public class Tank extends GameObject {

    private static final float MAX_HEALTH = 100;
    //classes the tank can collide with (or can't drive through)
    private static final Class[] collides = {Tank.class, Wall.class};
    private static final ArrayList<Tank> tankList = new ArrayList<Tank>();
    //components
    public final FreeCollider collider;
    public final GridPositionComponent gridPosComp;
    public final TankMovementComponent moveComp;
    public final HealthComponent health;
    public Vec vel; //velocity
    //turret object is the tank turret on top of the tank.
    public Turret turret;
    public float hue; //colour of the tank
    public double healthPoints;
    private Vec previousPos;
    /**
     * Constructor that creates a Tank object at a specified location,
     * and also a Turret object and a HBRenderComponent (health bar) for the Tank<br>
     *
     * @param handler  handler is passed to handle components
     * @param location the spawn coordinate
     * @param hue      the colour of the Tank
     */
    public Tank(Handler handler, Vec location, float hue) {
        super(handler, location);

        this.hue = hue;
        this.addComponent(new TankRenderComponent(pos, hue));
        this.turret = new Turret(handler, this);
        this.healthPoints = 100;

        this.addComponent(new HealthBarRenderComponent(this.pos, MAX_HEALTH));
        this.health = this.addComponent(new HealthComponent(this, MAX_HEALTH));

        previousPos = this.pos.get();
        collider = this.addComponent(new FreeCollider(pos, 62, 75, handler.getGridWorld()));
        collider.setCollides(collides);
        gridPosComp = this.addComponent(new GridPositionComponent(pos, handler.getGridWorld(), this));
        moveComp = this.addComponent(new TankMovementComponent(pos, collider, gridPosComp));
        gridPosComp.updateGridPos();

        tankList.add(this);

        //pather = new PathFinder();
        //super.handler.gridWorld.
        //new PathVis(handler, pather);


        this.initComponents();
    }

    public static ArrayList<Tank> getTankList() {
        return new ArrayList<>(tankList);
    }

    public static ArrayList<Tank> getSortedTankList(Vec near) {
        ArrayList<Tank> sorted = new ArrayList<>(tankList);
        sorted.sort(Comparator.comparingDouble(x -> Vec.subtract(x.pos.get(), near).getMagnitude()));
        return sorted;
    }

    public static ArrayList<Tank> getPathSortedTankList(Tank tank) {
        PathFinder pather = new PathFinder();
        ArrayList<Tank> sorted = new ArrayList<>(tankList);
        sorted.remove(tank);
        sorted.sort(Comparator.comparingInt(x -> validator(pather.tankToTank(tank, x))));
        return sorted;
    }

    private static int validator(ArrayList<Node> toCheck) {
        //System.out.println("Eval path length: " + toCheck.size());
        if (toCheck == null || toCheck.isEmpty()) return 99;
        else return toCheck.size();
    }

    /**
     * Updates the position of the Tank object each tick, and prevents the object from moving beyond
     * the edge of the stage.
     */
    public void tick() {
        this.vel = Vec.add(this.pos.get(), Vec.scalarMult(this.previousPos, -1));
        this.previousPos = pos.get();

    }

    /**
     * @return the current health points for this object
     */
    public double getHealthPoints() {
        return this.health.getHealthPoints();
    }

    /**
     * Reduces health by an amount.
     *
     * @param hpReduction the amount of health to be deducted
     */
    public void reduceHealthPoints(float hpReduction) {
        this.health.reduceHealthPoints(hpReduction);
    }

    /**
     * Deletes the Tank object and the child Turret object.
     */
    @Override
    public void remove() {
        gridPosComp.remove();
        tankList.remove(this);
        super.remove();
        turret.remove();
    }
}