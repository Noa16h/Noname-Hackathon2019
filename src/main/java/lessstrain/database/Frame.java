package lessstrain.database;

import java.util.Objects;

public final class Frame {
    private final long timestamp;
    private final double distance;
    private final int apm;

    Frame(final long timestamp, final double distance, final int apm) {
        this.timestamp = timestamp;
        this.distance = distance;
        this.apm = apm;
    }

    public Frame(final double distance, final int apm) {
        this(0, distance, apm);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getDistance() {
        return distance;
    }

    public int getApm() {
        return apm;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Frame)) return false;
        final Frame frame = (Frame) o;
        return timestamp == frame.timestamp &&
                Double.compare(frame.distance, distance) == 0 &&
                apm == frame.apm;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, distance, apm);
    }
}
