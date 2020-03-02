import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
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


    public void toJson(String filename) {
        Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create(); // use this for pretty print
        Gson gson = new Gson();
        try {
            gson.toJson(this, new FileWriter(filename + ".json"));
            gsonPretty.toJson(this, new FileWriter(filename + "_pretty.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toCSV(String filename) throws IOException {
        FileWriter writer = new FileWriter("../analysis/x.csv");
        StringBuilder sb = new StringBuilder();
        for (int bidderId = 0; bidderId < strategies.length; bidderId++) {
            for (int day = 0; day < days.length; day++) {
                // write csv line: bidderId, day, budget, value, bid, utility, payment, rank(by bids)
                DayData data = this.days[day];
                sb.append(bidderId);
                sb.append(',');
                String bidderStrategyName = this.strategies[bidderId].replace("Bidder_","");
                sb.append(bidderStrategyName);
                sb.append(',');
                sb.append(day);
                sb.append(',');
                sb.append(data.budgets[bidderId]);
                sb.append(',');
                sb.append(data.values[bidderId]);
                sb.append(',');
                sb.append(data.bids[bidderId]);
                sb.append(',');
                sb.append(data.utilities[bidderId]);
                sb.append(',');
                sb.append(data.payments[bidderId]);
                sb.append(',');
                sb.append(indexOf(data.sortedIDsByBids, bidderId));
                sb.append('\n');
            }
        }
        writer.write(sb.toString());
        writer.close();
    }

    public static int indexOf(Integer[] arr, int val) {
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == val) {
                index = i;
                break;
            }
        }
        return index;
    }

}
