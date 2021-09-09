package geektrust.money;

/*
 * Solution for GeekTrust problem
 * https://www.geektrust.in/coding-problem/backend/mymoney
 */

import geektrust.money.api.FileCommandProcessor;
import geektrust.money.manage.Portfolio;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Portfolio portfolio = new Portfolio();
        FileCommandProcessor fileCommandProcessor
                = new FileCommandProcessor(args[0], portfolio);
        fileCommandProcessor.process().forEach(System.out::println);
    }

}
