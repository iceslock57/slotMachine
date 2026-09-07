import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Representa una maquina tragamonedas
 * @author Juan Felipe Carvajal Moyano, Santiago Rodriguez Reyes
 */
public class SlotMachine
{/* Ruedas que pertenecen a la maquina */
    private ArrayList<Wheel> wheels;
    private ArrayList<Symbol> symbols;
    /* Indica que ruedas estan bloqueadas */
    private ArrayList<Boolean> lockedWheels;
    /* Elementos visuales de la maquina */
    private Rectangle machineBody;
    private Rectangle[] windows;
    private Triangle winnerIndicator;
    /* Estado visual de la maquina */
    private boolean isVisible;
    /* Indica si actualmente hay jackpot */
    private boolean winner;
    /* Estado de la maquina */
    private boolean ok;
    /**
     * Crea una maquina vacia
     */
    public SlotMachine()
    {
        wheels = new ArrayList<Wheel>();
        symbols = new ArrayList<Symbol>();
        lockedWheels = new ArrayList<Boolean>(); 
        isVisible = false;
        winner = false;
        ok = true;
        /*Cuerpo de la maquina*/
        machineBody = new Rectangle();
        machineBody.changeSize(300, 500);
        machineBody.moveHorizontal(533);
        machineBody.moveVertical(134);
        machineBody.changeColor("black");
        /*Ventanas donde aparecen las ruedas*/
        windows = new Rectangle[3];
        for (int i = 0; i < windows.length; i++)
        {
            windows[i] = new Rectangle();
            windows[i].changeSize(80, 80);
            windows[i].changeColor("white");
            windows[i].moveHorizontal(20 + i * 100);
            windows[i].moveVertical(70);
        }
        /*Indicador visual de jackpot*/
        winnerIndicator = new Triangle();
        winnerIndicator.changeSize(30, 30);
        winnerIndicator.changeColor("yellow");
        /*La maquina comienza invisible*/
        machineBody.makeInvisible();
        for (Rectangle window : windows)
        {
            window.makeInvisible();
        }
        winnerIndicator.makeInvisible();
    }
    /**
     * Agrega una rueda en la posicion indicada
     */
    public void addWheel(int pos)
    {
        /*
         * La posicion debe ser valida
         */
        if (pos < 1 || pos > wheels.size() + 1 || wheels.size() >= 3)
        {
            ok = false;
            showError("La posicion indicada para la rueda no es valida");
            return;
        }
        /*Posicion grafica de la nueva rueda*/
        int positionX = 120 + (pos - 1) * 90;
        int positionY = 110;
        Wheel wheel = new Wheel(positionX, positionY);
        /*Cada nueva rueda recibe una copia de los simbolos existentes en la maquina*/
        for (Symbol symbol : symbols)
        {
            Symbol copy = new Symbol(
                symbol.getColor(),
                positionX,
                positionY
            );
            wheel.addSymbol(copy);
        }
        /*Insertamos la rueda en la posicion indicada*/
        wheels.add(pos - 1, wheel);
        lockedWheels.add(pos - 1, false);
        /*hacemos visible la nueva rueda*/
        if (isVisible)
        {
            wheel.makeVisible();
        }
        ok = true;
        checkWinner();
    }
    /**
     * Elimina una rueda de la posicion indicada
     */
    public void delWheel(int pos)
    {
        if (!validWheel(pos))
        {
            ok = false;
            showError("La rueda indicada no existe");
            return;
        }
        Wheel wheel = wheels.remove(pos - 1);
        lockedWheels.remove(pos - 1);
        wheel.makeInvisible();
        /*Reubicamos visualmente las ruedas que quedan*/
        repositionWheels();
        winner = false;
        ok = true;
        checkWinner();
    }
    /**
     * Intercambia dos ruedas
     * @param wheel1 primera rueda
     * @param wheel2 segunda rueda
     */
    public void swap(int wheel1, int wheel2)
    {
        if (!validWheel(wheel1) || !validWheel(wheel2))
        {
            ok = false;
            showError("Una de las ruedas indicadas no existe");
            return;
        }
        if (wheel1 == wheel2)
        {
            ok = true;
            return;
        }
        /*Intercambiamos las ruedas*/
        Wheel temporary = wheels.get(wheel1 - 1);
        wheels.set(
            wheel1 - 1,
            wheels.get(wheel2 - 1)
        );
        wheels.set(
            wheel2 - 1,
            temporary
        );
        /*Tambien debemos intercambiar sus estados de bloqueo*/
        Boolean temporaryLock = lockedWheels.get(wheel1 - 1);
        lockedWheels.set(
            wheel1 - 1,
            lockedWheels.get(wheel2 - 1)
        );
        lockedWheels.set(
            wheel2 - 1,
            temporaryLock
        );
        repositionWheels();
        ok = true;
        checkWinner();
    }
    /**
     * Fija una rueda para impedir que gire
     * @param wheel rueda que se desea fijar
     */
    public void lock(int wheel)
    {
        if (!validWheel(wheel))
        {
            ok = false;
            showError("La rueda indicada no existe");
            return;
        }
        lockedWheels.set(wheel - 1, true);
        ok = true;
    }
    /**
     * Suelta una rueda previamente fijada
     * @param wheel rueda que se desea soltar
     */
    public void unlock(int wheel)
    {
        if (!validWheel(wheel))
        {
            ok = false;
            showError("La rueda indicada no existe");
            return;
        }
        lockedWheels.set(wheel - 1, false);
        ok = true;
    }
    /**
     * Agrega un simbolo en la posicion indicada
     * @param pos posicion del nuevo simbolo, comenzando en 1
     * @param color color del simbolo
     */
    public void addSymbol(int pos, String color)
    {
        if (color == null || color.length() == 0)
        {
            ok = false;
            showError("Debe indicar un color.");
            return;
        }
        /*No permitimos colores repetidos*/
        for (Symbol symbol : symbols)
        {
            if (symbol.getColor().equals(color))
            {
                ok = false;
                showError("Ese simbolo ya existe.");
                return;
            }
        }

        /*La posicion valida va desde 1 hasta size + 1*/
        if (pos < 1 || pos > symbols.size() + 1)
        {
            ok = false;
            showError("La posicion del simbolo no es valida.");
            return;
        }

        /*El simbolo maestro se guarda en la lista de simbolos de la maquina.*/
        Symbol symbol = new Symbol(color, 0, 0);
        symbols.add(pos - 1, symbol);
        /*Cada rueda debe recibir una copia del simbolo*/
        for (int i = 0; i < wheels.size(); i++)
        {
            Wheel wheel = wheels.get(i);

            int x = 120 + i * 90;
            int y = 110;

            Symbol copy = new Symbol(color, x, y);

            wheel.addSymbol(copy);
        }
        ok = true;
        checkWinner();
    }
    /**
     * Elimina un simbolo de la maquina.
     * @param symbol color del simbolo que se desea eliminar
     */
    public void delSymbol(String symbol)
    {
        if (symbol == null)
        {
            ok = false;
            showError("El simbolo no puede ser null.");
            return;
        }
        /*La maquina debe conservar al menos un simbolo.*/
        if (symbols.size() <= 1)
        {
            ok = false;
            showError("La maquina debe conservar al menos un simbolo");
            return;
        }
        boolean found = false;
        /*Buscamos el simbolo maestro*/
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
            showError("El simbolo no existe.");
            return;
        }
        /*Eliminamos el simbolo correspondiente de todas las ruedas*/
        for (Wheel wheel : wheels)
        {
            wheel.removeSymbol(symbol);
        }
        winner = false;
        ok = true;
        checkWinner();
    }
    /**
     * Coloca un simbolo especifico en una rueda
     * @param wheel rueda donde se quiere colocar
     * @param symbol color del simbolo
     */
    public void placeSymbol(int wheel, String symbol)
    {
        if (!validWheel(wheel))
        {
            ok = false;
            showError("La rueda indicada no existe");
            return;
        }
        if (symbol == null || !containsSymbol(symbol))
        {
            ok = false;
            showError("El simbolo indicado no existe.");
            return;
        }
        /*No permitimos modificar una rueda fijada*/
        if (lockedWheels.get(wheel - 1))
        {
            ok = false;
            showError("La rueda esta fijada.");
            return;
        }
        Wheel selectedWheel = wheels.get(wheel - 1);
        String[] available = selectedWheel.getSymbols();
        if (available.length == 0)
        {
            ok = false;
            return;
        }
        /*Si ya esta mostrando el simbolo no hacemos nada*/
        if (selectedWheel.getCurrentColor().equals(symbol))
        {
            ok = true;
            checkWinner();
            return;
        }
        /*Intentamos encontrar el simbolo mediante giros*/
        int maximumAttempts = available.length * 20;
        for (int i = 0; i < maximumAttempts; i++)
        {
            selectedWheel.spin();

            if (selectedWheel.getCurrentColor().equals(symbol))
            {
                ok = true;
                checkWinner();
                return;
            }
        }

        /*Si no se encontro, la operacion no fue exitosa*/
        ok = false;
        showError("No fue posible colocar el simbolo indicado.");
    }
    /**
     * Gira una rueda una vez
     * @param wheel rueda que se desea girar
     */
    public void spin(int wheel)
    {
        if (!validWheel(wheel))
        {
            ok = false;
            showError("La rueda indicada no existe.");
            return;
        }

        if (lockedWheels.get(wheel - 1))
        {
            ok = false;
            showError("La rueda esta fijada.");
            return;
        }

        wheels.get(wheel - 1).spin();

        ok = true;
        checkWinner();
    }

    /**
     * Gira una rueda un numero determinado de pasos
     * @param wheel rueda que se desea girar
     * @param steps numero de pasos
     */
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
            showError("El numero de pasos no puede ser negativo");
            return;
        }
        Wheel selectedWheel = wheels.get(wheel - 1);
        /*Realizamos un giro por cada paso*/
        for (int i = 0; i < steps; i++)
        {
            selectedWheel.spin();
            /*Si esta visible, actualizamos el estado visual despues de cada paso*/
            if (isVisible)
            {
                selectedWheel.makeVisible();
                /*Una pequeña pausa permite mirar un movimiento paso a paso.*/
                try
                {
                    Thread.sleep(150);
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
    /**
     * Configura la maquina con un conjunto de simbolos.
     *
     * @param setSymbols configuracion deseada
     */
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
            showError(
                "La configuracion debe tener un simbolo por rueda."
            );
            return;
        }
        /*Primero comprobamos que todos los simbolos existan y que ninguna rueda bloqueada */
        for (int i = 0; i < setSymbols.length; i++)
        {
            if (!containsSymbol(setSymbols[i]))
            {
                ok = false;
                showError(
                    "El simbolo " + setSymbols[i] +
                    " no existe."
                );
                return;
            }
            if (lockedWheels.get(i) &&
                !wheels.get(i).getCurrentColor().equals(setSymbols[i]))
            {
                ok = false;
                showError(
                    "La rueda " + (i + 1) +
                    " esta fijada."
                );
                return;
            }
        }
        /*Colocamos cada simbolo*/
        for (int i = 0; i < setSymbols.length; i++)
        {
            if (!lockedWheels.get(i))
            {
                placeSymbol(
                    i + 1,
                    setSymbols[i]
                );
            }
        }

        ok = true;
        checkWinner();
    }
    /**
     * Gira todas las ruedas que no esten fijadas.
     */
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
     * Devuelve los simbolos disponibles en la maquina.
     * @return arreglo con los colores disponibles
     */
    public String[] symbols0()
    {
        String[] result = new String[symbols.size()];

        for (int i = 0; i < symbols.size(); i++)
        {
            result[i] = symbols.get(i).getColor();
        }
        return result;
    }
    /**
     * Retorna el numero de simbolos diferentes disponibles.
     * @return cantidad de simbolos
     */
    public int distinctSymbols()
    {
        return symbols.size();
    }
    /**
     * Obtiene la configuracion actual de la maquina
     * @return simbolos actualmente visibles
     */
    public String[] configuration()
    {
        String[] result = new String[wheels.size()];

        for (int i = 0; i < wheels.size(); i++)
        {
            result[i] = wheels.get(i).getCurrentColor();
        }
        return result;
    }
    /**
     * Determina si todas las ruedas muestran el mismo simbolo
     * @return true si existe jackpot, false en caso contrario
     */
    public boolean isJackpot()
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
     * Hace invisible la maquina.
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
    /**Termina el simulador y limpia sus elementos*/
    public void exit()
    {
        makeInvisible();
        wheels.clear();
        symbols.clear();
        lockedWheels.clear();
        winner = false;
        ok = true;
    }
    /**
     * Verifica si una rueda existe
     * @param wheel numero de rueda
     * @return true si existe
     */
    private boolean validWheel(int wheel)
    {
        return wheel >= 1 && wheel <= wheels.size();
    }
    /**
     * Verifica si un simbolo existe
     * @param color color que se desea buscar
     * @return true si existe
     */
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
    /**
     * Reubica las ruedas despues de una insercion, eliminacion o intercambio
     */
    private void repositionWheels()
    {
        for (int i = 0; i < wheels.size(); i++)
        {
            Wheel wheel = wheels.get(i);

            String[] colors = wheel.getSymbols();
        }
    }
    /**
     * Actualiza el indicador de jackpot.
     */
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
    /**
     * Muestra mensajes de error solamente si la maquina esta visible.
     * @param message mensaje que se desea mostrar
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
}