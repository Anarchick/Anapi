package fr.anarchick.anapi.bukkit.inventory;

public enum SlotAxis {
    UP(-9), DOWN(9), LEFT(-1), RIGHT(1),
    UP_LEFT(-10), UP_RIGHT(-8), DOWN_LEFT(8), DOWN_RIGHT(10);

    private final int offset;
    SlotAxis(int offset) {
        this.offset = offset;
    }

    public int offset(int distance) {
        return this.offset * distance;
    }

}
