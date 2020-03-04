package edu.ucsd.cs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

import org.junit.Test;

import edu.ucsd.cs.SlowDownloader.DownloadedFile;

/**
 * Unit test for simple App.
 */
public class SlowDownloaderTest {

    public void testStaticDownload1() {

        try {
            
            final URL url = new URL("https://cseweb.ucsd.edu/~gmporter/kitten.png");
            SlowDownloader httpclient = new SlowDownloader("0 10000", Instant.now());
            final DownloadedFile kitten = httpclient.slowGetURL(url);

            assertEquals("image/png", kitten.contentType);
            assertEquals(1176071, kitten.contents.length);

            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            assertEquals("d5d9d36d2043aec0ee148563ce624086bd7f5848d6d4f168d3b683f1c7ec3260",
                         toHexString(md.digest(kitten.contents)));

        } catch (final NoSuchAlgorithmException e) {
            fail(e.getMessage());
        } catch (final MalformedURLException e) {
            fail(e.getMessage());
        } catch (final IOException e) {
            fail(e.toString());
        }
    }

    // https://www.geeksforgeeks.org/sha-256-hash-in-java/
    public static String toHexString(final byte[] hash) {
        // Convert byte array into signum representation
        final BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        final StringBuilder hexString = new StringBuilder(number.toString(16));
  
        // Pad with leading zeros 
        while (hexString.length() < 32)  
        {  
            hexString.insert(0, '0');  
        }  
  
        return hexString.toString();  
    } 
}
