import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Vein extends ActivityEntity {

    public Vein(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images, actionPeriod);
    }


    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        Optional<Point> openPt = world.findOpenAround(position);

        if (openPt.isPresent())
        {
            Ore ore = new Ore(Functions.ORE_ID_PREFIX + id,
                    openPt.get(), imageStore.getImageList(Functions.ORE_KEY), Functions.ORE_CORRUPT_MIN +
                    Functions.rand.nextInt(Functions.ORE_CORRUPT_MAX - Functions.ORE_CORRUPT_MIN));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduleActions(scheduler, world, imageStore);
    }

}
