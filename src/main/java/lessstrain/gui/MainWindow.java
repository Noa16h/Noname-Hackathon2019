package lessstrain.gui;
import java.awt.SystemTray;

public class MainWindow {
    public static void main(String[] args){
        System.out.println("test");
        boolean isTrayEnabled = SystemTray.isSupported();
        System.out.println("is enabled: " + isTrayEnabled);
    }
}
