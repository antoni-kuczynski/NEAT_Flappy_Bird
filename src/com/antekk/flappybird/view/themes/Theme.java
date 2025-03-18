package com.antekk.flappybird.view.themes;

public enum Theme {
    DAY,
    NIGHT;


    @Override
    public String toString() {
        String s = super.toString();
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
