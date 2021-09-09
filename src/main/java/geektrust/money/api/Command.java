package geektrust.money.api;

import geektrust.money.CalendarMonth;
import geektrust.money.manage.Portfolio;

import java.util.Optional;
import java.util.function.BiFunction;

public enum Command {

    ALLOCATE((portfolio, strings) -> {

        portfolio.allocate(new int[]{
                Integer.parseInt(strings[1]),
                Integer.parseInt(strings[2]),
                Integer.parseInt(strings[3])
        });
        return Optional.empty();
    }),
    SIP((portfolio, strings) -> {
        portfolio.sip(new int[]{
                Integer.parseInt(strings[1]),
                Integer.parseInt(strings[2]),
                Integer.parseInt(strings[3])
        });
        return Optional.empty();
    }),
    CHANGE((portfolio, strings) -> {
        portfolio.monthlyChange(CommandUtility.deriveMarketChange(new String[]{strings[1], strings[2], strings[3]}),
                CalendarMonth.valueOf(strings[4]));
        return Optional.empty();
    }),
    BALANCE((portfolio, strings) -> {
        return Optional.of(portfolio.balanceForMonth(CalendarMonth.valueOf(strings[1])));
    }),
    REBALANCE((portfolio, strings) -> {
        return Optional.of(portfolio.getRedistributedAllocation());
    });

    private final BiFunction<Portfolio, String[], Optional<String>> processor;


    Command(BiFunction<Portfolio, String[], Optional<String>> processor) {
        this.processor = processor;
    }

    public Optional<String> process(String[] commandElements, Portfolio portfolio) {
        return processor.apply(portfolio, commandElements);
    }
}
