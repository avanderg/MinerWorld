import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

interface PathingStrategy
{
    /**
     * Returns a prefix of a path from the start point to a point within reach
     * of the end point.  This path is valid ("clear") when returned, but
     * may be invalidated in the future by the movement of other entities.
     * <p>
     * The returned value doesn't include the start point or the end point.
     *
     * @param start   The start point for the path.
     * @param end     The goal, the end point for the path.
     * @param canPassThrough      A function that returns true if the
     *                            given point isn't blocked.
     * @param potentialNeighbors  A function that gives the potential neighbors
     *                            for a given point.  This is just the set
     *                            of points an entity could move to assuming
     *                            nothing were blocked.
     */
    List<Point> computePath(Point start, Point end,
                            Predicate<Point> canPassThrough,
                            Function<Point, List<Point>> potentialNeighbors);
}
