package ucl.ac.uk.classes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Note extends Item
{
    private List<Block> blocks;

    // new note
    public Note(String name)
    {
        super(name);
        this.blocks = new ArrayList<>();
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

    public void addBlock(Block block)
    {
        if (checkBlock(block.getId()))
        {
            blocks.add(block);
            updateLastEdited();
        }
    }

    public Boolean checkBlock(String blockId)
    {
        for (Block b: blocks)
        {
            if (b.getId().equals(blockId))
            {
                return true;
            }
        }
        return false;
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
