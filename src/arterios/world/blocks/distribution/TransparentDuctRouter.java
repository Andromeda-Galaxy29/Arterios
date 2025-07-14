package arterios.world.blocks.distribution;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.Building;
import mindustry.graphics.*;
import mindustry.world.blocks.distribution.*;

import static mindustry.Vars.*;

public class TransparentDuctRouter extends DuctRouter {
    public Color transparentColor = new Color(0.4f, 0.4f, 0.4f, 0.1f);
    public TextureRegion bottomRegion;
    public TextureRegion itemRegion;

    public TransparentDuctRouter(String name) {
        super(name);
    }

    @Override
    public void init() {
        super.init();
        itemCapacity = 1;
    }

    @Override
    public void load() {
        super.load();
        bottomRegion = Core.atlas.find(name+"-bottom");
        itemRegion = Core.atlas.find(name+"-item");
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{bottomRegion, region, topRegion};
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        Draw.rect(bottomRegion, plan.drawx(), plan.drawy());
        super.drawPlanRegion(plan, list);
    }

    public class TransparentDuctRouterBuild extends DuctRouterBuild {
        Building target, drawTarget;

        @Override
        public void updateTile(){
            progress += edelta() / speed * 2f;

            if(current != null){
                if(target == null){
                    target = target();
                }
                if(target != null){
                    drawTarget = target;
                }
                if(progress >= (1f - 1f/speed)){
                    if(target != null){
                        target.handleItem(this, current);
                        items.remove(current, 1);
                        current = null;
                        progress %= (1f - 1f/speed);
                    }
                    target = null;
                }
            }else{
                progress = 0;
            }

            if(current == null && items.total() > 0){
                current = items.first();
            }
        }

        @Override
        public void draw() {
            Draw.z(Layer.blockUnder);
            Draw.rect(bottomRegion, x, y);

            Draw.z(Layer.blockUnder + 0.1f);

            if(current != null && drawTarget != null){
                Tmp.v1.set(Geometry.d4x(rotation - 2) * tilesize / 2f, Geometry.d4y(rotation - 2) * tilesize / 2f)
                        .lerp(Geometry.d4x(relativeTo(drawTarget)) * tilesize / 2f, Geometry.d4y(relativeTo(drawTarget)) * tilesize / 2f,
                                Mathf.clamp((progress + 1f) / (2f - 1f/speed)));

                Draw.rect(current.fullIcon, x + Tmp.v1.x, y + Tmp.v1.y, itemSize, itemSize);
            }

            Draw.color(transparentColor);
            Draw.rect(bottomRegion, x, y);
            Draw.color();

            Draw.z(Layer.blockUnder + 0.2f);

            Draw.rect(region, x, y);
            if(sortItem != null){
                Draw.color(sortItem.color);
                Draw.rect(itemRegion, x, y);
                Draw.color();
            }else{
                Draw.rect(topRegion, x, y, rotdeg());
            }
        }
    }
}
