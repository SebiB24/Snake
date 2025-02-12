package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Queue;

public class Snake extends JPanel implements ActionListener, KeyListener {

    int rowCount = 20;
    int columnCount = 30;
    int tilesize = 32;
    int boardwidth = columnCount * tilesize;
    int boardheight = rowCount * tilesize;

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    Queue<Character> inputQueue = new LinkedList<>();


    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP) {
            scheduleCommand('U');
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            scheduleCommand('D');
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            scheduleCommand('L');
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            scheduleCommand('R');
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    void scheduleCommand(char direction) {
        inputQueue.add(direction);
        if (inputQueue.size() == 1) {
            executor.schedule(this::processNextCommand, 100, TimeUnit.MILLISECONDS);
        }
    }

    void processNextCommand() {
        if (!inputQueue.isEmpty()) {
            char direction = inputQueue.poll();
            if(validateInput(direction)) {
                snakeHead.updateDirection(direction);
            }

            if (!inputQueue.isEmpty()) {
                executor.schedule(this::processNextCommand, 100, TimeUnit.MILLISECONDS);
            }
        }
    }


    class Block{
        int x;
        int y;
        int StartX;
        int StartY;
        int width;
        int height;
        char direction = 'U';
        int velocityX = 0;
        int velocityY = 0;
        Image image;

        public Block(int x, int y, int width, int height, Image image) {
            this.x = x;
            this.y = y;
            this.StartX = x;
            this.StartY = y;
            this.width = width;
            this.height = height;
            this.image = image;
        }

        public void updateDirection(char direction){

            updateImage(direction);
            this.direction = direction;
            updateVelocity(direction);
        }

        public void updateVelocity(char direction){
            if(direction == 'U'){
                velocityX = 0;
                velocityY = -tilesize;
            }
            else if(direction == 'D'){
                velocityX = 0;
                velocityY = tilesize;
            }
            else if(direction == 'L'){
                velocityX = -tilesize;
                velocityY = 0;
            }
            else if(direction == 'R'){
                velocityX = tilesize;
                velocityY = 0;
            }
        }

        public void updateImage(char direction){
            if(direction == 'U'){
                this.image = snakeHeadUp;
            }
            else if(direction == 'D'){
                this.image = snakeHeadDown;
            }
            else if(direction == 'L'){
                this.image = snakeHeadLeft;
            }
            else if(direction == 'R'){
                this.image = snakeHeadRight;
            }
        }
    }

    Random rand = new Random();
    Timer gameLoop;

    Block food;
    Block snakeHead;
    ArrayList<Block> snakeTail;
    Image snakeHeadUp;
    Image snakeHeadDown;
    Image snakeHeadLeft;
    Image snakeHeadRight;
    Image appleImage;
    Image snakeTailUp;
    Image snakeTailDown;
    Image snakeTailLeft;
    Image snakeTailRight;


    public Snake() {

        snakeHeadUp = new ImageIcon(getClass().getResource("/Snake/SnakeHeadUp.png")).getImage();
        snakeHeadDown = new ImageIcon(getClass().getResource("/Snake/SnakeHeadDown.png")).getImage();
        snakeHeadLeft = new ImageIcon(getClass().getResource("/Snake/SnakeHeadLeft.png")).getImage();
        snakeHeadRight = new ImageIcon(getClass().getResource("/Snake/SnakeHeadRight.png")).getImage();
        appleImage = new ImageIcon(getClass().getResource("/Snake/Apple.png")).getImage();
        snakeTailUp = new ImageIcon(getClass().getResource("/Snake/TailSegmentUp.png")).getImage();
        snakeTailDown = new ImageIcon(getClass().getResource("/Snake/TailSegmentDown.png")).getImage();
        snakeTailLeft = new ImageIcon(getClass().getResource("/Snake/TailSegmentLeft.png")).getImage();
        snakeTailRight = new ImageIcon(getClass().getResource("/Snake/TailSegmentRight.png")).getImage();
        setPreferredSize(new Dimension(boardwidth, boardheight));
        setBackground(Color.GRAY);

        loadMap();
        gameLoop = new Timer(100, this);
        gameLoop.start();

        addKeyListener(this);
        setFocusable(true);


    }

    void loadMap() {
        food = new Block(rand.nextInt(29)*tilesize, rand.nextInt(19)*tilesize, tilesize, tilesize, appleImage);
        snakeHead = new Block(14*tilesize, 9*tilesize, tilesize, tilesize, snakeHeadRight);
        snakeTail = new ArrayList<Block>();
        for(int i = 0; i < 2; i++){
            Block TailSegment = new Block( 14*tilesize, 9*tilesize, tilesize, tilesize, snakeTailRight);
            snakeTail.add(TailSegment);
        }
    }

    public boolean validateInput(char direction) {
        if(snakeHead.direction == 'U' && direction == 'D') {
            return false;
        }
        if(snakeHead.direction == 'D' && direction == 'U') {
            return false;
        }
        if(snakeHead.direction == 'L' && direction == 'R') {
            return false;
        }
        if(snakeHead.direction == 'R' && direction == 'L') {
            return false;
        }


        char InitialDirection = snakeHead.direction;
        snakeHead.updateDirection(direction);
        snakeHead.x += snakeHead.velocityX;
        snakeHead.y += snakeHead.velocityY;
        if(checkColision(snakeHead, snakeTail.getFirst())) {
            return false;
        }
        snakeHead.x -= snakeHead.velocityX;
        snakeHead.y -= snakeHead.velocityY;
        snakeHead.updateDirection(InitialDirection);


        return true;
    }


    public void CheckAndWorp(Block block){
        if(block.x >= boardwidth){
            block.x = 0;
        }
        else if(block.x < 0){
            block.x = boardwidth;
        }
        else if(block.y >= boardheight){
            block.y = 0;
        }
        else if(block.y < 0){
            block.y = boardheight;
        }
    }

    public boolean checkColision(Block a, Block b){
        return a.x < b.x+b.width &&
                a.x+a.width > b.x &&
                a.y < b.y+b.height &&
                a.y+a.height > b.y;


    }

    int score = 0;

    Image getTailImage(){
        if(snakeHead.direction == 'U')
            return snakeTailUp;
        if(snakeHead.direction == 'D')
            return snakeTailDown;
        if(snakeHead.direction == 'L')
            return snakeTailLeft;
        return snakeTailRight;
    }

    void move(){

        for(int i = snakeTail.size()-1; i > 0; i--){
            snakeTail.get(i).x = snakeTail.get(i-1).x;
            snakeTail.get(i).y = snakeTail.get(i-1).y;
            snakeTail.get(i).image = snakeTail.get(i-1).image;
        }
        snakeTail.get(0).x = snakeHead.x;
        snakeTail.get(0).y = snakeHead.y;
        snakeTail.get(0).image = getTailImage();

        snakeHead.x += snakeHead.velocityX;
        snakeHead.y += snakeHead.velocityY;
        if(checkColision(snakeHead, food)){
            score += 10;
            food.x = rand.nextInt(29)*tilesize;
            food.y = rand.nextInt(19)*tilesize;
            Block tailSegment = new Block(snakeTail.getLast().x, snakeTail.getLast().y, tilesize, tilesize, snakeTailRight);
            snakeTail.add(tailSegment);
        }
        if(snakeTail.size() > 3){
            for(Block tail: snakeTail){
                if(checkColision(snakeHead, tail)){
                    loadMap();
                }
            }
        }

        CheckAndWorp(snakeHead);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        g.drawImage(food.image, food.x, food.y, food.width, food.height, null);

        for(Block tail : snakeTail){
            g.drawImage(tail.image, tail.x, tail.y, tail.width, tail.height, null);
        }
        g.drawImage(snakeHead.image, snakeHead.x, snakeHead.y, snakeHead.width, snakeHead.height, null);
    }
}
