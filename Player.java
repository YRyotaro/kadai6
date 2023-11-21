import java.awt.*;

public class Player extends GameObject {
    private Image img;
    public Player(int x, int y) {
        super(x, y, 60, 60);
        img = Toolkit.getDefaultToolkit().getImage("img/sentoki1_2.png");
    }

    public void moveLeft() {
        dx = -GamePanel.PLAYER_SPEED;
    }

    public void moveRight() {
        dx = GamePanel.PLAYER_SPEED;
    }

    public void stopMoving() {
        dx = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(Graphics g) {

        int[] xPoints = {x, x + width / 2, x + width};
        int[] yPoints = {y + height, y, y + height};

        g.drawImage(img, x, y, width, height, null);
    }

}