package GUI.demo;

import GUI.notification.NotificationFactory;
import GUI.notification.NotificationFactory.Location;
import GUI.notification.manager.QueueManager;
import GUI.notification.types.WindowNotification;
import GUI.platform.Platform;
import GUI.theme.ThemePackagePresets;
import GUI.utils.IconUtils;
import GUI.utils.Time;

import java.awt.*;

public class QueueManagerDemo {
	public static void main(String[] args) throws Exception {
		// activate the right platform
		Platform.instance().setAdjustForPlatform(true);

		// notifyfactory used for creating notes, use the style dracula
		NotificationFactory factory = new NotificationFactory(ThemePackagePresets.dracula());

		// queue manager, make notes appear on bottom left
		QueueManager manager = new QueueManager(Location.SOUTHEAST);
		//let them stack upwards
		manager.setScrollDirection(QueueManager.ScrollDirection.NORTH);
		// sets how quickly old notifications should move out of the way for new ones
		manager.setSnapFactor(0.2);

		for (int i = 0; i < 4; i++) {
			//create notification
			WindowNotification note = factory.buildTextNotification("LessStrain", "Please take a break\n and get lunch!");
			// when the notification is clicked, it should hide itself
			note.setCloseOnClick(true);
			// make it show in the queue for five seconds
			manager.addNotification(note, Time.seconds(10));
			// one second delay between creations
			Thread.sleep(1000);
		}
	}
}