package ucl.ac.uk.classes;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Folder extends Item
{
    private Map<String, Item> contents;

    // new note
    public Folder(String name)
    {
        super(name);
        this.contents = new HashMap<>();
    }

    // loading old note
    public Folder(String id, String name, Map<String, Item> contents, String createdAt, String lastEdited)
    {
        super(id, name, createdAt, lastEdited);
        this.contents = contents;
    }

    @Override
    public String generateId()
    {
        return name.replaceAll("\\s+", "-");  // replace all whitespace
    }

    public void addItem(Item item)
    {
        contents.put(item.getId(), item);
        super.lastEdited = generateTimeStamp();
    }

    public Boolean checkItem(String itemId)
    {
        return contents.containsKey(itemId);
    }

    public void deleteItem(String itemId)
    {
        contents.remove(itemId);
    }

    public Item getItem(String itemId)
    {
        return contents.get(itemId);
    }

    public void setItemList(Map<String, Item> itemList)
    {
        this.contents = itemList;
        updateLastEdited();
    }

    public List<Item> getContentsList(String sortBy, boolean ascending)
    {
        return new ArrayList<>(contents.values());
    }

    public List<Folder> getFoldersList(String sortBy, boolean ascending)
    {
        List<Folder> folders = new ArrayList<>();

        for (Item i: contents.values())
        {
            if (i instanceof Folder)
            {
                folders.add((Folder) i);
            }
        }

        return folders;
    }

    public List<Note> getNotesList(String sortBy, boolean ascending)
    {
        List<Note> notes = new ArrayList<>();

        for (Item i: contents.values())
        {
            if (i instanceof Note)
            {
                notes.add((Note) i);
            }
        }

        return notes;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("createdAt", createdAt);
        jsonObject.put("lastEdited", lastEdited);

        JSONObject contentsJson = new JSONObject();  // Ensure it's a dictionary, not a list

        for (Item item : contents.values()) { // Assuming contents is a Map<String, Item>
            contentsJson.put(item.getId(), item.toJson()); // Use item ID as key
        }

        jsonObject.put("contents", contentsJson);
        return jsonObject;
    }
}
