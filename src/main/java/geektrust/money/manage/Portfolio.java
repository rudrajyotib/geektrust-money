package geektrust.money.manage;

import geektrust.money.CalendarMonth;

import java.util.Arrays;

public class Portfolio {
    private final int[] investments;
    private final float[] distribution;
    private final int[][] history;
    private final boolean[] marketChangeTracker;
    private int[] sip;

    public Portfolio() {
        this.investments = new int[3];
        this.distribution = new float[3];
        this.history = new int[12][3];
        this.marketChangeTracker = new boolean[12];
        this.sip = new int[3];
    }

    public void allocate(int[] allocation) {
        if (allocation.length == this.investments.length) {
            for (int i = 0; i < investments.length; i++) {
                investments[i] += allocation[i];
            }
        }

        int totalPortfolio = Arrays.stream(investments).sum();
        this.distribution[0] = (float) investments[0] / totalPortfolio;
        this.distribution[1] = (float) investments[1] / totalPortfolio;
        this.distribution[2] = (float) investments[2] / totalPortfolio;
    }

    public void sip(int[] allocation) {
        if (allocation.length == investments.length) {
            this.sip = allocation;
        }
    }

    public String presentStateOfAllocation() {
        return String.format("%d %d %d", this.investments[0], this.investments[1], this.investments[2]);
    }

    public String balanceForMonth(CalendarMonth calendarMonth) {
        if (!this.marketChangeTracker[calendarMonth.getIndex()]) {
            throw new IllegalArgumentException("For the specified month, investment not evaluated");
        }
        return String.format("%d %d %d", this.history[calendarMonth.getIndex()][0],
                this.history[calendarMonth.getIndex()][1],
                this.history[calendarMonth.getIndex()][2]);
    }

    public void monthlyChange(float[] changeRate, CalendarMonth month) {
        if (this.marketChangeTracker[month.getIndex()]) {
            return;
        }
        for (int i = 0; i < 3; i++) {
            if (month != CalendarMonth.JANUARY) {
                investments[i] = investments[i] + sip[i];
            }
            investments[i] = (int) (investments[i] + (investments[i] * changeRate[i]));
        }
        if ((month == CalendarMonth.JUNE) || (month == CalendarMonth.DECEMBER)) {
            redistributeAllocation();
        }
        this.history[month.getIndex()] = investments.clone();
        this.marketChangeTracker[month.getIndex()] = true;
    }

    private void redistributeAllocation() {
        int totalWorthOfInvestment = Arrays.stream(investments).sum();
        this.investments[0] = (int) (totalWorthOfInvestment * distribution[0]);
        this.investments[1] = (int) (totalWorthOfInvestment * distribution[1]);
        this.investments[2] = (int) (totalWorthOfInvestment * distribution[2]);
    }

    public String getRedistributedAllocation() {
        if (this.marketChangeTracker[11]) {
            return String.format("%d %d %d", this.history[11][0], this.history[11][1], this.history[11][2]);
        }
        if (this.marketChangeTracker[5]) {
            return String.format("%d %d %d", this.history[5][0], this.history[5][1], this.history[5][2]);
        }
        return "CANNOT_REBALANCE";
    }


}
