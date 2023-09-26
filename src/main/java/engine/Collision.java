package engine;

public class Collision {
    private final Vec pos;
    private final Entity entity;

    public Collision(Vec pos, Entity entity) {
        this.pos = pos;
        this.entity = entity;
    }

    public Vec getPos() {
        return pos;
    }

    public Entity getEntity() {
        return entity;
    }
}
