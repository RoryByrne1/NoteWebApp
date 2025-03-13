package ucl.ac.uk.classes;

import java.util.Map;

public class ImageBlock extends Block
{
    private String imagePath;

    public ImageBlock(String imagePath)
    {
        super(generateBlockID());
        this.imagePath = imagePath;
    }

    public ImageBlock(String id, String imagePath)
    {
        super(id);
        this.imagePath = imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    @Override
    public Map<String, Object> toJson()
    {
        return Map.of(
                "id", getId(),
                "type", "image",
                "imagePath", imagePath
        );
    }
}
