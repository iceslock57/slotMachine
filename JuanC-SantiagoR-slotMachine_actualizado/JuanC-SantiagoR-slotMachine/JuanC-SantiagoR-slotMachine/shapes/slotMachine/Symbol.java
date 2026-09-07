import java.util.List;

/**
 * 
 * @author Juan Felipe Carvajal Moyano, Santiago Rodriguez Reyes
 * @version 1.0
 */

/**
 * symbol es nuestra clase base, se encarga de hacer todos los simbolos que vamos a ver
 * en el slotMachine 
 */
public class Symbol
{
    private int xPosition;
    private int yPosition;
    private boolean isVisible;
    private String color;
    private Circle moneda;
    
    /**
     * Constructor de los objetos para symbol, crea el objeto sirviendose de Circle
     */
    public Symbol(String color, int x, int y)
    {
     this.color = color;
     this.xPosition = x;
     this.yPosition = y;
     this.isVisible = false;
     this.moneda = new Circle();
     int initialX = 20;
     int initialY = 15;
     this.moneda.changeColor(color);
     this.moneda.moveHorizontal(x - initialX);
     this.moneda.moveVertical(y - initialY);
    }

    /**
     * retorna el color que tenga el objeto
     */
    public String getColor()
    {
        return color;
        
    }
    
    /**
     * cambio de coordenadas de posición para el objeto y verifica que el objeto
     * no sea null para hacer el movimiento
     */
    public void setPosition(int x, int y){
        int horizontalDistance = x - xPosition;
        int verticalDistance = y - yPosition;
        xPosition = x;
        yPosition = y;
        if (moneda != null){
            moneda.moveHorizontal(horizontalDistance);
            moneda.moveVertical(verticalDistance);
        }
    }
    
    /**
     * Hace visible el objeto en el canvas
     */
    public void makeVisible(){
        isVisible=true;
        if (moneda != null){
            moneda.makeVisible();
        }
    }
    /**
     * hace invisible el objeto en el canvas
     */
    public void makeInvisible(){
        isVisible=false;
        if (moneda !=null){
             moneda.makeInvisible();   
        }
        }
    /**
     * permite al usuario modificar el color del objeto
     */
    public void changeColor(String change){
        color = change;
        if (moneda != null){
            moneda.changeColor(change);
        }
    }
    
    /** 
    * Dice si simbolo esta visible
    */
    public boolean isVisible()
    {
        return isVisible;
    }
    }
