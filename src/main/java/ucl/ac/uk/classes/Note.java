package ucl.ac.uk.classes;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Note extends Item
{
    private List<Block> blocks;
    private List<String> categories;

    // new note
    public Note(String name)
    {
        super(name);
        this.blocks = new LinkedList<>();
    }

    // loading old note
    public Note(String id, String name, List<Block> blocks, String createdAt, String lastEdited)
    {
        super(id, name, createdAt, lastEdited);
        this.blocks = blocks;
    }

    @Override
    public String generateId()
    {
        return "n" + Instant.now().toEpochMilli();
    }

    public boolean checkBlock(String blockId)
    {
        for (Block b: blocks)
        {
            if (b.getId().equals(blockId))
                return true;
        }
        return false;
    }

    public boolean inCategory(String category) {
        return categories.contains(category);
    }

    public boolean inCategories(List<String> categories) {
        return this.categories.containsAll(categories);
    }

    public void addBlock(Block block)
    {
        if (!checkBlock(block.getId()))
        {
            blocks.add(block);
        }
    }

    public void addBlockFrom(String blockId, Block block, boolean beneath)
    {
        if (!checkBlock(block.getId()))
        {
            int i = getBlockIndex(blockId);
            if (beneath)
                blocks.add(i + 1, block);
            else
                blocks.add(i, block);
            updateLastEdited();
        }
    }

    public int getBlockIndex(String blockId)
    {
        for (int i = 0; i < blocks.size(); i++)
        {
            if (blocks.get(i).getId().equals(blockId))
                return i;
        }
        return -1;
    }

    public void moveBlock(String blockId, boolean down) {
        int i = getBlockIndex(blockId);
        if (i == -1)
        {
            System.out.println("blockId " + blockId + " not found in note " + id);
            return;
        }
        if ((i == 0 && !down) || (i == blocks.size() - 1 && down))
            return;
        if (down)
            Collections.swap(blocks, i, i+1);
        else
            Collections.swap(blocks, i-1, i);
    }

    public void editBlock(String blockId, String newContent)
    {
        Block b = getBlock(blockId);
        if (b instanceof TextBlock)
            ((TextBlock) b).setText(newContent);
        if (b instanceof ImageBlock)
            ((ImageBlock) b).setImagePath(newContent);
        if (b instanceof URLBlock)
            ((URLBlock) b).setURL(newContent);
    }

    public void deleteBlock(String blockId)
    {
        blocks.remove(getBlock(blockId));
    }

    public Block getBlock(String blockId)
    {
        Block block = null;
        for (Block b: blocks)
        {
            if (b.getId().equals(blockId))
            {
                block = b;
            }
        }
        return block;
    }

    public void setBlockList(List<Block> blockList) // add blockList?
    {
        this.blocks = blockList;
        updateLastEdited();
    }

    public List<Block> getBlocksList()
    {
        return blocks;
    }

    @Override
    public Map<String, Object> toJson()
    {
        return Map.of(
                "name", name,
                "blocks", blocks.stream().map(Block::toJson).collect(Collectors.toList()),
                "createdAt", createdAt,
                "lastEdited", lastEdited
        );
    }
}
