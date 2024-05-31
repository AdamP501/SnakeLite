/**
 Adam Pei
 31st May 2024
 Snake Lite
 Help given by Ms Nahar, CS@CIS, Sander Day, w3schools, GeeksForGeeks, GPT 4, Stanford CS page, and Max O'Didily's youtube channel
 This is a snake game
 **/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import java.util.Random;
import acm.graphics.*;
import acm.program.GraphicsProgram;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.Timer;
import java.util.Random;
import acm.graphics.GObject;
import java.awt.event.MouseListener;
import acm.graphics.GLabel;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import javax.sound.sampled.*;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;




public class MainClass extends GraphicsProgram implements ActionListener
{
    public GOval fruit;
    private ArrayList<GRect> snakeBody;
    public Timer timer = new Timer(100, this);
    private int score;
    private GLabel scoreLabel;
    private Color snakeColor = Color.GREEN;
    private Color backgroundColor = Color.BLUE;
    boolean blockKey = false;
    boolean goingUp = false;
    boolean goingRight = false;
    boolean goingDown = false;
    boolean goingLeft = false;
    GLabel gameTitle = new GLabel("Snake Lite");

    public void run() {
        setUpMainMenu();
    }

    //Credit to Sander, Stanford CS Page, and GPT 4 for the help.
    private void setUpMainMenu() {
        removeAll(); // Clear the canvas to ensure only the main menu is showing
        setSize(500,500);

        setBackground(Color.GRAY);

        GLabel title = new GLabel("Snake Lite");
        title.setFont(new Font("Monospaced", Font.BOLD, 36));
        add(title, getWidth() / 2 - title.getWidth() / 2, getHeight() / 5);

        GRect playButton = new GRect(getWidth() / 2 - 75, getHeight() / 2 - 100, 150, 50);
        playButton.setFilled(true);
        playButton.setFillColor(Color.WHITE);
        add(playButton);

        GLabel playLabel = new GLabel("Play");
        playLabel.setFont(new Font("Monospaced", Font.BOLD, 17));
        add(playLabel, playButton.getX() + (playButton.getWidth() - playLabel.getWidth()) / 2, playButton.getY() + (playButton.getHeight() + playLabel.getAscent()) / 2);

        GRect instructionsButton = new GRect(getWidth() / 2 - 75, getHeight() / 2 - 25, 150, 50);
        instructionsButton.setFilled(true);
        instructionsButton.setFillColor(Color.WHITE);
        add(instructionsButton);

        GLabel instructionsLabel = new GLabel("Instructions");
        instructionsLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        add(instructionsLabel, instructionsButton.getX() + (instructionsButton.getWidth() - instructionsLabel.getWidth()) / 2, instructionsButton.getY() + (instructionsButton.getHeight() + instructionsLabel.getAscent()) / 2);

        GRect snakeColorButton = new GRect(getWidth() / 2 - 75, getHeight() / 2 + 50, 150, 50);
        snakeColorButton.setFilled(true);
        snakeColorButton.setFillColor(Color.WHITE);
        add(snakeColorButton);

        GLabel snakeColorLabel = new GLabel("Change Snake Color");
        snakeColorLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        add(snakeColorLabel, snakeColorButton.getX() + (snakeColorButton.getWidth() - snakeColorLabel.getWidth()) / 2, snakeColorButton.getY() + (snakeColorButton.getHeight() + snakeColorLabel.getAscent()) / 2);

        GRect backgroundColorButton = new GRect(getWidth() / 2 - 75, getHeight() / 2 + 125, 150, 50);
        backgroundColorButton.setFilled(true);
        backgroundColorButton.setFillColor(Color.WHITE);
        add(backgroundColorButton);

        GLabel backgroundColorLabel = new GLabel("Change Background Color");
        backgroundColorLabel.setFont(new Font("Monospaced", Font.BOLD, 10));
        add(backgroundColorLabel, backgroundColorButton.getX() + (backgroundColorButton.getWidth() - backgroundColorLabel.getWidth()) / 2, backgroundColorButton.getY() + (backgroundColorButton.getHeight() + backgroundColorLabel.getAscent()) / 2);

        addMouseListeners(playButton, playLabel, instructionsButton, instructionsLabel, snakeColorButton, snakeColorLabel, backgroundColorButton, backgroundColorLabel);
    }

    /*adds functionality to the buttons - mouse clicks will trigger certain actions.
    Credit to GPT 4 for the help*/
    private void addMouseListeners(GRect playButton, GLabel playLabel, GRect instructionsButton, GLabel instructionsLabel, GRect snakeColorButton, GLabel snakeColorLabel, GRect backgroundColorButton, GLabel backgroundColorLabel) {
        getGCanvas().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                GObject obj = getElementAt(e.getX(), e.getY());
                if (obj == playButton || obj == playLabel) {
                    startGame();
                } else if (obj == instructionsButton || obj == instructionsLabel) {
                    displayInstructions();
                } else if (obj == snakeColorButton || obj == snakeColorLabel) {
                    changeSnakeColor();
                } else if (obj == backgroundColorButton || obj == backgroundColorLabel) {
                    changeBackgroundColor();
                }
            }
        });
    }

    //Credit to GeeksForGeeks for the help
    private void displayInstructions() {
        JOptionPane.showMessageDialog(this, "This is a snake game. Use arrow keys to move. Eat fruit to increase the length of the snake and add points to the scoreboard. The game ends if the snake crashes into the wall or itself", "Instructions", JOptionPane.INFORMATION_MESSAGE);
    }

    //Credit to Max O'Didily for the help.
    public static void playSound(String location)
    {
        try
        {
            File musicPath = new File(location);

            if(musicPath.exists())
            {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            }
            else {
                System.out.println("can't find file");
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }


    /*Shows a drop-down menu of colours for the user to choose from.
    Credit to GeeksForGeeks, w3schools, and GPT 4 for the help*/
    private void changeSnakeColor() {
        String[] colors = {"Red", "Yellow", "Blue", "Green", "White", "Black"};
        String color = (String) JOptionPane.showInputDialog(this, "Select Snake Color", "Snake Color", JOptionPane.PLAIN_MESSAGE, null, colors, colors[0]);
        switch (color) {
            case "Red":
                snakeColor = Color.RED;
                break;
            case "Yellow":
                snakeColor = Color.YELLOW;
                break;
            case "Blue":
                snakeColor = Color.BLUE;
                break;
            case "Green":
                snakeColor = Color.GREEN;
                break;
            case "White":
                snakeColor = Color.WHITE;
                break;
            case "Black":
                snakeColor = Color.BLACK;
                break;
        }
    }

    /*Shows a drop-down menu of colours for the user to choose from
    Credit to w3schools and GPT 4 for the help */
    private void changeBackgroundColor() {
        String[] colors = {"Red", "Yellow", "Blue", "Green", "White", "Black"};
        String color = (String) JOptionPane.showInputDialog(this, "Select Background Color", "Background Color", JOptionPane.PLAIN_MESSAGE, null, colors, colors[0]);
        switch (color) {
            case "Red":
                backgroundColor = Color.RED;
                break;
            case "Yellow":
                backgroundColor = Color.YELLOW;
                break;
            case "Blue":
                backgroundColor = Color.BLUE;
                break;
            case "Green":
                backgroundColor = Color.GREEN;
                break;
            case "White":
                backgroundColor = Color.WHITE;
                break;
            case "Black":
                backgroundColor = Color.BLACK;
                break;
        }
    }

    //Credit to CS@CIS webpage for the help
    private void startGame() {
        removeAll(); // Clear the canvas to start the game
        timer.start();
        goingUp = false;
        goingRight = false;
        goingDown = false;
        goingLeft = false;
        addKeyListeners();
        setBackground(backgroundColor);
        gameTitle.setFont(new Font("Monospaced", Font.BOLD, 25));
        setCanvasSize(500, 500);
        fruit = new GOval(50, 50, 15, 15);
        fruit.setColor(Color.RED);
        fruit.setFillColor(Color.RED);
        fruit.setFilled(true);
        randomFood();
        add(fruit);
        setUpInfo();
        add(gameTitle, getWidth() / 2, 20);
        snakeBody = new ArrayList<>();
        drawSnake();
        timer.start();
    }

    /*Spawn the food at random positions on the screen
    Credit to CS@CIS webpage for the help*/
    public void randomFood()
    {
        Random random = new Random();
        int randX = random.nextInt(getGCanvas().getWidth()-30); //the -30 ensures the full ball is visible on the screen
        int randY = random.nextInt(getGCanvas().getHeight()-30);
        fruit.setLocation(randX, randY);
    }


    /*Show the score
    Credit to CS@CIS webpage for the help*/
    public void setUpInfo()
    {
        score = 0;
        scoreLabel = new Scoreboard("Score: " + score, 50, 50);
        scoreLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        add(scoreLabel);
    }

    /*draw the initial snake
    Credit to CS@CIS webpage for the help*/
    public void drawSnake()
    {
        int x = 0;
        for (int i = 0; i < 3; i++)
        {
            GRect part = new GRect(250 + x,250, 20, 20);
            add(part);
            part.setFilled(true);
            part.setFillColor(snakeColor);
            part.setColor(snakeColor);
            snakeBody.add(part);
            x+=21;
        }
    }


    /*Make sure the snake is still moving after the user lets go of the arrow keys
    Credit to CS@CIS webpage for the help*/
    @Override
    public void keyReleased (KeyEvent e)
    {
        if (blockKey)
        {
            blockKey = false;
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_UP:
                    if (!goingDown)
                    {
                        goingUp = true;
                        goingRight = false;
                        goingLeft = false;
                        goingDown = false;
                        break;
                    }

                case KeyEvent.VK_DOWN:
                    if (!goingUp)
                    {
                        goingUp = false;
                        goingRight = false;
                        goingLeft = false;
                        goingDown = true;
                        break;
                    }

                case KeyEvent.VK_LEFT:
                    if (!goingRight)
                    {
                        goingUp = false;
                        goingRight = false;
                        goingLeft = true;
                        goingDown = false;
                        break;
                    }

                case KeyEvent.VK_RIGHT:
                    if (!goingLeft)
                    {
                        goingUp = false;
                        goingRight = true;
                        goingLeft = false;
                        goingDown = false;
                        break;
                    }
            }
        }
    }
    public void keyPressed(KeyEvent keyPressed)
    {
        blockKey = true;
    }

    //delete the snake's tail
    private void redrawSnake()
    {
        GRect tail = snakeBody.remove(snakeBody.size()-1);
        remove(tail);
    }


    //Credit to GPT 4 for the logic
    private void growSnake()
    {
        GRect tail = snakeBody.get(snakeBody.size()-1);
        double tailX = tail.getX();
        double tailY = tail.getY();
        GRect newTail = new GRect(tailX, tailY, 20, 20);
        newTail.setFilled(true);
        newTail.setFillColor(snakeColor);
        newTail.setColor(snakeColor);
        snakeBody.add(newTail);
    }

    private boolean snakeCrash()
    {
        GRect head = snakeBody.get(0);
        double headX = head.getX();
        double headY = head.getY();

        //Check if snake crashed into wall
        if (headX < 0 || headX + head.getWidth() > getWidth() || headY < 0 || headY + head.getHeight() > getHeight())
        {
            return true;
        }

        //Check if snake crashed into itself
        for (int i = 1; i < snakeBody.size(); i++) {
            GRect bodyPart = snakeBody.get(i);
            if (head.getBounds().intersects(bodyPart.getBounds())) {
                return true;
            }
        }
        return false;
    }

    //move the snake head up
    private void moveUp()
    {
        GRect head = snakeBody.get(0);
        double headX = head.getX();
        double headY = head.getY();
        GRect newHead = new GRect(headX,headY - 21, 20, 20);
        newHead.setFillColor(snakeColor);
        newHead.setColor(snakeColor);
        newHead.setFilled(true);
        add(newHead);
        snakeBody.add(0, newHead);
    }

    //move the snake head down
    private void moveDown()
    {
        GRect head = snakeBody.get(0);
        double headX = head.getX();
        double headY = head.getY();
        GRect newHead = new GRect(headX,headY + 21, 20, 20);
        newHead.setFillColor(snakeColor);
        newHead.setColor(snakeColor);
        newHead.setFilled(true);
        add(newHead);
        snakeBody.add(0, newHead);
    }

    //move the snake head left
    private void moveLeft()
    {
        GRect head = snakeBody.get(0);
        double headX = head.getX();
        double headY = head.getY();
        GRect newHead = new GRect(headX-21,headY, 20, 20);
        newHead.setFillColor(snakeColor);
        newHead.setColor(snakeColor);
        newHead.setFilled(true);
        add(newHead);
        snakeBody.add(0, newHead);
    }

    //move the snake head right
    private void moveRight()
    {
        GRect head = snakeBody.get(0);
        double headX = head.getX();
        double headY = head.getY();
        GRect newHead = new GRect(headX+21,headY, 20, 20);
        newHead.setFillColor(snakeColor);
        newHead.setColor(snakeColor);
        newHead.setFilled(true);
        add(newHead);
        snakeBody.add(0, newHead);
    }

    //Show the final score and ask the user if they want to play again when the snake crashes
    public void gameOver() {
        playSound("bang1.wav");
        timer.stop();
        int response = JOptionPane.showConfirmDialog(this, "Game Over! Final Score: " + score + "\nDo you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            startGame();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        if (snakeCrash())
        {
            gameOver();
        }
        if (goingUp) {
            moveUp();
            redrawSnake();
        } else if (goingDown) {
            moveDown();
            redrawSnake();
        } else if (goingLeft) {
            moveLeft();
            redrawSnake();
        } else if (goingRight) {
            moveRight();
            redrawSnake();
        }
        GRect head = snakeBody.get(0);
        if (head.getBounds().intersects(fruit.getBounds())) {
            playSound("chomp1.wav");
            randomFood();
            growSnake();
            score++;
            scoreLabel.setLabel("Score: " + score);
        }
        if (snakeCrash())
        {
            gameOver();
        }
    }

    public static void main(String[] args)
    {
        new MainClass().start();
    }
}

