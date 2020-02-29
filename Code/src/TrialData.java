import java.util.List;

public class TrialData {
    String[] strategies;
    AuctionConfig config;
    DayData[] days;

    class DayData {
        double[] bids = new double[strategies.length];
        double[] values = new double[strategies.length];
        Integer[] sortedIDsByBids = new Integer[strategies.length];
        double[] budgets = new double[strategies.length];
        double[] payments = new double[strategies.length];
        double[] utilities = new double[strategies.length];
    }

    TrialData(List<Class<? extends Bidder>> strategies, AuctionConfig config) {
        this.strategies = new String[strategies.size()];
        for (int i = 0; i < strategies.size(); i++) {
            this.strategies[i] = strategies.get(i).getSimpleName();
        }
        this.config = config;
        this.days = new DayData[config.getDays()];
        for (int i = 0; i < this.days.length; i++) {
            this.days[i] = new DayData();
        }
    }


}
