import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Cloud {
    private int x;
    private int y;
    private int speed;
    private Image cloudImage;

    public Cloud(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.cloudImage = Toolkit.getDefaultToolkit().getImage("img/cloud.png");
    }

    public void move() {
        // 雲の移動ロジック
        x += speed;
        if (x > GamePanel.WIDTH) {
            // 雲が画面外に出たら画面左端に戻す
            x = -cloudImage.getWidth(null);
        }
    }

    public void draw(Graphics g) {
        // 雲の描画
        g.drawImage(cloudImage, x, y, null);
    }
}