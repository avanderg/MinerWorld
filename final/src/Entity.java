import processing.core.PImage;

import java.util.List;

public abstract class Entity {

    protected final String id;
    protected Point position;
    protected final  List<PImage> images;
    protected int imageIndex;


    public Entity(String id, Point position,
                      List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public List<PImage> getImages()
    {
        return images;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public Point getPosition() {
        return position;
    }


    public void nextImage() {
        imageIndex = (imageIndex + 1) % images.size();
    }

    public void setPosition(Point p) {
        position = p;
    }

    public PImage getCurrentImage()
    {
        return images.get(imageIndex);

    }
}
