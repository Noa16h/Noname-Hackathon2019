package lessstrain.gui.demo;

import lessstrain.gui.notification.NotificationFactory;
import lessstrain.gui.notification.NotificationManager;
import lessstrain.gui.notification.manager.SimpleManager;
import lessstrain.gui.notification.types.AcceptNotification;
import lessstrain.gui.notification.types.IconNotification;
import lessstrain.gui.notification.types.ProgressBarNotification;
import lessstrain.gui.notification.types.TextNotification;
import lessstrain.gui.theme.ThemePackagePresets;
import lessstrain.gui.utils.IconUtils;
import lessstrain.gui.utils.Time;

/**
 * This is a simple demo which uses the SimpleManager to show different types of Notifications.
 */
public class SimpleManagerDemo {
	public static void main(String[] args) throws InterruptedException {
		// this will make the Notifications match the limits of the platform
		// this will mean no fading on unix machines (since it doesn't look too good)
		// Platform.instance().setAdjustForPlatform(true);

		// makes a factory with the built-in clean dark theme
		NotificationFactory factory = new NotificationFactory(ThemePackagePresets.cleanLight());
		// a normal manager that just pops up the notification
		NotificationManager plain = new SimpleManager(NotificationFactory.Location.NORTHWEST);
		// a fade manager that will make the window fade in and out over a two second period
		SimpleManager fade = new SimpleManager(NotificationFactory.Location.SOUTHWEST);
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
				IconUtils.createIcon("/lessstrain/gui/demo/exclamation.png", 50, 50));
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
