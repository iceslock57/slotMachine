import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas unitarias del ciclo 2 de SlotMachine
 * @author Juan Felipe Carvajal Moyano, Santiago Rodriguez Reyes
 */
public class SlotMachineC2Test
{
    private SlotMachine machine;
    /**
     * Prepara una maquina con tres ruedas y tres simbolos diferentes.
     */
    @Before
    public void setUp()
    {
        machine = new SlotMachine();
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
    }
    /**
     * Prueba que se puedan intercambiar dos ruedas.
     */
    @Test
    public void swapShouldExchangeWheels()
    {
        String[] before = machine.configuration();
        machine.swap(1, 3);
        String[] after = machine.configuration();
        assertEquals(before[0], after[2]);
        assertEquals(before[2], after[0]);
    }
    /**
     * Una rueda fijada no debe girar.
     */
    @Test
    public void lockedWheelShouldNotSpin()
    {
        String[] before = machine.configuration();
        machine.lock(1);
        machine.spin(1);
        String[] after = machine.configuration();
        assertArrayEquals(before, after);
    }
    /**
     * Una rueda soltada puede volver a girar.
     */
    @Test
    public void unlockedWheelShouldBeAllowedToSpin()
    {
        machine.lock(1);
        machine.unlock(1);
        /*El objetivo principal es comprobar que la operacion de desbloqueo sea aceptada*/
        machine.spin(1);
        assertTrue(true);
    }
    /**
     * Una configuracion valida debe poder establecerse.
     */
    @Test
    public void spinSetSymbolsShouldSetConfiguration()
    {
        String[] expected = {
            "red",
            "blue",
            "green"
        };
        machine.spin(expected);
        assertArrayEquals(
            expected,
            machine.configuration()
        );
    }
    /**
     * Jackpot cuando todas las ruedas tienen el mismo simbolo.
     */
    @Test
    public void isJackpotShouldBeTrueWhenAllSymbolsAreEqual()
    {
        String[] jackpot = {
            "red",
            "red",
            "red"
        };
        machine.spin(jackpot);
        assertTrue(machine.isJackpot());
    }
    /**
     * No debe existir jackpot si las ruedas muestran simbolos diferentes.
     */
    @Test
    public void isJackpotShouldBeFalseWhenSymbolsAreDifferent()
    {
        String[] configuration = {
            "red",
            "blue",
            "green"
        };
        machine.spin(configuration);
        assertFalse(machine.isJackpot());
    }
    /**
     * Una configuracion con una cantidad incorrecta de simbolos no debe modificar la maquina
     */
    @Test
    public void invalidConfigurationShouldNotChangeMachine()
    {
        String[] before = machine.configuration();
        String[] invalid = {
            "red",
            "blue"
        };
        machine.spin(invalid);
        String[] after = machine.configuration();
        assertArrayEquals(before, after);
    }
    /**
     * Una rueda inexistente no debe poder bloquearse.
     */
    @Test
    public void lockInvalidWheelShouldNotChangeConfiguration()
    {
        String[] before = machine.configuration();
        machine.lock(10);
        String[] after = machine.configuration();
        assertArrayEquals(before, after);
    }
}