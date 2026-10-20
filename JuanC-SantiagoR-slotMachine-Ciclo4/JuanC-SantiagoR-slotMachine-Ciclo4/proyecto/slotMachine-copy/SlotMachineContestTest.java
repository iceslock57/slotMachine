import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SlotMachineContestTest {

    private SlotMachineContest contest;

    @Before
    public void setUp() {
        contest = new SlotMachineContest();
    }

    @Test
    public void testSolveReturnsValidResult() {
        int n = 3;
        int result = contest.solve(n);
        assertTrue(result >= -1);
    }

    @Test
    public void testSimulateExecution() {
        int n = 3;
        // Verifica que simulate no arroje excepciones
        try {
            contest.simulate(n);
            assertTrue(true);
        } catch (Exception e) {
            fail("Simulate lanzó una excepción inesperada: " + e.getMessage());
        }
    }
}