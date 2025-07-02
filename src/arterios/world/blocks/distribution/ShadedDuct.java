package arterios.world.blocks.distribution;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.util.Eachable;
import mindustry.entities.units.BuildPlan;
import mindustry.graphics.Layer;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.distribution.Duct;

import static mindustry.Vars.tilesize;

public class ShadedDuct extends Duct {

    public TextureRegion[][] sideRegions;

    public ShadedDuct(String name) {
        super(name);
    }

    @Override
    public void load(){
        super.load();
        sideRegions = new TextureRegion[7][4];
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 4; j++){
                sideRegions[i][j] = Core.atlas.find(name+"-side-"+i+"-"+j);
            }
        }
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        int[] bits = getTiling(plan, list);

        if(bits == null) return;

        Draw.scl(bits[1], bits[2]);
        Draw.alpha(0.5f);
        Draw.rect(botRegions[bits[0]], plan.drawx(), plan.drawy(), plan.rotation * 90);
        Draw.reset();

        int blendbits2 = bits[0];
        if(blendbits2 == 1 && (bits[1] != 1 || bits[2] != 1)){ //Flips corner ducts
            blendbits2 = 5;
        }
        if(blendbits2 == 2 && (bits[1] != 1 || bits[2] != 1)){ //Flips T-junction ducts
            blendbits2 = 6;
        }
        Draw.rect(sideRegions[blendbits2][plan.rotation], plan.drawx(), plan.drawy());
    }

    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{Core.atlas.find("duct-bottom"), sideRegions[0][0]};
    }

    public class ShadedDuctBuild extends Duct.DuctBuild {

        @Override
        public void draw(){
            super.draw();

            //draw extra ducts facing this one for non-square tiling purposes
            Draw.z(Layer.blockUnder);
            for(int i = 0; i < 4; i++){
                if((blending & (1 << i)) != 0){
                    int dir = rotation - i;
                    float rot = i == 0 ? rotation * 90 : (dir)*90;

                    if(Mathf.mod(dir, 4) == 1 || Mathf.mod(dir, 4) == 2) Draw.yscl = -1;
                    Draw.rect(sliced(sideRegions[0][0], i != 0 ? SliceMode.bottom : SliceMode.top), x + Geometry.d4x(dir) * tilesize*0.75f, y + Geometry.d4y(dir) * tilesize*0.75f, rot);
                }
            }
            Draw.yscl = 1;

            Draw.z(Layer.block - 0.2f);
            int blendbits2 = blendbits;
            if(blendbits2 == 1 && (xscl != 1 || yscl != 1)){ //Flips corner ducts
                blendbits2 = 5;
            }
            if(blendbits2 == 2 && (xscl != 1 || yscl != 1)){ //Flips T-junction ducts
                blendbits2 = 6;
            }
            Draw.rect(sideRegions[blendbits2][rotation], x, y);
        }

        @Override
        protected void drawAt(float x, float y, int bits, float rotation, SliceMode slice) {
            Draw.z(Layer.blockUnder);
            Draw.rect(sliced(botRegions[bits], slice), x, y, rotation);

            Draw.z(Layer.blockUnder + 0.2f);
            Draw.color(transparentColor);
            Draw.rect(sliced(botRegions[bits], slice), x, y, rotation);
            Draw.color();
        }
    }
}
