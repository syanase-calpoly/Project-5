/**
 * An action that can be taken by an entity
 */
public final class Activity implements Action {
    private final ExecuteActivity entity;
    private final WorldModel world;
    private final ImageStore imageStore;

    public Activity(ExecuteActivity entity, WorldModel world, ImageStore imageStore) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler) {
        entity.executeActivity(world, imageStore, scheduler);
    }
}
