import javax.swing.*;
import java.awt.*;


public class Brick {
    private ImageIcon brickImage = new ImageIcon("brick.png");
    public static final int imageWidth = 160;
    public static final int imageHeight = 32;
    private boolean rightMove = false;

    public boolean isRightMove() {
        return rightMove;
    }

    public void setRightMove(boolean rightMove) {
        this.rightMove = rightMove;
    }

    private Point position;

    public Brick(int x, int y) {
        position = new Point(x, y);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public boolean collidesWith(Rectangle hitbox){
        Rectangle brickRectangle = new Rectangle(position.x, position.y, imageWidth, imageHeight);
        return brickRectangle.intersects(hitbox);
    }

    public void paintBrick(Graphics g) {
        g.drawImage(brickImage.getImage(), position.x, position.y, imageWidth, imageHeight, brickImage.getImageObserver());
    }
}
