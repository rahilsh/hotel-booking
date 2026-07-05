package in.rsh.hotel.booking.util;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for tracking performance metrics across the application.
 * Thread-safe implementation using ConcurrentHashMap.
 */
@Slf4j
public class PerformanceMetrics {

  private static final ConcurrentHashMap<String, MetricStats> metrics = new ConcurrentHashMap<>();

  public static void recordOperation(String operationName, long durationMs) {
    metrics.compute(
        operationName,
        (key, existingStats) -> {
          if (existingStats == null) {
            return new MetricStats(durationMs);
          } else {
            existingStats.addMeasurement(durationMs);
            return existingStats;
          }
        });
  }

  public static void logMetrics() {
    if (metrics.isEmpty()) {
      return;
    }

    log.info("=== Performance Metrics ===");
    metrics.forEach(
        (operationName, stats) ->
            log.info(
                "{}: avg={}ms, min={}ms, max={}ms, count={}",
                operationName,
                stats.getAverageDuration(),
                stats.getMinDuration(),
                stats.getMaxDuration(),
                stats.getCount()));
  }

  public static void reset() {
    metrics.clear();
    log.debug("Performance metrics cleared");
  }

  @Slf4j
  public static class MetricStats {
    private volatile long totalDuration = 0;
    private volatile long minDuration = Long.MAX_VALUE;
    private volatile long maxDuration = 0;
    private volatile long count = 0;

    public MetricStats(long durationMs) {
      this.totalDuration = durationMs;
      this.minDuration = durationMs;
      this.maxDuration = durationMs;
      this.count = 1;
    }

    public synchronized void addMeasurement(long durationMs) {
      totalDuration += durationMs;
      minDuration = Math.min(minDuration, durationMs);
      maxDuration = Math.max(maxDuration, durationMs);
      count++;
    }

    public long getAverageDuration() {
      return count > 0 ? totalDuration / count : 0;
    }

    public long getMinDuration() {
      return minDuration == Long.MAX_VALUE ? 0 : minDuration;
    }

    public long getMaxDuration() {
      return maxDuration;
    }

    public long getCount() {
      return count;
    }
  }
}
