package geektrust.money.api;

import geektrust.money.CalendarMonth;
import geektrust.money.manage.Portfolio;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileCommandProcessorTest {

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
    public void shouldNotProcessInvalidPath() throws IOException {

        final States portfolioState = mockery.states("portfolio-state").startsAs("Initiated");

        mockery.checking(new Expectations() {
            {
                exactly(1).of(mockPortfolio).allocate(with(new int[]{6000, 3000, 1000}));
                when(portfolioState.is("Initiated"));
                then(portfolioState.is("Allocated"));

                exactly(1).of(mockPortfolio).sip(with(new int[]{2000, 1000, 500}));
                when(portfolioState.is("Allocated"));
                then(portfolioState.is("Sipped"));

                exactly(1).of(mockPortfolio).monthlyChange(with(new float[]{4.0f, 10.0f, 2.0f}), with(CalendarMonth.JANUARY));
                when(portfolioState.is("Sipped"));
                then(portfolioState.is("January"));

                exactly(1).of(mockPortfolio).monthlyChange(with(new float[]{-10.0f, 40.0f, 0.0f}), with(CalendarMonth.FEBRUARY));
                when(portfolioState.is("January"));
                then(portfolioState.is("February"));

                exactly(1).of(mockPortfolio).balanceForMonth(with(CalendarMonth.MARCH));
                when(portfolioState.is("February"));
                will(returnValue("00"));
                then(portfolioState.is("Enquired"));

                exactly(1).of(mockPortfolio).getRedistributedAllocation();
                when(portfolioState.is("Enquired"));
                will(returnValue("01"));
                then(portfolioState.is("rebalanced"));
            }
        });


        Path commandFile = Paths.get("src", "test", "resources", "command-success");
        FileCommandProcessor fileCommandProcessor
                = new FileCommandProcessor(commandFile.toFile().getAbsolutePath(), mockPortfolio);
        List<String> result = fileCommandProcessor.process();
        assertEquals(2, result.size());
        assertEquals("00", result.get(0));
        assertEquals("01", result.get(1));
    }

    @Test
    public void shouldThrowExceptionForInvalidPath() {
        FileCommandProcessor fileCommandProcessor
                = new FileCommandProcessor("someInvalidPath", null);
        assertThrows(IllegalArgumentException.class, fileCommandProcessor::process);
    }

    @Test
    public void shouldThrowExceptionForDirectoryPathGivenAsFilePath() {
        Path commandFile = Paths.get("src", "test", "resources");
        FileCommandProcessor fileCommandProcessor
                = new FileCommandProcessor(commandFile.toFile().getAbsolutePath(), null);
        assertThrows(IllegalArgumentException.class, fileCommandProcessor::process);
    }

}