package objects.health;

import engine.Entity;
import engine.components.Component;
import engine.components.ComponentType;
import engine.components.PositionComponent;
import objects.RenderComponents.HealthBarRenderComponent;

public class HealthComponent implements Component {

    private final Entity parent;
    private HealthBarRenderComponent render;
    private float health;
    private float maxHealth;
    public static int maxLives;
    private int lives;

    public HealthComponent(Entity parent, float maxHealth) {
        this.parent = parent;
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.render = parent.getComponent(HealthBarRenderComponent.class);
        this.lives = maxLives;
        //parent.ad
        //this.addComponent(new HealthBarRenderComponent(this.pos, healthPoints));
    }

    public double getHealthPoints() {
        return this.health;
    }

    /**
     * Reduces health by an amount.
     *
     * @param hpReduction the amount of health to be deducted
     */
    public void reduceHealthPoints(float hpReduction) {
        this.health = this.health - hpReduction;
        if (health <= 0) {

//            When TANK health becomes 0, it loses a Life.
            this.lives = this.lives - 1;
            if (this.lives > 0){
                //respawns the tank if it has lives left
                parent.getComponent(PositionComponent.class).setPos(parent.handler.getGridWorld().getSpawn());
                this.health = maxHealth;
                if (render == null) render = parent.getComponent(HealthBarRenderComponent.class);
                else {
                    render.healthPoints = health;
                }
            } else{
                parent.remove();
            }
        }
        else {
            if (render == null) render = parent.getComponent(HealthBarRenderComponent.class);
            else {
                render.healthPoints = health;
            }
        }
    }

    @Override
    public ComponentType getType() {
        return null;
    }
}
