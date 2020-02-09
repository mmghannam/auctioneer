import java.util.List;

public class Bidder_ja_probability_improved implements Bidder{
    private double budget = Auctioneer.defaultConfig.getBudget();
    private int num_bidders = 30;
    double nk;

    public Bidder_ja_probability_improved(){
        this.nk = n_choose_10(num_bidders);
    }

    // given your value for the day, determine an action
    public double getBid(double v) {
        //double a = Math.pow(1./eps * n_choose_k(num_bidders, 10), 10);
        double eps =(1. - v / 200.);
        double a = Math.pow(1./eps * nk, 1./10);
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
