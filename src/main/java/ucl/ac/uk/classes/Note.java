package ucl.ac.uk.classes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Note
{
    private String title;
    private List<Block> blocks;
    final private String createdAt;
    private String lastEdited;

    // new note
    public Note(String title)
    {
        this.title = title;
        this.blocks = new ArrayList<>();
        this.createdAt = generateTimeStamp();
        this.lastEdited = generateTimeStamp();
    }

    // loading old note
    public Note(String title, List<Block> blocks, String createdAt, String lastEdited)
    {
        this.title = title;
        this.blocks = blocks;
        this.createdAt = createdAt;
        this.lastEdited = lastEdited;
    }

    private static String generateTimeStamp()
    {
        return Instant.now().toString();
    }

    public void addContent(Block block)
    {
        blocks.add(block);
        this.lastEdited =  generateTimeStamp();
    }

    public void setTitle(String title)
    {
        this.title = title;
        this.lastEdited = generateTimeStamp();
    }

    public void setContentList(List<Block> contentList) // add contentList?
    {
        this.blocks = contentList;
        this.lastEdited = generateTimeStamp();
    }

    public String getTitle()
    {
        return title;
    }

    public List<Block> getContentList()
    {
        return blocks;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }

    public String getLastEdited()
    {
        return lastEdited;
    }

    //  jsonify note contents (name is handled outside)
    public Map<String, Object> toJson()
    {
        return Map.of(
                "blocks", blocks.stream().map(Block::toJson).collect(Collectors.toList()),
                "createdAt", createdAt,
                "lastEdited", lastEdited
        );
    }
}
