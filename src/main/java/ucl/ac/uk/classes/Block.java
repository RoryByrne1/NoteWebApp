package ucl.ac.uk.classes;

import java.time.Instant;
import java.util.Map;

public abstract class Block
{
    private final String id;

    public Block(String id)
    {
        this.id = id;
    }

    public static String generateBlockID()
    {
        return "b" + Instant.now().toEpochMilli();
    }

    public String getId()
    {
        return id;
    }

    public abstract Map<String, Object> toJson();
}
