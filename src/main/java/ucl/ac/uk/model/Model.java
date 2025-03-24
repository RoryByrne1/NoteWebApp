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
import java.util.Map;

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

    public static Item resolvePathFrom(List<String> path, Item i)
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

    // list of {"note":Note, "path":List<String>}
    public List<Map<String, Object>> searchNotesFrom(List<String> path, String sortBy, boolean ascending, String query)
    {
        List<Map<String, Object>> notes = new ArrayList<>();

        if (!checkFolder(path))
            return null;

        for (Item item: ((Folder)resolvePath(path)).getContentsList(sortBy, ascending))
        {
            if (item instanceof Note)
                if (((Note)item).search(query))
                    notes.add(Map.of("note", item,
                                     "path", path));
            if (item instanceof Folder)
            {
                List<String> newPath = new ArrayList<>(path);
                newPath.add(item.getId());
                notes.addAll(searchNotesFrom(newPath, sortBy, ascending, query));
            }
        }

        notes.sort((map1, map2) -> {
            boolean isLess = ((Item)map1.get("note")).isLessThan(((Item)map2.get("note")), sortBy, ascending);
            return isLess ? -1 : 1;
        });

        return notes;
    }

    public List<Map<String, Object>> searchNotes(String sortBy, boolean ascending, String query)
    {
        return searchNotesFrom(new ArrayList<>(), sortBy, ascending, query);
    }

    public void pin(List<String> path, boolean isPinned)
    {
        if (checkNote(path))
            ((Note) resolvePath(path)).setPinned(isPinned);
        saveNotes();
    }

    public List<Map<String, Object>> getPinned(String sortBy, boolean ascending)
    {
        List<Map<String, Object>> notes = searchNotes(sortBy, ascending, "");
        List<Map<String, Object>> pinned = new ArrayList<>();
        for (Map<String, Object> noteMap: notes)
        {
            if (((Note) noteMap.get("note")).getPinned())
                pinned.add(noteMap);
        }
        return pinned;
    }

    public List<Map<String, Object>> getRecents(int quantity)
    {
        List<Map<String, Object>> notes = searchNotes("lastEdited",false, "");
        System.out.println(notes);
        if (notes.size() > quantity)
            return notes.subList(0, quantity);
        return notes;
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
        saveNotes();
    }

    public void addBlockFrom(List<String> path, String blockId, Block block, boolean beneath)
    {
        if (checkBlock(path, blockId))
            ((Note)resolvePath(path)).addBlockFrom(blockId, block, beneath);
        saveNotes();
    }

    public void moveBlock(List<String> path, String blockId, boolean down)
    {
        if (checkBlock(path, blockId))
            ((Note) resolvePath(path)).moveBlock(blockId, down);
        saveNotes();
    }

    public void editBlock(List<String> path, String blockId, String newContent) // blockId??
    {
        if (checkBlock(path, blockId))
        {
            ((Note) resolvePath(path)).editBlock(blockId, newContent);
        }
        saveNotes();
    }

    public void deleteBlock(List<String> path, String blockId)
    {
        if (checkBlock(path, blockId))
            ((Note)resolvePath(path)).deleteBlock(blockId);
        saveNotes();
    }

    public void updateLastEditedAlong(List<String> path)
    {
        for (int i = 0; i <= path.size(); i++) {
            List<String> subPath = path.subList(0, i);
            resolvePath(subPath).updateLastEdited();
        }
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

        JSONObject contents = (JSONObject) jsonObject.get("contents");
        Map<String, Item> itemsMap = new HashMap<>();
        if (contents != null) {
            for (Object key : contents.keySet()) {
                String itemId = (String) key;
                JSONObject item = (JSONObject) contents.get(itemId);
                if (item.containsKey("contents")) {
                    itemsMap.put(itemId, parseFolder(itemId, item));
                } else {
                    itemsMap.put(itemId, parseNote(itemId, item));
                }
            }
        }
        return new Folder(id, name, itemsMap, createdAt, lastEdited);
    }

    private Note parseNote(String id, JSONObject noteJson) {
        String name = (String) noteJson.get("name");
        String createdAt = (String) noteJson.get("createdAt");
        String lastEdited = (String) noteJson.get("lastEdited");
        boolean pinned = (boolean) noteJson.get("pinned");

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
                } else if (type.equals("url")) {
                    block = new URLBlock((String) blockJson.get("id"), (String) blockJson.get("url"));
                } else {
                    continue;
                }
                blocks.add(block);
            }
        }

        return new Note(id, name, blocks, createdAt, lastEdited, pinned);
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