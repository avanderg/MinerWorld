import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class Miner extends MoveEntity{

    protected final int resourceLimit;
    protected int resourceCount;

    public Miner(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount,
                 int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

//    @Override
//    public Point nextPosition(WorldModel world, Point destPos) {
//        int horiz = Integer.signum(destPos.x - position.x);
//        Point newPos = new Point(position.x + horiz,
//                position.y);
//        Optional<Entity> occupant = world.getOccupant(newPos);
//
//        if (horiz == 0 || occupant.isPresent())
//        {
//            int vert = Integer.signum(destPos.y - position.y);
//            newPos = new Point(position.x,
//                    position.y + vert);
//            occupant = world.getOccupant(newPos);
//
//            if (vert == 0 || occupant.isPresent())
//            {
//                newPos = position;
//            }
//        }

//        return newPos;
//    }

    @Override
    public boolean occupantBlocks(Optional<Entity> occupant) {
        return occupant.isPresent();
    }

    protected void transformHelper(WorldModel world,
                                   EventScheduler scheduler, ImageStore imageStore, Miner miner) {
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    @Override
    protected List <Point> calcPotentialNeighbors(Point p) {
        return new ArrayList<>(Arrays.asList(new Point(p.x+1, p.y),
                new Point(p.x, p.y+1),
                new Point(p.x-1, p.y),
                new Point(p.x, p.y-1)));
    }

}
