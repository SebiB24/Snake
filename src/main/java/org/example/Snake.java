package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Snake extends JPanel implements ActionListener, KeyListener {

    int rowCount = 20;
    int columnCount = 30;
    int tilesize = 32;
    int boardwidth = columnCount * tilesize;
    int boardheight = rowCount * tilesize;
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    class Block{
        int x;
        int y;
        int StartX;
        int StartY;
        int width;
        int height;

        public Block(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.StartX = x;
            this.StartY = y;
            this.width = width;
            this.height = height;
        }
    }

    Random rand = new Random();
    Block food;
    Timer gameLoop;

    public Snake() {
        setPreferredSize(new Dimension(boardwidth, boardheight));
        setBackground(Color.BLACK);

        loadMap();
        gameLoop = new Timer(50, this);
        gameLoop.start();
    }

    void loadMap() {
        food = new Block(rand.nextInt(29)*tilesize, rand.nextInt(19)*tilesize, tilesize, tilesize);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        g.setColor(Color.PINK);
        g.fillRect(food.x, food.y, food.width, food.height);
    }
}
