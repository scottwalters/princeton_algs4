
import java.util.ArrayList;

public class PercolationStats {

    double mean;
    double stddev;
    double confidenceLo;
    double confidenceHi;

    ArrayList<Integer> percolates = new ArrayList<Integer>();
    ArrayList<Integer> doesnt_percolate = new ArrayList<Integer>();

    public PercolationStats(int N, int T) {

        // perform T independent experiments on an N-by-N grid

        for( int i = 0; i < T; i++ ) {
            Percolation perc = new Percolation(N, 0.5); // p value is chance that any square will be blocked XXX dynamically come up with this value
            // System.out.println( "debug: percolates? " + ( perc.percolates() ? "yes" : "no" ) );
            // PercolationVisualizer.draw(perc, N);
            if( perc.percolates() ) {
                
            } else {
            }
        }

    }

    public double mean() {
        // sample mean of percolation threshold
        return this.mean;
    }
    public double stddev() {
        // sample standard deviation of percolation threshold
        return this.stddev;
    }
    public double confidenceLo() {
        // low  endpoint of 95% confidence interval
        return this.confidenceLo;
    }
    public double confidenceHi() {
        // high endpoint of 95% confidence interval
        return this.confidenceHi;
    }

    public static void main(String[] args) {
        // test client

        Stopwatch stopwatch = new Stopwatch();

        PercolationStats ps = new PercolationStats( Integer.parseInt(args[0]), Integer.parseInt(args[1]) );

        double time = stopwatch.elapsedTime();

        System.out.println( "runtime: " + time );
    }

}
