import processing.core.PImage;

import java.util.List;

public class Quake extends AnimationEntity {

public Quake(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }


    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }
}
