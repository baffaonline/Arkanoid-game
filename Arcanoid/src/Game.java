import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;

public class Game extends JFrame {
    private GamePanel gamePanel;
    private PointPanel pointPanel;
    private JLabel arkanoidLabel;
    private JButton startButton;
    private Observable keyObserver;
    public static final int WIDTH = 700;
    public static final int HEIGHT = 500;

    public Game() {
        setLayout(new GridLayout(2, 1));
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        ImageIcon arkanoidImage = new ImageIcon("arkanoid.png");
        ImageIcon startImage = new ImageIcon("start.png");
        arkanoidLabel = new JLabel(arkanoidImage);
        arkanoidLabel.setFocusable(false);
        add(arkanoidLabel);
        startButton = new JButton(startImage);
        startButton.setBackground(Color.gray);
        startButton.addActionListener(e -> startGame());
        startButton.setFocusable(false);
        add(startButton);
        keyObserver = new Observable() {
            @Override
            public void notifyObservers(Object arg) {
                setChanged();
                super.notifyObservers(arg);
            }
        };
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyObserver.notifyObservers(e);
            }
        });
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void startGame() {
        setLayout(new BorderLayout());
        remove(arkanoidLabel);
        remove(startButton);
        pointPanel = new PointPanel(new Dimension(getWidth(), getHeight() / 10));
        add(pointPanel, BorderLayout.NORTH);
        gamePanel = new GamePanel(new Dimension(getWidth(), getHeight() - pointPanel.getHeight()), pointPanel);
        add(gamePanel);
        pointPanel.setFocusable(false);
        gamePanel.setFocusable(false);
        keyObserver.addObserver(gamePanel);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Game());
    }
}