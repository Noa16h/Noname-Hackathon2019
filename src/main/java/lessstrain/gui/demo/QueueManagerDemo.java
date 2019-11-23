package lessstrain.gui.demo;

import lessstrain.gui.notification.NotificationFactory;
import lessstrain.gui.notification.manager.QueueManager;
import lessstrain.gui.notification.types.WindowNotification;
import lessstrain.gui.platform.Platform;
import lessstrain.gui.theme.ThemePackagePresets;
import lessstrain.gui.utils.IconUtils;
import lessstrain.gui.utils.Time;

import java.awt.*;

public class QueueManagerDemo {
	public static void main(String[] args) throws Exception {
		// activate the right platform
		Platform.instance().setAdjustForPlatform(true);

		// notifyfactory used for creating notes, use the style dracula
		NotificationFactory factory = new NotificationFactory(ThemePackagePresets.dracula());

		// queue manager, make notes appear on bottom left
		QueueManager manager = new QueueManager(NotificationFactory.Location.SOUTHEAST);
		//let them stack upwards
		manager.setScrollDirection(QueueManager.ScrollDirection.NORTH);
		// sets how quickly old notifications should move out of the way for new ones
		manager.setSnapFactor(0.2);

		for (int i = 0; i < 10; i++) {
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