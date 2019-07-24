public class ActivityAction implements Action {
    private final ActivityEntity entity;
    private final WorldModel world;
    private final ImageStore imageStore;

    public ActivityAction(ActivityEntity entity, WorldModel world, ImageStore imageStore) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    @Override
    public void executeAction(EventScheduler scheduler) {
        executeActivityAction(scheduler);
    }

    private void executeActivityAction(EventScheduler scheduler) {
        entity.executeActivity(world, imageStore, scheduler);
    }
}
