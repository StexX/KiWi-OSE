/**
 *
 */


package kiwi.util.izpack;

/**
 * Defines the database system used by the kiwi web setup system.
 *
 * @author mradules
 */
enum DatabaseSystem {

    /**
     * The MySQL database.
     */
    MYSQL("MySQL", "com.mysql.jdbc.Driver",
            "org.hibernate.dialect.MySQLDialect", "jdbc:mysql://localhost:3306"),

    /**
     * The PostgreSQL database.
     */
    POSTGRESS("PostgreSQL", "org.postgresql.Driver",
            "org.hibernate.dialect.PostgreSQLDialect", "jdbc:postgresql:"),

    /**
     * The Hyperdrive (H2) database.
     */
    HYPERDRIVE("H2", "org.h2.Driver", "org.hibernate.dialect.H2Dialect",
            "jdbc:h2:tcp://localhost/~/");

    /**
     * The human readable database name.
     */
    private final String name;

    /**
     * The class absolute name for the jdbc driver.
     */
    private final String driverClassName;

    /**
     * The class absolute name for the jdbc driver.
     */
    private final String dialect;

    private final String urlPrefix;

    /**
     * @param name
     * @param dialectClassName
     */
    private DatabaseSystem(String name, String driver, String dialect,
            String urlPrefix) {
        this.name = name;
        this.dialect = dialect;
        driverClassName = driver;
        this.urlPrefix = urlPrefix;
    }

    /**
     * Returns the human readable database name for this database system.
     *
     * @return the human readable database name for this database system.
     */
    String getName() {
        return name;
    }

    /**
     * Returns the class absolute name for the jdbc driver for this database
     * system.
     *
     * @return the class absolute name for the jdbc driver for this database
     *         system.
     */
    String getDriverClassName() {
        return driverClassName;
    }

    String getDialect() {
        return dialect;
    }

    String getUrlPrefix() {
        return urlPrefix;
    }

    static String getDatabaseName(final String url,
            final DatabaseSystem databaseSystem) {
        final String result;
        switch (databaseSystem) {
        case POSTGRESS:
            result = url.substring(url.lastIndexOf(":") + 1);
            break;
        case MYSQL:
            // fall through
        case HYPERDRIVE:
            result = url.substring(url.lastIndexOf("/") + 1);
            break;

        default:
            result = null;
            break;
        }
        return result;
    }

    static String getDatabaseName(final String url, final String driver) {
        final DatabaseSystem databaseForDriver = DatabaseSystem
                .getDatabaseForDriver(driver);
        return getDatabaseName(url, databaseForDriver);
    }

    static DatabaseSystem getDatabaseForName(String name) {
        for (final DatabaseSystem databaseSystem : values()) {
            if (databaseSystem.getName().equalsIgnoreCase(name)) {
                return databaseSystem;
            }
        }
        return null;
    }

    static DatabaseSystem getDatabaseForToString(String toString) {
        for (final DatabaseSystem databaseSystem : values()) {
            if (databaseSystem.toString().equals(toString)) {
                return databaseSystem;
            }
        }
        return null;
    }

    static DatabaseSystem getDatabaseForDriver(String driver) {
        for (final DatabaseSystem databaseSystem : values()) {
            if (databaseSystem.getDriverClassName().equals(driver)) {
                return databaseSystem;
            }
        }
        return null;
    }

    static DatabaseSystem getDatabaseForDialect(String dialect) {
        for (final DatabaseSystem databaseSystem : values()) {
            if (databaseSystem.getDialect().equals(dialect)) {
                return databaseSystem;
            }
        }
        return null;
    }

    static String getdatabaseURL(DatabaseSystem databaseSystem, String name,
            String host, String port) {

        if (databaseSystem == null) {
            new NullPointerException(
                    "The database system argument can not be null.");
        }

        if (name == null) {
            new NullPointerException(
                    "The database name argument can not be null.");
        }

        final String result;
        switch (databaseSystem) {
        case POSTGRESS:
                result = "jdbc:postgresql://" + host + ":" + port + "/" + name;
            break;
        case MYSQL:
                result = "jdbc:mysql://" + host + ":" + port + "/" + name;
            break;
        case HYPERDRIVE:
                result = "jdbc:h2:tcp://" + host + "/~/" + name;
            break;

        default:
            result = null;
            break;
        }

        return result;
    }
}
