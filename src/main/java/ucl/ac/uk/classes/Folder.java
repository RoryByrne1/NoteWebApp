package ucl.ac.uk.classes;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public void checkName(Item item) {
        String originalName = item.getName();
        String newName = originalName;
        int count = 1;

        // check for duplicates
        while (true) {
            boolean exists = false;
            for (Item i : contents.values()) {
                if (i.getName().equals(newName)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                break;
            }
            newName = originalName + count;
            count++;
        }

        item.setName(newName);
    }

    public void addItem(Item item)
    {
        checkName(item);
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
        return  Stream.concat(getFoldersList(sortBy, ascending).stream(),
                              getNotesList(sortBy, ascending).stream()).collect(Collectors.toList());
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

        folders.sort((folder1, folder2) -> {
            boolean isLess = folder1.isLessThan(folder2, sortBy, ascending);
            return isLess ? -1 : 1;
        });

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

        notes.sort((note1, note2) -> {
            boolean isLess = note1.isLessThan(note2, sortBy, ascending);
            return isLess ? -1 : 1;
        });

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
