package lessstrain.gui.demo;

import lessstrain.gui.notification.Notification;
import lessstrain.gui.notification.NotificationFactory;
import lessstrain.gui.notification.manager.SequenceManager;
import lessstrain.gui.notification.types.WindowNotification;
import lessstrain.gui.platform.Platform;
import lessstrain.gui.theme.ThemePackagePresets;
import lessstrain.gui.utils.IconUtils;
import lessstrain.gui.utils.Time;

/**
 * This is a simple demo which tests the SequenceManager.
 */
public class SequenceManagerDemo {
	public static void main(String[] args) {
		// this will make the Notifications match the limits of the platform
		// this will mean no fading on unix machines (since it doesn't look too good)
		Platform.instance().setAdjustForPlatform(true);

		// makes a factory with the built-in clean light theme
		NotificationFactory factory = new NotificationFactory(ThemePackagePresets.aqua());
		SequenceManager sequence = new SequenceManager();
		// makes the notifications fade in and out
		sequence.setFadeEnabled(true);

		// builds the various test Notifications
		Notification note = factory.buildTextNotification("Test", "test");
		WindowNotification note2 = factory.buildIconNotification("You must click this!", "To make it go away.",
				IconUtils.createIcon("/lessstrain/gui/demo/exclamation.png", 50, 50));
		note2.setCloseOnClick(true);
		Notification note3 = factory.buildAcceptNotification("Test 3", "This will show for three seconds");

		// adds the Notifications in the order that they should be shown in
		// the next Notification will not appear until the previous has become hidden
		sequence.addNotification(note, Time.seconds(3));
		sequence.addNotification(note2, Time.infinite());
		sequence.addNotification(note3, Time.seconds(3));
	}
}
