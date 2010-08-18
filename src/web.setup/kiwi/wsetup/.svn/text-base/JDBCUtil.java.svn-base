

package kiwi.wsetup;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


class JDBCUtil {

    /**
     *
     */
    private JDBCUtil() {
        // UNIMPLEMETNED
    }

    static Connection getSimpleConnection(String url, String driver,
            String user, String password) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, SQLException {

        Class.forName(driver).newInstance();

        final Connection result =
                DriverManager.getConnection(url, user, password);
        return result;
    }

    static boolean canConnectTo(String url, String driver, String user,
            String password) {
        try {
            getSimpleConnection(url, driver, user, password);
        } catch (final Exception e) {
            return false;
        }

        return true;
    }
}
