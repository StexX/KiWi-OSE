

package kiwi.service;


import org.h2.tools.HyperDriveStarter;
import org.jboss.system.ServiceMBeanSupport;


public class H2StartupService extends ServiceMBeanSupport implements
        H2StartupServiceMBean {

    private final HyperDriveStarter starter;

    public H2StartupService() {
        starter = new HyperDriveStarter();
    }

    @Override
    protected void startService() throws Exception {
        starter.start();
        final StringBuilder msg = new StringBuilder();
        msg.append("The hyperdrive databse server si started,");
        msg.append("you can connect with a web browser on :");
        msg.append(starter.getWebURL());
        log.info(msg);
    }

    @Override
    protected void stopService() throws Exception {
        starter.shutdown();
        log.info("The hyperdrive db is shutdown.");
    }
}
