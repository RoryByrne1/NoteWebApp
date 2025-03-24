package ucl.ac.uk.classes;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Note extends Item
{
    private List<Block> blocks;
    private static final int summaryLength = 50;
    private boolean pinned;

    // new note
    public Note(String name)
    {
        super(name);
        blocks = new ArrayList<>();
        pinned = false;
    }

    // loading old note
    public Note(String id, String name, List<Block> blocks, String createdAt, String lastEdited, boolean pinned)
    {
        super(id, name, createdAt, lastEdited);
        this.blocks = blocks;
        this.pinned = pinned;
    }

    @Override
    public String generateId()
    {
        return "n" + Instant.now().toEpochMilli();
    }

    public boolean search(String query)
    {
        for (Block b: blocks)
        {
            if (b.search(query))
                return true;
        }
        return false;
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

    public void addBlock(Block block)
    {
        if (!checkBlock(block.getId()))
        {
            blocks.add(block);
            updateLastEdited();
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
        updateLastEdited();
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
        updateLastEdited();
    }

    public void deleteBlock(String blockId)
    {
        blocks.remove(getBlock(blockId));
    }

    public String getSummary()
    {
        StringBuilder summary = new StringBuilder();
        for (Block b: blocks)
        {
            if (b instanceof TextBlock)
            {
                summary.append(((TextBlock) b).getText());
                break;
            }
        }
        return summary.length() > summaryLength? summary.substring(0, summaryLength) + "..." : summary.toString();
    }

    public boolean getPinned() { return pinned; }

    public void setPinned(boolean isPinned) { pinned = isPinned; }

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
                "lastEdited", lastEdited,
                "pinned", pinned
        );
    }
}
