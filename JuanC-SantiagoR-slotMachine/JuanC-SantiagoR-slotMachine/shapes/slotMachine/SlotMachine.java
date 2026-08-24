import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * la maquina tragamonedas
 * administra las ruedas y los simbolos pueden girar, consultar estado
 * y preguntar si es ganador
 * @author Juan Felipe Carvajal Moyano, Santiago Rodriguez Reyes
 */
public class SlotMachine
{
    /* ruedas de la maquinas */
    private ArrayList<Wheel> wheels;
    /* simbolos de la maquina */
    private ArrayList<Symbol> symbols;
    /* elementos de shapes */
    private Rectangle machineBody;
    private Rectangle[] windows;
    private Triangle winnerIndicator;
    private boolean isVisible;
    /* dice si es ganador */
    private boolean winner;
    
    /**
     * Crea la maquina vacia.
     */
    public SlotMachine()
    {
        wheels = new ArrayList<Wheel>();
        symbols = new ArrayList<Symbol>();
        isVisible = false;
        winner = false;
        /* vista de la maquina con rectangulos*/
        machineBody = new Rectangle();
        machineBody.changeSize(300, 500);
        machineBody.moveHorizontal(-50);
        machineBody.moveVertical(30);
        machineBody.changeColor("black");
        /* 3 ventanas*/
        windows = new Rectangle[3];
        for (int i = 0; i < windows.length; i++)
        {
            windows[i] = new Rectangle();
            windows[i].changeSize(80, 80);
            windows[i].changeColor("white");
            /* ventanas separadas*/
            windows[i].moveHorizontal(20 + i * 100);
            windows[i].moveVertical(70);
        }
        /*aparece un triangulo amarillo cuando se gana*/
        winnerIndicator = new Triangle();
        winnerIndicator.changeSize(30, 30);
        winnerIndicator.changeColor("yellow");
        /* es inviseble al comienzo */
        machineBody.makeInvisible();
        for (Rectangle window : windows)
        {
            window.makeInvisible();
        }
        winnerIndicator.makeInvisible();
    }

    /**
     * Agrega una rueda a la maquina
     */
    public boolean addWheel()
    {
        /*limite a solo 3 ranuras*/
        if (wheels.size() >= 3)
        {
            showError("No se pueden agregar más de 3 ruedas.");
            return false;
        }
        int positionX = 120 + wheels.size() * 90;
        int positionY = 110;
        Wheel wheel = new Wheel(positionX, positionY);
        /* todos los simbolos pasan a estar en la rueda */
        for (Symbol symbol : symbols)
        {
            Symbol copy = new Symbol(
                symbol.getColor(),
                positionX,
                positionY
            );
            wheel.addSymbol(copy);
        }
        wheels.add(wheel);
        if (isVisible)
        {
            wheel.makeVisible();
        }

        return true;
    }

    /**
     * Elimina la ultima rueda creada
     */
    public boolean removeWheel()
    {
        if (wheels.size() == 0)
        {
            showError("No hay ruedas para eliminar.");
            return false;
        }
        Wheel wheel = wheels.remove(wheels.size() - 1);
        wheel.makeInvisible();
        winner = false;
        return true;
    }


    /**
     * Agrega un nuevo simbolo a la maquina 
     */
    public boolean addSymbol(String color)
    {
        if (color == null || color.length() == 0)
        {
            showError("Debe indicar un color");
            return false;
        }
        /* el color no se puede repetir se verifica*/
        for (Symbol symbol : symbols)
        {
            if (symbol.getColor().equals(color))
            {
                showError("Ese color ya existe");
                return false;
            }
        }
        /* creacion del simbolo*/
        Symbol symbol = new Symbol(color, 0, 0);
        symbols.add(symbol);
        /* copia para cada rueda*/
        for (int i = 0; i < wheels.size(); i++)
        {
            Wheel wheel = wheels.get(i);
            Symbol wheelSymbol = new Symbol(color,50 + i * 100,100);
            wheel.addSymbol(wheelSymbol);
        }
        return true;
    }

    /**
     * Elimina un simbolo de la maquina segun el color
     */
    public boolean removeSymbol(String color)
    {
        if (symbols.size() <= 1)
        {
            showError("La máquina debe conservar al menos un símbolo.");
            return false;
        }
        for (int i = 0; i < symbols.size(); i++)
        {
            if (symbols.get(i).getColor().equals(color))
            {
                symbols.remove(i);
                /* y se elimina en cada rueda*/
                for (Wheel wheel : wheels)
                {
                    wheel.removeSymbol(color);
                }
                winner = false;
                return true;
            }
        }
        showError("El simbolo no existe");
        return false;
    }


    /**
     * Gira todas las ruedas de la máquina.
     */
    public void spin()
    {
        if (wheels.size() == 0)
        {
            showError("No existen ruedas para girar");
            return;
        }
        if (symbols.size() == 0)
        {
            showError("No existen simbolos");
            return;
        }
        /* giran todas las ruedas*/
        for (Wheel wheel : wheels)
        {
            wheel.spin();
        }
        /* despues de girar comprobamos si ganoo*/
        checkWinner();
    }

    /**
     * Gira una rueda especifica
     */
    public void spinWheel(int number)
    {
        if (number < 1 || number > wheels.size())
        {
            showError("La rueda indicada no existe.");
            return;
        }
        wheels.get(number - 1).spin();
        checkWinner();
    }

    /**
     * Consulta los simbolos disponibles
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


    /**
     * Consulta los simbolos que actualmente muestran las ruedas
     */
    public String[] getCurrentSymbols()
    {
        String[] result = new String[wheels.size()];
        for (int i = 0; i < wheels.size(); i++)
        {
            result[i] = wheels.get(i).getCurrentColor();
        }
        return result;
    }


    /**
     * Comprueba si todas las ruedas muestran el mismo simbolo para mirar si es ganador
     */
    public boolean isWinner()
    {
        if (wheels.size() == 0)
        {
            return false;
        }
        String firstColor = wheels.get(0).getCurrentColor();
        if (firstColor.equals(""))
        {
            return false;
        }
        for (int i = 1; i < wheels.size(); i++)
        {
            if (!wheels.get(i).getCurrentColor().equals(firstColor))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Actualiza el estado de la maquina y cambia su apariencia
     */
    private void checkWinner()
    {
        winner = isWinner();
        if (winner)
        {
            winnerIndicator.changeColor("yellow");

            if (isVisible)
            {
                winnerIndicator.makeVisible();
            }
        }
        else
        {
            winnerIndicator.makeInvisible();
        }
    }

    /**
     * Hace visible la maquina
     */
    public void makeVisible()
    {
        isVisible = true;
        machineBody.makeVisible();
        for (Rectangle window : windows)
        {
            window.makeVisible();
        }
        for (Wheel wheel : wheels)
        {
            wheel.makeVisible();
        }
        if (winner)
        {
            winnerIndicator.makeVisible();
        }
    }

    /**
     * Hace invisible la maquina
     */
    public void makeInvisible()
    {
        isVisible = false;
        machineBody.makeInvisible();
        for (Rectangle window : windows)
        {
            window.makeInvisible();
        }
        for (Wheel wheel : wheels)
        {
            wheel.makeInvisible();
        }
        winnerIndicator.makeInvisible();
    }

    /**
     * Indica si la maquina es visible
     */
    public boolean isVisible()
    {
        return isVisible;
    }


    /**
     * Muestra un mensaje de error unicamente cuando la maquina es visible JOptionPane 
     */
    private void showError(String message)
    {
        if (isVisible)
        {
            JOptionPane.showMessageDialog(
                null,
                message,
                "Slot Machine",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    /** Termina el simulador*/
    public void exit()
    {
        /* solo pone invible y quita la lo visual*/
        makeInvisible();
        wheels.clear();
        symbols.clear();
        winner = false;
    }
}