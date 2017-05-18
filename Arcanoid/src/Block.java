import java.awt.*;

public class Block {
    private Point position = new Point(0, 0);
    private int width = 70;
    private int height = 30;

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Color getMainColor() {
        return mainColor;
    }

    public void setMainColor(Color mainColor) {
        this.mainColor = mainColor;
    }

    private Color mainColor = Color.red.darker();

    public Point bounceVector(Rectangle hitbox) {
        Point p = new Point(1, 1);
        Rectangle hb_t = new Rectangle(position.x, position.y, width, height / 5);
        Rectangle hb_b = new Rectangle(position.x, position.y + height - height / 5, width, height / 5);
        Rectangle hb_l = new Rectangle(position.x, position.y, width / 10, height);
        Rectangle hb_r = new Rectangle(position.x + width - width / 10, position.y, width / 10, height);
        if (hb_t.intersects(hitbox) || hb_b.intersects(hitbox)) p.y = -1;
        if (hb_r.intersects(hitbox) || hb_l.intersects(hitbox)) p.x = -1;
        return p;
    }

    public void render(Graphics g) {
        g.setColor(mainColor);
        g.fillRect(position.x, position.y, width, height);

        for (int i = 0; i < height / 4; i++) {
            g.setColor(mainColor.darker());
            g.drawLine(position.x + i, position.y + height - i, position.x + width - 1,
                    position.y + height - i);
            g.drawLine(position.x + width - 1 - i, position.y + i, position.x + width - 1 - i,
                    position.y + height);
            g.setColor(mainColor.brighter());
            g.drawLine(position.x, position.y + i, position.x + width - 1 - i, position.y + i);
            g.drawLine(position.x + i, position.y + height - i, position.x + i, position.y);
        }
    }

}