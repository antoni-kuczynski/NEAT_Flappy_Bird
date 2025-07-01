package com.antekk.flappybird.game.bird;

import com.antekk.flappybird.game.ai.NeuralNetwork;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Birds implements PlayerBird, Iterable<Bird> {
    private final ArrayList<Bird> birds = new ArrayList<>();

    public Birds(int amount) {
        for(int i = 0; i < amount; i++) birds.add(new Bird(new NeuralNetwork()));
    }

    @Override
    public void draw(Graphics g) {
        for(Bird bird : birds) bird.draw(g);
    }

    @Override
    public void drawWithoutRotation(Graphics g) {
        for(Bird bird : birds) bird.drawWithoutRotation(g);
    }

    @Override
    public void resetPosition() {
        for(Bird bird : birds) {
            bird.resetPosition();
            bird.isAlive = true;
        }
    }

    @Override
    public void flap() {
        for(Bird bird : birds) bird.flap();
    }

    @Override
    public Iterator<Bird> iterator() {
        return birds.iterator();
    }

    public boolean areAllBirdsDead() {
        for(Bird bird : this) {
            if(bird.isAlive)
                return false;
        }
        return true;
    }

    public Bird getDefault() {
        return birds.get(0);
    }

    public Bird get(int index) {
        return birds.get(index);
    }

    public int size() {
        return birds.size();
    }
}
