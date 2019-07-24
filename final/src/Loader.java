import java.util.Scanner;

public class Loader {
    public static void load(Scanner in, WorldModel world, ImageStore imageStore)
    {
        int lineNumber = 0;
        while (in.hasNextLine())
        {
            try
            {
                if (!processLine(in.nextLine(), world, imageStore))
                {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            }
            catch (NumberFormatException e)
            {
                System.err.println(String.format("invalid entry on line %d",
                        lineNumber));
            }
            catch (IllegalArgumentException e)
            {
                System.err.println(String.format("issue on line %d: %s",
                        lineNumber, e.getMessage()));
            }
            lineNumber++;
        }
    }

    private static boolean processLine(String line, WorldModel world,
                                       ImageStore imageStore)
    {
        String[] properties = line.split("\\s");
        if (properties.length > 0)
        {
            switch (properties[Functions.PROPERTY_KEY])
            {
                case Functions.BGND_KEY:
                    return parseBackground(properties, world, imageStore);
                case Functions.MINER_KEY:
                    return parseMiner(properties, world, imageStore);
                case Functions.OBSTACLE_KEY:
                    return parseObstacle(properties, world, imageStore);
                case Functions.ORE_KEY:
                    return parseOre(properties, world, imageStore);
                case Functions.SMITH_KEY:
                    return parseSmith(properties, world, imageStore);
                case Functions.VEIN_KEY:
                    return parseVein(properties, world, imageStore);
            }
        }

        return false;
    }

    private static boolean parseBackground(String [] properties,
                                           WorldModel world, ImageStore imageStore)
    {
        if (properties.length == Functions.BGND_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[Functions.BGND_COL]),
                    Integer.parseInt(properties[Functions.BGND_ROW]));
            String id = properties[Functions.BGND_ID];
            world.setBackground(pt,
                    new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == Functions.BGND_NUM_PROPERTIES;
    }

    private static boolean parseMiner(String [] properties, WorldModel world,
                                      ImageStore imageStore)
    {
        if (properties.length == Functions.MINER_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[Functions.MINER_COL]),
                    Integer.parseInt(properties[Functions.MINER_ROW]));
            MinerNotFull entity = new MinerNotFull(properties[Functions.MINER_ID], pt,
                    imageStore.getImageList(Functions.MINER_KEY),
                    Integer.parseInt(properties[Functions.MINER_LIMIT]), 0,
                    Integer.parseInt(properties[Functions.MINER_ACTION_PERIOD]),
                    Integer.parseInt(properties[Functions.MINER_ANIMATION_PERIOD]));
            world.addEntity(entity);
        }

        return properties.length == Functions.MINER_NUM_PROPERTIES;
    }

    private static boolean parseObstacle(String [] properties, WorldModel world,
                                         ImageStore imageStore)
    {
        if (properties.length == Functions.OBSTACLE_NUM_PROPERTIES)
        {
            Point pt = new Point(
                    Integer.parseInt(properties[Functions.OBSTACLE_COL]),
                    Integer.parseInt(properties[Functions.OBSTACLE_ROW]));
            Obstacle entity = new Obstacle(properties[Functions.OBSTACLE_ID],
                    pt, imageStore.getImageList(Functions.OBSTACLE_KEY));
            world.addEntity(entity);
        }

        return properties.length == Functions.OBSTACLE_NUM_PROPERTIES;
    }

    private static boolean parseOre(String [] properties, WorldModel world,
                                    ImageStore imageStore)
    {
        if (properties.length == Functions.ORE_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[Functions.ORE_COL]),
                    Integer.parseInt(properties[Functions.ORE_ROW]));
            Ore entity = new Ore(properties[Functions.ORE_ID],
                    pt,  imageStore.getImageList(Functions.ORE_KEY),
                    Integer.parseInt(properties[Functions.ORE_ACTION_PERIOD]));
            world.addEntity(entity);
        }

        return properties.length == Functions.ORE_NUM_PROPERTIES;
    }

    private static boolean parseSmith(String [] properties, WorldModel world,
                                      ImageStore imageStore)
    {
        if (properties.length == Functions.SMITH_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[Functions.SMITH_COL]),
                    Integer.parseInt(properties[Functions.SMITH_ROW]));
            Blacksmith entity = new Blacksmith(properties[Functions.SMITH_ID],
                    pt, imageStore.getImageList(Functions.SMITH_KEY));
            world.addEntity(entity);
        }

        return properties.length == Functions.SMITH_NUM_PROPERTIES;
    }

    private static boolean parseVein(String [] properties, WorldModel world,
                                     ImageStore imageStore)
    {
        if (properties.length == Functions.VEIN_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[Functions.VEIN_COL]),
                    Integer.parseInt(properties[Functions.VEIN_ROW]));
            Vein entity = new Vein(properties[Functions.VEIN_ID],
                    pt,imageStore.getImageList(Functions.VEIN_KEY),
                    Integer.parseInt(properties[Functions.VEIN_ACTION_PERIOD]));
            world.addEntity(entity);
        }

        return properties.length == Functions.VEIN_NUM_PROPERTIES;
    }
}
