import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class GamePanel extends JPanel implements Observer {
    private Sound bounceSound = new Sound(new File("bounce.wav"));
    private Sound gameOverSound = new Sound(new File("gameOver.wav"));
    private Sound loseLifeSound = new Sound(new File("loseLife.wav"));
    private Sound victorySound = new Sound(new File("victory.wav"));
    private PointPanel pointPanel;
    private Brick brick;
    private Ball ball;
    private boolean isPausing = false;
    private boolean isStarted = false;
    private Timer timer;
    private ArrayList<Block> blocks;
    private Observable gameObserver = new Observable() {
        @Override
        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(arg);
        }
    };
    private Color[] rowColors = new Color[]{Color.gray, Color.red.darker(), Color.yellow.darker(),
            Color.blue.darker(), Color.magenta, Color.green.darker()};

    public GamePanel(Dimension dimension, PointPanel panel) {
        setSize(dimension);
        setBackground(Color.BLACK);
        pointPanel = panel;
        gameObserver.addObserver(pointPanel);
        start();
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                moveBrickWithMouse(e);
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        brick.paintBrick(g);
        ball.paintBall(g);
        if (blocks != null) for (Block block : blocks) block.render(g);
    }

    public Brick getBrick() {
        return brick;
    }

    public void setBrick(Brick brick) {
        this.brick = brick;
    }

    private void createBlocks(int rows, int columns) {
        blocks = new ArrayList<>();
        int gap = 10;
        double blockWidth = (((double) getWidth() - gap) / columns) - gap;
        double blockHeight = 30;
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                Block b = new Block();
                b.setMainColor(rowColors[y % rowColors.length]);
                b.setPosition(new Point((int) (x * (blockWidth + gap) + gap), (int) (y * (blockHeight + gap) + gap)));
                b.setHeight((int) blockHeight);
                b.setWidth((int) blockWidth);
                blocks.add(b);
            }
        }
    }

    private void moveBrickWithMouse(MouseEvent e) {
        if (!isPausing) {
            if (e.getX() - Brick.imageWidth / 2 >= 0 && e.getX() + Brick.imageWidth / 2 <= getWidth()) {
                if (e.getX() >= brick.getPosition().x + Brick.imageWidth / 2)
                    brick.setRightMove(true);
                else brick.setRightMove(false);
                brick.setPosition(new Point(e.getX() - Brick.imageWidth / 2, getHeight() - Brick.imageHeight));
                if (!isStarted)
                    ball.setPosition(new Point(e.getX() - Ball.imageWidth / 2, ball.getPosition().y));
            }
            repaint();
        }
    }

    private void moveBrickWithKeyboard(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && brick.getPosition().x - 15 >= 0) {
            brick.setPosition(new Point(brick.getPosition().x - 20, brick.getPosition().y));
            if (!isStarted)
                ball.setPosition(new Point(ball.getPosition().x - 20, ball.getPosition().y));
            repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && brick.getPosition().x + Brick.imageWidth + 15 <= getWidth()) {
            brick.setPosition(new Point(brick.getPosition().x + 20, brick.getPosition().y));
            if (!isStarted)
                ball.setPosition(new Point(ball.getPosition().x + 20, ball.getPosition().y));
            repaint();
        }
    }

    private void pushBall() {
        if (!isStarted) {
            ball.setDx(10);
            ball.setDy(-10);
            timer = new Timer(30, e -> {
                ballFly();
                repaint();
            });
        } else {
            timer = new Timer(30, e -> {
                ballFly();
                repaint();
            });
        }
        timer.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof KeyEvent) {
            if (((KeyEvent) arg).getKeyCode() == KeyEvent.VK_ENTER && !isStarted) {
                pushBall();
                isStarted = true;
            } else if (((KeyEvent) arg).getKeyCode() == KeyEvent.VK_ENTER && isStarted) {
                if (isPausing) {
                    isPausing = false;
                    pushBall();
                } else {
                    isPausing = true;
                    pause();
                }
            }
            if (!isPausing)
                moveBrickWithKeyboard((KeyEvent) arg);
        }
    }

    private void start() {
        brick = new Brick((getWidth() - Brick.imageWidth) / 2, getHeight() - Brick.imageHeight);
        ball = new Ball((getWidth() - Ball.imageWidth) / 2, getHeight() - Brick.imageHeight - Ball.imageHeight);
        if (pointPanel.getLives() == 0 || pointPanel.getLives() == 3) {
            createBlocks(5, 10);
            if (pointPanel.getLives() == 0)
                pointPanel.setLevel(1);
            pointPanel.setLives(3);
            repaint();
        }
        pointPanel.setScore(0);
        isStarted = false;
    }


    private void pause() {
        timer.stop();
    }

    private void ballFly() {
        if (ball.getPosition().x + ball.getDx() + Ball.imageWidth >= getWidth() ||
                ball.getPosition().x + ball.getDx() <= 0) {
            ball.setDx(-ball.getDx());
            bounceSound.play();
        }
        if (ball.getPosition().y + ball.getDy() <= 0) {
            ball.setDy(-ball.getDy());
            bounceSound.play();
        }
        if (ball.getPosition().y + ball.getDy() + Ball.imageHeight >= getHeight()) {
            ball.setPosition(new Point(ball.getPosition().x + ball.getDx(), ball.getPosition().y + ball.getDy()));
            repaint();
            loseLife();
            return;
        }

        intersectsWithBlocks();
        intersectsWithBrick();
        ball.setPosition(new Point(ball.getPosition().x + ball.getDx(), ball.getPosition().y + ball.getDy()));
    }

    private void win() {
        victorySound.play();
        int answer = JOptionPane.showConfirmDialog(null, "You win!!!! Do you want to play this " +
                "beautiful game again?");
        if (answer == 0) {
            pointPanel.setLives(3);
            start();
            victorySound.stop();
        } else if (answer == 1) System.exit(0);
    }

    private void intersectsWithBlocks() {
        Rectangle hitbox = new Rectangle(ball.getPosition().x + ball.getDx(), ball.getPosition().y + ball.getDy(),
                Ball.radius * 2, Ball.radius * 2);
        Point heatPoint;

        for (int i = 0; i < blocks.size(); i++) {
            Block b = blocks.get(i);
            heatPoint = b.bounceVector(hitbox);
            ball.setDx(ball.getDx() * heatPoint.x);
            ball.setDy(ball.getDy() * heatPoint.y);
            if (heatPoint.x < 0 || heatPoint.y < 0) {
                blocks.remove(b);
                if (blocks.isEmpty())
                    win();
                else {
                    gameObserver.notifyObservers("Add score");
                    pointPanel.revalidate();
                    bounceSound.play();
                }
            }
        }
    }

    private void intersectsWithBrick() {
        Rectangle hitbox = new Rectangle(ball.getPosition().x + ball.getDx(), ball.getPosition().y + ball.getDy(),
                Ball.radius * 2, Ball.radius * 2);
        if (brick.collidesWith(hitbox)) {
            ball.setDy(-ball.getDy());
            bounceSound.play();
        }
    }

    private void loseLife() {
        timer.stop();
        gameObserver.notifyObservers("Game over");
        if (pointPanel.getLives() == 0)
            gameOver();
        else {
            loseLifeSound.play();
            start();
        }
    }

    private void gameOver() {
        gameOverSound.play();
        int answer = JOptionPane.showConfirmDialog(null, "Game over. Do you want to play again?");
        if (answer == 0) {
            start();
            gameObserver.notifyObservers("New game");
        } else if (answer == 1) System.exit(0);
    }
}
