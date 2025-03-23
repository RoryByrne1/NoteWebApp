package ucl.ac.uk.classes;

import java.util.Map;

public class ImageBlock extends Block
{
    private String imagePath;

    public ImageBlock()
    {
        super(generateBlockID());
        imagePath = "";
    }

    public ImageBlock(String id, String imagePath)
    {
        super(id);
        this.imagePath = imagePath;
    }

    public boolean search(String query)
    {
        return imagePath.contains(query);
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
                "id", id,
                "type", "image",
                "imagePath", imagePath
        );
    }
}
