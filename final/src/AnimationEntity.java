import processing.core.PImage;

import java.util.List;

public abstract class AnimationEntity extends ActivityEntity{

    int animationPeriod;

    public AnimationEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod() {
        return animationPeriod;
    }

    @Override
    public void scheduleActions( EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        super.scheduleActions(scheduler, world, imageStore);
        scheduler.scheduleEvent(this, new AnimationAction(this, 0),
                animationPeriod);
    }

}