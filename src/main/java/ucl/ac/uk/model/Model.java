package ucl.ac.uk.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ucl.ac.uk.classes.Block;
import ucl.ac.uk.classes.ImageBlock;
import ucl.ac.uk.classes.Note;
import ucl.ac.uk.classes.TextBlock;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

// ==============================================
// ADD saveNotes() TO THE END OF EACH METHOD?????
// ==============================================

public class Model {
    final String jsonFilePath = "data/notes.json";
    private Map<String, Map<String, Note>> categoryMap;

    public Model()
    {
        File f = new File(jsonFilePath);
        if (f.exists() && !f.isDirectory())
        {
            this.categoryMap = loadNotes();
        }
        else
        {
            this.categoryMap = new HashMap<>();
            saveNotes();
        }
    }

    public Boolean checkCategory(String categoryName)
    {
        if (!categoryMap.containsKey(categoryName))
        {
            System.out.println("category " + categoryName + "not found");
            return false;
        }
        return true;
    }

    public Boolean checkNote(String category, String noteTitle)
    {
        if (checkCategory(category) && !categoryMap.get(category).containsKey(noteTitle))
        {
            System.out.println("note " + noteTitle + "not found in " + category);
            return false;
        }
        return true;
    }

    public Boolean checkBlock(String category, String noteTitle, String blockId)
    {
        if (checkNote(category, noteTitle) && !categoryMap.get(category).get(noteTitle).checkBlock(blockId))
        {
            System.out.println("block " + blockId + " not found in " + noteTitle + " in category " + category);
            return false;
        }
        return true;
    }

    public Map<String, List<Note>> getNotesList()
    {
        Map<String, List<Note>> notesList = new LinkedHashMap<>();

        for (String category: categoryMap.keySet())
        {
            List<Note> notes = new ArrayList<>();
            for (String noteTitle: categoryMap.get(category).keySet())
            {
                notes.add(categoryMap.get(category).get(noteTitle));
            }
            notesList.put(category, notes);
        }

        return notesList;
    }

    public void addCategory(String categoryName)
    {
        if (categoryMap.containsKey(categoryName))
        {
            System.out.println("category " + categoryName + " already exists");
            return;
        }

        categoryMap.put(categoryName, new HashMap<>());
    }

    public void deleteCategory(String categoryName)
    {
        if (checkCategory(categoryName))
            categoryMap.remove(categoryName);
    }

    public void addNote(String category, Note note)
    {
        if (checkCategory(category))
        {
            if (categoryMap.get(category).containsKey(note.getTitle()))
            {
                System.out.println("note " + note.getTitle() + "already exists in " + category);
                return;
            }

            categoryMap.get(category).put(note.getTitle(), note);
        }
    }

    public void deleteNote(String category, String noteTitle)
    {
        if (checkNote(category, noteTitle))
            categoryMap.get(category).remove(noteTitle);
    }

    public void editNote(String category, String noteTitle, Note updatedNote)
    {
        if (checkNote(category, noteTitle))
        {
            // edit block?
            // replace blocks?
            // replace note?
        }
    }

    public void editTitle(String category, String oldTitle, String newTitle)
    {
        if (checkNote(category, oldTitle))
        {
            Note note = categoryMap.get(category).remove(oldTitle);  // remove old key
            note.setTitle(newTitle);  // update title in the note
            categoryMap.get(category).put(newTitle, note);  // insert with new title
        }
    }

    public void editCategoryName(String oldCategoryName, String newCategoryName)
    {
        if (checkCategory(oldCategoryName))
            categoryMap.put(newCategoryName, categoryMap.remove(oldCategoryName));
    }

    public void moveNote(String oldCategory, String newCategory, String noteTitle)
    {
        if (checkNote(oldCategory, noteTitle) && checkCategory(newCategory))
        {
            Note note = categoryMap.get(oldCategory).remove(noteTitle);
            categoryMap.get(newCategory).put(noteTitle, note);
        }
    }

    public void addBlock(String category, String noteTitle, Block block)
    {
        if (checkNote(category, noteTitle))
            categoryMap.get(category).get(noteTitle).addBlock(block);
    }

    public void moveBlock(String category, String noteTitle, int fromIndex, int toIndex)
    {
        // use block ids? (Block block, int toIndex)
        // keep using these indexes?
    }

    public void editBlock(String category, String noteTitle, Block block, String newContent) // blockId??
    {
        if (checkBlock(category, noteTitle, block.getId()))
        {

        }
    }

    public void deleteBlock(String category, String noteTitle, String blockId)
    {
        if (checkBlock(category, noteTitle, blockId))
            categoryMap.get(category).get(noteTitle).deleteBlock(blockId);
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
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(jsonFilePath));

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

        try (FileWriter file = new FileWriter(jsonFilePath))
        {
            file.write(jsonObject.toJSONString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}