package Main;
 import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

class Snake extends JFrame implements KeyListener, Runnable {

    private JPanel p1, p2;
    private JButton[] body = new JButton[200];
    private JButton bonusfood;
    private JTextArea sc;


    private  Point bfp = new Point();

    private int x = 500, y = 300, gu = 3, directionx = 0, directiony = 0, speed = 100, score = 0;
    private int[] bx = new int[300];
    private int[] by = new int[300];
    private  Point[] bodyp = new Point[300];
    //private Point bfp = new Point();
    private Thread myt;
    private boolean food = false, runl = false, runr = true, runu = true, rund = true;
    private  Random r = new Random();
    private JMenuBar mybar;
    private JMenu game, help;

    public void initializeValues() {
        gu = 3;
        by[0] = 100;
        bx[0] = 150;
        directionx = 10;
        directiony = 0;

        score = 0;
        food = false;
        runl = false;
        runr = true;
        runu = true;
        rund = true;

    }

    Snake() {
        super("Snake: PSU edition");
        setBounds(200, 200, 506, 380);
        creatbar();
        initializeValues();
        // GUI панельки
        p1 = new JPanel();
        p2 = new JPanel();
        // Текстовое поле очков
        setResizable(false);
        sc = new JTextArea("Score : " + score);
        sc.setEnabled(false);
        sc.setBounds(400, 400, 100, 100);
        sc.setBackground(Color.black);
        // Змея ест и ростет
        bonusfood = new JButton();
        bonusfood.setEnabled(false);
        // Создаем начальную 3-ех кнопочную змею
        createFirstSnake();

        p1.setLayout(null);
        p2.setLayout(new GridLayout(0, 1));
        p1.setBounds(0, 0, x, y);
        p1.setBackground(Color.cyan);
        p2.setBounds(0, y, x, 30);
        p2.setBackground(Color.black);

        p2.add(sc); // will contain score board
        // end of UI design
        getContentPane().setLayout(null);
        getContentPane().add(p1);
        getContentPane().add(p2);
        show();
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addKeyListener(this);

        myt = new Thread(this);
        myt.start();
    }
    //Создаем начальную змейку(из 3 кнопок)
    public void createFirstSnake() {

        for (int i = 0; i < 3; i++) {
            body[i] = new JButton(" " + i);
            body[i].setEnabled(false);
            p1.add(body[i]);
            body[i].setBounds(bx[i], by[i], 10, 10);
            bx[i + 1] = bx[i] - 10;
            by[i + 1] = by[i];
        }
    }
    //Создаем меню бар
    public void creatbar() {
        mybar = new JMenuBar();

        game = new JMenu("Игра");

        JMenuItem newgame = new JMenuItem("Новая игра");
        JMenuItem exit = new JMenuItem("Выход");
        newgame.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        reset();
                    }
                });

        exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        game.add(newgame);
        game.addSeparator();//Разделитель
        game.add(exit);
    }
    //Начало новой игры
    void reset() {
        initializeValues();
        p1.removeAll();

        myt.stop();

        createFirstSnake();
        sc.setText("Score: " + score);

        myt = new Thread(this);
        myt.start();
    }
    //кушаем растем
    void growup() {
        body[gu] = new JButton(" " + gu);
        body[gu].setEnabled(false);
        p1.add(body[gu]);
        body[gu].setBounds(bodyp[gu-1].x, bodyp[gu-1].y, 10, 10);
        gu++;
    }

    /**
     *
     */
    void moveForward() {
        for(int i = 0; i < gu; i++) {
            bodyp[i] = body[i].getLocation();
        }

        bx[0] += directionx;
        by[0] += directiony;
        body[0].setBounds(bx[0], by[0], 10, 10);
// Для эффекта летницы
        for (int i = 1; i < gu; i++) {
            body[i].setLocation(bodyp[i - 1]);
        }

        if (bx[0] == x) {
            myt.stop();

        } else if (bx[0] == 0) {

            myt.stop();
        } else if (by[0] == y) {

            myt.stop();
        } else if (by[0] == 0) {

            myt.stop();
        }



        if (food == false) {
            p1.add(bonusfood);
            bonusfood.setBounds((10 * r.nextInt(50)), (10 * r.nextInt(25)), 10, 10);
            bfp = bonusfood.getLocation();
            food = true;
        }


        if (food == true) {
            if (bfp.x == bx[0] && bfp.y == by[0]) {
                p1.remove(bonusfood);
                score += 1;
                growup();
                sc.setText("Scores: " + score);
                food = false;

            }
        }
        p1.repaint();

    }

    public void keyPressed(KeyEvent e) {
        // sвлево
        if (runl == true && e.getKeyCode() == 37) {
            directionx = -10;
            directiony = 0;
            runr = false;
            runu = true;
            rund = true;
        }
        //вверх
        if (runu == true && e.getKeyCode() == 38) {
            directionx = 0;
            directiony = -10;
            rund = false;
            runr = true;
            runl = true;
        }
        // вправо
        if (runr == true && e.getKeyCode() == 39) {
            directionx = +10;
            directiony = 0;
            runl = false;
            runu = true;
            rund = true;
        }
        // вниз
        if (rund == true && e.getKeyCode() == 40) {
            directionx = 0;
            directiony = +10;
            runu = false;
            runr = true;
            runl = true;
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void run() {
        while(true) {

            moveForward();
            try {
                Thread.sleep(speed);
            } catch (InterruptedException ie) {
            }
        }
    }
}
