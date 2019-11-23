package lessstrain.gui.demo;

import lessstrain.gui.notification.NotificationFactory;
import lessstrain.gui.notification.NotificationManager;
import lessstrain.gui.notification.manager.SimpleManager;
import lessstrain.gui.platform.Platform;
import lessstrain.gui.theme.ThemePackagePresets;
import lessstrain.gui.utils.Time;

public class CustomNotificationDemo {
	public static void main(String[] args) throws Exception {
		Platform.instance().setAdjustForPlatform(true);
		// UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		// register the custom builder with the factory
		NotificationFactory factory = new NotificationFactory(ThemePackagePresets.cleanLight());
		factory.addBuilder(CustomNotification.class, new CustomNotification.CustomBuilder());

		// add the Notification
		NotificationManager manager = new SimpleManager(NotificationFactory.Location.NORTH);
		manager.addNotification(factory.build(CustomNotification.class, "this is test text"), Time.infinite());
	}
}
