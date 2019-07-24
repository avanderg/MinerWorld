import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerNotFull extends Miner{

    public MinerNotFull(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount,
                        int actionPeriod, int animationPeriod) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        Optional<Entity> notFullTarget = world.findNearest(position, e -> e instanceof Ore);

        if (!notFullTarget.isPresent() ||
                !moveTo(world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    new ActivityAction(this, world, imageStore),
                    actionPeriod);
        }
    }

    @Override
    public boolean moveTo(WorldModel world,
                          Entity target, EventScheduler scheduler) {
        if (position.adjacent(target.getPosition()))
        {
            resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            moveToHelper(world, target, scheduler);
            return false;
        }
    }

    private boolean transformNotFull(WorldModel world,
                                     EventScheduler scheduler, ImageStore imageStore)
    {
        if (resourceCount >= resourceLimit)
        {
            MinerFull miner = new MinerFull(id, position, images,
                    resourceLimit, 0, actionPeriod, animationPeriod);
            transformHelper(world, scheduler, imageStore, miner);
            return true;
        }

        return false;
    }

}
