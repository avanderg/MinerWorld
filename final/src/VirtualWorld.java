import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import processing.core.*;

public final class VirtualWorld
    extends PApplet
{
    public static final int TIMER_ACTION_PERIOD = 100;

    public static final int VIEW_WIDTH = 640;
    public static final int VIEW_HEIGHT = 480;
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    public static final int WORLD_WIDTH_SCALE = 2;
    public static final int WORLD_HEIGHT_SCALE = 2;

    public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    public static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    public static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    public static final String IMAGE_LIST_FILE_NAME = "imagelist";
    public static final String DEFAULT_IMAGE_NAME = "background_default";
    public static final int DEFAULT_IMAGE_COLOR = 0x808080;

    public static final String LOAD_FILE_NAME = "gaia.sav";

    public static final String FAST_FLAG = "-fast";
    public static final String FASTER_FLAG = "-faster";
    public static final String FASTEST_FLAG = "-fastest";
    public static final double FAST_SCALE = 0.5;
    public static final double FASTER_SCALE = 0.25;
    public static final double FASTEST_SCALE = 0.10;

    public static double timeScale = 1.0;

    public ImageStore imageStore;
    public WorldModel world;
    public WorldView view;
    public EventScheduler scheduler;

    public long next_time;

    public void settings()
    {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
        Processing entry point for "sketch" setup.
    */
    public void setup()
    {
        this.imageStore = new ImageStore(
            createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
            createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
            TILE_WIDTH, TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
        registerMethod("mouseEvent", this);
    }

    public void mouseEvent(processing.event.MouseEvent event) {
        if (event.getAction() == processing.event.MouseEvent.PRESS) {
            int x = event.getX();
            int y = event.getY();
//            System.out.println(imageStore.getImages());

            Background dirtBackground = new Background("dirt", imageStore.getImageList("dirt"));
            int row = x/TILE_WIDTH;
            int col = y/TILE_HEIGHT;

            Viewport viewport = view.getViewport();
            Point center = viewport.viewportToWorld(row, col);
            world.changeBackground(dirtBackground, center, imageStore, scheduler);
            Wyvern wyvern = new Wyvern("1", center, imageStore.getImageList("wyvern"),
                    500, Functions.WYVERN_ANIMATION_PERIOD);
//            System.out.println(imageStore.getImageList("miner"));
//            System.out.println(imageStore.getImageList(Functions.BGND_KEY));
            world.tryAddEntity(wyvern);
            wyvern.scheduleActions(scheduler, world, imageStore);
//            System.out.println(imageStore.getImages());

//            System.out.println("Mouse pressed at " + x + ", " + y);
        }
    }

    public void draw()
    {
        long time = System.currentTimeMillis();
        if (time >= next_time)
        {
            scheduler.updateOnTime(time);
            next_time = time + TIMER_ACTION_PERIOD;
        }

        view.drawViewport();
    }

    public void keyPressed()
    {
        if (key == CODED)
        {
            int dx = 0;
            int dy = 0;

            switch (keyCode)
            {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            view.shiftView(dx, dy);
        }
    }

    private static Background createDefaultBackground(ImageStore imageStore)
    {
        return new Background(DEFAULT_IMAGE_NAME,
                imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    private static PImage createImageColored(int width, int height, int color)
    {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++)
        {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    private static void loadImages(String filename, ImageStore imageStore,
        PApplet screen)
    {
        try
        {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, screen);
        }
        catch (FileNotFoundException e)
        {
            System.err.println(e.getMessage());
        }
    }

    private static void loadWorld(WorldModel world, String filename,
        ImageStore imageStore)
    {
        try
        {
            Scanner in = new Scanner(new File(filename));
            Loader.load(in, world, imageStore);
        }
        catch (FileNotFoundException e)
        {
            System.err.println(e.getMessage());
        }
    }

    private static void scheduleActions(WorldModel world,
        EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.getEntities())
        {
            if (entity instanceof ActivityEntity) {
                ((ActivityEntity)entity).scheduleActions(scheduler, world, imageStore);
            }

        }
    }

    private static void parseCommandLine(String [] args)
    {
        for (String arg : args)
        {
            switch (arg)
            {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    public static void main(String [] args)
    {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
