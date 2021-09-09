package geektrust.money.api;

import geektrust.money.CalendarMonth;
import geektrust.money.manage.Portfolio;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {


    @RegisterExtension
    private final Mockery mockery = new JUnit5Mockery() {
        {
            setImposteriser(ByteBuddyClassImposteriser.INSTANCE);
        }
    };

    private Portfolio mockPortfolio;

    @BeforeEach
    public void setUp() {
        mockPortfolio = mockery.mock(Portfolio.class);
    }

    @Test
    public void shouldInvokeAllocateFundWithCorrectValues() {
        mockery.checking(new Expectations() {
            {
                exactly(1).of(mockPortfolio).allocate(with(new BaseMatcher<int[]>() {
                    @Override
                    public boolean matches(Object o) {
                        int[] items = (int[]) o;
                        assertArrayEquals(new int[]{100, 200, 300}, items);
                        return true;
                    }

                    @Override
                    public void describeTo(Description description) {

                    }
                }));
            }
        });
        assertTrue(Command.ALLOCATE.process(new String[]{"ALLOCATE", "100", "200", "300"}, mockPortfolio).isEmpty());
    }

    @Test
    public void shouldInvokeSipAllocationFundWithCorrectValues() {
        mockery.checking(new Expectations() {
            {
                exactly(1).of(mockPortfolio).sip(with(new BaseMatcher<int[]>() {
                    @Override
                    public boolean matches(Object o) {
                        int[] items = (int[]) o;
                        assertArrayEquals(new int[]{100, 200, 300}, items);
                        return true;
                    }

                    @Override
                    public void describeTo(Description description) {

                    }
                }));
            }
        });
        assertTrue(Command.SIP.process(new String[]{"SIP", "100", "200", "300"}, mockPortfolio).isEmpty());
    }

    @Test
    public void shouldInvokeMarketChange() {
        mockery.checking(new Expectations() {
            {
                exactly(1).of(mockPortfolio).monthlyChange(with(new BaseMatcher<float[]>() {
                    @Override
                    public boolean matches(Object o) {
                        float[] items = (float[]) o;
                        assertArrayEquals(new float[]{2.0f, 3.0f, -1.0f}, items);
                        return true;
                    }

                    @Override
                    public void describeTo(Description description) {

                    }
                }), with(CalendarMonth.JANUARY));
            }
        });
        assertTrue(Command.CHANGE.process(new String[]{"CHANGE", "2.00%", "3.0000%", "-1.0%", "JANUARY"}, mockPortfolio).isEmpty());
    }

    @Test
    public void shouldInvokeBalance() {
        mockery.checking(new Expectations() {
            {
                exactly(1).of(mockPortfolio).balanceForMonth(with(CalendarMonth.JANUARY));
                will(returnValue("00"));
            }
        });
        Optional<String> result = Command.BALANCE.process(new String[]{"BALANCE", "JANUARY"}, mockPortfolio);
        assertFalse(result.isEmpty());
        assertEquals("00", result.get());
    }

    @Test
    public void shouldInvokeReBalance() {
        mockery.checking(new Expectations() {
            {
                exactly(1).of(mockPortfolio).getRedistributedAllocation();
                will(returnValue("00"));
            }
        });
        Optional<String> result = Command.REBALANCE.process(new String[]{"REBALANCE"}, mockPortfolio);
        assertFalse(result.isEmpty());
        assertEquals("00", result.get());
    }


}