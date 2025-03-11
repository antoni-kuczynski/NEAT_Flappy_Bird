package com.antekk.flappybird.game.bird;

import com.antekk.flappybird.game.pipes.BottomPipe;
import com.antekk.flappybird.game.pipes.PipeFormation;
import com.antekk.flappybird.game.pipes.TopPipe;
import com.antekk.flappybird.view.GamePanel;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import static com.antekk.flappybird.view.GamePanel.getBlockSizePx;
import static com.antekk.flappybird.view.themes.GameColors.*;

public class Bird {
    private int posX;
    private int posY;
    private int width;
    private int height;
    public boolean isMovingUp = false;
    public int framesSinceBirdStartedMoving = 0;
    public int rotationAngle = 0;

    private static BufferedImage rotateImage(BufferedImage image, double angle) {
        int w = image.getWidth();
//        int w = width;
        int h = image.getHeight();
//        int h = height;
        double radians = Math.toRadians(angle);

        int newWidth = (int) Math.round(w * Math.abs(Math.cos(radians)) + h * Math.abs(Math.sin(radians)));
        int newHeight = (int) Math.round(h * Math.abs(Math.cos(radians)) + w * Math.abs(Math.sin(radians)));

//        System.out.println(newWidth + " " + newHeight);

        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotatedImage.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        AffineTransform transform = new AffineTransform();
        transform.translate((newWidth - w) / 2.0, (newHeight - h) / 2.0);
        transform.rotate(radians, w / 2.0, h / 2.0);
        g2d.setTransform(transform);

        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return rotatedImage;
    }

    public void resetPosition() {
        posX = (int) ((GamePanel.getBoardCols() - 1.5) * getBlockSizePx() / 2);
        posY = (GamePanel.getBoardRows() - 2) * getBlockSizePx() / 2;
        width = (int) (1.41 * getBlockSizePx());
        height = getBlockSizePx();
    }

    public void flap() {
        framesSinceBirdStartedMoving = 0;
        isMovingUp = true;
    }

    public void draw(Graphics g) {
        //so that the bird doesnt flip when falling
        if(rotationAngle > 33) {
            rotationAngle = 33;
        }

        if(isMovingUp && framesSinceBirdStartedMoving != 0 || rotationAngle < 15) {
            BufferedImage img = rotateImage(birdUpFlap, -20);
            g.drawImage(img, getX(), getY(), getWidth(), getHeight(), null);
        } else if(!isMovingUp && framesSinceBirdStartedMoving != 0) {
            g.drawImage(rotateImage(birdDownFlap, 3 * rotationAngle - 15), getX(), getY(), getWidth(), getHeight(), null);

        } else {
            g.drawImage(birdMidFlap, getX(), getY(), getWidth(), getHeight(), null);
        }
    }

    public void drawWithoutRotation(Graphics g) {
        if(isMovingUp && framesSinceBirdStartedMoving != 0) {
            g.drawImage(birdUpFlap, getX(), getY(), getWidth(), getHeight(), null);
        } else if(!isMovingUp && framesSinceBirdStartedMoving != 0) {
            g.drawImage(birdDownFlap, getX(), getY(), getWidth(), getHeight(), null);

        } else {
            g.drawImage(birdMidFlap, getX(), getY(), getWidth(), getHeight(), null);
        }
    }

    public void moveUpBy(int dy) {
        posY -= dy;
    }

    public boolean collidesWithPipeFormation(PipeFormation pipeFormation) {
        BottomPipe bottomPipe = pipeFormation.getBottomPipe();
        TopPipe topPipe = pipeFormation.getTopPipe();

        //bottom pipe collision from top
        if((getX() + getWidth() >= bottomPipe.getX() &&
                getX() <= bottomPipe.getX() + bottomPipe.getWidth()) &&
                getY() + getHeight() >= bottomPipe.getY()) {
            return true;
        }

        //top pipe collision from bottom
        if((getX() >= topPipe.getX() &&
                getX() <= topPipe.getX() + topPipe.getWidth()) &&
                getY() <= topPipe.getY() + topPipe.getHeight()) {
            return true;
        }

        //collisions for pipes sides
        if((getX() + getWidth() >= topPipe.getX() && getX() <= topPipe.getX() + getBlockSizePx()) &&
                (getY() <= (topPipe.getY() + topPipe.getHeight()) || getY() >= (bottomPipe.getY()))) {
            return true;
        }

        return false;
    }

    public boolean isBetweenPipes(PipeFormation pipeFormation) {
        return (getX() + getWidth() >= pipeFormation.getTopPipe().getX() &&
                getX() <= pipeFormation.getTopPipe().getX() + pipeFormation.getTopPipe().getWidth() &&
                getY() >= pipeFormation.getTopPipe().getY() + pipeFormation.getTopPipe().getHeight() &&
                getY() + getHeight() <= pipeFormation.getBottomPipe().getY());
    }

    public Bird() {
        resetPosition();
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
