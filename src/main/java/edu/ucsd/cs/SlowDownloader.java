package edu.ucsd.cs;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.time.Instant;

public final class SlowDownloader {
    private IntervalCalculator calc;

    public SlowDownloader(File bwspec, Instant sTime) {
        try {
            this.calc = new IntervalCalculator(bwspec, sTime);
        } catch (IOException e) {
            System.err.println("Error with bandwidth specification: " + bwspec);
            System.err.println(e.toString());
            System.exit(1);
        }
    }

    public SlowDownloader(String bwspec, Instant sTime) {
        this.calc = new IntervalCalculator(bwspec, sTime);
    }
    
    public static final class DownloadedFile {

        public final byte[] contents;
        public final String contentType;
        public Duration downloadTime;

        public DownloadedFile(final byte[] contents, final String contentType) {
            this.contents = contents;
            this.contentType = contentType;
            this.downloadTime = Duration.ZERO;
        }
    }

    private static final DownloadedFile getURL(final URL url) throws IOException {
        final Instant start = Instant.now();
        final URLConnection urlconn = url.openConnection();
        urlconn.setConnectTimeout(5000);

        final BufferedInputStream in = new BufferedInputStream(urlconn.getInputStream());
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        final byte[] data = new byte[4096];
        while ((nRead = in.read(data)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        final Instant end = Instant.now();

        final DownloadedFile result = new DownloadedFile(buffer.toByteArray(), urlconn.getContentType());
        result.downloadTime = Duration.between(start, end);
        return result;
    }

    public final DownloadedFile slowGetURL(final URL url) throws IOException {
        final DownloadedFile f = getURL(url);
        final long emulatedDownloadTime = calc.calcDownloadTime(f.contents.length * 8);
        System.out.println("size: " + f.contents.length + " time: " + emulatedDownloadTime);

        final long timeToSleep = emulatedDownloadTime - f.downloadTime.toMillis();
        if (timeToSleep <= 0) {
            System.err.println("Warning: actual network bandwidth is less than emulated network bandwidth");
        }

        try {
            if (timeToSleep > 0) { Thread.sleep(timeToSleep); }
        } catch (final InterruptedException e) {
            System.err.println("Got interrupted, continuing");
        }

        return f;
    }
}
