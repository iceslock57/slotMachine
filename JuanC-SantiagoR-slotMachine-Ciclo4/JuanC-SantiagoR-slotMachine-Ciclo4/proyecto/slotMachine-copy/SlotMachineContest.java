/**
 * Resuelve y simula el problema de la maratón (Slot Machine Contest).
 */
public class SlotMachineContest
{
    /**
     * REQUISITO 14: Resuelve el problema permaneciendo INVISIBLE.
     * Utiliza la tragamonedas como 'testing tool' (SlotMachine(n), spin(wheel, steps), distinctSymbols()).
     * @param n Numero de ruedas y simbolos
     * @return Numero total de giros ejecutados para ganar
     */
    public int solve(int n)
    {
        // 1. Instanciar la máquina mediante la testing tool (permanece invisible)
        SlotMachine machine = new SlotMachine(n);
        machine.makeInvisible(); // Regla de diseño: debe permanecer invisible en solve[cite: 1]

        int moves = 0;
        int limit = n * n * 5;

        // Algoritmo para intentar alinear todas las ruedas al mismo simbolo
        while (!machine.isJackpot() && moves < limit)
        {
            // Estrategia de rotacion progresiva en las ruedas
            for (int w = 1; w <= n; w++)
            {
                machine.spin(w, 1);
                moves++;
                if (machine.isJackpot())
                {
                    break;
                }
            }
        }

        return machine.isJackpot() ? moves : -1;
    }

    /**
     * REQUISITO 15: Simula la solucion permaneciendo VISIBLE.
     * @param n Numero de ruedas y simbolos
     */
    public void simulate(int n)
    {
        SlotMachine machine = new SlotMachine(n);
        machine.makeVisible(); // Regla de diseño: debe permanecer visible en simulate[cite: 1]

        int moves = 0;
        int limit = n * n * 5;

        while (!machine.isJackpot() && moves < limit)
        {
            for (int w = 1; w <= n; w++)
            {
                machine.spin(w, 1);
                moves++;
                
                try
                {
                    Thread.sleep(200); // Demora para visualizacion en la GUI
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }

                if (machine.isJackpot())
                {
                    break;
                }
            }
        }
    }
}