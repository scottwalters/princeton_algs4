
//
// in a grid randomly composed of rock and sand , what percentage of squares need to be permeable for the top to percolate to the bottom?
//

// guessing that maintaining the tree has a higher cost than simply doing a connectivity sweep on a plain 2D array

// alternatively, do floodfill every time a space gets opened, but not actually filling unless we hit more fluid or the top

// XXX the "going back up" thing isn't working

public class Percolation {

    private int grid_size;
    private int[][] grid;

    public Percolation(int N) {

        // create an N by N grid, with all sites blocked XXX

        // XXX to do the weighted-quick-union thing, probably have to use two arrays, one for open/blocked/conducting and one for the tree structure

        this(N, 1.0);

    }

    public Percolation(int N, double p) {
        // p is the chance that any site will be blocked
        if(N <= 0) throw new java.lang.IllegalArgumentException();
        grid_size = N;
        grid = new int[N][];
        for(int y = 0; y < N; y++) {
            grid[y] = new int[N];
            for(int x = 0; x < N; x++) {
                grid[y][x] = StdRandom.bernoulli(p) ? 1 : 0;
            }
        }
        
    }

    private void check_coordinates(int y, int x) {
        // bounds checking
        if(x < 1 || x > grid_size || y < 1 || y > grid_size ) throw new java.lang.IllegalArgumentException();
    }

    public void open(int y, int x) {
        
        // open site (row y, column x) if it is not open already

        check_coordinates(y, x);
        grid[y][x] = 0;
    }

    public boolean isOpen(int y, int x) {
        // is site x, y clear?
        check_coordinates(y, x);
        x--; y--;  // 0 based indices
        return grid[y][x] == 0 ? true : false;
    }

    public boolean isFull(int y, int x) {
        // is site x, y occupied?  or rather, is it not blocked and filled with liquid?
        check_coordinates(y, x);
        x--; y--;  // 0 based indices
        return grid[y][x] == 2 ? true : false;
    }

    public boolean percolates() {

        // does the system percolate?

        // could use floodfill (on each open top space, iteratively)

        // could do a more directed, simplified version of that, line by line, more game of life style
        // yeah, let's try that
        // this is pretty clearly N^2
        // yeah, that's making route finding more attractive

        int[] flowing = new int[grid_size];
        for(int i = 0; i < grid_size; i++) flowing[i] = 1;  // start off with liquid flowing down at every square

        line_by_line:
        for(int line = 0; line < grid_size; line++) {

            // debug
            String s = "";
            for( int i = 0; i < grid_size; i++ ) {
                s = s + ( grid[line][i] == 0 ? " " : "X" );
            }

            // step 1, remove squares from flowing[] that are blocked in grid[line][] (blocked=1)
            // if this leaves no open sites, there's no connectivity XXX
            int flowing_squares = 0;
            for( int i = 0; i < grid_size; i++ ) {
                if( grid[line][i] == 1 ) flowing[i] = 0;
                flowing_squares += flowing[i];
            }
            if( flowing_squares == 0 ) return false;

            // step 2, add squares to flowing[] that are open in grid[line][] and adjacent to flowing water in flowing[]
            // sweep to the right then sweep to the left
            // for( int i = 1; i < grid_size; i++ ) if( flowing[i-1] == 1 && grid[line][i] == 0 ) flowing[i] = 1;
            // for( int i = grid_size-2 /* one left of the leftmost element */; i >= 0; i-- ) if( flowing[i+1] == 1 && grid[line][i] == 0 ) flowing[i] = 1;

            // step 3, send water back uphill again.  freak out and back up if we notice that we've spilled left/right under an open spot above us.
            // flowing[] is really non directional with respect to up/down.

            boolean have_to_go_back_up = false;

            for( int i = 1; i < grid_size; i++ ) if( flowing[i] == 1 && grid[line][i] == 0 ) grid[line][i] = 2; // XXX testing ... argh, no, this will cost us speed!

            for( int i = 1; i < grid_size; i++ ) if( flowing[i-1] == 1 && ( grid[line][i] == 0 || grid[line][i] == 2 ) ) {
                // spill to the right
                if( line > 1 && flowing[i] == 0 && grid[line-1][i] == 0 ) have_to_go_back_up = true;
                flowing[i] = 1;
                grid[line][i] = 2; // XXX testing
            }
            for( int i = grid_size-2 /* one left of the leftmost element */; i >= 0; i-- ) if( flowing[i+1] == 1 && ( grid[line][i] == 0 || grid[line][i] == 2 ) ) {
                // spill to the left
                if( line > 1 && flowing[i] == 0 && grid[line-1][i] == 0 ) have_to_go_back_up = true;
                flowing[i] = 1;
                grid[line][i] = 2; // XXX testing
            }

            if( have_to_go_back_up ) { System.out.println("had to go back up"); line--; continue line_by_line; }

            // debug, continued
            s = s + "    ";
            for( int i = 0; i < grid_size; i++ ) {
                s = s + ( flowing[i] == 0 ? " " : "~" );
            }
            System.out.println(s);

        }

        // if we haven't yet aborted due to complete lack of flow across an entire line, then there is flow

        return true;

    }

    public static void main(String[] args) {
        Percolation p = new Percolation(50, 0.5);
        p.percolates();
    }
}
