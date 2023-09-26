package objects.grid;

import objects.GameObject;
import engine.handler.Handler;
import engine.components.FreeCollider;
import engine.components.PositionComponent;
import engine.Vec;
import engine.SoundFX;
import engine.world.GridWorld;
import objects.RenderComponents.LandmineRenderComponent;
import objects.tank.Tank;

/**
 * A Landmine is an object that explodes on contact with a tank. it spawns naturally in the level and
 * can also (maybe) be placed on the map by Tank objects.<br>
 * The Landmine does not move and does not despawn unless it explodes.<br>
 * <p></p>
 * **MAIN METHODS**
 * Landmine - Constructor for the class, creates a Landmine at a location on the map
 * tick - checks if a Tank has made contact with the Landmine each in-game tick
 * hit - handles what happens when a Tank makes contact with the Landmine.
 */
public class Landmine extends GameObject {

    //components
    private final FreeCollider collider;
    private final PositionComponent pos;

    //if a tank drops a mine, it will be set as the parent so it doesn't immediately blow itself up.
    //if the mine spawns naturally, parent will remain set to null.
    private Tank parent = null;

    private final int damage = 40;
    private final Class[] targetObjects = {Tank.class}; //objects that can trigger the landmine

    /**
     * constructor class for naturally - spawning Landmines
     * @param handler handles components
     * @param x the x grid coordinate of the mine
     * @param y the y grid coordinate of the mine
     * @param grid the map grid is passed as a parameter so that the mine can be placed on it.
     */
    public Landmine(Handler handler, int x, int y, GridWorld grid){
        super(handler, grid.getCentre(x, y));

        grid.addObject(this, x, y); //add Landmine to grid

        //dimensions of Landmine
        int width = 37;
        int height = 37;

        //initialise components
        this.pos = new PositionComponent(grid.getCentre(x, y));
        this.addComponent(new LandmineRenderComponent(this.addComponent(pos)));
        this.collider = this.addComponent(new FreeCollider(pos, width, height, handler.getGridWorld()));

        this.initComponents();
    }

    /**
     * use this constructor for tanks dropping a landmine
     * @param handler handles components
     * @param parent the tank that dropped the landmine
     */
    //if we decide tanks can drop mines, use this constructor
    public Landmine(Handler handler, Tank parent) {
        super(handler, parent.pos.get());

        this.parent = parent; //assign parent

        //dimensions of Landmine
        int width = 37;
        int height = 37;

        //initialise components
        this.pos = new PositionComponent(parent.pos.get());
        this.addComponent(new LandmineRenderComponent(this.addComponent(pos)));
        this.collider = this.addComponent(new FreeCollider(pos, width, height, handler.getGridWorld()));

        this.initComponents();
    }

    /**
     * called when a Tank object comes into contact with the Landmine. deals damage to the relevant
     * Tank, plays an explosion effect and removes the exploded Landmine from play.
     * @param position the current position of the Landmine
     * @param hpReduction the amount of health to be deducted from the tank that blows up
     * @param parent the tank that dropped the mine (or null if none).
     */
    public void hit(Vec position, float hpReduction, Tank parent) {
        for(Tank t : Tank.getTankList()) {
            //the tank that dropped the landmine can't blow it up
            //otherwise the landmine would explode as soon as it is dropped since the tank would be
            //touching it at the time.
            if(t == parent) {
                ;
            }
            else {
                //check which tank is touching the mine
                if (collider.testForCollisionWithObject(t.collider, position)) {
                    //play explosion sound
                    SoundFX bang = new SoundFX("src/main/resources/sounds/landmineBoom.mp3");
                    bang.play();
                    //deal damage to tank that ran over the mine
                    t.reduceHealthPoints(hpReduction);
                    //remove the landmine from the stage (since it exploded)
                    this.remove();
                }
            }
        }
    }

    /**
     * called every in game tick to check if the Landmine needs to explode yet.\n
     * if Landmine is in contact with a tank, call the hit method.
     */
    public void tick() {
        //System.out.println("ticking");
        if(collider.localCollisionTest(pos.get(), targetObjects)) { //if a tank is touching a landmine:
            //it explodes, damaging the tank that hit it.
            hit(pos.get(), damage, this.parent);
            //System.out.println("Bang.");


        }
    }
}
