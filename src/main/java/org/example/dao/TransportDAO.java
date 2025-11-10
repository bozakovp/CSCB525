package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Transport;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.io.*;
import java.nio.file.*;

public class TransportDAO {
    
    public static void saveTransport(Transport transport) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(transport);
            transaction.commit();
        }
    }

    public static Transport getTransportById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Transport transport = session.get(Transport.class, id);
            transaction.commit();
            return transport;
        }
    }

    public static List<Transport> getTransports() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            List<Transport> transports = session
                    .createQuery("FROM Transport", Transport.class)
                    .getResultList();
            transaction.commit();
            return transports;
        }
    }

    public static List<Transport> getTransportsByCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<Transport> query = session.createQuery(
                "FROM Transport t WHERE t.vehicle.company.id = :companyId",
                Transport.class
            );
            query.setParameter("companyId", companyId);
            List<Transport> transports = query.getResultList();
            transaction.commit();
            return transports;
        }
    }

    public static void exportTransportsToCSV(List<Transport> transports, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // Write CSV header
            writer.println("ID,Start Point,End Point,Departure Date,Price,Driver,Vehicle");

            // Write transport data
            for (Transport t : transports) {
                writer.printf("%d,%s,%s,%s,%.2f,%s,%s%n",
                    t.getId(),
                    t.getStartPoint(),
                    t.getEndPoint(),
                    t.getDepartureDate(),
                    t.getPrice(),
                    t.getDriver().getName(),
                    t.getVehicle().getType());
            }
        } catch (IOException e) {
            System.err.println("Error exporting to CSV: " + e.getMessage());
        }
    }

    public static void updateTransport(Transport transport) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(transport);
            transaction.commit();
        }
    }

    public static void deleteTransport(Transport transport) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(transport);
            transaction.commit();
        }
    }

    public static List<Transport> getTransportsByDestination(String destination) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<Transport> query = session.createQuery(
                "FROM Transport WHERE endPoint LIKE :destination",
                Transport.class
            );
            query.setParameter("destination", "%" + destination + "%");
            List<Transport> transports = query.getResultList();
            transaction.commit();
            return transports;
        }
    }

    public static List<Transport> getTransportsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<Transport> query = session.createQuery(
                "FROM Transport WHERE departureDate BETWEEN :startDate AND :endDate",
                Transport.class
            );
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            List<Transport> transports = query.getResultList();
            transaction.commit();
            return transports;
        }
    }

    public static List<Transport> getTransportsByDriver(Long driverId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<Transport> query = session.createQuery(
                "FROM Transport WHERE driver.id = :driverId",
                Transport.class
            );
            query.setParameter("driverId", driverId);
            List<Transport> transports = query.getResultList();
            transaction.commit();
            return transports;
        }
    }

    public static void exportTransportsToFile(String filePath, List<Transport> transports) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write("ID,Start Point,End Point,Departure Date,Arrival Date,Cargo Weight,Price,Driver,Vehicle\n");
            for (Transport transport : transports) {
                writer.write(String.format("%d,%s,%s,%s,%s,%.2f,%.2f,%s,%s\n",
                    transport.getId(),
                    transport.getStartPoint(),
                    transport.getEndPoint(),
                    transport.getDepartureDate(),
                    transport.getArrivalDate(),
                    transport.getCargoWeight(),
                    transport.getPrice(),
                    transport.getDriver().getName(),
                    transport.getVehicle().getType()
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BigDecimal calculateTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<BigDecimal> query = session.createQuery(
                "SELECT COALESCE(SUM(t.price), 0) FROM Transport t " +
                "WHERE t.departureDate BETWEEN :startDate AND :endDate",
                BigDecimal.class
            );
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            BigDecimal revenue = query.getSingleResult();
            transaction.commit();
            return revenue;
        }
    }

    public static List<DriverStats> getDriverStatistics() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            String hql = """
                SELECT NEW org.example.dao.DriverStats(
                    t.driver.id,
                    t.driver.name,
                    COUNT(t),
                    SUM(t.price),
                    AVG(t.cargoWeight)
                )
                FROM Transport t
                GROUP BY t.driver.id, t.driver.name
                """;
            List<DriverStats> stats = session.createQuery(hql, DriverStats.class).getResultList();
            transaction.commit();
            return stats;
        }
    }
}

class DriverStats {
    private final Long driverId;
    private final String driverName;
    private final long totalTransports;
    private final BigDecimal totalRevenue;
    private final double averageCargoWeight;

    public DriverStats(Long driverId, String driverName, long totalTransports, 
                      BigDecimal totalRevenue, double averageCargoWeight) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.totalTransports = totalTransports;
        this.totalRevenue = totalRevenue;
        this.averageCargoWeight = averageCargoWeight;
    }

    public Long getDriverId() { return driverId; }
    public String getDriverName() { return driverName; }
    public long getTotalTransports() { return totalTransports; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public double getAverageCargoWeight() { return averageCargoWeight; }
}

/*
Example Usage:
// Create a new transport
Transport transport = new Transport();
transport.setStartPoint("Sofia");
transport.setEndPoint("Varna");
transport.setDepartureDate(LocalDateTime.now());
transport.setCargoWeight(1500.0f);
transport.setPrice(new BigDecimal("2500.00"));
transport.setDriver(driver);    // TransportEmployee instance
transport.setVehicle(vehicle);  // Vehicle instance
TransportDAO.saveTransport(transport);

// Find transports by destination
List<Transport> varnaTransports = TransportDAO.getTransportsByDestination("Varna");

// Get driver statistics
List<DriverStats> driverStats = TransportDAO.getDriverStatistics();
for (DriverStats stats : driverStats) {
    System.out.printf("Driver: %s, Transports: %d, Revenue: %s%n",
        stats.getDriverName(),
        stats.getTotalTransports(),
        stats.getTotalRevenue()
    );
}

// Export transports to file
List<Transport> transports = TransportDAO.getTransports();
TransportDAO.exportTransportsToFile("transports.csv", transports);
*/