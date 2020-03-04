package edu.ucsd.cs;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Random;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class VideoTargetTest {
    @Test
    public void testStreamReporter1() throws FileNotFoundException, NoSuchAlgorithmException {
        final Random rnd = new Random();
        final VideoTarget vtarget = new VideoTarget("videotranscript.txt", Instant.now());

        final byte[] chunk = new byte[4096];
        rnd.nextBytes(chunk);
        vtarget.deliver(1, 5, chunk);

        try {
            Thread.sleep(1500);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }

        rnd.nextBytes(chunk);
        vtarget.deliver(2, 3, chunk);

        try {
            Thread.sleep(2300);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }

        rnd.nextBytes(chunk);
        vtarget.deliver(3, 3, chunk);
    }
}
