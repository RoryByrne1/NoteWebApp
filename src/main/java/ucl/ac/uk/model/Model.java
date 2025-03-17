package ucl.ac.uk.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ucl.ac.uk.classes.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Model {
    final String jsonFilePath = "data/notes.json";
    private Folder rootDirectory;

    public Model()
    {
        File f = new File(jsonFilePath);
        if (f.exists() && !f.isDirectory())
        {
            this.rootDirectory = loadNotes();
        }
        else
        {
            this.rootDirectory = new Folder("root");
            saveNotes();
        }
    }

    public Item resolvePathFrom(List<String> path, Item i)
    {
        Item current = i;

        for (String itemId: path)
        {
            if (!(current instanceof Folder))
                return null;

            current = ((Folder) current).getItem(itemId);

            if (current == null)
            {
                System.out.println("item " + itemId + " could not be found in path " + path.toString());
                return null;
            }
        }

        return current;
    }

    public Item resolvePath(List<String> path)
    {
        return resolvePathFrom(path, rootDirectory);
    }

    public boolean checkPath(List<String> path)
    {
        return resolvePath(path) != null;
    }

    public boolean checkFolder(List<String> path)
    {
        Item i = resolvePath(path);
        if (i instanceof Folder)
            return true;
        System.out.println("path " + path + " does not resolve to a folder");
        return false;
    }

    public boolean checkNote(List<String> path)
    {
        Item i = resolvePath(path);
        if (i instanceof Note)
            return true;
        System.out.println("path " + path + " does not resolve to a note");
        return false;
    }

    public boolean checkBlock(List<String> path, String blockId)
    {
        if (checkNote(path) && !((Note)resolvePath(path)).checkBlock(blockId))
        {
            System.out.println("block " + blockId + " not found in path " + path.toString());
            return false;
        }
        return true;
    }

    public List<Item> getContentsListFrom(List<String> path, String sortBy, boolean ascending)
    {
        if (checkFolder(path))
            return ((Folder)resolvePath(path)).getContentsList(sortBy, ascending);
        return null;
    }

    public Note getNote(List<String> path)
    {
        if (checkNote(path))
            return (Note) resolvePath(path);
        return null;
    }

    public void addItem(List<String> path, Item folder)
    {
        if (checkFolder(path))
            ((Folder) resolvePath(path)).addItem(folder);
        saveNotes();
    }

    public void deleteItem(List<String> path, String itemId)
    {
        if (checkFolder(path))
            ((Folder) resolvePath(path)).deleteItem(itemId);
        saveNotes();
    }

    public void editItemName(List<String> path, String newName)
    {
        if (path.isEmpty())
        {
            System.out.println("path cannot be empty");
            return;
        }

        if (!(checkPath(path)))
            return;

        resolvePath(path).setName(newName);

        saveNotes();
    }

    public void moveItem(List<String> path, List<String> newFolderPath)
    {
        if (checkPath(path) && checkFolder(newFolderPath))
        {
            Item item = resolvePath(path);
            Folder oldFolder = (Folder) resolvePath(path.subList(0, path.size() - 1));
            Folder newFolder = (Folder) resolvePath(newFolderPath);

            oldFolder.deleteItem(item.getId());
            newFolder.addItem(item);
        }
        saveNotes();
    }

    public void addBlock(List<String> path, Block block)
    {
        if (checkNote(path))
            ((Note)resolvePath(path)).addBlock(block);
    }

    public void moveBlock(List<String> path, int fromIndex, int toIndex)
    {
        // use block ids? (Block block, int toIndex)
        // keep using these indexes?
    }

    public void editBlock(List<String> path, String blockId, String newContent) // blockId??
    {
        if (checkBlock(path, blockId))
        {

        }
    }

    public void deleteBlock(List<String> path, String blockId)
    {
        if (checkBlock(path, blockId))
            ((Note)resolvePath(path)).deleteBlock(blockId);
    }

    public Folder getRootDirectory()
    {
        return rootDirectory;
    }

    private Folder loadNotes() {
        try {
            JSONObject jsonObject = (JSONObject) (new JSONParser()).parse(new FileReader(jsonFilePath));
            return parseFolder("root", (JSONObject) jsonObject.get("root"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Folder("root");
    }

    private Folder parseFolder(String id, JSONObject jsonObject) {
        String name = (String) jsonObject.get("name");
        String createdAt = (String) jsonObject.get("createdAt");
        String lastEdited = (String) jsonObject.get("lastEdited");
        Folder folder = new Folder(id, name, new HashMap<>(),createdAt, lastEdited);

        JSONObject contents = (JSONObject) jsonObject.get("contents");
        if (contents != null) {
            for (Object key : contents.keySet()) {
                String itemId = (String) key;
                JSONObject item = (JSONObject) contents.get(itemId);
                if (item.containsKey("contents")) {
                    folder.addItem(parseFolder(itemId, item));
                } else {
                    folder.addItem(parseNote(itemId, item));
                }
            }
        }
        return folder;
    }

    private Note parseNote(String id, JSONObject noteJson) {
        String name = (String) noteJson.get("name");
        String createdAt = (String) noteJson.get("createdAt");
        String lastEdited = (String) noteJson.get("lastEdited");

        JSONArray blocksJson = (JSONArray) noteJson.get("blocks");
        List<Block> blocks = new ArrayList<>();

        if (blocksJson != null) {
            for (Object blockObj : blocksJson) {
                JSONObject blockJson = (JSONObject) blockObj;
                String type = (String) blockJson.get("type");

                Block block;
                if (type.equals("text")) {
                    block = new TextBlock((String) blockJson.get("id"), (String) blockJson.get("text"));
                } else if (type.equals("image")) {
                    block = new ImageBlock((String) blockJson.get("id"), (String) blockJson.get("imagePath"));
                } else {
                    continue;
                }
                blocks.add(block);
            }
        }
        return new Note(id, name, blocks, createdAt, lastEdited);
    }

    public void saveNotes() {
        try (FileWriter file = new FileWriter(jsonFilePath)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("root", rootDirectory.toJson());
            file.write(jsonObject.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}