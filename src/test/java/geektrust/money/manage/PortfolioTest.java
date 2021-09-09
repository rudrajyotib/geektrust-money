package geektrust.money.manage;

import geektrust.money.CalendarMonth;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioTest {

    @Test
    public void shouldUpdatePortfolioOfFirstMonth() {
        Portfolio portfolio = new Portfolio();
        portfolio.allocate(new int[]{10, 20, 30});
        portfolio.monthlyChange(new float[]{0.1f, 0.1f, 0.1f}, CalendarMonth.JANUARY);
        assertEquals("11 22 33", portfolio.presentStateOfAllocation());
    }

    @Test
    public void shouldAddSipAndUpdatePortfolioForFirstMonth() {
        Portfolio portfolio = new Portfolio();
        portfolio.allocate(new int[]{10, 20, 30});
        portfolio.sip(new int[]{5, 5, 5});
        portfolio.monthlyChange(new float[]{0.05f, 0.05f, 0.05f}, CalendarMonth.JANUARY);
        assertEquals("10 21 31", portfolio.presentStateOfAllocation());
    }

    @Test
    public void shouldNotRecalculateForSameMonth()
    {
        Portfolio portfolio = new Portfolio();
        portfolio.allocate(new int[]{10, 20, 30});
        portfolio.sip(new int[]{5, 5, 5});
        portfolio.monthlyChange(new float[]{0.05f, 0.05f, 0.05f}, CalendarMonth.JANUARY);
        portfolio.monthlyChange(new float[]{0.05f, 0.05f, 0.05f}, CalendarMonth.JANUARY);
        portfolio.monthlyChange(new float[]{0.05f, 0.05f, 0.05f}, CalendarMonth.JANUARY);
        assertEquals("10 21 31", portfolio.presentStateOfAllocation());
    }

    @Test
    public void shouldSupportNegativeChangeOfMarket()
    {
        Portfolio portfolio = new Portfolio();
        portfolio.allocate(new int[]{10, 20, 30});
        portfolio.sip(new int[]{5, 5, 5});
        portfolio.monthlyChange(new float[]{0.05f, -0.05f, 0.05f}, CalendarMonth.JANUARY);
        assertEquals("10 19 31", portfolio.presentStateOfAllocation());
    }

    @Test
    public void shouldFetchHistoricData()
    {
        Portfolio portfolio = new Portfolio();
        portfolio.allocate(new int[]{100,100,100});
        portfolio.sip(new int[]{100,100,100});
        portfolio.monthlyChange(new float[]{0.1f, 0.1f, 0.1f}, CalendarMonth.JANUARY);
        portfolio.monthlyChange(new float[]{0.1f, 0.1f, 0.1f}, CalendarMonth.FEBRUARY);
        assertEquals("110 110 110", portfolio.balanceForMonth(CalendarMonth.JANUARY));
    }

    @Test
    public void shouldNotSupportBalanceEnquiryForMonthNotCalculated()
    {
        Portfolio portfolio = new Portfolio();
        portfolio.allocate(new int[]{100,100,100});
        portfolio.sip(new int[]{100,100,100});
        portfolio.monthlyChange(new float[]{0.1f, 0.1f, 0.1f}, CalendarMonth.JANUARY);
        portfolio.monthlyChange(new float[]{0.1f, 0.1f, 0.1f}, CalendarMonth.FEBRUARY);
        assertEquals("110 110 110", portfolio.balanceForMonth(CalendarMonth.JANUARY));
        assertThrows(IllegalArgumentException.class, () -> portfolio.balanceForMonth(CalendarMonth.AUGUST));
    }

    @Test
    public void shouldSupportReBalancing()
    {
        Portfolio portfolio = new Portfolio();
        portfolio.allocate(new int[]{100,200,300});
        portfolio.sip(new int[]{100,100,100});
        portfolio.monthlyChange(new float[]{0.2f, 0.2f, 0.2f}, CalendarMonth.JANUARY);
        portfolio.monthlyChange(new float[]{0.2f, 0.2f, 0.2f}, CalendarMonth.FEBRUARY);
        portfolio.monthlyChange(new float[]{0.2f, 0.2f, 0.2f}, CalendarMonth.MARCH);
        portfolio.monthlyChange(new float[]{0.2f, 0.2f, 0.2f}, CalendarMonth.APRIL);
        portfolio.monthlyChange(new float[]{0.2f, 0.2f, 0.2f}, CalendarMonth.MAY);
        portfolio.monthlyChange(new float[]{0.2f, 0.2f, 0.2f}, CalendarMonth.JUNE);
        assertEquals("743 1487 2231", portfolio.presentStateOfAllocation());
        assertEquals("743 1487 2231", portfolio.getRedistributedAllocation());
        portfolio.monthlyChange(new float[]{0.0f, 0.0f, 0.0f}, CalendarMonth.JULY);
        portfolio.monthlyChange(new float[]{0.0f, 0.0f, 0.0f}, CalendarMonth.AUGUST);
        portfolio.monthlyChange(new float[]{0.0f, 0.0f, 0.0f}, CalendarMonth.SEPTEMBER);
        portfolio.monthlyChange(new float[]{0.0f, 0.0f, 0.0f}, CalendarMonth.OCTOBER);
        portfolio.monthlyChange(new float[]{0.0f, 0.0f, 0.0f}, CalendarMonth.NOVEMBER);
        portfolio.monthlyChange(new float[]{0.0f, 0.0f, 0.0f}, CalendarMonth.DECEMBER);
        assertEquals("1043 2087 3130", portfolio.presentStateOfAllocation());
        assertEquals("1043 2087 3130", portfolio.getRedistributedAllocation());
        assertEquals("1243 1987 2731", portfolio.balanceForMonth(CalendarMonth.NOVEMBER));
    }

    @Test
    public void shouldReportWhenNotEnoughDataPresent()
    {
        Portfolio portfolio = new Portfolio();
        portfolio.allocate(new int[]{100,200,300});
        portfolio.sip(new int[]{100,100,100});
        portfolio.monthlyChange(new float[]{0.2f, 0.2f, 0.2f}, CalendarMonth.JANUARY);
        portfolio.monthlyChange(new float[]{0.2f, 0.2f, 0.2f}, CalendarMonth.FEBRUARY);
        assertEquals("CANNOT_REBALANCE", portfolio.getRedistributedAllocation());
    }
}