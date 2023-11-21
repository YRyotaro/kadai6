import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;
    public static final int PLAYER_SPEED = 5;
    public static final int ENEMY_SPEED = 3;
    public static final int BULLET_SPEED = 7;
    public static final int ENEMY_SPAWN_INTERVAL = 1000;
    public static final int BOSS_SCORE_THRESHOLD = 10;

    private Image img;
    private Image img2;

    private Player player;

    private List<Enemy> enemies;
    private List<Bullet> bullets;
    private List<BossBullet> bossBullets;
    private List<Cloud> clouds;

    private Timer bossBulletTimer;
    private Timer timer;
    private Timer cloudTimer;

    private int score;
    private int explosionX;
    private int explosionY;

    private boolean bossSpawned;
    private boolean playerdeath;

    private BossEnemy bossEnemy;


    public GamePanel() {
        img = Toolkit.getDefaultToolkit().getImage("img/sea.jpg");
        img2 = Toolkit.getDefaultToolkit().getImage("img/explosion.gif");
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);
    

        player = new Player(WIDTH / 2, HEIGHT - 50);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        bossBullets = new ArrayList<>();
        clouds = new ArrayList<>();
        timer = new Timer(16, this);
        
        score = 0;
        bossSpawned = false;
        playerdeath = false;
        
        timer.start();
        spawnEnemies();
        spawnClouds();
        bossEnemy = null;

        cloudTimer = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveClouds();
            }
        });
        cloudTimer.start();


        bossBulletTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (bossSpawned && bossEnemy != null) {
                    bossEnemy.shootBullet(bossBullets);
                }
            }
        });
        bossBulletTimer.start();
    }

    private void spawnEnemies() {
        timer = new Timer(ENEMY_SPAWN_INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Random random = new Random();
                int enemyType = random.nextInt(2); // 2 types of enemies for simplicity

                if (enemyType == 0) {
                    enemies.add(new BasicEnemy(random.nextInt(WIDTH), 0));
                } else {
                    enemies.add(new FastEnemy(random.nextInt(WIDTH), 0));
                }
            }
        });
        timer.start();
    }

    private void spawnClouds() {

        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(150); 
            int speed = random.nextInt(3) + 1; 
            clouds.add(new Cloud(x, y, speed));
        }
    }

    private void moveClouds() {
        for (Cloud cloud : clouds) {
            cloud.move();
        }
    }

    private void shoot() {
        bullets.add(new Bullet(player.getX() + player.getWidth() / 2, player.getY()));
    }

    private void checkCollisions() {
        Rectangle playerBounds = player.getBounds();
        Rectangle bossBounds = null;


        for (Enemy enemy : enemies) {
            Rectangle enemyBounds = enemy.getBounds();

            

            if(bossSpawned && bossEnemy != null){
                bossBounds = bossEnemy.getBounds();

            }
            
            if (playerBounds.intersects(enemyBounds)) {
                System.out.println("Player and enemy collision detected!");
                
                gameOver();
                
                return;
            }


            for (Bullet bullet : bullets) {
                Rectangle bulletBounds = bullet.getBounds();
                
                if (bulletBounds.intersects(enemyBounds)) {

                    if (bossSpawned && bossEnemy != null) {

                            if (bulletBounds.intersects(bossBounds)) {
                                bullets.remove(bullet);
                                bossEnemy.takeDamage();
                                if (bossEnemy.isDefeated()) {
                                    bossEnemy = null;
                                    bossSpawned = false;
                                    score += 100;  // Bonus points for defeating the boss
                                }
                            return;
                            }
                    }
                    enemies.remove(enemy);
                    bullets.remove(bullet);
                    score += 10;

                    if (score % 10 == 0 && !bossSpawned) {
                        BossEnemy boss = new BossEnemy(WIDTH / 2, 0);
                        enemies.add(boss);
                        bossEnemy = boss;
                        bossSpawned = true;
                        bossBullets = new ArrayList<>();
                        
                        boss.setBossBullets(bossBullets);
                
                    }
                    

                    return;
                }
            }
        }
        for (BossBullet bossbullet : bossBullets) {
            Rectangle bossBulletBounds = bossbullet.getBounds();

            if (bossBulletBounds.intersects(playerBounds)) {
                System.out.println("Boss bullet and player collision detected!");
                gameOver();
                return;
            }
        }
    }

    private void gameOver() {

        timer.stop();

        playerdeath = true;
        explosionX = player.getX();
        explosionY = player.getY();

        repaint();
        int option = JOptionPane.showConfirmDialog(
                this,
                "Game Over! Your Score: " + score + "\nRetry?",
                "Game Over",
                JOptionPane.YES_NO_OPTION
        );
    
        if (option == JOptionPane.YES_OPTION) {
            // User clicked "Yes," restart the game
            restartGame();
        } else {
            // User clicked "No," exit the application
            System.exit(0);
        }
    }
    
    private void restartGame() {
        score = 0;
        bossSpawned = false;
        playerdeath = false;
        bossEnemy = null;
        player = new Player(WIDTH / 2, HEIGHT - 50);
        enemies.clear();
        bullets.clear();
        bossBullets.clear();
        clouds.clear();
        timer.start();
        spawnEnemies();
        spawnClouds();
    }



    public void actionPerformed(ActionEvent e) {
        player.move();
        for (Enemy enemy : enemies) {
            enemy.move();
        }
        for (Bullet bullet : bullets) {
            bullet.move();
        }
        for (BossBullet bossBullet : bossBullets) {
            bossBullet.move();
        }


        checkCollisions();
        repaint();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);


        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        
        player.draw(g);

        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }

        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
        
        for (Cloud cloud : clouds) {
            cloud.draw(g);
        }

        for (BossBullet bossBullet : bossBullets) {
            bossBullet.draw(g);
        }


        g.drawString("Score: " + score, 20, 20);

        if(playerdeath){
            g.drawImage(img2,  explosionX, explosionY+10,60,60, this);
        }

    }


    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            player.moveLeft();
        } else if (key == KeyEvent.VK_RIGHT) {
            player.moveRight();
        }
        else if (key == KeyEvent.VK_SPACE) {
            shoot(); // Call the shoot method when the space key is pressed
        }
    }


    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            player.stopMoving();
        }
    }


    public void keyTyped(KeyEvent e) {
        // Not used
    }
}
