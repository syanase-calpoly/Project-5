/**
 * An action that can be taken by an entity
 */
public final class Action {
    private final ActionKind kind;
    private final Entity entity;
    private final WorldModel world;
    private final ImageStore imageStore;
    private final int repeatCount;

    public Action(ActionKind kind, Entity entity, WorldModel world, ImageStore imageStore, int repeatCount) {
        this.kind = kind;
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    private void executeAnimationAction(EventScheduler scheduler) {
        entity.nextImage();

        if (repeatCount != 1) {
            scheduler.scheduleEvent(entity, entity.createAnimationAction(Math.max(repeatCount - 1, 0)), entity.getAnimationPeriod());
        }
    }

    private void executeActivityAction(EventScheduler scheduler) {
        switch (entity.getKind()) {
            case SAPLING:
                entity.executeSaplingActivity(world, imageStore, scheduler);
                break;
            case TREE:
                entity.executeTreeActivity(world, imageStore, scheduler);
                break;
            case FAIRY:
                entity.executeFairyActivity(world, imageStore, scheduler);
                break;
            case DUDE_NOT_FULL:
                entity.executeDudeNotFullActivity(world, imageStore, scheduler);
                break;
            case DUDE_FULL:
                entity.executeDudeFullActivity(world, imageStore, scheduler);
                break;
            default:
                throw new UnsupportedOperationException(String.format("executeActivityAction not supported for %s", entity.getKind()));
        }
    }

    public void executeAction(EventScheduler scheduler) {
        switch (kind) {
            case ACTIVITY:
                this.executeActivityAction(scheduler);
                break;

            case ANIMATION:
                this.executeAnimationAction(scheduler);
                break;
        }
    }

}
