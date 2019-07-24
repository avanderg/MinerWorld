import processing.core.PImage;

import java.util.List;

public abstract class ActivityEntity extends Entity{
    int actionPeriod;
    public ActivityEntity(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }
    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                actionPeriod);
    }
}
