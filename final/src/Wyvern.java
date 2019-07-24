import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Wyvern extends HunterEntity {

    public Wyvern(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    @Override
    protected List <Point> calcPotentialNeighbors(Point p) {
        return new ArrayList<>(Arrays.asList(new Point(p.x+1, p.y),
                new Point(p.x, p.y+1),
                new Point(p.x-1, p.y),
                new Point(p.x, p.y-1),
                new Point (p.x+1, p.y+1),
                new Point(p.x-1, p.y+1),
                new Point(p.x-1, p.y-1),
                new Point(p.x+1, p.y-1)));
    }

    @Override
    protected Optional<Entity> calcTarget(WorldModel world) {
        return world.findNearest(position, e-> e instanceof Miner);
    }

    @Override
    protected void newEntity(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Point tgtPos) {
        Ash ash = new Ash(Functions.ASH_ID, new Point(tgtPos.x-4, tgtPos.y-3),
                    imageStore.getImageList(Functions.ASH_KEY));
        world.addEntity(ash);


    }
}
