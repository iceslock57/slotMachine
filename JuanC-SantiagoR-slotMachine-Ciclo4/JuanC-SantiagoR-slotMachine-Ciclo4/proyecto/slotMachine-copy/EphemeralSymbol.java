/**
 * Simbolo que se encoge cada vez que es seleccionado hasta quedar como un punto
 * 
 * @author Juan Carvajal Moyano-Santiago Rodriguez Reyes
 */


public class EphemeralSymbol extends Symbol
{
    private static final int STEP = 5;
    private static final int MIN_SIZE = 5;
    private int diameter = 30;

    public EphemeralSymbol(String color, int x, int y)
    {
        super(color, x, y);
        resize(diameter);
    }

    public int getDiameter()
    {
        return diameter;
    }

    @Override
    protected void resize(int newSize)
    {
        diameter = Math.max(MIN_SIZE, newSize);
        super.resize(diameter);
    }

    @Override
    public void onSelected()
    {
        if (getDiameter() > MIN_SIZE)
        {
            resize(Math.max(MIN_SIZE, getDiameter() - STEP));
        }
    }

    @Override
    public Symbol copyAt(int x, int y)
    {
        EphemeralSymbol copy = new EphemeralSymbol(getColor(), x, y);
        copy.resize(getDiameter());
        return copy;
    }
}
