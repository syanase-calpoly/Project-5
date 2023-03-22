import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface Dude extends Move {
    default Point nextPosition(WorldModel world, Point destPos) {
        PathingStrategy strat = new AStarPathingStrategy();
        Predicate<Point> canPassThrough = p -> world.withinBounds(p) && (!world.isOccupied(p) || world.getOccupancyCell(p).getClass() == Stump.class);
        BiPredicate<Point, Point> withinReach = Point::adjacent;
        List<Point> path = strat.computePath(
                getPosition(),
                destPos,
                canPassThrough,
                withinReach,
                PathingStrategy.CARDINAL_NEIGHBORS
        );

        if (path.size() == 0) {
            return getPosition();
        }
        return path.get(0);
//        int horiz = Integer.signum(destPos.x - getPosition().x);
//        Point newPos = new Point(getPosition().x + horiz, getPosition().y);
//
//        if (horiz == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != Stump.class) {
//            int vert = Integer.signum(destPos.y - getPosition().y);
//            newPos = new Point(getPosition().x, getPosition().y + vert);
//
//            if (vert == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != Stump.class) {
//                newPos = getPosition();
//            }
//        }
//
//        return newPos;
    }

    void transformBarbarian(WorldModel world, EventScheduler scheduler, ImageStore imageStore);
}
