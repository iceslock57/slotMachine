
/**
 *  Simbolo comodin cuenta como cualquier otro al evaluar el jackpot
 * 
 * @author Juan Carvajal Moyano-Santiago Rodriguez Reyes
 */

public class WildSymbol extends Symbol
{
    public WildSymbol(String color, int x, int y)
    {
        super(color, x, y);
    }

    @Override
    public boolean isWild()
    {
        return true;
    }

    @Override
    public Symbol copyAt(int x, int y)
    {
        return new WildSymbol(getColor(), x, y);
    }
}
