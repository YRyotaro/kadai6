import java.awt.*;

class Enemy extends GameObject {
    public Enemy(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void draw(Graphics g) {
        //g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
        //g.setColor(Color.RED);
    }

}
