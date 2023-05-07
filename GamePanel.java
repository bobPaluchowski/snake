import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int body_parts = 3;
    int apples_eaten;
    int apple_x;
    int apple_y;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        start_game();
    }

    public void start_game() {
        new_apple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        
        if(running) {
            /*
             * for(int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
             * g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
             * g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
             * }
             */
            g.setColor(Color.PINK);
            g.fillOval(apple_x, apple_y, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i < body_parts; i++) {
                if(i == 0) {
                    g.setColor(Color.WHITE);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.GREEN);
            g.setFont(new Font("Ink Free", Font.BOLD, 25));
            FontMetrics fm = getFontMetrics(g.getFont());
            g.drawString("Score: " + apples_eaten, (SCREEN_WIDTH - fm.stringWidth("Score: " + apples_eaten)) / 2, g.getFont().getSize());
        }
        else {
            game_over(g);
        }
    }

    public void new_apple() {
        apple_x = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        apple_y = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for(int i = body_parts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch(direction) {
            case 'U':
            y[0] = y[0] - UNIT_SIZE;
            break;
            case 'D':
            y[0] = y[0] + UNIT_SIZE;
            break;
            case 'L':
            x[0] = x[0] - UNIT_SIZE;
            break;
            case 'R':
            x[0] = x[0] + UNIT_SIZE;
            break;
        }
    }

    public void check_apple() {

        if((x[0] == apple_x) && (y[0] == apple_y)) {
            body_parts++;
            apples_eaten++;
            new_apple();
        }
    }

    public void check_collisions() {

        for(int i = body_parts; i > 0; i--) {
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        
        // Coordinates 0.0 -> top left
        if(x[0] < 0) {
            running = false;
        }
        if(x[0] > SCREEN_WIDTH) {
            running = false;
        }
        if(y[0] < 0) {
            running = false;
        }
        if(y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if(!running) {
            timer.stop();
        }
    }

    public void game_over (Graphics g) {
        g.setColor(Color.GREEN);
        g.setFont(new Font("Ink Free", Font.BOLD, 25));
        FontMetrics fm1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + apples_eaten, (SCREEN_WIDTH - fm1.stringWidth("Score: " + apples_eaten)) / 2, g.getFont().getSize());

        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 50));
        FontMetrics fm = getFontMetrics(g.getFont());
        g.drawString("YOU HAVE LOST", (SCREEN_WIDTH - fm.stringWidth("YOU HAVE LOST")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(running) {
            move();
            check_apple();
            check_collisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                if(direction != 'R') {
                    direction = 'L';
                }
                break;
                case KeyEvent.VK_RIGHT:
                if(direction != 'L') {
                    direction = 'R';
                }
                break;
                case KeyEvent.VK_UP:
                if(direction != 'D') {
                    direction = 'U';
                }
                break;
                case KeyEvent.VK_DOWN:
                if(direction != 'U') {
                    direction = 'D';
                }
                break;
            }
        }
    }
}