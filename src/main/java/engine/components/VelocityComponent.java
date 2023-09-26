package engine.components;

import engine.Vec;

public class VelocityComponent extends Vec implements Component {

    //private float v = 0.0F;
    private final PositionComponent pos;
    //private Vec velVec = new Vec(0, 0);
    //private float rotation = 0;

    public VelocityComponent(PositionComponent pos) {
        super(0, 0);
        this.pos = pos;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.SIMPLE;
    }

    public void move() {
        //Collision detection link here?
        //if (v != 0) pos.setPos(Vec.add(pos.getPos(), Vec.scalarMult(pos.getRVec(), v)));
        pos.setPos(Vec.add(pos.get(), this));
    }
}
