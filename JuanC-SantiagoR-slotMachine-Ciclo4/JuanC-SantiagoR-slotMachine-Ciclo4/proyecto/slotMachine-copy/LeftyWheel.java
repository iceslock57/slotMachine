
/**
 * Rueda que, al girar, copia el estado de la rueda de su izquierda
 * 
 * @author Juan Cavajal Moyano-Santiago Rodriguez Reyes
 */

public class LeftyWheel extends Wheel
{
    public LeftyWheel(int x, int y)
    {
        super(x, y);
    }

    public boolean spin(Wheel leftWheel)
    {
        if (leftWheel == null)
        {
            return false;
        }

        String color = leftWheel.getCurrentColor();

        if (color == null || color.isEmpty())
        {
            return false;
        }

        return selectSymbol(color);
    }
}
