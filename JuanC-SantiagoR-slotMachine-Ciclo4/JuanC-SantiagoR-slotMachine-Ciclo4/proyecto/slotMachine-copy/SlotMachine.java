import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Representa una maquina tragamonedas adaptada para el Ciclo 3.
 * @author Juan Felipe Carvajal Moyano, Santiago Rodriguez Reyes
 * @version 3.0
 */
public class SlotMachine
{
    private ArrayList<Wheel> wheels;
    private ArrayList<Symbol> symbols;
    /* Indica si cada rueda esta bloqueada */
    private ArrayList<Boolean> lockedWheels;
    /* Cuerpo de la maquina */
    private Rectangle machineBody;
    /* Precondicion maximo de 10 ventanas */
    private Rectangle[] windows;
    private Triangle winnerIndicator;
    private boolean isVisible;
    /* Indica si la configuracion actual es jackpot */
    private boolean winner;
    /* Indica si la ultima operacion fue correcta */
    private boolean ok;

    /**
     * Crea una maquina tragamonedas vacia
     */
    public SlotMachine()
    {
        wheels = new ArrayList<Wheel>();
        symbols = new ArrayList<Symbol>();
        lockedWheels = new ArrayList<Boolean>();
        windows = new Rectangle[10];
        isVisible = false;
        winner = false;
        ok = true;
        
        /* Creacion cuerpo de la maquina */
        machineBody = new Rectangle();
        machineBody.changeSize(220, 750);
        machineBody.moveHorizontal(100);
        machineBody.moveVertical(100);
        machineBody.changeColor("black");
        
        /* Indicador de jackpot */
        winnerIndicator = new Triangle();
        winnerIndicator.changeSize(30, 30);
        winnerIndicator.changeColor("yellow");
        
        /* Al comenzar todo es invisible */
        machineBody.makeInvisible();
        winnerIndicator.makeInvisible();
    }

    /**
     * REQUISITO 13: Crea una maquina tragamonedas con n ruedas y n simbolos
     * inicializados de forma aleatoria.
     * @param n Cantidad de ruedas y simbolos (1 <= n <= 10)
     */
    public SlotMachine(int n)
    {
        this();
        if (n < 1 || n > 10)
        {
            ok = false;
            showError("El numero n debe estar entre 1 y 10.");
            return;
        }

        String[] baseColors = {"red", "blue", "green", "yellow", "orange", "magenta", "black", "cyan", "pink", "gray"};
        
        // 1. Agregar n simbolos con colores distintos
        for (int i = 1; i <= n; i++)
        {
            addSymbol(i, baseColors[i - 1]);
        }

        // 2. Agregar n ruedas
        for (int i = 1; i <= n; i++)
        {
            addWheel(i);
        }

        // 3. Desordenar o girar aleatoriamente las ruedas
        Random random = new Random();
        for (int i = 1; i <= n; i++)
        {
            int steps = random.nextInt(n);
            if (steps > 0)
            {
                spin(i, steps);
            }
        }
        
        ok = true;
        checkWinner();
    }

    /**
     * Retorna si la ultima operacion fue exitosa.
     * Exigido por el diagrama de clases del Ciclo 3.
     * @return true si la ultima operacion fue correcta.
     */
    public boolean ok()
    {
        return ok;
    }

    private Wheel createWheel(String type, int x, int y)
    {
        if (type.equals("lefty"))
        {
            return new LeftyWheel(x, y);
        }
        if (type.equals("rebel"))
        {
            return new RebelWheel(x, y);
        }
        return new Wheel(x, y);
    }
    public void addWheel(int pos){
        addWheel(pos,"normal");
    }
    public void addWheelLefty(int pos){
        addWheel(pos,"lefty");
    }
    public void addWheelRebel(int pos){
        addWheel(pos,"rebel");
    }    
    /**
     * Agrega una rueda en la posicion indicada con la ventana
     * @param pos posicion de la rueda, empezando en 1
     */
    public void addWheel(int pos, String type)
    {
        /*validacion de tipo*/
        if (type == null || !(type.equals("normal")
                || type.equals("lefty")
                || type.equals("rebel")))
        {
            ok = false;
            showError("Tipo de rueda no valido.");
            return;
        }
        /* No se permiten mas de 10 ruedas */
        if (wheels.size() >= 10)
        {
            ok = false;
            showError("No se pueden agregar mas de 10 ruedas.");
            return;
        }
        /* La posicion debe estar entre 1 y size + 1 */
        if (pos < 1 || pos > wheels.size() + 1)
        {
            ok = false;
            showError("La posicion indicada no es valida.");
            return;
        }
        
        /* Calculamos la posicion grafica */
        int x = getWheelX(pos);
        int y = getWheelY();
        
        /* Creamos la rueda */
        Wheel wheel = createWheel(type, x - 15, y - 15);
        
        /* La nueva rueda recibe una copia de todos los simbolos existentes */
        for (Symbol symbol : symbols)
        {
            Symbol copy = new Symbol(
                symbol.getColor(),
                x,
                y
            );
            wheel.addSymbol(copy);
        }
        
        /* Insertamos la rueda en la posicion indicada */
        wheels.add(pos - 1, wheel);
        
        /* La rueda comienza desbloqueada */
        lockedWheels.add(pos - 1, false);
        
        /* Creamos la ventana correspondiente */
        createWindow(pos);
        
        /* Si la maquina ya estaba visible, mostramos la rueda y su ventana */
        if (isVisible)
        {
            wheel.makeVisible();
            windows[pos - 1].makeVisible();
        }
        
        repositionWheelsAndWindows();
        ok = true;
        checkWinner();
    }

    private void createWindow(int pos)
    {
        int index = pos - 1;
        windows[index] = new Rectangle();
        windows[index].changeSize(60, 60);
        windows[index].changeColor("white");
        windows[index].moveHorizontal(getWindowX(pos));
        windows[index].moveVertical(getWindowY());
        windows[index].makeInvisible();
    }


    public void delWheel(int wheel)
    {
        if (!validWheel(wheel))
        {
            ok = false;
            showError("La rueda indicada no existe");
            return;
        }

        Wheel selectedWheel = wheels.get(wheel - 1);

        if (!selectedWheel.canDelete())
        {
            ok = false;
            showError("La RebelWheel no se puede eliminar");
            return;
        }

        Wheel wheela = wheels.remove(wheel - 1);
        wheela.makeInvisible();

        lockedWheels.remove(wheel - 1);
        removeWindow(wheel);
        repositionWheelsAndWindows();

        winner = false;
        ok = true;
        checkWinner();
    }


    private void removeWindow(int pos)
    {
        int index = pos - 1;
        if (windows[index] != null)
        {
            windows[index].makeInvisible();
        }
        for (int i = index; i < windows.length - 1; i++)
        {
            windows[i] = windows[i + 1];
        }
        windows[windows.length - 1] = null;
    }

    public void swap(int wheel1, int wheel2)
    {
        if (!validWheel(wheel1) || !validWheel(wheel2))
        {
            ok = false;
            showError("Una de las ruedas no existe.");
            return;
        }
        if (wheel1 == wheel2)
        {
            ok = true;
            return;
        }
        Wheel firstWheel = wheels.get(wheel1 - 1);
        Wheel secondWheel = wheels.get(wheel2 - 1);

        if (!firstWheel.canSwap() || !secondWheel.canSwap())
        {
            ok = false;
            showError("La RebelWheel no se puede intercambiar");
            return;
        }       
        
        Wheel temporary = wheels.get(wheel1 - 1);
        wheels.set(wheel1 - 1, wheels.get(wheel2 - 1));
        wheels.set(wheel2 - 1, temporary);
        
        Boolean temporaryLock = lockedWheels.get(wheel1 - 1);
        lockedWheels.set(wheel1 - 1, lockedWheels.get(wheel2 - 1));
        lockedWheels.set(wheel2 - 1, temporaryLock);
        
        wheels.get(wheel1 - 1).setPosition(getWheelX(wheel1) - 15, getWheelY() - 15);
        wheels.get(wheel2 - 1).setPosition(getWheelX(wheel2) - 15, getWheelY() - 15);
        
        checkWinner();
        ok = true;
    }

    public void lock(int wheel)
    {
        if (!validWheel(wheel))
        {
            ok = false;
            showError("La rueda indicada no existe");
            return;
        }
        Wheel selectedWheel = wheels.get(wheel - 1);

        if (!selectedWheel.canLock())
        {
            ok = false;
            showError("La RebelWheel no se puede bloquear");
            return;
        }
        lockedWheels.set(wheel - 1, true);
        ok = true;
    }

    public void unlock(int wheel)
    {
        if (!validWheel(wheel))
        {
            ok = false;
            showError("La rueda indicada no existe.");
            return;
        }
        lockedWheels.set(wheel - 1, false);
        ok = true;
    }

    public void addSymbol(int wheel, String color)
    {
        /* Un simbolo sin tipo es un simbolo normal */
        addSymbol(wheel, color, "normal");
    }
    
    public void addSymbolEphemeral(int wheel, String color)
    {
        /* Un simbolo ephemeral */
        addSymbol(wheel, color, "ephemeral");
    }
    
    public void addSymbolShy(int wheel, String color)
    {
        /* Un simbolo shy*/
        addSymbol(wheel, color, "shy");
    }
    public void addSymbolWild(int wheel, String color)
    {
        /* Un simbolo wild */
        addSymbol(wheel, color, "wild");
    }
    /**
     * Crea el simbolo que corresponde al tipo pedido.
     * Retorna null si el tipo no existe.
     */
    public Symbol createSymbol(String type, String color, int x, int y)
    {
        if (type == null)
        {
            return null;
        }
        if (type.equals("normal"))
        {
            return new Symbol(color, x, y);
        }
        if (type.equals("ephemeral"))
        {
            return new EphemeralSymbol(color, x, y);
        }
        if (type.equals("shy"))
        {
            return new ShySymbol(color, x, y);
        }
        if (type.equals("wild"))
        {
            return new WildSymbol(color, x, y);
        }
        return null;
    }
    
    /**
     * Agrega un simbolo del tipo indicado ("normal", "ephemeral", "shy" o "wild")
     * a UNA rueda. El mismo color puede estar en otras ruedas, pero no repetirse
     * dentro de la misma.
     */
    
    public void addSymbol(int wheel, String color, String type)
    {
        if (!validWheel(wheel))
        {
            ok = false;
            showError("La rueda indicada no existe");
            return;
        }
        if (color == null || color.length() == 0)
        {
            ok = false;
            showError("Debe indicar un color");
            return;
        }
        
        /* Solo se revisa duplicado dentro de ESTA rueda */
        Wheel selectedWheel = wheels.get(wheel - 1);
        if (selectedWheel.hasSymbol(color))
        {
            ok = false;
            showError("Esa rueda ya tiene ese simbolo.");
            return;
        }
        
        /* Creamos el simbolo segun su tipo (null si el tipo no existe) */
        Symbol symbol = createSymbol(type, color, getWheelX(wheel), getWheelY());
        if (symbol == null)
        {
            ok = false;
            showError("Tipo de simbolo no valido.");
            return;
        }
        
        /* La rueda se encarga de mostrarlo si es su primer simbolo y esta visible */
        selectedWheel.addSymbol(symbol);
        symbols.add(symbol);
        ok = true;
        checkWinner();
    }
    
    public void delSymbol(String symbol)
    {
        if (symbol == null)
        {
            ok = false;
            showError("El simbolo no puede ser null");
            return;
        }
        if (symbols.size() <= 1)
        {
            ok = false;
            showError("La maquina debe conservar al menos un simbolo");
            return;
        }
        
        boolean found = false;
        for (int i = 0; i < symbols.size(); i++)
        {
            if (symbols.get(i).getColor().equals(symbol))
            {
                symbols.remove(i);
                found = true;
                break;
            }
        }
        
        if (!found)
        {
            ok = false;
            showError("El simbolo no existe");
            return;
        }
        
        for (Wheel wheel : wheels)
        {
            wheel.removeSymbol(symbol);
        }
        
        winner = false;
        ok = true;
        checkWinner();
  }

    public void placeSymbol(int wheel, String symbol)
    {
        if (!validWheel(wheel))
        {
            ok = false;
            showError("La rueda indicada no existe");
            return;
        }
    
        Wheel selectedWheel = wheels.get(wheel - 1);
    
        if (!selectedWheel.hasSymbol(symbol))
        {
            ok = false;
            showError("La rueda no tiene ese simbolo");
            return;
        }
        if (lockedWheels.get(wheel - 1))
        {
            ok = false;
            showError("La rueda esta fijada");
            return;
        }
    
        /* Deja la rueda mostrando ese simbolo, sin giros al azar */
        if (selectedWheel.showColor(symbol))
        {
            ok = true;
            checkWinner();
            return;
        }
    
        ok = false;
        showError("No fue posible colocar el simbolo");

  }

    
    public void spin(int wheel)
    {
        if (!validWheel(wheel))
        {
            ok = false;
            showError("La rueda indicada no existe");
            return;
        }

        if (lockedWheels.get(wheel - 1))
        {
            ok = false;
            showError("La rueda esta fijada");
            return;
        }

        Wheel selectedWheel = wheels.get(wheel - 1);

        if (selectedWheel instanceof LeftyWheel)
        {
            if (wheel == 1)
            {
                ok = false;
                showError("La LeftyWheel necesita una rueda a su izquierda");
                return;
            }
    
            LeftyWheel lefty = (LeftyWheel) selectedWheel;
            boolean copied = lefty.spin(wheels.get(wheel - 2));

            if (!copied)
            {
                ok = false;
                showError("No se pudo copiar el simbolo de la rueda izquierda");
                return;
            }
        }
        else
        {
            selectedWheel.spin();
        }
        ok = true;
        checkWinner();
    }

    public void spin(int wheel, int steps)
    {
        if (!validWheel(wheel))
        {
            ok = false;
            showError("La rueda indicada no existe");
            return;
        }
        if (lockedWheels.get(wheel - 1))
        {
            ok = false;
            showError("La rueda esta fijada");
            return;
        }
        if (steps < 0)
        {
            ok = false;
            showError("Los pasos no pueden ser negativos");
            return;
        }
        
        Wheel selectedWheel = wheels.get(wheel - 1);
        for (int i = 0; i < steps; i++)
        {
            selectedWheel.spin();
            if (isVisible)
            {
                selectedWheel.makeVisible();
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
            }
        }
        
        ok = true;
        checkWinner();
    }

    public void spin(String[] setSymbols)
    {
        if (setSymbols == null)
        {
            ok = false;
            showError("La configuracion no puede ser null");
            return;
        }
        if (setSymbols.length != wheels.size())
        {
            ok = false;
            showError("La cantidad de simbolos no coincide con la cantidad de ruedas");
            return;
        }
        
        for (int i = 0; i < setSymbols.length; i++)
        {
            if (!containsSymbol(setSymbols[i]))
            {
                ok = false;
                showError("El simbolo " + setSymbols[i] + " no existe");
                return;
            }
            if (lockedWheels.get(i) && !wheels.get(i).getCurrentColor().equals(setSymbols[i]))
            {
                ok = false;
                showError("La rueda " + (i + 1) + " esta fijada.");
                return;
            }
        }
        
        for (int i = 0; i < setSymbols.length; i++)
        {
            if (!lockedWheels.get(i))
            {
                placeSymbol(i + 1, setSymbols[i]);
            }
        }
        
        ok = true;
        checkWinner();
    }

    public void spin()
    {
        if (wheels.size() == 0)
        {
            ok = false;
            showError("No existen ruedas para girar");
            return;
        }
        if (symbols.size() == 0)
        {
            ok = false;
            showError("No existen simbolos");
            return;
        }

        for (int i = 0; i < wheels.size(); i++)
        {
            if (!lockedWheels.get(i))
            {
                wheels.get(i).spin();
                if (isVisible)
                {
                    wheels.get(i).makeVisible();
                }
            }
        }
        
        ok = true;
        checkWinner();
    }

    /**
     * Retorna los simbolos de la maquina según la firma exigida en Astah.
     * @return Arreglo con los colores de los simbolos.
     */
    public String[] symbols()
    {
        String[] result = new String[symbols.size()];
        for (int i = 0; i < symbols.size(); i++)
        {
            result[i] = symbols.get(i).getColor();
        }
        return result;
    }

    public int distinctSymbols()
    {
        return symbols.size();
    }

    public String[] configuration()
    {
        String[] result = new String[wheels.size()];
        for (int i = 0; i < wheels.size(); i++)
        {
            result[i] = wheels.get(i).getCurrentColor();
        }
        return result;
    } 

    public boolean isJackpot()
    {
        if (wheels.size() == 0)
        {
            return false;
        }
        
        String firstColor = wheels.get(0).getCurrentColor();
        if (firstColor == null || firstColor.equals(""))
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

    public void makeVisible()
    {
        isVisible = true;
        machineBody.makeVisible();
        for (int i = 0; i < wheels.size(); i++)
        {
            if (windows[i] != null)
            {
                windows[i].makeVisible();
            }
            wheels.get(i).makeVisible();
        }
        if (winner)
        {
            winnerIndicator.makeVisible();
        }
    }

    public void makeInvisible()
    {
        isVisible = false;
        machineBody.makeInvisible();
        for (int i = 0; i < windows.length; i++)
        {
            if (windows[i] != null)
            {
                windows[i].makeInvisible();
            }
        }
        for (Wheel wheel : wheels)
        {
            wheel.makeInvisible();
        }
        winnerIndicator.makeInvisible();
    }

    public void exit()
    {
        makeInvisible();
        wheels.clear();
        symbols.clear();
        lockedWheels.clear();
        for (int i = 0; i < windows.length; i++)
        {
            windows[i] = null;
        }
        winner = false;
        ok = true;
    }

    private int getWheelX(int pos)
    {
        return 150 + (pos - 1) * 70;
    }

    private int getWheelY()
    {
        return 180;
    }

    private int getWindowX(int pos)
    {
        return 120 + (pos - 1) * 70;
    }

    private int getWindowY()
    {
        return 145;
    }

    private void repositionWheelsAndWindows()
    {
        for (int i = 0; i < wheels.size(); i++)
        {
            if (isVisible)
            {
                wheels.get(i).makeVisible();
            }
        }
    }

    private boolean validWheel(int wheel)
    {
        return wheel >= 1 && wheel <= wheels.size();
    }

    private boolean containsSymbol(String color)
    {
        if (color == null)
        {
            return false;
        }
        for (Symbol symbol : symbols)
        {
            if (symbol.getColor().equals(color))
            {
                return true;
            }
        }
        return false;
    }

    private void checkWinner()
    {
        winner = isJackpot();
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
}