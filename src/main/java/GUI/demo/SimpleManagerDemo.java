package GUI.demo;

import GUI.notification.NotificationFactory;
import GUI.notification.NotificationFactory.Location;
import GUI.notification.NotificationManager;
import GUI.notification.manager.SimpleManager;
import GUI.notification.types.AcceptNotification;
import GUI.notification.types.IconNotification;
import GUI.notification.types.ProgressBarNotification;
import GUI.notification.types.TextNotification;
import GUI.theme.ThemePackagePresets;
import GUI.utils.IconUtils;
import GUI.utils.Time;

/**
 * This is a simple demo which uses the SimpleManager to show different types of Notifications.
 */
public class SimpleManagerDemo {
	public static void main(String[] args) throws InterruptedException {
		// this will make the Notifications match the limits of the platform
		// this will mean no fading on unix machines (since it doesn't look too good)
		// Platform.instance().setAdjustForPlatform(true);

		// makes a factory with the built-in clean dark theme
		NotificationFactory factory = new NotificationFactory(ThemePackagePresets.cleanDark());
		// a normal manager that just pops up the notification
		NotificationManager plain = new SimpleManager(Location.NORTHWEST);
		// a fade manager that will make the window fade in and out over a two second period
		SimpleManager fade = new SimpleManager(Location.SOUTHWEST);
		fade.setFadeEnabled(true);
		fade.setFadeTime(Time.seconds(2));

		// adds a text notification to the first manager
		TextNotification notification = factory.buildTextNotification("This is the dark theme",
				"You can have multiple lines\nOf subtitle text as well\nLine 3");
		notification.setCloseOnClick(true);
		// the notification will stay there forever until you click it to exit
		plain.addNotification(notification, Time.infinite());

		Thread.sleep(2000);
		// adds an icon notification that should fade in - note that results may vary depending on the platform
		IconNotification icon = factory.buildIconNotification("This is a really really really long title", "See the cutoff?",
				IconUtils.createIcon("/GUI/demo/exclamation.png", 50, 50));
		fade.addNotification(icon, Time.seconds(2));

		Thread.sleep(6000);
		AcceptNotification accept = factory.buildAcceptNotification("Do you accept?", "This is a fading notification.");
		fade.addNotification(accept, Time.infinite());

		boolean didAccept = accept.blockUntilReply();
		ProgressBarNotification reply = null;
		if (didAccept) {
			reply = factory.buildProgressBarNotification("You accepted");
		} else {
			reply = factory.buildProgressBarNotification("You did not accept");
		}
		reply.setCloseOnClick(true);
		fade.addNotification(reply, Time.infinite());

		for (int i = 0; i < 100; i++) {
			reply.setProgress(i);
			Thread.sleep(100);
		}
		fade.removeNotification(reply);
	}
}
