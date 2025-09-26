package de.zuse.hotel.db;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;


/**
 * This class provides a JDBC connector to the hotel database.
 * It also contains a method to obtain an entity manager factory for JPA persistence.
 * The class uses the HSQLDB embedded database engine and the Hibernate JPA provider.
 */
public class JDBCConnecter
{
    //jdbc:hsqldb:hsql://localhost/testdb

    private static final String DB_NAME = "jdbc:hsqldb:file:src/main/resources/de/zuse/hotel/db/example";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root123";
    public static final String PERSISTENCE_NAME = "ZuseHotel";
    private static Connection conn;
    private static EntityManagerFactory factory;


    /**
     * Prints the available JDBC drivers.
     */
    public static void printDrivers()
    {
        final Enumeration<Driver> driver = DriverManager.getDrivers();

        while (driver.hasMoreElements())
        {
            System.out.println(driver.nextElement().getClass().getName());
        }
    }

    /**
     * Obtains a connection to the database.
     *
     * @return the connection to the database
     * @throws Exception if an error occurs while obtaining the connection
     */
    public static Connection getConnection() throws Exception
    {
        conn = DriverManager.getConnection(DB_NAME, USER_NAME, PASSWORD);
        return conn;
    }

    /**
     * Shuts down the connection and the entity manager factory.
     *
     * @throws Exception if an error occurs while shutting down the connection or the entity manager factory
     */
    public static void shutdown() throws Exception
    {
        if (conn != null)
            conn.close();

        if (factory != null)
            factory.close();

        factory = null;
    }

    /**
     * Obtains the entity manager factory for JPA.
     *
     * @return the entity manager factory for JPA
     */
    public static EntityManagerFactory getEntityManagerFactory()
    {
        try
        {
            if (factory == null)
            {
                factory = Persistence.createEntityManagerFactory(PERSISTENCE_NAME);
            }

            return factory;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
