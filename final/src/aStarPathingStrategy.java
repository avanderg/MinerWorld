import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
//import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class aStarPathingStrategy implements PathingStrategy {
    private List<Point> closedSet = new ArrayList<>();
    private List<Point> openSet = new ArrayList<>();
    private HashMap<Point, Point> cameFrom = new HashMap<>();
    private HashMap<Point, Integer> gScore = new HashMap<>();
    private HashMap<Point, Integer> fScore = new HashMap<>();
    private Integer inf = 100000;

    @Override
    public List<Point> computePath(
            final Point start, final Point end,
            Predicate<Point> canPassThrough,
            Function<Point, List<Point>> potentialNeighbors) {

        openSet.add(start);
        gScore.put(start, 0);

        Integer cost = hce(start, end);
        fScore.put(start, cost);

        while (!openSet.isEmpty()) {

            Point current = minimum();
            if (current.equals(end)) {

                List<Point> myList = reconstructPath(current);
                Collections.reverse(myList);
                myList.remove(0);
                return myList;
            }

            openSet.remove(current);
            closedSet.add(current);

            for (Point neighbor : potentialNeighbors.apply(current)) {
                boolean bool1 = !canPassThrough.test(neighbor);
                boolean bool2 = current==start;
//                if (bool2) {
//                    System.out.println("Current==start");
//                }
//                else {
//                    System.out.println("Current!=start");
//                }
//
//                if (bool1) {
//                    System.out.println("!canpassthrough(neighbor)");
//                }
//
//                else {
//                    System.out.println("canpassthrough(neighbor)");
//                }
//
//                System.out.println("bool1: " + bool1);
//                System.out.println("bool2: " + bool2);
//                System.out.println(bool1 && bool2);
//                if (bool1 && bool2) {
//                    System.out.println("interesting");
//                }
//
//                else {
//                    System.out.println("No obstacle...?");
//                }

                if ((!canPassThrough.test(neighbor)) && (current==start)) {
                    continue;

                }
                if (closedSet.contains(neighbor)) {
                    continue;
                }


                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }

                if (gScore.get(current)==null) {
                    gScore.put(current, inf);
                }

                int tentativeGScore = gScore.get(current) + hce(current, neighbor);

                if (gScore.get(neighbor)==null) {
                    gScore.put(neighbor, inf);
                }

                if (tentativeGScore >= gScore.get(neighbor)) {
                    continue;
                }
                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeGScore);
                fScore.put(neighbor, gScore.get(neighbor) + hce(neighbor, end));
            }
        }

        return null;

    }

    private Point minimum() {
        int min = inf;
        Point point = openSet.get(0);
        for (Point p : openSet) {
            if (fScore.get(p)==null) {
                fScore.put(p, inf);
            }

            Integer temp = fScore.get(p);
            if (temp < min) {
                point = p;
                min = temp;
            }
        }
        return point;
    }

    private List<Point> reconstructPath(Point current) {
        List<Point> totalPath= new ArrayList<>();
        totalPath.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.add(current);
        }
        return totalPath;
    }

    private Integer hce(Point start, Point end) {

        Integer dx = Math.abs(start.x - end.x);
        Integer dy = Math.abs(start.y - end.y);
        return dx + dy;
    }
}



