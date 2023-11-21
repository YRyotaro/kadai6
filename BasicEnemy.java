import java.awt.Color;
import java.util.Random;
import java.awt.Graphics;
import java.awt.*;

class BasicEnemy extends Enemy {
    private Image img;
    public BasicEnemy(int x, int y) {
        super(x, y, 20, 20);
        img = Toolkit.getDefaultToolkit().getImage("img/misairu1.png");
        dy = GamePanel.ENEMY_SPEED;
    }
    public void draw(Graphics g) {
        g.drawImage(img, x, y, width*2, height*2, null);
    }
}


