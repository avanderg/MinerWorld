public class AnimationAction implements Action {
    private final AnimationEntity entity;
    private final int repeatCount;

    public AnimationAction(AnimationEntity entity, int repeatCount) {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    @Override
    public void executeAction(EventScheduler scheduler) {
        executeAnimationAction(scheduler);
    }

    private void executeAnimationAction(EventScheduler scheduler) {
        entity.nextImage();

        if (repeatCount != 1)
        {
            scheduler.scheduleEvent(entity,
                    new AnimationAction(entity,
                            Math.max(repeatCount - 1, 0)),
                    entity.getAnimationPeriod());
        }
    }
}
