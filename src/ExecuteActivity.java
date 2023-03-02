public interface ExecuteActivity extends Scheduler {
    void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    double getActionPeriod();
    default void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Functions.createActivityAction(this, world, imageStore), getActionPeriod());
        Scheduler.super.scheduleActions(scheduler, world, imageStore);
    }

}
