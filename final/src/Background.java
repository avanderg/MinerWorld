import java.util.List;
import processing.core.PImage;
final class Background
{
    private final String id;
    private final List<PImage> images;
    private final int imageIndex;

    public Background(String id, List<PImage> images)
    {
        this.id = id;
        this.images = images;
        this.imageIndex = 0;
    }

    public static PImage getCurrentImage(Object entity)
    {
        if (entity instanceof Background)
        {
            return ((Background)entity).images
                    .get(((Background)entity).imageIndex);
        }
        else if (entity instanceof Entity)
        {
            return ((Entity)entity).getImages().get(((Entity)entity).getImageIndex());
        }
        else
        {
            throw new UnsupportedOperationException(
                    String.format("getCurrentImage not supported for %s",
                            entity));
        }
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public List<PImage> getImages() {
        return images;
    }
}
