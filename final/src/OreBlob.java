import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class OreBlob extends HunterEntity{

    public OreBlob(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    @Override
    protected List <Point> calcPotentialNeighbors(Point p) {
        return new ArrayList<>(Arrays.asList(new Point(p.x+1, p.y),
                new Point(p.x, p.y+1),
                new Point(p.x-1, p.y),
                new Point(p.x, p.y-1)));
    }

    @Override
    protected Optional<Entity> calcTarget(WorldModel world) {
        return world.findNearest(position, e-> e instanceof Vein);
    }

    @Override
    protected void newEntity(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Point tgtPos) {
        Quake quake = new Quake(Functions.QUAKE_ID, tgtPos,
                imageStore.getImageList(Functions.QUAKE_KEY),
                Functions.QUAKE_ACTION_PERIOD, Functions.QUAKE_ANIMATION_PERIOD);

        world.addEntity(quake);
        quake.scheduleActions(scheduler, world, imageStore);

    }
}
