package org.example.configuration;

import org.example.model.Car;
import org.example.model.Motorcycle;
import org.example.model.User;
import org.example.model.Vehicle;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration cfg =new Configuration();
            cfg.configure("example/resources/hibernate.cfg.xml")
                    .addAnnotatedClass(Vehicle.class)
                    .addAnnotatedClass(Car.class)
                    .addAnnotatedClass(Motorcycle.class)
                    .addAnnotatedClass(User.class);
            sessionFactory = cfg.buildSessionFactory();
        }
        return sessionFactory;
    }
}
