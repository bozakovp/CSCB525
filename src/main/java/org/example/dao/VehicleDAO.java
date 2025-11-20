package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.VehicleDto;
import org.example.entity.Vehicle;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class VehicleDAO {
    
    public static void saveVehicle(Vehicle vehicle) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(vehicle);
            transaction.commit();
        }
    }

    public static Vehicle getVehicleById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Vehicle vehicle = session.get(Vehicle.class, id);
            transaction.commit();
            return vehicle;
        }
    }



    public static void updateVehicle(Vehicle vehicle) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(vehicle);
            transaction.commit();
        }
    }

    public static void deleteVehicle(Vehicle vehicle) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(vehicle);
            transaction.commit();
        }
    }







    // DTO-based methods
    public static List<VehicleDto> getVehiclesByCompanyDto(Long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Query<VehicleDto> query = session.createQuery(
                "SELECT new org.example.dto.VehicleDto(" +
                "v.id, v.type, v.company.id, v.company.name) " +
                "FROM Vehicle v " +
                "WHERE v.company.id = :companyId",
                VehicleDto.class
            );
            query.setParameter("companyId", companyId);
            return query.getResultList();
        }
    }
}