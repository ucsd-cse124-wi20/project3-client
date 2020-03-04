package edu.ucsd.cs;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.Instant;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class DownloadTimeCalculatorTest {

    @Test
    public void testBandwidthAt1() throws IOException {
        String spec = "0 10000";
        IntervalCalculator calc = new IntervalCalculator(spec, Instant.now());
        assertEquals(calc.getValueAtTime(0), 10000);
        assertEquals(calc.getValueAtTime(5000), 10000);
    }

    @Test
    public void testBandwidthAt2() throws IOException {
        String spec = "0 10000, 1000 5000";
        IntervalCalculator calc = new IntervalCalculator(spec, Instant.now());
        assertEquals(calc.getValueAtTime(0), 10000);
        assertEquals(calc.getValueAtTime(1000), 5000);
        assertEquals(calc.getValueAtTime(5000), 5000);
    }

    @Test
    public void testBandwidthAt3() throws IOException {
        String spec = "0 10000, 1000 5000, 2000 7500";
        IntervalCalculator calc = new IntervalCalculator(spec, Instant.now());
        assertEquals(calc.getValueAtTime(0), 10000);
        assertEquals(calc.getValueAtTime(750), 10000);
        assertEquals(calc.getValueAtTime(1000), 5000);
        assertEquals(calc.getValueAtTime(1500), 5000);
        assertEquals(calc.getValueAtTime(2000), 7500);
        assertEquals(calc.getValueAtTime(2300), 7500);
    }

    @Test
    public void testDownloadTime0() throws IOException {
        String spec = "0 10000";
        IntervalCalculator calc = new IntervalCalculator(spec, Instant.now());
        assertEquals(50, calc.calcDownloadTimeAt(0, 500));
    }

    @Test
    public void testDownloadTime1() throws IOException {
        String spec = "0 10000";
        IntervalCalculator calc = new IntervalCalculator(spec, Instant.now());
        assertEquals(500, calc.calcDownloadTimeAt(0, 5000));
    }

    @Test
    public void testDownloadTime2() throws IOException {
        String spec = "0 10000";
        IntervalCalculator calc = new IntervalCalculator(spec, Instant.now());
        assertEquals(800, calc.calcDownloadTimeAt(0, 8000));
    }

    @Test
    public void testDownloadTime3() throws IOException {
        String spec = "0 10000";
        IntervalCalculator calc = new IntervalCalculator(spec, Instant.now());
        assertEquals(800, calc.calcDownloadTimeAt(1000, 8000));
    }

    @Test
    public void testDownloadTime4() throws IOException {
        String spec = "0 10000, 2000 5000";
        IntervalCalculator calc = new IntervalCalculator(spec, Instant.now());
        assertEquals(500, calc.calcDownloadTimeAt(0, 5000));
    }

    @Test
    public void testDownloadTime5() throws IOException {
        String spec = "0 10000, 2000 5000";
        IntervalCalculator calc = new IntervalCalculator(spec, Instant.now());
        assertEquals(500, calc.calcDownloadTimeAt(1000, 5000));
    }

    @Test
    public void testDownloadTime6() throws IOException {
        String spec = "0 5000, 1000 7500, 2000 6500, 2500 8000, 3000 2100";
        IntervalCalculator calc = new IntervalCalculator(spec, Instant.now());
        assertEquals(800, calc.calcDownloadTimeAt(0, 4000));
    }

    @Test
    public void testDownloadTime7() throws IOException {
        String spec = "0 5000, 1000 7500, 2000 6500, 2500 8000, 3000 2100";
        IntervalCalculator calc = new IntervalCalculator(spec, Instant.now());
        assertEquals(800, calc.calcDownloadTimeAt(50, 4000));
    }

    @Test
    public void testDownloadTime8() throws IOException {
        String spec = "0 5000, 1000 7500, 2000 6500, 2500 8000, 3000 2100";
        IntervalCalculator calc = new IntervalCalculator(spec, Instant.now());
        assertEquals(700, calc.calcDownloadTimeAt(500, 4000));
    }
}