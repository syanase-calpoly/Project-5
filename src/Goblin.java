import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Goblin implements Move {

    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final double actionPeriod;
    private final double animationPeriod;

    public Goblin(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;

    }

    public Point nextPosition(WorldModel world, Point destPos) {
        PathingStrategy strat = new AStarPathingStrategy();
        Predicate<Point> canPassThrough = p -> world.withinBounds(p) && !world.isOccupied(p);
        BiPredicate<Point, Point> withinReach = Point::adjacent;
        List<Point> path = strat.computePath(
                position,
                destPos,
                canPassThrough,
                withinReach,
                PathingStrategy.CARDINAL_NEIGHBORS
        );

        if (path.size() == 0) {
            return getPosition();
        }
        return path.get(0);
        //        int horiz = Integer.signum(destPos.x - position.x);
//        Point newPos = new Point(position.x + horiz, position.y);
//
//        if (horiz == 0 || world.isOccupied(newPos)) {
//            int vert = Integer.signum(destPos.y - position.y);
//            newPos = new Point(position.x, position.y + vert);
//
//            if (vert == 0 || world.isOccupied(newPos)) {
//                newPos = position;
//            }
//        }
//
//        return newPos;


    }

    public double getAnimationPeriod() {
        return animationPeriod;
    }

    public void nextImage() {
        imageIndex = imageIndex + 1;
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (Functions.adjacent(position, target.getPosition())) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest(position, new ArrayList<>(List.of(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (this.moveTo(world, fairyTarget.get(), scheduler)) {

                Scheduler sapling = Functions.createSapling(Functions.SAPLING_KEY + "_" + fairyTarget.get().getId(), tgtPos, imageStore.getImageList(Functions.SAPLING_KEY), 0);

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this, Functions.createActivityAction(this,world, imageStore), actionPeriod);
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
