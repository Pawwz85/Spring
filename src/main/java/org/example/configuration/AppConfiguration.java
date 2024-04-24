package org.example.configuration;

import org.example.dao.IUserRepository;
import org.example.dao.IVehicleRepository;
import org.example.dao.hibernate.UserDAO;
import org.example.dao.hibernate.VehicleDAO;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
@Configuration
@ComponentScan("org.example")
//@EnableScheduling
public class AppConfiguration {
    @Bean
    public SessionFactory sessionFactory() {
        return HibernateUtil.getSessionFactory();
    }
    @Bean
    public IUserRepository userRepository(){
        return UserDAO.getInstance(sessionFactory());
    }

    @Bean
    public IVehicleRepository vehicleRepository(){
        return VehicleDAO.getInstance(sessionFactory());
    }
}
