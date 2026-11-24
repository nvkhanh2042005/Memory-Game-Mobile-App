package com.example.memorygame;

public class MemoryCard {

    private final int value;
    private boolean flipped;
    private boolean matched;

    public MemoryCard(int value) {
        this.value = value;
        this.flipped = false;
        this.matched = false;
    }

    public int getValue() {
        return value;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }
}