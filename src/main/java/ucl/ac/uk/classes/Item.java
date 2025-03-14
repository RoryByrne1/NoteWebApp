package ucl.ac.uk.classes;

import java.time.Instant;
import java.util.Map;

public abstract class Item
{
    public final String id;
    public String name;
    public final String createdAt;
    public String lastEdited;

    // MAYBE ADD PATH?????

    public Item(String name)
    {
        this.name = name;
        this.id = generateId();
        this.createdAt = generateTimeStamp();
        this.lastEdited = generateTimeStamp();
    }

    public Item(String id, String name, String createdAt, String lastEdited)
    {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.lastEdited = lastEdited;
    }

    public static String generateTimeStamp()
    {
        return Instant.now().toString();
    }

    public abstract String generateId();

    public abstract Map<String, Object> toJson();

    public void updateLastEdited() { lastEdited = generateTimeStamp(); }

    public boolean isLessThan(Item other, String sortBy, Boolean ascending)
    {
        boolean result = switch (sortBy) {
            case "name" -> name.compareToIgnoreCase(other.getName()) < 0;
            case "createdAt" -> createdAt.compareTo(other.getCreatedAt()) < 0;
            case "lastEdited" -> lastEdited.compareTo(other.getLastEdited()) < 0;
            default -> throw new IllegalArgumentException("invalid sortBy parameter: " + sortBy);
        };
        return ascending == result;
    }

    public String getId() { return id; }

    public String getName() { return name; }

    public void setName(String name)
    {
        this.name = name;
        updateLastEdited();
    }

    public String getCreatedAt() { return createdAt; }

    public String getLastEdited() { return lastEdited; }
}
