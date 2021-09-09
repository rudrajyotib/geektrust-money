package geektrust.money.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandUtilityTest {

    @Test
    public void shouldParseMarketChanges()
    {
        assertArrayEquals(new float[]{0.01f, -0.02f, 0.0f},
                CommandUtility.deriveMarketChange(new String[]{"1.00%", "-2.00%", "0.00%"}));
    }

}