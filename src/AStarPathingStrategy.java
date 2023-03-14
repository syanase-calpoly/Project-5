import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        List<Point> path = new ArrayList<>();
        Comparator<Node> compareNode = new CompareNode();
        PriorityQueue<Node> openList = new PriorityQueue<>(compareNode);
        HashSet<Point> closedSet = new HashSet<>();
        HashSet<Point> openSet = new HashSet<>();
        Node currentNode = new Node(0, start.distance(end), start.distance(end), null, start);
        openList.add(currentNode);
        openSet.add(currentNode.getPoint());
        while (!openList.isEmpty()) {
            currentNode = openList.poll();
            openSet.remove(currentNode.getPoint());

            if (withinReach.test(currentNode.getPoint(), end)) {
                while (currentNode != null) {
                    if (currentNode.getPrev() == null) {
                        break;
                    }
                    path.add(0, currentNode.getPoint());
                    currentNode = currentNode.getPrev();
                }
                break;
            }
            List <Point> neighbors = potentialNeighbors.apply(currentNode.getPoint()).filter(canPassThrough)
                    .filter(pt ->
                            !pt.equals(start)
                                    && !pt.equals(end)).filter(p -> !closedSet.contains(p))
                    .collect(Collectors.toList());

            for (Point point : neighbors) {
                if (!closedSet.contains(point)) {

                    int g = currentNode.getG() + 1;

                    int h = point.distance(end);
                    int f = h + g;
                    Node nextNode = new Node(g, h, f, currentNode, point);


                    if (!openSet.contains(point)) {
                        openList.add(nextNode);
                        openSet.add(point);
                    } else {

                        if (nextNode.getG() < currentNode.getG()) {
                            openList.remove(currentNode);
                            openSet.remove(point);
                            openList.add(nextNode);
                            openSet.add(point);

                        }

                    }
                }


            }
            closedSet.add(currentNode.getPoint());

        }

        return path;
    }

}