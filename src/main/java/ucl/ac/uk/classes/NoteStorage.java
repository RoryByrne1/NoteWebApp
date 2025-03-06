package ucl.ac.uk.classes;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
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
        File f = new File(filePath);
        if(f.exists() && !f.isDirectory())
        {
            this.categoryMap = loadNotes();
        }
        else
        {
            this.categoryMap = new HashMap<>();
            saveNotes();
        }
    }

    /*
    addCategory(String categoryName)
    deleteCategory(String categoryName)
    addNote(String category, Note note)
    deleteNote(String category, String noteTitle)
    editNote(String category, String noteTitle, Note updatedNote)
    editTitle(String category, String oldTitle, String newTitle)
    editCategoryName(String oldCategoryName, String newCategoryName)
    moveNote(String oldCategory, String newCategory, String noteTitle)
    moveBlock(String category, String noteTitle, int fromIndex, int toIndex)
     */

    public void editTitle(String category, String oldTitle, String newTitle)
    {
        if (!categoryMap.containsKey(category))
        {
            System.out.println("category not found");
            return;
        }

        Map<String, Note> notesInCategory = categoryMap.get(category);
        if (!notesInCategory.containsKey(oldTitle))
        {
            System.out.println("note not found");
            return;
        }

        Note note = notesInCategory.remove(oldTitle);  // remove old key
        note.setTitle(newTitle);  // update title in the note
        notesInCategory.put(newTitle, note);  // insert with new title

        saveNotes();
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

                    JSONArray blocksJson = (JSONArray) noteData.get("blocks");

                    if (blocksJson == null)
                    {
                        blocksJson = new JSONArray(); // Ensure it's not null
                    }

                    List<Block> blocks = new ArrayList<>();

                    for (Object blockObj : blocksJson)
                    {
                        JSONObject blockJson = (JSONObject) blockObj;
                        String type = (String) blockJson.get("type");

                        Block block;
                        if ("text".equals(type))
                        {
                            block = new TextBlock((String) blockJson.get("id"), (String) blockJson.get("text"));
                        }
                        else if ("image".equals(type))
                        {
                            block = new ImageBlock((String) blockJson.get("id"), (String) blockJson.get("imagePath"));
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