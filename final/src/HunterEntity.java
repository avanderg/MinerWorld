import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class HunterEntity extends MoveEntity {

    public HunterEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        Optional<Entity> target = calcTarget(world);
        long nextPeriod = actionPeriod;

        if (target.isPresent())
        {
            Point tgtPos = target.get().getPosition();

            if (moveTo(world, target.get(), scheduler))
            {
                newEntity(world, imageStore, scheduler, tgtPos);
                nextPeriod += actionPeriod;
            }
        }

        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                nextPeriod);
    }

    protected abstract Optional<Entity> calcTarget(WorldModel world);
    protected abstract void newEntity(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Point tgtPos);


    @Override
    public boolean moveTo(WorldModel world,
                          Entity target, EventScheduler scheduler)
    {
        if (position.adjacent(target.getPosition()))
        {
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

    @Override
    public boolean occupantBlocks(Optional<Entity> occupant) {
        return occupant.isPresent() && !(Ore.class.isInstance(occupant));
    }

    protected abstract List <Point> calcPotentialNeighbors(Point p);

}
