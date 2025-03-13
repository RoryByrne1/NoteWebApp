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

    public Boolean checkNote(String category, String noteId)
    {
        if (checkCategory(category) && !categoryMap.get(category).containsKey(noteId))
        {
            System.out.println("note " + noteId + "not found in " + category);
            return false;
        }
        return true;
    }

    public Boolean checkBlock(String category, String noteId, String blockId)
    {
        if (checkNote(category, noteId) && !categoryMap.get(category).get(noteId).checkBlock(blockId))
        {
            System.out.println("block " + blockId + " not found in " + noteId + " in category " + category);
            return false;
        }
        return true;
    }

    public Map<String, List<Note>> getNotesList()
    {
        Map<String, List<Note>> notesList = new LinkedHashMap<>();

        for (String category: categoryMap.keySet())
        {
            List<Note> notes = new ArrayList<>(categoryMap.get(category).values());
            notesList.put(category, notes);
        }

        return notesList;
    }

    public Note getNote(String noteId)
    {
        Note note = null;

        for (String category: categoryMap.keySet())
        {
            for (Note n: categoryMap.get(category).values())
            {
                if (n.getId().equals(noteId))
                    note = n;
            }
        }

        return note;
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
            if (categoryMap.get(category).containsKey(note.getId()))
            {
                System.out.println("note " + note.getId() + " already exists in " + category);
                return;
            }

            categoryMap.get(category).put(note.getId(), note);
        }
    }

    public void deleteNote(String category, String noteId)
    {
        if (checkNote(category, noteId))
            categoryMap.get(category).remove(noteId);
    }

    public void editNote(String category, String noteId, Note updatedNote)
    {
        if (checkNote(category, noteId))
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

    public void moveNote(String oldCategory, String newCategory, String noteId)
    {
        if (checkNote(oldCategory, noteId) && checkCategory(newCategory))
        {
            Note note = categoryMap.get(oldCategory).remove(noteId);
            categoryMap.get(newCategory).put(noteId, note);
        }
    }

    public void addBlock(String category, String noteId, Block block)
    {
        if (checkNote(category, noteId)) {
            if (categoryMap.get(category).get(noteId).checkBlock(block.getId())) {
                System.out.println("block " + block.getId() + " already exists in note " + noteId + " in category " + category);
                return;
            }

            categoryMap.get(category).get(noteId).addBlock(block);
        }

    }

    public void moveBlock(String category, String noteId, int fromIndex, int toIndex)
    {
        // use block ids? (Block block, int toIndex)
        // keep using these indexes?
    }

    public void editBlock(String category, String noteId, String blockId, String newContent) // blockId??
    {
        if (checkBlock(category, noteId, blockId))
        {

        }
    }

    public void deleteBlock(String category, String noteId, String blockId)
    {
        if (checkBlock(category, noteId, blockId))
            categoryMap.get(category).get(noteId).deleteBlock(blockId);
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
                    String noteId = (String) noteKey;
                    JSONObject noteData = (JSONObject) categoryNotes.get(noteId);
                    String noteTitle = (String) noteData.get("title");
                    String createdAt = (String) noteData.get("createdAt");
                    String lastEdited = (String) noteData.get("lastEdited");

                    JSONArray blocksJson = (JSONArray) noteData.get("blocks");

                    if (blocksJson == null)
                    {
                        blocksJson = new JSONArray();
                    }

                    List<Block> blocks = new ArrayList<>();

                    for (Object blockObj : blocksJson)
                    {
                        JSONObject blockJson = (JSONObject) blockObj;
                        String type = (String) blockJson.get("type");

                        Block block;
                        if (type.equals("text"))
                        {
                            block = new TextBlock((String) blockJson.get("id"), (String) blockJson.get("text"));
                        }
                        else if (type.equals("image"))
                        {
                            block = new ImageBlock((String) blockJson.get("id"), (String) blockJson.get("imagePath"));
                        }
                        else
                        {
                            continue;
                        }

                        blocks.add(block);
                    }

                    notes.put(noteId, new Note(noteId, noteTitle, blocks, createdAt, lastEdited));
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

            for (Note note : notes.values())
            {
                categoryNotes.put(note.getId(), note.toJson());
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