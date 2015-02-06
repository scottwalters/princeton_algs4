
//
// in a grid randomly composed of rock and sand, is water permeable from the top to the bottom, according to a connectivity test?
//

public class Percolation {

    private WeightedQuickUnionUF qu;
    private int grid_size;
    private boolean[] grid;     // true indicates space is occupied

    // these get set after we check connectivity
    private int a_top_square = 0;
    private boolean found_a_top_square = false;

    public Percolation(int N) {

        // create an N by N grid, with all sites blocked

        this(N, 1.0);

    }

    public Percolation(int N, double p) {

        // p is the chance that any site will be blocked

        if(N <= 0) throw new java.lang.IllegalArgumentException();

        grid_size = N;

        qu = new WeightedQuickUnionUF( N * N );   // mapping 2D stuff onto 1D

        grid = new boolean[ N * N ];

        for(int i = 0; i < N * N; i++) {
            grid[i] = true;  // blocked by default; do this pass first as this.open() gets excited when it sees unblocked spaces
        }

        for(int y = 1; y <= N; y++) {              // dumb API has arrays starting and index 1
            for(int x = 1; x <= N; x++) {
                if( ! StdRandom.bernoulli(p) ) this.open(y, x);
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
        y--; x--;  // 0 based indices

        int our_index = y * grid_size + x;
        int top_index = y * grid_size + x - grid_size;
        int bot_index = y * grid_size + x + grid_size;
        int left_index = y * grid_size + x - 1;
        int right_index = y * grid_size + x + 1;

        grid[our_index] = false;  // unblocked now

        // if any adjacent squares are marked as unblocked, merge our group into their group

        if( y > 0 && ! grid[top_index] )             qu.union( our_index, top_index );
        if( y < grid_size-2 && ! grid[bot_index] )   qu.union( our_index, bot_index );
        if( x > 0 && ! grid[left_index] )            qu.union( our_index, left_index );
        if( x < grid_size-2 && ! grid[right_index] ) qu.union( our_index, right_index );

    }

    public boolean isOpen(int y, int x) {
        // is site x, y clear?
        check_coordinates(y, x);
        x--; y--;  // 0 based indices
        return ! grid[ y * grid_size + x];
    }

    public boolean isFull(int y, int x) {
        // is site x, y filled with fluid?
        check_coordinates(y, x);
        if( ! this.isOpen( y, x ) ) return false;
        x--; y--;  // 0 based indices
        if( ! found_a_top_square ) {
            percolates();
        }
        return qu.connected( a_top_square, y * grid_size + x );
    }

    public boolean percolates() {

        // does the system percolate?

        // put all of the open top squares into the same group, then test if any of the open bottom squares are in that group

        for( int x = 0; x < grid_size; x++ ) {
            if( found_a_top_square && grid[x] == false ) {     // == false means not blocked
                qu.union( a_top_square, x );                   // join them
            } else if( grid[x] == false ) {
                a_top_square = x;
                found_a_top_square = true;
            }
        }

        if( ! found_a_top_square ) return false;  // it might happen that the entire top is blocked solid, in which case there is no connectivity and we need to avoid further tests that would error

        for( int x = 0; x < grid_size; x++ ) {
            int index = ( grid_size - 1 ) * grid_size + x;
            if( grid[index] == false && qu.connected( a_top_square, index ) ) return true;
        }

        return false;

    }

    public static void main(String[] args) {
        Percolation p = new Percolation(50, 0.5);
        p.percolates();
    }
}
