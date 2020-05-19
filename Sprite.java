import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;

public class Sprite
{
    private Image image;
    private double positionX;
    private double positionY;    
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;
    private final double maxSpeed = -1000;
 
    public Sprite()
    {
        positionX = 0;
        positionY = 0;    
        velocityX = 0;
        velocityY = 0;
    }

    public void setImage(Image i)
    {
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }

    public void setImage(String filename)
    {
        Image i = new Image(filename);
        setImage(i);
    }

    public void setPosition(double x, double y)
    {
        positionX = x;
        positionY = y;
    }

    public void setVelocity(double x, double y)
    {
    	velocityX = x; 
    	velocityY = y;
    }

    public void addVelocity(double x, double y)
    {
    	if(velocityX > Math.abs(maxSpeed) || velocityX < maxSpeed)
    	{
    		if(velocityX > Math.abs(maxSpeed))
    		{
    			velocityX = Math.abs(maxSpeed);
    		}
    		else
    		{
    			velocityX = maxSpeed;
    		}
    	}
    	else
    	{
    		velocityX += x;
    	}
    	if(velocityY > Math.abs(maxSpeed) || velocityY < maxSpeed)
    	{
    		if(velocityY > Math.abs(maxSpeed))
    		{
    			velocityY = Math.abs(maxSpeed);
    		}
    		else
    		{
    			velocityY = maxSpeed;
    		}
    	}
    	else
    	{
    		velocityY += y;
    	}
    }
    public double getPositionX()
    {
    	return positionX;
    }
    public double getPositionY()
    {
    	return positionY;
    }
    public double getHeight()
    {
    	return height;
    }
    public double getWidth()
    {
    	return width;
    }
    public void update(double time)
    {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }
 
    public void render(GraphicsContext gc)
    {
        gc.drawImage( image, positionX, positionY );
    }
 
    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(positionX,positionY,width,height);
    }
 
    public boolean intersects(Sprite s)
    {
        return s.getBoundary().intersects( this.getBoundary() );
    }
}