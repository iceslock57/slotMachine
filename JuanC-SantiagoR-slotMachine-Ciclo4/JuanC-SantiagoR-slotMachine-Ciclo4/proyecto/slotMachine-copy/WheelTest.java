import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class WheelTest
{
    private Wheel wheel;

    @Before
    public void setUp()
    {
        wheel = new Wheel(100, 50);
    }

    @After
    public void tearDown()
    {
        wheel.makeInvisible();
    }

    // Unico lugar donde se construye un Symbol
    private Symbol crearSimbolo(String color, int x, int y)
    {
        return new Symbol(color, x, y);
    }

    private void agregarRojoVerdeAzul()
    {
        wheel.addSymbol(crearSimbolo("red", 15, 15));
        wheel.addSymbol(crearSimbolo("green", 35, 17));
        wheel.addSymbol(crearSimbolo("blue", 20, 25));
    }

    // Gira hasta que el simbolo actual tenga el color pedido
    private void girarHastaObtener(String color)
    {
        int intentos = 0;
        while (!wheel.getCurrentColor().equals(color) && intentos < 1000)
        {
            wheel.spin();
            intentos++;
        }
        assertEquals(color, wheel.getCurrentColor());
    }

    private boolean contiene(String color)
    {
        return Arrays.asList(wheel.getSymbols()).contains(color);
    }

    @Test
    public void nuevaRueda_estaVacia()
    {
        assertEquals(0, wheel.symbolCount());
        assertEquals("", wheel.getCurrentColor());
        assertEquals(0, wheel.getSymbols().length);
    }

    @Test
    public void addSymbol_primerSimbolo_quedaComoActual()
    {
        wheel.addSymbol(crearSimbolo("red", 15, 15));

        assertEquals(1, wheel.symbolCount());
        assertEquals("red", wheel.getCurrentColor());
    }

    @Test
    public void addSymbol_variosSimbolos_elActualSigueSiendoElPrimero()
    {
        agregarRojoVerdeAzul();

        assertEquals(3, wheel.symbolCount());
        assertEquals("red", wheel.getCurrentColor());
    }

    @Test
    public void addSymbol_conservaElOrdenDeInsercion()
    {
        agregarRojoVerdeAzul();

        assertArrayEquals(new String[] {"red", "green", "blue"}, wheel.getSymbols());
    }

    @Test
    public void addSymbol_null_seIgnora()
    {
        wheel.addSymbol(null);

        assertEquals(0, wheel.symbolCount());
        assertEquals("", wheel.getCurrentColor());
    }

    @Test
    public void addSymbol_nullEntreSimbolosValidos_noAfectaLaRueda()
    {
        wheel.addSymbol(crearSimbolo("red", 15, 15));
        wheel.addSymbol(null);
        wheel.addSymbol(crearSimbolo("green", 35, 17));

        assertArrayEquals(new String[] {"red", "green"}, wheel.getSymbols());
    }

    @Test
    public void addSymbol_colorRepetido_seAgregaDeTodasFormas()
    {
        wheel.addSymbol(crearSimbolo("red", 15, 15));
        wheel.addSymbol(crearSimbolo("red", 15, 15));

        assertEquals(2, wheel.symbolCount());
    }

    @Test
    public void addSymbol_conRuedaVisible_noAlteraElSimboloActual()
    {
        wheel.makeVisible();
        wheel.addSymbol(crearSimbolo("red", 15, 15));
        wheel.addSymbol(crearSimbolo("green", 35, 17));

        assertEquals(2, wheel.symbolCount());
        assertEquals("red", wheel.getCurrentColor());
    }

    @Test
    public void removeSymbol_existente_retornaTrueYDisminuyeElConteo()
    {
        agregarRojoVerdeAzul();

        assertTrue(wheel.removeSymbol("green"));

        assertEquals(2, wheel.symbolCount());
        assertFalse(contiene("green"));
        assertArrayEquals(new String[] {"red", "blue"}, wheel.getSymbols());
    }

    @Test
    public void removeSymbol_inexistente_retornaFalseYNoCambiaNada()
    {
        agregarRojoVerdeAzul();

        assertFalse(wheel.removeSymbol("yellow"));

        assertEquals(3, wheel.symbolCount());
        assertArrayEquals(new String[] {"red", "green", "blue"}, wheel.getSymbols());
    }

    @Test
    public void removeSymbol_ruedaVacia_retornaFalse()
    {
        assertFalse(wheel.removeSymbol("red"));
        assertEquals(0, wheel.symbolCount());
    }

    @Test
    public void removeSymbol_null_retornaFalse()
    {
        agregarRojoVerdeAzul();

        assertFalse(wheel.removeSymbol(null));
        assertEquals(3, wheel.symbolCount());
    }

    @Test
    public void removeSymbol_distingueMayusculasDeMinusculas()
    {
        wheel.addSymbol(crearSimbolo("red", 15, 15));

        assertFalse(wheel.removeSymbol("RED"));
        assertEquals(1, wheel.symbolCount());
    }

    @Test
    public void removeSymbol_colorRepetido_eliminaSoloElPrimero()
    {
        wheel.addSymbol(crearSimbolo("red", 15, 15));
        wheel.addSymbol(crearSimbolo("green", 35, 17));
        wheel.addSymbol(crearSimbolo("red", 15, 15));

        assertTrue(wheel.removeSymbol("red"));

        assertEquals(2, wheel.symbolCount());
        assertArrayEquals(new String[] {"green", "red"}, wheel.getSymbols());
    }

    @Test
    public void removeSymbol_unicoSimbolo_dejaLaRuedaVacia()
    {
        wheel.addSymbol(crearSimbolo("red", 15, 15));

        assertTrue(wheel.removeSymbol("red"));

        assertEquals(0, wheel.symbolCount());
        assertEquals("", wheel.getCurrentColor());
    }

    @Test
    public void removeSymbol_simboloActual_dejaUnActualValido()
    {
        agregarRojoVerdeAzul();

        assertTrue(wheel.removeSymbol("red"));

        String actual = wheel.getCurrentColor();
        assertFalse(actual.equals("red"));
        assertTrue(actual.equals("green") || actual.equals("blue"));
    }

    @Test
    public void removeSymbol_ultimoSimboloEsElActual_elIndiceVuelveAlPrimero()
    {
        agregarRojoVerdeAzul();
        girarHastaObtener("blue");

        assertTrue(wheel.removeSymbol("blue"));

        assertEquals("red", wheel.getCurrentColor());
    }

    // Falla a proposito: revela el bug de currentSymbol al eliminar un simbolo anterior al actual
    @Test
    public void removeSymbol_simboloAnteriorAlActual_elActualNoCambia()
    {
        agregarRojoVerdeAzul();
        girarHastaObtener("blue");

        assertTrue(wheel.removeSymbol("red"));

        assertEquals("blue", wheel.getCurrentColor());
    }

    @Test
    public void spin_ruedaVacia_noLanzaExcepcionYSigueVacia()
    {
        wheel.spin();

        assertEquals("", wheel.getCurrentColor());
        assertEquals(0, wheel.symbolCount());
    }

    @Test
    public void spin_unSoloSimbolo_siempreMuestraElMismo()
    {
        wheel.addSymbol(crearSimbolo("red", 15, 15));

        for (int i = 0; i < 20; i++)
        {
            wheel.spin();
            assertEquals("red", wheel.getCurrentColor());
        }
    }

    @Test
    public void spin_variosSimbolos_siempreMuestraUnColorExistente()
    {
        agregarRojoVerdeAzul();

        for (int i = 0; i < 100; i++)
        {
            wheel.spin();
            assertTrue(contiene(wheel.getCurrentColor()));
        }
    }

    @Test
    public void spin_variosSimbolos_conMuchosGirosAparecenTodos()
    {
        agregarRojoVerdeAzul();
        Set<String> vistos = new HashSet<String>();

        for (int i = 0; i < 500; i++)
        {
            wheel.spin();
            vistos.add(wheel.getCurrentColor());
        }

        assertEquals(3, vistos.size());
    }

    @Test
    public void spin_noAlteraElContenidoDeLaRueda()
    {
        agregarRojoVerdeAzul();

        for (int i = 0; i < 10; i++)
        {
            wheel.spin();
        }

        assertEquals(3, wheel.symbolCount());
        assertArrayEquals(new String[] {"red", "green", "blue"}, wheel.getSymbols());
    }

    @Test
    public void spin_conRuedaVisible_noLanzaExcepcion()
    {
        agregarRojoVerdeAzul();
        wheel.makeVisible();

        for (int i = 0; i < 10; i++)
        {
            wheel.spin();
        }

        assertTrue(contiene(wheel.getCurrentColor()));
    }

    @Test
    public void getSymbols_retornaUnaCopia_modificarloNoAfectaLaRueda()
    {
        agregarRojoVerdeAzul();

        String[] copia = wheel.getSymbols();
        copia[0] = "modificado";

        assertArrayEquals(new String[] {"red", "green", "blue"}, wheel.getSymbols());
        assertEquals("red", wheel.getCurrentColor());
    }

    @Test
    public void symbolCount_seActualizaConAgregarYEliminar()
    {
        assertEquals(0, wheel.symbolCount());

        wheel.addSymbol(crearSimbolo("red", 15, 15));
        wheel.addSymbol(crearSimbolo("green", 35, 17));
        assertEquals(2, wheel.symbolCount());

        wheel.removeSymbol("red");
        assertEquals(1, wheel.symbolCount());
    }

    @Test
    public void makeVisible_ruedaVacia_noLanzaExcepcion()
    {
        wheel.makeVisible();

        assertEquals(0, wheel.symbolCount());
        assertEquals("", wheel.getCurrentColor());
    }

    @Test
    public void makeInvisible_ruedaVacia_noLanzaExcepcion()
    {
        wheel.makeInvisible();

        assertEquals(0, wheel.symbolCount());
    }

    @Test
    public void makeVisible_conSimbolos_noAlteraElEstado()
    {
        agregarRojoVerdeAzul();

        wheel.makeVisible();

        assertEquals(3, wheel.symbolCount());
        assertEquals("red", wheel.getCurrentColor());
        assertArrayEquals(new String[] {"red", "green", "blue"}, wheel.getSymbols());
    }

    @Test
    public void makeInvisible_conSimbolos_noAlteraElEstado()
    {
        agregarRojoVerdeAzul();
        wheel.makeVisible();

        wheel.makeInvisible();

        assertEquals(3, wheel.symbolCount());
        assertEquals("red", wheel.getCurrentColor());
    }

    @Test
    public void ruedaInvisible_sigueFuncionandoAlGirarYVolverAMostrar()
    {
        agregarRojoVerdeAzul();
        wheel.makeVisible();
        wheel.makeInvisible();

        wheel.spin();
        wheel.makeVisible();

        assertTrue(contiene(wheel.getCurrentColor()));
        assertEquals(3, wheel.symbolCount());
    }
}