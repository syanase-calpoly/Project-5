import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public class Dude_Full implements Dude {
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final int resourceLimit;
    private final double actionPeriod;
    private final double animationPeriod;

    public Dude_Full(String id, Point position, List<PImage> images, int resourceLimit, double actionPeriod, double animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public double getAnimationPeriod() {
        return animationPeriod;
    }

    public void nextImage() {
        imageIndex = imageIndex + 1;
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (Functions.adjacent(position, target.getPosition())) {
            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    private void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Scheduler dude = Functions.createDudeNotFull(id, position, actionPeriod, animationPeriod, resourceLimit, images);

        world.removeEntity(scheduler, this);

        world.addEntity(dude);
        dude.scheduleActions(scheduler, world, imageStore);
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(position, new ArrayList<>(List.of(House.class)));

        if (fullTarget.isPresent() && this.moveTo(world, fullTarget.get(), scheduler)) {
            this.transformFull(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this, Functions.createActivityAction(this, world, imageStore), actionPeriod);
        }
    }
    public  PImage getCurrentImage() {
        return this.images.get(this.imageIndex % this.images.size());
    }

    public String getId() {
        return id;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point point) {
        this.position = point;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public double getActionPeriod() {
        return actionPeriod;
    }
}
