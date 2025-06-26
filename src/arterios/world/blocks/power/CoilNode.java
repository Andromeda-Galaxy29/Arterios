package arterios.world.blocks.power;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.graphics.*;
import mindustry.world.blocks.power.PowerNode;

public class CoilNode extends PowerNode {

    public float warmupSpeed = 0.01f;
    public float rotateSpeed = 4f;
    public TextureRegion rotatorRegion;

    public CoilNode(String name) {
        super(name);
        update = true;
    }

    @Override
    public void load() {
        super.load();
        rotatorRegion = Core.atlas.find(name+"-rotator");
    }

    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{region, rotatorRegion};
    }

    public class CoilNodeBuild extends PowerNodeBuild {
        public float rotateSpeedMultiplier, coilRotation = 0;

        @Override
        public void updateTile(){
            super.updateTile();

            consume();

            rotateSpeedMultiplier = Mathf.lerpDelta(rotateSpeedMultiplier, power.graph.getSatisfaction(), warmupSpeed);
            coilRotation += rotateSpeed * rotateSpeedMultiplier;
        }

        @Override
        public void draw() {
            super.draw();

            Draw.z(Layer.blockOver);
            Drawf.spinSprite(rotatorRegion, x, y, coilRotation);
        }
    }
}
