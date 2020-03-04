package edu.ucsd.cs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class IntervalCalculator {

    private static class Interval {
        // Provided by the spec file
        public long startTime;
        public long value;

        // derived values
        public long length;
    }

    private List<Interval> timeline;
    private Instant startTime;

    public IntervalCalculator(String spec, Instant sTime) {
        this.timeline = parseSpec(spec);
        this.startTime = sTime;
    }

    public IntervalCalculator(File specfile, Instant sTime) throws IOException {
        this(new String(Files.readAllBytes(specfile.toPath())), sTime);
    }

    private static List<Interval> parseSpec(String spec) {
        ArrayList<Interval> intervals = new ArrayList<Interval>();

        // Create a timeline of time offsets and values
        long previousStartTime = Long.MIN_VALUE;
        for (String line : spec.split("\\r?\\n?,")) {
            String[] pairs = line.trim().split(" ");
            assert(pairs.length == 2);

            Interval interval = new Interval();
            interval.startTime = Long.parseLong(pairs[0]);
            interval.value = Long.parseLong(pairs[1]);

            if (interval.startTime <= previousStartTime) {
                System.err.println("Times in spec must increase monotonically");
                System.exit(1);
            }
            previousStartTime = interval.startTime;

            intervals.add(interval);
        }

        for (int i = 0; i < intervals.size(); i++) {
            Interval current = intervals.get(i);

            if (i < intervals.size() - 1) {
                Interval next = intervals.get(i+1);
                current.length = next.startTime - current.startTime;
            } else {
                current.length = Long.MAX_VALUE;
            }
        }

        return intervals;
    }

    public long getValueAtTime(int timeoffset) {
        Interval interval = getIntervalAtTime(timeoffset);
        assert(interval != null);
        return interval.value;
    }

    private Interval getIntervalAtTime(long timeoffset) {
        assert(timeline.size() > 0);

        for (int i = 0; i < timeline.size(); i++) {
            Interval current = timeline.get(i);

            if (current.length == Long.MAX_VALUE) {
                return current;
            }

            if (current.startTime <= timeoffset && timeoffset < current.startTime + current.length) {
                return current;
            }
        }

        // Why do we want this null here?
        return null;
    }

    public long calcDownloadTime(long size) {
        long timeoffset = Duration.between(startTime, Instant.now()).toMillis();
        return calcDownloadTimeAt(timeoffset, size);
    }

	public long calcDownloadTimeAt(long timeoffset, long size) {
        
        // Determine the current time interval and corresponding rate
        Interval curr_interval = getIntervalAtTime(timeoffset);
        long curr_rate = curr_interval.value;
        long curr_left_length = curr_interval.length + curr_interval.startTime - timeoffset;

        // Compute the expected download time 
        long download_time = (long) (((double) size / curr_rate) * 1000);

        // If the left time in this interval is not enough, we compute the left time recursively
        if (download_time > curr_left_length) {
            long curr_max_download = (curr_rate * curr_left_length / 1000);

            download_time = curr_left_length + calcDownloadTimeAt(timeoffset + curr_left_length,
                size - curr_max_download);
        }

        return download_time;
    }
}
