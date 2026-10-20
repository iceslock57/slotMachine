/**
 * Rueda que no se deja bloquear, intercambiar ni eliminar
 * @author Juan Carvajal Moyano Santiago Rodriguez Reyes 
 */

public class RebelWheel extends Wheel
{
    public RebelWheel(int x, int y)
    {
        super(x, y);
    }

    @Override
    public boolean canLock()
    {
        return false;
    }

    @Override
    public boolean canSwap()
    {
        return false;
    }

    @Override
    public boolean canDelete()
    {
        return false;
    }
}
