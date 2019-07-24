import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class MoveEntity extends AnimationEntity {

    public MoveEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

    abstract boolean occupantBlocks(Optional<Entity> occupant);

    private Point oldNextPosition(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - position.x);
        Point newPos = new Point(position.x + horiz,
                position.y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 || occupantBlocks(occupant))
        {
            int vert = Integer.signum(destPos.y - position.y);
            newPos = new Point(position.x, position.y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 || occupantBlocks(occupant))
            {
                newPos = position;
            }
        }

        return newPos;
    }


    private Point nextPosition(WorldModel world, Point destPos) {
//        PathingStrategy strat = new SingleStepPathingStrategy();
        PathingStrategy strat = new aStarPathingStrategy();
        Point currentPos = position;
        ArrayList<Point> path = (ArrayList<Point>) strat.computePath(currentPos,
                destPos, p -> (world.withinBounds(p) &&
                        !occupantBlocks(world.getOccupant(p))),
                p -> calcPotentialNeighbors(p));

        if (path==null){
            return currentPos;
        }

        if (path.isEmpty()) {
            return currentPos;
        }

        else {
            return path.get(0);
        }
    }

    protected abstract List<Point> calcPotentialNeighbors(Point p);

    protected void moveToHelper(WorldModel world, Entity target, EventScheduler scheduler) {
        Point nextPos = this.nextPosition(world, target.getPosition());

        if (!position.equals(nextPos)) {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent()) {
                scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
        }
    }
}
