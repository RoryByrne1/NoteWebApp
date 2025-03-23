package ucl.ac.uk.classes;

import java.util.Map;

public class URLBlock extends Block
{
    private String url;

    public URLBlock()
    {
        super(generateBlockID());
        url = "";
    }

    public URLBlock(String id, String url)
    {
        super(id);
        this.url = url;
    }

    public boolean search(String query)
    {
        return url.contains(query);
    }

    public void setURL(String url)
    {
        this.url = url;
    }

    public String getURL()
    {
        return url;
    }

    @Override
    public Map<String, Object> toJson()
    {
        return Map.of(
                "id", id,
                "type", "url",
                "url", url
        );
    }
}
