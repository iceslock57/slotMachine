import java.util.ArrayList;
import java.util.Random;

/**
 * Representa una rueda de la maquina.
 *
 * Una rueda contiene varios simbolos y mantiene uno de ellos
 * como el simbolo actualmente seleccionado.
 *
 * @author Juan Felipe Carvajal Moyano, Santiago Rodriguez Reyes
 */

import java.util.ArrayList;
import java.util.Random;

public class Wheel
{
    private ArrayList<Symbol> symbols;
    private int xPosition;
    private int yPosition;
    private int currentSymbol;
    private boolean isVisible;
    private Random random;

    public Wheel(int x, int y)
    {
        symbols = new ArrayList<Symbol>();
        xPosition = x;
        yPosition = y;
        currentSymbol = 0;
        isVisible = false;
        random = new Random();
    }

    public void addSymbol(Symbol symbol)
    {
        if (symbol == null)
        {
            return;
        }

        symbol.setPosition(xPosition, yPosition);
        symbol.makeInvisible();
        symbols.add(symbol);

        if (symbols.size() == 1)
        {
            currentSymbol = 0;
            updateVisibleSymbol();
        }
    }

    public boolean removeSymbol(String color)
    {
        for (int i = 0; i < symbols.size(); i++)
        {
            if (symbols.get(i).getColor().equals(color))
            {
                symbols.get(i).makeInvisible();
                symbols.remove(i);

                if (symbols.isEmpty())
                {
                    currentSymbol = 0;
                }
                else if (currentSymbol >= symbols.size())
                {
                    currentSymbol = 0;
                }

                updateVisibleSymbol();
                return true;
            }
        }

        return false;
    }

    public void spin()
    {
        if (symbols.isEmpty())
        {
            return;
        }

        selectIndex(random.nextInt(symbols.size()));
    }

    public void spin(int steps)
    {
        if (symbols.isEmpty())
        {
            return;
        }

        int next = Math.floorMod(currentSymbol + steps, symbols.size());
        selectIndex(next);
    }

    public boolean selectSymbol(String color)
    {
        for (int i = 0; i < symbols.size(); i++)
        {
            if (symbols.get(i).getColor().equals(color))
            {
                selectIndex(i);
                return true;
            }
        }

        return false;
    }

    protected void selectIndex(int index)
    {
        if (symbols.isEmpty())
        {
            return;
        }

        if (currentSymbol < symbols.size())
        {
            symbols.get(currentSymbol).makeInvisible();
        }

        currentSymbol = Math.floorMod(index, symbols.size());
        symbols.get(currentSymbol).onSelected();
        updateVisibleSymbol();
    }

    private void updateVisibleSymbol()
    {
        if (symbols.isEmpty())
        {
            return;
        }

        Symbol symbol = symbols.get(currentSymbol);
        symbol.setPosition(xPosition, yPosition);

        if (isVisible)
        {
            symbol.makeVisible();
        }
        else
        {
            symbol.makeInvisible();
        }
    }

    public void setPosition(int x, int y)
    {
        xPosition = x;
        yPosition = y;

        for (Symbol symbol : symbols)
        {
            symbol.setPosition(x, y);
        }
    }

    public String getCurrentColor()
    {
        if (symbols.isEmpty())
        {
            return "";
        }

        return symbols.get(currentSymbol).getColor();
    }

    public boolean isCurrentWild()
    {
        return !symbols.isEmpty()
            && symbols.get(currentSymbol).isWild();
    }

    public void makeVisible()
    {
        isVisible = true;
        updateVisibleSymbol();
    }

    public void makeInvisible()
    {
        isVisible = false;

        for (Symbol symbol : symbols)
        {
            symbol.makeInvisible();
        }
    }

    public int symbolCount()
    {
        return symbols.size();
    }

    public String[] getSymbols()
    {
        String[] result = new String[symbols.size()];

        for (int i = 0; i < symbols.size(); i++)
        {
            result[i] = symbols.get(i).getColor();
        }

        return result;
    }

    public boolean canLock()
    {
        return true;
    }

    public boolean canSwap()
    {
        return true;
    }

    public boolean canDelete()
    {
        return true;
    }
    public boolean hasSymbol(String color)
    {
    if (color == null)
    {
        return false;
    }

    for (Symbol symbol : symbols)
    {
        if (color.equals(symbol.getColor()))
        {
            return true;
        }
    }

    return false;
    }
    public boolean showColor(String color)
    {
    if (color == null)
    {
        return false;
    }

    for (int i = 0; i < symbols.size(); i++)
    {
        if (color.equals(symbols.get(i).getColor()))
        {
            selectIndex(i);
            return true;
        }
    }

    return false;
    }
}
