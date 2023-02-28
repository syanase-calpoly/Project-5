public interface Scheduler extends Entity {
    double getAnimationPeriod();
    void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

}
