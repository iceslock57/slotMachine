import java.util.ArrayList;
import java.util.Random;

/**
 * Representa una de las ruedas de la maquina tragamonedas
 * una rueda contiene varios simbolos y mantiene uno de ellos
 * como el simbolo actualmente mostrado
 * @author Juan Felipe Carvajal Moyano, Santiago Rodriguez Reyes
 * 
 */
public class Wheel
{
    /* lista de simbolos de la rueda */
    private ArrayList<Symbol> symbols;
    private int xPosition;
    private int yPosition;
    /* indice simbolo */
    private int currentSymbol;
    private boolean isVisible;
    /* generado giros aleatorios */
    private Random random;


    /**
     * Crea una rueda vacia
     */
    public Wheel(int x, int y)
    {
        symbols = new ArrayList<Symbol>();
        xPosition = x;
        yPosition = y;
        currentSymbol = 0;
        isVisible = false;
        random = new Random();
    }

    /**
     * Agrega un simbolo a la rueda
     */
    public void addSymbol(Symbol symbol)
    {
        if (symbol != null)
        {
            symbol.setPosition(xPosition, yPosition);
            symbols.add(symbol);
            /* simbolo actual si es el primer simbolo */
            if (symbols.size() == 1)
            {
                currentSymbol = 0;
                if (isVisible)
                {
                    symbol.makeVisible();
                }
            }
            
            else{
                symbol.makeInvisible();
            }
            
        }
    }


    /**
     * Elimina un simbolo de la rueda
     */
    public boolean removeSymbol(String color)
    {
        for (int i = 0; i < symbols.size(); i++)
        {
            if (symbols.get(i).getColor().equals(color))
            {
                Symbol symbol = symbols.get(i);
                symbol.makeInvisible();
                symbols.remove(i);
                /* indice se quedo fuera de listo vuelve primer simbol */
                if (symbols.size() == 0)
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

    /**
     * Gira la rueda y selecciona aleatoriamente uno de sus simbolos
     */
    public void spin()
    {
       if (symbols.size() == 0)
        {
            return;
        }
        /* ocultamos el simbolo que se estaba mostrando*/
        symbols.get(currentSymbol).makeInvisible();
        /* seleccionamos otro símbolo aleatoriamente*/
        currentSymbol = random.nextInt(symbols.size());
        /* mostramos el nuevo simbolo en la misma posicion*/
        updateVisibleSymbol();
    }

    /**
     * Actualiza el símbolo que debe verse.
     */
    private void updateVisibleSymbol()
    {
        if (symbols.size() > 0)
        {
            Symbol symbol = symbols.get(currentSymbol);
            symbol.setPosition(xPosition, yPosition);
            if (isVisible)
            {
                symbol.makeVisible();
            }
        }
    }

    /**
     * Obtiene el color del simbolo actualmente mostrado
     */
    public String getCurrentColor()
    {
        if (symbols.size() == 0)
        {
            return "";
        }
        return symbols.get(currentSymbol).getColor();
    }

    /**
     * Hace visible la rueda
     */
    public void makeVisible()
    {
        isVisible = true;
        if (symbols.size() > 0)
        {
            symbols.get(currentSymbol).makeVisible();
        }
    }


    /**
     * Hace invisible la rueda
     */
    public void makeInvisible()
    {
        isVisible = false;

        for (Symbol symbol : symbols)
        {
            symbol.makeInvisible();
        }
    }


    /**
     * Obtiene el numero de simbolos de la rueda
     */
    public int symbolCount()
    {
        return symbols.size();
    }


    /**
     * Obtiene los colores de los simbolos de la rueda
     */
    public String[] getSymbols()
    {
        String[] result = new String[symbols.size()];
        for (int i = 0; i < symbols.size(); i++)
        {
            result[i] = symbols.get(i).getColor();
        }
        return result;
    }
}