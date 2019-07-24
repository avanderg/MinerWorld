import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerFull extends Miner{

    public MinerFull(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount,
                        int actionPeriod, int animationPeriod) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }


    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        Optional<Entity> fullTarget = world.findNearest(position, e -> e instanceof Blacksmith);

        if (fullTarget.isPresent() &&
                moveTo(world, fullTarget.get(), scheduler))

        {
            transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this, new ActivityAction(this, world, imageStore),
                    actionPeriod);
        }
    }


    @Override
    public boolean moveTo(WorldModel world,
                          Entity target, EventScheduler scheduler) {
        if (position.adjacent(target.getPosition()))
        {
            return true;
        }
        else
        {
            moveToHelper(world, target, scheduler);
            return false;
        }
    }

    private void transformFull(WorldModel world,
                               EventScheduler scheduler, ImageStore imageStore)
    {
        MinerNotFull miner = new MinerNotFull(id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);

        transformHelper(world, scheduler, imageStore, miner);
    }

}

