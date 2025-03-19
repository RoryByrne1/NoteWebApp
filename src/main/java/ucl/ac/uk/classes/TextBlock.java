package ucl.ac.uk.classes;

import java.util.Map;

public class TextBlock extends Block
{
    private String text;

    public TextBlock()
    {
        super(generateBlockID());
        this.text = "";
    }

    public TextBlock(String id, String text)
    {
        super(id);
        this.text = text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    @Override
    public Map<String, Object> toJson()
    {
        return Map.of(
                "id", id,
                "type", "text",
                "text", text
        );
    }
}
