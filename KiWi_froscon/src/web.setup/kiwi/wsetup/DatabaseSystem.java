/**
 *
 */


package kiwi.wsetup;


/**
 * Defines the database system used by the kiwi web setup system.
 * 
 * @author mradules
 */
public enum DatabaseSystem {

    /**
     * The MySQL database.
     */
    MYSQL("MySQL", "com.mysql.jdbc.Driver",
            "org.hibernate.dialect.MySQLDialect"),

    /**
     * The PostgreSQL database.
     */
    POSTGRESS("PostgreSQL", "org.postgresql.Driver",
            "org.hibernate.dialect.PostgreSQLDialect"),

    /**
     * The Hyperdrive (H2) database.
     */
    HYPERDRIVE("H2", "org.h2.Driver", "org.hibernate.dialect.H2Dialect");

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

    /**
     * @param name
     * @param dialectClassName
     */
    private DatabaseSystem(String name, String driver, String dialect) {
        this.name = name;
        this.dialect = dialect;
        driverClassName = driver;
    }

    /**
     * Returns the human readable database name for this database
     * system.
     * 
     * @return the human readable database name for this database
     *         system.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the class absolute name for the jdbc driver for
     * this database system.
     * 
     * @return the class absolute name for the jdbc driver for
     *         this database system.
     */
    public String getDriverClassName() {
        return driverClassName;
    }

    public String getDialect() {
        return dialect;
    }

    static DatabaseSystem getDatabaseForName(String name) {
        for (final DatabaseSystem databaseSystem : values()) {
            if (databaseSystem.getName().equals(name)) {
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

    static String getdatabaseURL(DatabaseSystem databaseSystem, String name) {

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
                result = "jdbc:postgresql:" + name;
                break;
            case MYSQL:
                result = "jdbc:mysql://localhost:3306/" + name;
                break;
            case HYPERDRIVE:
                result = "jdbc:h2:tcp://localhost/~/" + name;
                break;

            default:
                result = null;
                break;
        }

        return result;
    }
}
