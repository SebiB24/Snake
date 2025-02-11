package Game;


import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        int rowCount = 20;
        int columnCount = 30;
        int tilesize = 32;
        int boardWidth = columnCount * tilesize;
        int boardHeight = rowCount * tilesize;

        JFrame frame = new JFrame("Snake");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Snake snake = new Snake();
        frame.add(snake);
        frame.pack();
        snake.requestFocus();
    }

}