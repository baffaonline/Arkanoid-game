import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class PointPanel extends JPanel implements Observer {
    private int score;
    private int lives;
    private int level;
    private JLabel scoreLabel, livesLabel, levelLabel;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public PointPanel(Dimension dimension) {
        setLayout(new GridLayout(1, 3));
        setBackground(Color.GRAY);
        setSize(dimension);
        score = 0;
        lives = 3;
        levelLabel = new JLabel("Level: " + 1);
        levelLabel.setFont(new Font(Font.SERIF, Font.CENTER_BASELINE, 30));
        levelLabel.setForeground(Color.WHITE);
        add(levelLabel);

        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font(Font.SERIF, Font.CENTER_BASELINE, 30));
        scoreLabel.setForeground(Color.WHITE);
        add(scoreLabel);

        livesLabel = new JLabel("Lives: " + lives);
        livesLabel.setFont(new Font(Font.SERIF, Font.CENTER_BASELINE, 30));
        livesLabel.setForeground(Color.WHITE);
        add(livesLabel);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    @Override
    public void update(Observable o, Object arg) {
        String message = (String) arg;
        if (message.compareTo("Game over") == 0){
            lives--;
            score = 0;
            scoreLabel.setText("Score: " + score);
            livesLabel.setText("Lives: " + lives);
        }
        else if (message.compareTo("Add score") == 0){
            score++;
            scoreLabel.setText("Score: " + score);
        }
        else if (message.compareTo("New game") == 0){
            score = 0;
            lives = 3;
            scoreLabel.setText("Score: " + score);
            livesLabel.setText("Lives: " + lives);
        }
        revalidate();
        repaint();
    }
}
