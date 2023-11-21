import java.util.Random;
import java.util.List;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.text.CollationElementIterator;
import java.awt.*;

class BossEnemy extends Enemy {
    private List<BossBullet> bossBullets; 
    public int health;
    private Random random;
    private int num = 0;
    private int cnt;
    private Image img;

    public BossEnemy(int x, int y) {
        
        super(x, y, 50, 50);
        img = Toolkit.getDefaultToolkit().getImage("img/sentoki2_2.png");
        dy = GamePanel.ENEMY_SPEED;
        this.health = 5;
        random = new Random();
    }
    public void setBossBullets(List<BossBullet> bossBullets) {
        this.bossBullets = bossBullets;
    }

    public void takeDamage(){
        this.health--;
    }

    public boolean isDefeated() {
        return this.health <= 1;
    }

    public void move() {
        // ランダムに動く
        num++;
        if(num > 10){
            dx = random.nextInt(5) - 2;  
            
            num = 0;
        }
        super.move();

        // 端に当たると反射する
        if (x <= 0 || x + width >= GamePanel.WIDTH) {
            dx = -(dx*5);
        }
        if(y <= 0 || y + height >= GamePanel.HEIGHT){
            dy = -dy;
        }
    }

    public void shootBullet(List<BossBullet> bullets) {
        // ボスが弾を発射するロジック
        bossBullets.add(new BossBullet(x + getWidth() / 2, y + getHeight()));
    }
     
    
    public void draw(Graphics g) {


        g.drawImage(img, x, y, width*2, height*2, null);

    }   
}
