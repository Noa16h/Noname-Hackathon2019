package GUI.demo;

import GUI.notification.NotificationFactory;
import GUI.notification.NotificationManager;
import GUI.notification.NotificationFactory.Location;
import GUI.notification.manager.SimpleManager;
import GUI.platform.Platform;
import GUI.theme.ThemePackagePresets;
import GUI.utils.Time;

public class CustomNotificationDemo {
	public static void main(String[] args) throws Exception {
		Platform.instance().setAdjustForPlatform(true);
		// UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		// register the custom builder with the factory
		NotificationFactory factory = new NotificationFactory(ThemePackagePresets.cleanLight());
		factory.addBuilder(CustomNotification.class, new CustomNotification.CustomBuilder());

		// add the Notification
		NotificationManager manager = new SimpleManager(Location.NORTH);
		manager.addNotification(factory.build(CustomNotification.class, "this is test text"), Time.infinite());
	}
}
