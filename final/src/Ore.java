import processing.core.PImage;
import java.util.List;

public class Ore extends ActivityEntity {

    public Ore(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images, actionPeriod);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        Point pos = position;    // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        OreBlob blob = new OreBlob(id + Functions.BLOB_ID_SUFFIX,
                pos,  imageStore.getImageList(Functions.BLOB_KEY),actionPeriod / Functions.BLOB_PERIOD_SCALE,
                Functions.BLOB_ANIMATION_MIN +
                        Functions.rand.nextInt(Functions.BLOB_ANIMATION_MAX - Functions.BLOB_ANIMATION_MIN));
//        System.out.println(imageStore.getImageList(Functions.BLOB_KEY));
        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }

}
