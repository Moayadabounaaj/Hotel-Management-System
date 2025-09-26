package de.zuse.hotel.db;

import de.zuse.hotel.core.Address;
import de.zuse.hotel.util.ZuseCore;
import org.hibernate.cfg.NotYetImplementedException;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * This class implements the DatabaseOperations interface to perform CRUD operations on Address objects in the database.
 */
public class AddressConnecter implements DatabaseOperations
{

    /**
     * Creates a new record for the given Address object in the database.
     *
     * @param object The Address object to create in the database.
     */
    @Override
    public void dbCreate(Object object)
    {
        if (!(object instanceof Address))
            ZuseCore.coreAssert(false, "object must be Address");

        EntityManager manager = JDBCConnecter.getEntityManagerFactory().createEntityManager();
        Address address = (Address) object;
        manager.getTransaction().begin();
        manager.persist(address);
        manager.getTransaction().commit();
        manager.close();
    }

    /**
     * Searches the database for all Address objects and returns them as a List.
     *
     * @return A List containing all Address objects in the database.
     */
    @Override
    public List<Address> dbsearchAll()
    {
        EntityManager manager = JDBCConnecter.getEntityManagerFactory().createEntityManager();
        manager.getTransaction().begin();
        List<Address> oneAddresses = manager.createNativeQuery("SELECT * FROM Address", Address.class).getResultList();
        manager.getTransaction().commit();
        manager.close();
        return oneAddresses;
    }

    /**
     * Searches the database for an Address object with the given ID.
     *
     * @param id The ID of the Address object to search for.
     * @return The Address object with the given ID, or null if not found.
     */
    @Override
    public <T> T dbsearchById(int id)
    {
        EntityManager manager = JDBCConnecter.getEntityManagerFactory().createEntityManager();
        manager.getTransaction().begin();
        Address address = manager.find(Address.class, id);
        manager.getTransaction().commit();
        manager.close();
        return (T) address;
        // TODO: this need to be changed latter pls by Basel, should be safer
    }

    /**
     * Move all Address object from the Address Table to another table named Address_trash_collection .
     * then Remove the Address objects from the database.
     */
    @Override
    public void dbRemoveAll()
    {
        EntityManager manager = JDBCConnecter.getEntityManagerFactory().createEntityManager();
        manager.getTransaction().begin();
        manager.createNativeQuery("INSERT INTO Address_trash_collection SELECT * FROM Address").executeUpdate();
        manager.createNativeQuery("DELETE FROM Address").executeUpdate();
        manager.getTransaction().commit();
        manager.close();
    }

    /**
     * Move the Address object with the given ID from the Address Table to another table named Address_trash_collection .
     * then Remove the Address objects from the database.
     * @param id The ID of the Address object to remove.
     */
    @Override
    public void dbRemoveById(int id)
    {
        EntityManager manager = JDBCConnecter.getEntityManagerFactory().createEntityManager();
        manager.getTransaction().begin();

        manager.createNativeQuery("INSERT INTO Address_trash_collection SELECT * FROM Address WHERE Address_id = :id")
                .setParameter("id", id)
                .executeUpdate();

        manager.createNativeQuery("DELETE FROM Address WHERE Address_id = :id")
                .setParameter("id", id)
                .executeUpdate();

        manager.getTransaction().commit();
        manager.close();
    }

    /**
     * Updates the given Address object in the database.
     *
     * @param object The Address object to update in the database.
     */
    @Override
    public void dbUpdate(Object object)
    {
        if (!(object instanceof Address))
            ZuseCore.coreAssert(false, "object must be Address");

        EntityManager manager = JDBCConnecter.getEntityManagerFactory().createEntityManager();
        Address address = (Address) object;
        System.out.println((Address) dbsearchById(address.getId()));
        manager.getTransaction().begin();
        manager.merge(address);
        manager.getTransaction().commit();
        manager.close();

    }

    /**
     * Searches the database for Address objects matching the given search filter.
     * Currently not supported for Address objects.
     *
     * @param searchFilter The search filter to use.
     *
     */
    @Override
    public List<Address> dbSerschforanythinhg(Object searchFilter)
    {
        throw new NotYetImplementedException(); // we do not Support filter for address yet
    }
}
