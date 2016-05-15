import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.stage.WindowEvent;
import java.util.Random;
import javafx.scene.media.AudioClip;
import java.net.URL;
import javafx.scene.input.MouseEvent;

import java.awt.event.*;
import javafx.scene.shape.ArcType;


public class ClientRunner extends Application
{
    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    Random random = new Random();
    Canvas canvas = new Canvas(WIDTH, HEIGHT);

    static long lastNanoTime = System.nanoTime();    
    
    Point mouse = new Point();
    Point center = new Point(WIDTH/2, HEIGHT/2);
    Point runner = new Point();
    double runnerAngle = 0;
    boolean paused;

    public static void main(String[] args) 
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) 
    {

        stage.setTitle("Incredible Next Gen Title");        
        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        root.getChildren().add(canvas);

        ArrayList<String> input = new ArrayList<String>();

        // INITIATION
        
        scene.setOnKeyPressed(new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e)
                {
                    paused = !paused;
                }
            });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e)
                {         
                }
            });
        scene.setOnMousePressed(new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent e)
                {
                }

            });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent e)
                {
                    mouse = new Point(e.getX(), e.getY());
                }
            });

        GraphicsContext g = canvas.getGraphicsContext2D();

        LongValue lastNanoTime = new LongValue(System.nanoTime());

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;      
                
                double angle = center.angle(mouse);
                if(!paused)
                runnerAngle+= Math.toRadians(15)*elapsedTime;
                if(runnerAngle >= Math.PI)
                runnerAngle-= Math.PI*2;
                
                runner = new Point(center.getX() + 100 * Math.cos(runnerAngle), 
                    center.getY() + 100 * Math.sin(runnerAngle));
                
                g.setFill(Color.BLACK);
                g.fillRect(0, 0, WIDTH, HEIGHT);
                g.setFill(Color.WHITE);
                g.setStroke(Color.WHITE);
                g.strokeLine(center.getX(), center.getY(), center.getX() + 100 * Math.cos(angle),
                    center.getY() + 100 * Math.sin(angle));
                    
                    
                g.fillOval(runner.getX() - 4, runner.getY() - 4, 8, 8);
                g.fillText("Angle = "+angle, 16, 16);
                g.fillText("Target angle = "+runnerAngle, 16, 32);
                g.setStroke(Color.RED);
                g.strokeLine(center.getX(), center.getY(), runner.getX(), runner.getY());
                g.fillText("Clock direction = "+center.clockDirection(runner, angle), 16, 48);
                g.fillText("Angle difference= "+Math.toDegrees(center.angleDifference(runner, angle)), 16, 64);
                
            }
        }.start();

        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>()
            {
                @Override
                public void handle(WindowEvent e)
                {
                    System.exit(1);
                }
            });
    }
}