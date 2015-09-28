package name.felixbecker.freemarkerdebug.jmx;

import name.felixbecker.freemarkerdebug.FMDebugConfiguration;

public class FMDebugJMXBeanImpl implements FMDebugJMXBean {

	@Override
	public long getAlertThresholdInMs() {
		return FMDebugConfiguration.ALERT_THRESHOLD_IN_MS;
	}

	@Override
	public void setAlertThresholdInMs(long threshold) {
		FMDebugConfiguration.ALERT_THRESHOLD_IN_MS = threshold;
	}

	

}
