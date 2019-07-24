import processing.core.PImage;

import java.util.*;

final class WorldModel
{
    private final int numRows;
    private final int numCols;
    private final Background background[][];
    private final Entity occupancy[][];
    private Set<Entity> entities;

    public WorldModel(int numRows, int numCols, Background defaultBackground)
    {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++)
        {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public Optional<Point> findOpenAround(Point pos)
    {
        for (int dy = -Functions.ORE_REACH; dy <= Functions.ORE_REACH; dy++)
        {
            for (int dx = -Functions.ORE_REACH; dx <= Functions.ORE_REACH; dx++)
            {
                Point newPt = new Point(pos.x + dx, pos.y + dy);
                if (withinBounds(newPt) &&
                        !isOccupied(newPt))
                {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }

    public boolean withinBounds(Point pos)
    {
        return pos.y >= 0 && pos.y < numRows &&
                pos.x >= 0 && pos.x < numCols;
    }

    public boolean isOccupied(Point pos)
    {
        return withinBounds(pos) &&
                getOccupancyCell(pos) != null;
    }

    private Optional<Entity> nearestEntity(List<Entity> entities,
                                                 Point pos)
    {
        if (entities.isEmpty())
        {
            return Optional.empty();
        }
        else
        {
            Entity nearest = entities.get(0);
            int nearestDistance = nearest.getPosition().distanceSquared(pos);

            for (Entity other : entities)
            {
                int otherDistance = other.getPosition().distanceSquared(pos);

                if (otherDistance < nearestDistance)
                {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    public Optional<Entity> oldfindNearest(Point pos, Class theClass)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : entities)
        {
            if (theClass.isInstance(entity))
            {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType, pos);
    }

    public Optional<Entity> findNearest(Point pos, java.util.function.Predicate<Entity> isTarget)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : entities)
        {
            if (isTarget.test(entity))
            {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType, pos);
    }


    public void addEntity(Entity entity)
    {
        if (withinBounds(entity.getPosition()))
        {
            setOccupancyCell(entity.getPosition(), entity);
            entities.add(entity);
        }
    }

    public void moveEntity(Entity entity, Point pos)
    {
        Point oldPos = entity.getPosition();
        if (this.withinBounds(pos) && !pos.equals(oldPos))
        {
            setOccupancyCell(oldPos, null);
            removeEntityAt(pos);
            setOccupancyCell(pos, entity);
            entity.setPosition(pos);
        }
    }

    public void removeEntity(Entity entity)

    {
        removeEntityAt(entity.getPosition());
    }

    private void removeEntityAt(Point pos)
    {
        if (withinBounds(pos)
                && getOccupancyCell(pos) != null)
        {
            Entity entity = getOccupancyCell(pos);

            /* this moves the entity just outside of the grid for
                debugging purposes */
            entity.setPosition(new Point(-1, -1));
            entities.remove(entity);
            setOccupancyCell(pos, null);
        }
    }

    public Optional<PImage> getBackgroundImage(Point pos)
    {
        if (withinBounds(pos))
        {
            return Optional.of(Background.getCurrentImage(getBackgroundCell(pos)));
        }
        else
        {
            return Optional.empty();
        }
    }

    public void setBackground(Point pos,
                                     Background background)
    {
        if (withinBounds(pos))
        {
            setBackgroundCell(pos, background);
        }
    }


    public Optional<Entity> getOccupant(Point pos)
    {
        if (isOccupied(pos))
        {
            return Optional.of(getOccupancyCell(pos));
        }
        else
        {
            return Optional.empty();
        }
    }

    private Entity getOccupancyCell(Point pos)
    {
        return occupancy[pos.y][pos.x];
    }

    private void setOccupancyCell(Point pos, Entity entity)
    {
        occupancy[pos.y][pos.x] = entity;
    }

    private Background getBackgroundCell(Point pos)
    {
        return background[pos.y][pos.x];
    }

    private void setBackgroundCell(Point pos, Background background)
    {
        this.background[pos.y][pos.x] = background;
    }

    public void changeBackground(Background newB, Point center, ImageStore imageStore, EventScheduler scheduler) {
        Point [] points = new Point[]{
                center,
                center.shift(1, 0),
                center.shift(1, 1),
                center.shift(0, 1),
                center.shift(-1, 1),
                center.shift(-1, 0),
                center.shift(-1, -1),
                center.shift(0, -1),
                center.shift(1, -1)
        };

        for (Point p : points) {
            this.setBackground(p, newB);
            Optional<Entity> occupant = getOccupant(p);
            if (occupant.isPresent()) {
                Entity entity = occupant.get();
                if (entity instanceof Miner) {
                    removeEntity(entity);
                    scheduler.unscheduleAllEvents(entity);
                    Freeze freeze = new Freeze("freeze", p, imageStore.getImageList("freeze"), 900,
                            100);
                    addEntity(freeze);
                    freeze.scheduleActions(scheduler, this, imageStore);
                }
            }
        }


    }
}
