import java.util.List;

public class Bidder_ja_probability implements Bidder{
    private double budget = Auctioneer.defaultConfig.getBudget();
    private int num_bidders = 30;
    double eps = 0.01;
    double a;

    public Bidder_ja_probability(){
        this.a = Math.pow(1./eps * n_choose_10(num_bidders), 1./10);
    }

    // given your value for the day, determine an action
    public double getBid(double v) {
        return Math.min(a, v);
    }

    // callback function with results
    public void addResults(List<Double> bids, int myBid, double myPayment) {
        // record my utility and budget
        if (myBid >= 0) {
            budget -= myPayment;
        }
    }

    private double n_choose_10(double n){
        // returns (n choose 10) = n!/(10!*(n-10)!)
        double mult = n;
        for (int i = 1; i<10; i++){
            mult *= n-i;
        }
        return mult/3628800;
    }
}
