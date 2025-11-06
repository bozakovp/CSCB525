package org.example.configuration;

import org.example.entity.TransportCompanies;
import org.example.entity.Employee;
import org.example.entity.Vehicle;
import org.example.entity.TransportType;
import org.example.entity.Transport;
import org.example.entity.Qualification;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class SessionFactoryUtil {
    private static SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(TransportCompanies.class);
            configuration.addAnnotatedClass(Employee.class);
            configuration.addAnnotatedClass(Vehicle.class);
            configuration.addAnnotatedClass(TransportType.class);
            configuration.addAnnotatedClass(Transport.class);
            configuration.addAnnotatedClass(Qualification.class);    
            ServiceRegistry serviceRegistry
                    = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }
}