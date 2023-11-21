import java.awt.*;

public class Bullet extends GameObject {
    private Image img;
    public Bullet(int x, int y) {
        super(x, y, 5, 10);
        img = Toolkit.getDefaultToolkit().getImage("img/misairu2.png");
        dy = -GamePanel.BULLET_SPEED;
    }

    public void draw(Graphics g) {
        g.drawImage(img, x, y, width*2, height*2, null);
    }
}

