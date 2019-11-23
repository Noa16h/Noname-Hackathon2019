package GUI.platform;

public class DefaultOperatingSystem implements OperatingSystem {
	@Override
	public boolean isSupported(String feature) {
		return true;
	}
}
