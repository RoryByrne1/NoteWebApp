package ucl.ac.uk.classes;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Map;

public abstract class Item
{
    public final String id;
    public String name;
    public final String createdAt;
    public String lastEdited;

    public Item(String name)
    {
        this.name = name;
        id = generateId();
        createdAt = generateTimeStamp();
        lastEdited = generateTimeStamp();
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

    public static String readableDate(String isoDate, boolean longDate) {
        try {
            Instant instant = Instant.parse(isoDate);
            DateTimeFormatter formatter;
            if (longDate)
                formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm:ss")
                        .withLocale(Locale.UK)
                        .withZone(ZoneId.systemDefault());
            else
                 formatter = DateTimeFormatter.ofPattern("dd/MM/yy")
                         .withLocale(Locale.UK)
                         .withZone(ZoneId.systemDefault());

            return formatter.format(instant);
        } catch (Exception e) {
            return "invalid date";
        }
    }

    public String getCreatedAtReadable(boolean longDate) { return readableDate(createdAt, longDate); }

    public String getLastEditedReadable(boolean longDate) { return readableDate(lastEdited, longDate); }
}
