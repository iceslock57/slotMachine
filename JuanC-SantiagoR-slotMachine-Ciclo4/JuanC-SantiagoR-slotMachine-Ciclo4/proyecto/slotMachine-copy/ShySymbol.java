
/**
 * cada vez que es seleccionado alterna entre visible e invisible
 * 
 * @author Juan Carvajal Moyano-Santiago Rodriguez Reyes
 */

public class ShySymbol extends Symbol
{
    private boolean shyVisible = true;

    public ShySymbol(String color, int x, int y)
    {
        super(color, x, y);
    }

    @Override
    public void onSelected()
    {
        shyVisible = !shyVisible;

        if (shyVisible)
        {
            super.makeVisible();
        }
        else
        {
            super.makeInvisible();
        }
    }

    @Override
    public void makeVisible()
    {
        if (shyVisible)
        {
            super.makeVisible();
        }
        else
        {
            super.makeInvisible();
        }
    }

    @Override
    public Symbol copyAt(int x, int y)
    {
        ShySymbol copy = new ShySymbol(getColor(), x, y);

        if (!shyVisible)
        {
            copy.onSelected();
        }

        return copy;
    }
}
