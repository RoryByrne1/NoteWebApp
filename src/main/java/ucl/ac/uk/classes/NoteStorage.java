package ucl.ac.uk.classes;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class NoteStorage
{
    private final String filePath;
    private Map<String, Map<String, Note>> categoryMap;

    public NoteStorage(String filePath)
    {
        this.filePath = filePath;
        this.categoryMap = loadNotes();
    }

    public Map<String, Map<String, Note>> getCategoryMap()
    {
        return categoryMap;
    }

    private Map<String, Map<String, Note>> loadNotes()
    {
        Map<String, Map<String, Note>> map = new HashMap<>();

        try
        {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(filePath));

            for (Object categoryKey : jsonObject.keySet())
            {
                String category = (String) categoryKey;
                JSONObject categoryNotes = (JSONObject) jsonObject.get(category);
                Map<String, Note> notes = new HashMap<>();

                for (Object noteKey : categoryNotes.keySet())
                {
                    String noteTitle = (String) noteKey;
                    JSONObject noteData = (JSONObject) categoryNotes.get(noteTitle);

                    String createdAt = (String) noteData.get("createdAt");
                    String lastEdited = (String) noteData.get("lastEdited");

                    // Load blocks
                    JSONObject blocksJson = (JSONObject) noteData.get("blocks");
                    List<Block> blocks = new ArrayList<>();

                    for (Object blockId : blocksJson.keySet())
                    {
                        JSONObject blockJson = (JSONObject) blocksJson.get(blockId);
                        String type = (String) blockJson.get("type");

                        Block block;
                        if ("text".equals(type))
                        {
                            block = new TextBlock((String) blockId, (String) blockJson.get("text"));
                        }
                        else if ("image".equals(type))
                        {
                            block = new ImageBlock((String) blockId, (String) blockJson.get("imagePath"));
                        }
                        else
                        {
                            continue; // Ignore unknown block types
                        }

                        blocks.add(block);
                    }

                    notes.put(noteTitle, new Note(noteTitle, blocks, createdAt, lastEdited));
                }

                map.put(category, notes);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return map;
    }

    public void saveNotes()
    {
        JSONObject jsonObject = new JSONObject();

        for (String category : categoryMap.keySet())
        {
            JSONObject categoryNotes = new JSONObject();
            Map<String, Note> notes = categoryMap.get(category);

            for (String noteTitle : notes.keySet())
            {
                categoryNotes.put(noteTitle, notes.get(noteTitle).toJson());
            }

            jsonObject.put(category, categoryNotes);
        }

        try (FileWriter file = new FileWriter(filePath))
        {
            file.write(jsonObject.toJSONString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}