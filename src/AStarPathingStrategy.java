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
        HashSet<Node> openSet = new HashSet<>();
        Node currentNode = new Node(0, start.distance(end), start.distance(end), null, start);
        openList.add(currentNode);
        openSet.add(currentNode);
        while (!openList.isEmpty() && !withinReach.test(currentNode.getPoint(), end)) {
            System.out.println(currentNode.getPoint().x + " " + currentNode.getPoint().y);
            currentNode = openList.poll();
            openSet.remove(currentNode);
            List <Point> neighbors = potentialNeighbors.apply(currentNode.getPoint()).filter(canPassThrough)
                    .filter(pt ->
                            !pt.equals(start)
                                    && !pt.equals(end)).filter(p -> !closedSet.contains(p))
                    .collect(Collectors.toList());

            for (Point point : neighbors) {

                int g = currentNode.getG() + 1;
                int h = point.distance(end);

                Node nextNode = new Node(g, h, g+h, currentNode, point);
                if (!openList.contains(nextNode)) {
                    openList.add(nextNode);
                    openSet.add(nextNode);
                } else {

                    if (nextNode.getG() < currentNode.getG()) {
                        openList.remove(currentNode);
                        openSet.remove(currentNode);
                        openList.add(nextNode);
                        openSet.add(nextNode);

                    }

                }


            }
            closedSet.add(currentNode.getPoint());

        }
        while (currentNode != null) {
            if (currentNode.getPrev() == null) {
                break;
            }
            path.add(0, currentNode.getPoint());
            currentNode = currentNode.getPrev();
        }
        return path;
    }

}



