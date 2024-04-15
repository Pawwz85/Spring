package org.example.dao.hibernate;


import org.example.configuration.HibernateUtil;
import org.example.dao.IVehicleRepository;
import org.example.model.User;
import org.example.model.Vehicle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collection;


public class VehicleDAO implements IVehicleRepository {
    SessionFactory sessionFactory;

    private static VehicleDAO instance = null;
    public VehicleDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean rentVehicle(String plate, String login) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            User user = session.get(User.class, login);
            Vehicle vehicle = session.get(Vehicle.class, plate);

            if (user != null && vehicle != null && user.getVehicle() == null) {
                vehicle.setUser(user);
                vehicle.setRent(true);
                user.setVehicle(vehicle);

                session.persist(user);
                session.persist(vehicle);

                transaction.commit();
                return true;
            } else {
                if (transaction != null) {
                    transaction.rollback();
                }
                return false;
            }
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }


    @Override
    public boolean addVehicle(Vehicle vehicle) {
        Session session = null;
        Transaction transaction = null;
        boolean success=false;

        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(vehicle);
            transaction.commit();
            success = true;
        } catch(RuntimeException e){
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
        } finally {

            if(session != null)
            session.close();
        }


        return success;
    }
    @Override
    public boolean removeVehicle(String plate) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        boolean success = false;
        try {
            transaction = session.beginTransaction();
            Vehicle vehicle = session.get(Vehicle.class, plate);
            if (vehicle != null && vehicle.getUser()==null) {
                session.remove(vehicle);
            } else {
                return false;
            }
            transaction.commit();
            success = true;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            success = false;
        } finally {
            session.close();
        }
        return success;
    }

    @Override
    public Vehicle getVehicle(String plate) {
        Session session = sessionFactory.openSession();
        try {
            Vehicle vehicle = session.get(Vehicle.class, plate);
            return vehicle;
        } finally {
            session.close();
        }
    }

    //Must implement old interface. Plate is no longer needed when User has Vehicle.
    public boolean returnVehicle(String plate,String login) {
        Session session = null;
        Transaction transaction = null;
        boolean success = false;
        User user = null;
        Vehicle vehicle = null;

        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            user = session.get(User.class, login);
            vehicle = user.getVehicle();

            if(vehicle != null)
            {
                vehicle.setRent(false);
                vehicle.setUser(null);
            }

            user.setVehicle(null);
            session.persist(user);
            if (vehicle != null) session.persist(vehicle);
            transaction.commit();
            success = true;
        } catch (RuntimeException e) {
            if(transaction != null) transaction.rollback();
        }
        finally {
         if(session != null)
             session.close();
        }

        return success;
    }

    @Override
    public Collection<Vehicle> getVehicles() {
        Collection<Vehicle> vehicles;
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            vehicles = session.createQuery("FROM Vehicle", Vehicle.class).getResultList();
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
        return vehicles;
    }

    static public VehicleDAO getInstance(){
        if (instance == null){
            instance =  new VehicleDAO(HibernateUtil.getSessionFactory());
        }
        return instance;
    }
}
