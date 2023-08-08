package fr.anarchick.anapi.bukkit;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChunkCoordinate implements Serializable {

    private final int x, z;

    public ChunkCoordinate(String chunkCoordinates) {
        Pattern pattern = Pattern.compile("^\\((-?\\d+);(-?\\d+)\\)$");
        Matcher matcher = pattern.matcher(chunkCoordinates);
        if (matcher.find()) {
            this.x = Integer.parseInt(matcher.group(1));
            this.z = Integer.parseInt(matcher.group(2));
        } else {
            this.x = 0;
            this.z = 0;
        }
    }

    public ChunkCoordinate(Chunk chunk) {
        this(chunk.getX(), chunk.getZ());
    }

    public ChunkCoordinate(int x, int z) {
        this.x = x;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkCoordinate that = (ChunkCoordinate) o;
        return x == that.x && z == that.z;
    }

    @Override
    public String toString() {
        return "ChunkCoordinate@(" + x + ";" + z +")";
    }

    public ChunkCoordinate getOffset(BlockFace direction) {
        int offsetX = x;
        int offsetZ = z;
        switch (direction) {
            case NORTH -> offsetZ--;
            case SOUTH -> offsetZ++;
            case EAST -> offsetX++;
            case WEST -> offsetX--;
        }
        return new ChunkCoordinate(offsetX, offsetZ);
    }

    @Nonnull
    public ChunkCoordinate[] getNear() {
        ChunkCoordinate[] coordinates = new ChunkCoordinate[4];
        coordinates[0] = getOffset(BlockFace.NORTH);
        coordinates[1] = getOffset(BlockFace.SOUTH);
        coordinates[2] = getOffset(BlockFace.EAST);
        coordinates[3] = getOffset(BlockFace.WEST);
        return coordinates;
    }

    public Chunk getChunk(World world) {
        return world.getChunkAt(x, z);
    }

}
