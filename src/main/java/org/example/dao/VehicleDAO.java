package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Vehicle;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
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

    public static List<Vehicle> getVehicles() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            List<Vehicle> vehicles = session
                    .createQuery("FROM Vehicle", Vehicle.class)
                    .getResultList();
            transaction.commit();
            return vehicles;
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

    public static List<Vehicle> getVehiclesByCompany(Long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<Vehicle> query = session.createQuery(
                "FROM Vehicle WHERE company.id = :companyId", 
                Vehicle.class
            );
            query.setParameter("companyId", companyId);
            List<Vehicle> vehicles = query.getResultList();
            transaction.commit();
            return vehicles;
        }
    }

    public static List<Vehicle> getVehiclesByType(String type) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<Vehicle> query = session.createQuery(
                "FROM Vehicle WHERE type = :type", 
                Vehicle.class
            );
            query.setParameter("type", type);
            List<Vehicle> vehicles = query.getResultList();
            transaction.commit();
            return vehicles;
        }
    }

    public static List<Vehicle> getAvailableVehicles(LocalDateTime startDate, LocalDateTime endDate) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            // Find vehicles that don't have any transports scheduled in the given time period
            String hql = """
                FROM Vehicle v
                WHERE v NOT IN (
                    SELECT DISTINCT t.vehicle
                    FROM Transport t
                    WHERE (t.departureDate BETWEEN :startDate AND :endDate)
                    OR (t.arrivalDate BETWEEN :startDate AND :endDate)
                )
                """;
            Query<Vehicle> query = session.createQuery(hql, Vehicle.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            List<Vehicle> vehicles = query.getResultList();
            transaction.commit();
            return vehicles;
        }
    }

    public static VehicleStats getVehicleStatistics(Long vehicleId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            
            // Get total number of transports
            Query<Long> countQuery = session.createQuery(
                "SELECT COUNT(t) FROM Transport t WHERE t.vehicle.id = :vehicleId",
                Long.class
            );
            countQuery.setParameter("vehicleId", vehicleId);
            int totalTransports = countQuery.getSingleResult().intValue();

            // Get average cargo weight
            Query<Double> weightQuery = session.createQuery(
                "SELECT AVG(t.cargoWeight) FROM Transport t WHERE t.vehicle.id = :vehicleId",
                Double.class
            );
            weightQuery.setParameter("vehicleId", vehicleId);
            Double avgWeight = weightQuery.getSingleResult();

            transaction.commit();
            return new VehicleStats(vehicleId, totalTransports, avgWeight != null ? avgWeight : 0.0);
        }
    }
}

class VehicleStats {
    private final Long vehicleId;
    private final int totalTransports;
    private final double averageCargoWeight;

    public VehicleStats(Long vehicleId, int totalTransports, double averageCargoWeight) {
        this.vehicleId = vehicleId;
        this.totalTransports = totalTransports;
        this.averageCargoWeight = averageCargoWeight;
    }

    public Long getVehicleId() { return vehicleId; }
    public int getTotalTransports() { return totalTransports; }
    public double getAverageCargoWeight() { return averageCargoWeight; }
}

/*
Example usage:
// Create a new vehicle
Vehicle vehicle = new Vehicle();
vehicle.setType("Truck");
vehicle.setCompany(company);  // company is a TransportCompany instance
VehicleDAO.saveVehicle(vehicle);

// Get company's vehicles
List<Vehicle> companyVehicles = VehicleDAO.getVehiclesByCompany(company.getId());

// Find available vehicles for a transport
List<Vehicle> availableVehicles = VehicleDAO.getAvailableVehicles(
    LocalDateTime.now(),
    LocalDateTime.now().plusDays(2)
);

// Get vehicle statistics
VehicleStats stats = VehicleDAO.getVehicleStatistics(vehicle.getId());
System.out.println("Total transports: " + stats.getTotalTransports());
*/