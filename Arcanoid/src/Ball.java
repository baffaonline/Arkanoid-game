import javax.swing.*;
import java.awt.*;

public class Ball {
    private ImageIcon ballImage = new ImageIcon("ball.png");
    public static final int imageWidth = 30;
    public static final int imageHeight = 30;
    public static int radius = imageWidth / 2;
    private Point position;
    private int dx = 0;
    private int dy = 0;

    public Ball(int x, int y) {
        position = new Point(x, y);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public void paintBall(Graphics g) {
        g.drawImage(ballImage.getImage(), position.x, position.y, imageWidth, imageHeight, ballImage.getImageObserver());
    }
}
