package lessstrain.gui.demo;

import lessstrain.gui.notification.NotificationFactory;
import lessstrain.gui.notification.manager.SlideManager;
import lessstrain.gui.notification.types.TextNotification;
import lessstrain.gui.theme.ThemePackagePresets;
import lessstrain.gui.utils.Time;

public class SlideManagerDemo {
	public static void main(String[] args) throws Exception {
		// makes a factory with the clean theme
		NotificationFactory factory = new NotificationFactory(ThemePackagePresets.cleanLight());
		// adds a new manager which displays Notifications at the bottom right of the screen
		SlideManager manager = new SlideManager(NotificationFactory.Location.SOUTHEAST);

		// adds a notification that appears southeast and slides in the default direction, north
		manager.addNotification(factory.buildTextNotification("This is sliding north", "Appeared southeast"), Time.seconds(5));

		manager.setSlideDirection(SlideManager.SlideDirection.WEST);
		// adds a notification that appears southeast and slides west
		manager.addNotification(factory.buildTextNotification("This is sliding west", "Appeared southeast"), Time.seconds(1));
		// these two notifications should finish in the same end position

		Thread.sleep(1000);
		manager.setLocation(NotificationFactory.Location.NORTHEAST);
		manager.setSlideDirection(SlideManager.SlideDirection.SOUTH);
		TextNotification northeastNotification = factory.buildTextNotification("This is sliding south", "Appeared northeast");
		northeastNotification.setCloseOnClick(true);
		manager.addNotification(northeastNotification, Time.seconds(3));
	}
}