import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

//5-16-2020
//Modified Heavily by Andrew Earl. Source from
//https://gamedevelopment.tutsplus.com/tutorials/introduction-to-javafx-for-game-development--cms-23835

public class MainClass extends Application 
{
    public static void main(String[] args) 
    {
        launch(args);
    }

    @Override
    public void start(Stage theStage) 
    {
        theStage.setTitle( "Shoot the targets" );

        Group root = new Group();
        Scene theScene = new Scene( root );
        theStage.setScene( theScene );

        Canvas canvas = new Canvas(1048,720);
        root.getChildren().add( canvas );

        ArrayList<String> input = new ArrayList<String>();

        theScene.setOnKeyPressed(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e)
                {
                    String code = e.getCode().toString();
                    if ( !input.contains(code) )
                        input.add( code );
                }
            });

        theScene.setOnKeyReleased(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e)
                {
                    String code = e.getCode().toString();
                    input.remove( code );
                }
            });

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Font theFont = Font.font( "Times New Roman", FontWeight.BOLD, 24 );
        gc.setFont( theFont );
        gc.setFill( Color.BLUE );
        gc.setStroke( Color.BLACK );
        gc.setLineWidth(1);
        
        Sprite ship = new Sprite();
        ship.setImage("Ship.png");
        ship.setPosition(canvas.getWidth()/2-(ship.getWidth()/2), canvas.getHeight()-ship.getHeight());
        
        ArrayList<Sprite> shotList = new ArrayList<Sprite>();
        Sprite arrow = new Sprite();
    	arrow.setImage("Arrow default.png");
    	shotList.add(arrow);
    	    	
        ArrayList<Sprite> targetList = new ArrayList<Sprite>();
        
        for (int i = 0; i < 20; i++)
        {
            Sprite target = new Sprite();
            target.setImage("Target.png");
            double px = 1000 * Math.random();
            double py = 600 * Math.random();          
            target.setPosition(px,py);
            targetList.add(target);
        }
        
        LongValue lastNanoTime = new LongValue( System.nanoTime() );

        IntValue score = new IntValue(0);

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                // calculate time since last update.
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;
                
                // game logic
                
                for (int i = 0; i < shotList.size(); i++)
                {
                	Sprite shot = shotList.get(i);
                	shot.addVelocity(0,-100);
                }
                
                
                
                //ship.setVelocity(0,0);
                if(input.isEmpty())
                {
                	ship.setVelocity(0,0);
                }
                if (input.contains("LEFT"))
                {
                	if(ship.getPositionX() < 0)
                    {
                    	ship.setPosition(0, canvas.getHeight()-ship.getHeight());
                    }
                	else
                	{
                		ship.addVelocity(-100,0);
                	}
                }
                if (input.contains("RIGHT"))
                {
                	if(ship.getPositionX() > canvas.getWidth()-ship.getWidth())
                    {
                    	ship.setPosition(canvas.getWidth()-ship.getWidth(), canvas.getHeight()-ship.getHeight());
                    }
                	else
                	{
                		ship.addVelocity(100,0);
                	}
                }  
                if (input.contains("UP")) 
                {
//                	Sprite shot = new Sprite();
//                	shotList.add(shot);
                	arrow.setPosition(ship.getPositionX()+(ship.getWidth()/2)-(arrow.getWidth()/2), ship.getPositionY());
                	arrow.setVelocity(0,0);
                }
                if (input.contains("DOWN")) 
                {
                	
                }
                
                ship.update(elapsedTime);
                arrow.update(elapsedTime);
                //arrow.setVelocity(0,0);
                // collision detection
                
                Iterator<Sprite> targetIter = targetList.iterator();
                while ( targetIter.hasNext() )
                {
                    Sprite target = targetIter.next();
                    if ( ship.intersects(target) || arrow.intersects(target))
                    {
                    	
                    	targetIter.remove();
                    	targetList.remove(target);
                        score.value++;
                    }
                }
                
                
                // render
                
                gc.clearRect(0, 0, 1048,720);
                ship.render(gc);
                arrow.render(gc);
                for (int i = 0; i < shotList.size(); i++)
                {
                	Sprite shot = shotList.get(i);
                	shot.render(gc);
                }
                //arrow.render(gc);
                for (Sprite moneybag : targetList )
                {
                	moneybag.render( gc );
                }
                    
                String pointsText = new String();
                if(targetList.isEmpty() == true)
                {
                	pointsText = "You win!: " + (100 * score.value);
                }
                else
                {
                	pointsText = "Points: " + (100 * score.value);
                }
                gc.fillText(pointsText, 0, 36 );
                gc.strokeText(pointsText, 0, 36 );
            }
        }.start();

        theStage.show();
    }
}