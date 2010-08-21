

package org.h2.tools;


import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.swing.AbstractAction;

import org.h2.server.ShutdownHandler;


public class HyperDriveStarter implements ShutdownHandler {

    private static final String KIWI_HOME =
            "http://localhost:8080/KiWi/home.seam";

    private Server web;

    private Server tcp;

    private Server pg;

    public HyperDriveStarter() {
    }

    public void start() throws IllegalArgumentException, SecurityException,
            InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException,
            ClassNotFoundException, IOException {
        try {
            startServers();

            final boolean headless = GraphicsEnvironment.isHeadless();
            if (!headless) {
                createTrayIcon();
            }
// if (web != null) {
// StartBrowser.openURL(web.getURL());
// }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    public void startServers() throws SQLException {
        final String[] args = new String[0];

        web = Server.createWebServer(args);
        web.setShutdownHandler(this);
        web.start();

        tcp = Server.createTcpServer(args);
        tcp.start();

        pg = Server.createPgServer(args);
        pg.start();
    }

    private Image loadImage(String s) throws IOException {

        final InputStream resourceAsStream =
                getClass().getClassLoader().getResourceAsStream(s);

        if (resourceAsStream == null) {
            Toolkit.getDefaultToolkit().createImage(new byte[0]);
        }

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int read = 0;
        while ((read = resourceAsStream.read()) != -1) {
            outputStream.write(read);
        }

        final byte[] byteArray = outputStream.toByteArray();
        return Toolkit.getDefaultToolkit().createImage(byteArray);
    }

    public void shutdown() {
        stopAll();
    }

    public void shutdownServAndJVM() {
        stopAll();
        System.exit(0);
    }

    private void stopAll() {
        if (web != null && web.isRunning(false)) {
            web.stop();
            web = null;
        }
        if (tcp != null && tcp.isRunning(false)) {
            tcp.stop();
            tcp = null;
        }
        if (pg != null && pg.isRunning(false)) {
            pg.stop();
            pg = null;
        }
    }

    private boolean createTrayIcon() throws IllegalArgumentException,
            SecurityException, InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException,
            ClassNotFoundException, IOException {

        final Image icon16 = loadImage("kiwi16.gif");
        final Image icon24 = loadImage("kiwi24.gif");

        final Boolean boolean1 =
                (Boolean) Class.forName("java.awt.SystemTray").getMethod(
                        "isSupported", new Class[0])
                        .invoke(null, new Object[0]);
        if (!boolean1.booleanValue()) {
            return false;
        }

        final PopupMenu popupmenu = new PopupMenu();

        final MenuItem showKiWi = new MenuItem("Show KiWi");
        showKiWi.addActionListener(new ShowKiWiAction());
        popupmenu.add(showKiWi);

        final MenuItem showH2Console = new MenuItem("H2 Console");
        showH2Console.addActionListener(new ShowH2ConsoleAction());
        popupmenu.add(showH2Console);

        final MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(new StopAllAction());
        popupmenu.add(exit);

        final Object obj =
                Class.forName("java.awt.SystemTray").getMethod("getSystemTray",
                        new Class[0]).invoke(null, new Object[0]);
        final Dimension dimension =
                (Dimension) Class.forName("java.awt.SystemTray").getMethod(
                        "getTrayIconSize", new Class[0]).invoke(obj,
                        new Object[0]);
        final Image image =
                dimension.width < 24 || dimension.height < 24 ? icon16 : icon24;

        System.out.println(".>" + image);

        final Object obj1 =
                Class.forName("java.awt.TrayIcon").getConstructor(
                        new Class[] {java.awt.Image.class,
                                java.lang.String.class,
                                java.awt.PopupMenu.class}).newInstance(
                        new Object[] {image, "KiWi Console", popupmenu});
        obj1.getClass().getMethod("addMouseListener",
                new Class[] {java.awt.event.MouseListener.class}).invoke(obj1,
                new Object[] {new H2MouseAdapter()});
        obj.getClass().getMethod("add",
                new Class[] {Class.forName("java.awt.TrayIcon")}).invoke(obj,
                new Object[] {obj1});
        return true;
    }

    private void startBrowser(String url) {
        if (web != null) {
            Server.openBrowser(url);
        }
    }

    public void mouseClicked(MouseEvent mouseevent) {
        if (mouseevent.getButton() == 1) {
            startBrowser(KIWI_HOME);
        }
    }

    public void mouseEntered(MouseEvent mouseevent) {
    }

    public void mouseExited(MouseEvent mouseevent) {
    }

    public void mousePressed(MouseEvent mouseevent) {
    }

    public void mouseReleased(MouseEvent mouseevent) {
    }

    public String getWebURL() {
        if (web == null) {
            return null;
        }

        return web.getURL();
    }

    private final class ShowKiWiAction extends AbstractAction {

        private ShowKiWiAction() {
            super("Start KiWi");
        }

        public void actionPerformed(ActionEvent e) {
            startBrowser(KIWI_HOME);
        }
    }

    private final class ShowH2ConsoleAction extends AbstractAction {

        private ShowH2ConsoleAction() {
            super("Show H2 console");
        }

        public void actionPerformed(ActionEvent e) {
            if (web != null) {
                startBrowser(web.getURL());
            }
        }
    }

    private final class StopAllAction extends AbstractAction {

        private StopAllAction() {
            super("Exit");
        }

        public void actionPerformed(ActionEvent e) {
            stopAll();
        }
    }

    private class H2MouseAdapter extends MouseAdapter {

        private H2MouseAdapter() {
            // UNIMPEMENTED
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            startBrowser(KIWI_HOME);
        }
    }
}
