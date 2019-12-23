package Test;

import com.company.ParallelMonteCarloPi;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParallelMonteCarloPiTest {

    private int CPU_COUNT;
    private ParallelMonteCarloPi pi;

    @Before
    public void setUP(){
            pi = new ParallelMonteCarloPi(8);


    }

    @Test
    public void apportionTest(){

        long[] expected = new long[8];
        long[] actual = pi.apportion(8);

        assertEquals(expected.length, actual.length);
    }

}
