public interface Scheduler extends Entity {
    double getAnimationPeriod();
    default void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Functions.createAnimationAction(this,0), this.getAnimationPeriod());
    }

}
