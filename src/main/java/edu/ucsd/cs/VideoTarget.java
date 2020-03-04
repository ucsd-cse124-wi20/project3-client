package edu.ucsd.cs;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public final class VideoTarget {

    private PrintWriter transcript;
    private Instant startTime;
    private MessageDigest digest;

    public VideoTarget(String transcriptFile, Instant sTime) {

        try {
            this.digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.toString());
            System.exit(1);
        }

        try {
            this.transcript = new PrintWriter(transcriptFile);
        } catch (FileNotFoundException e) {
            System.err.println("Cannot open transcript file: " + transcriptFile);
            System.err.println(e.toString());
            System.exit(1);
        }
        
        this.startTime = sTime;

        log("Starting VideoTarget");
    }

    public void deliver(final int chunknumber, final int chunkquality, final byte[] chunk) {
        if (chunknumber < 0) {
            System.err.println("Chunk number " + chunknumber + " must be non-negative");
            System.exit(1);
        }

        if (chunkquality < 1 || chunkquality > 5) {
            System.err.println("Chunk quality " + chunkquality + " must be between 1 and 5");
            System.exit(1);
        }
        
        StringBuilder log = new StringBuilder();
        log.append("chunk_num: " + chunknumber + ",");
        log.append("chunk_quality: " + chunkquality + ",");
        log.append("chunk_len: " + chunk.length + ",");
        log.append("chunk_hash: " + hashBytes(chunk));

        log(log.toString());
    }

    public void close() {
        this.transcript.close();
    }

    protected void log(String message) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(new Date() + ",");

        Duration timeSinceStart = Duration.between(this.startTime, Instant.now());
        buffer.append(timeSinceStart.toMillis() + ",");
        buffer.append(message);

        transcript.println(buffer.toString());
        transcript.flush();
    }

    protected long millisSinceStart() {
        return Duration.between(this.startTime, Instant.now()).toMillis();
    }

    protected String hashBytes(byte[] bytes) {
        byte[] hash = this.digest.digest(bytes);
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();

    }
}