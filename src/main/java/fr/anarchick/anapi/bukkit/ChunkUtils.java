package fr.anarchick.anapi.bukkit;

import fr.anarchick.anapi.java.Pair;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import javax.annotation.Nonnull;

import static org.bukkit.block.BlockFace.EAST;
import static org.bukkit.block.BlockFace.SOUTH;

public class ChunkUtils {

    private final int X, Z;
    private final Chunk chunk;

    public ChunkUtils(Chunk chunk) {
        this.X = chunk.getX();
        this.Z = chunk.getZ();
        this.chunk = chunk;
    }

    @Nonnull
    public Chunk getChunkOffset(BlockFace direction) {
        int offsetX = X, offsetZ = Z;
        switch (direction) {
            case NORTH -> offsetZ--;
            case SOUTH -> offsetZ++;
            case EAST -> offsetX++;
            case WEST -> offsetX--;
        }
        return chunk.getWorld().getChunkAt(offsetX, offsetZ);
    }

    @Nonnull
    public Block[] getEdge(BlockFace direction, int y) {
        Block[] blocks = new Block[16];
        int offsetX = (direction == EAST) ? 15 : 0;
        int offsetZ = (direction == SOUTH) ? 15 : 0;
        for (int i = 0; i < 16; i++) {
            blocks[i] = chunk.getBlock(offsetX, y, offsetZ);
            switch (direction) {
                case NORTH, SOUTH -> offsetX++;
                case EAST, WEST -> offsetZ++;
            }
        }
        return blocks;
    }

    @Nonnull
    public Pair<Integer, Integer> getDistance(@Nonnull Chunk chunk) {
        int x = Math.abs(X - chunk.getX());
        int z = Math.abs(Z - chunk.getZ());
        return new Pair<Integer, Integer>(x, z);
    }

}
