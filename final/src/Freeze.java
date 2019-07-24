import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Freeze extends HunterEntity {

    public Freeze(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
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
        return world.findNearest(position, e-> e instanceof OreBlob);
    }

    @Override
    protected void newEntity(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Point tgtPos) {
//        Santa santa = new Santa("santa", new Point(tgtPos.x-4, tgtPos.y-4),
//                imageStore.getImageList("santa"));
//        world.addEntity(santa);

        MinerNotFull miner= new MinerNotFull("1", tgtPos, imageStore.getImageList(Functions.MINER_KEY),
                Functions.MINER_LIMIT, 0, 992, 100);
//        System.out.println(imageStore.getImageList(Functions.BLOB_KEY));
        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

}
