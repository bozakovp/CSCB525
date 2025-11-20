package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.TransportDto;
import org.example.dto.DriverTransportCountDto;
import org.example.dto.CompanyRevenueDto;
import org.example.dto.DriverRevenueDto;
import org.example.entity.Transport;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

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





    // DTO-based methods
    public static List<TransportDto> getTransportsByDestinationDto(String destination) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Query<TransportDto> query = session.createQuery(
                "SELECT new org.example.dto.TransportDto(" +
                "t.id, t.vehicle.id, t.vehicle.type, t.driver.id, t.driver.name, " +
                "t.transportType.id, t.transportType.name, t.startPoint, t.endPoint, " +
                "t.departureDate, t.arrivalDate, t.cargoWeight, t.price) " +
                "FROM Transport t " +
                "WHERE t.endPoint LIKE :destination",
                TransportDto.class
            );
            query.setParameter("destination", "%" + destination + "%");
            return query.getResultList();
        }
    }

    public static List<TransportDto> getTransportsByCompanyDto(Long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Query<TransportDto> query = session.createQuery(
                "SELECT new org.example.dto.TransportDto(" +
                "t.id, t.vehicle.id, t.vehicle.type, t.driver.id, t.driver.name, " +
                "t.transportType.id, t.transportType.name, t.startPoint, t.endPoint, " +
                "t.departureDate, t.arrivalDate, t.cargoWeight, t.price) " +
                "FROM Transport t " +
                "WHERE t.vehicle.company.id = :companyId",
                TransportDto.class
            );
            query.setParameter("companyId", companyId);
            return query.getResultList();
        }
    }

    public static List<TransportDto> getTransportsDtoOrderedByDestination() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT new org.example.dto.TransportDto(" +
                "t.id, t.vehicle.id, t.vehicle.type, t.driver.id, t.driver.name, " +
                "t.transportType.id, t.transportType.name, t.startPoint, t.endPoint, " +
                "t.departureDate, t.arrivalDate, t.cargoWeight, t.price) " +
                "FROM Transport t " +
                "ORDER BY t.endPoint ASC",
                TransportDto.class
            ).getResultList();
        }
    }

    // Export DTOs to CSV file
    public static void exportTransportsDtoToCSV(List<TransportDto> transports, String fileName) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))) {
            // Write CSV header
            writer.write("ID,Vehicle Type,Driver,Transport Type,Start Point,End Point,Departure Date,Arrival Date,Cargo Weight,Price\n");
            
            // Write transport data
            for (TransportDto t : transports) {
                writer.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%.2f,%.2f\n",
                    t.getId(),
                    t.getVehicleType(),
                    t.getDriverName(),
                    t.getTransportTypeName(),
                    t.getStartPoint(),
                    t.getEndPoint(),
                    t.getDepartureDate(),
                    t.getArrivalDate(),
                    t.getCargoWeight(),
                    t.getPrice()
                ));
            }
        }
    }

    // Report methods
    public static Long getTotalTransportsCount() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT COUNT(t) FROM Transport t", Long.class)
                    .getSingleResult();
        }
    }

    public static BigDecimal getTotalRevenue() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT COALESCE(SUM(t.price), 0) FROM Transport t", BigDecimal.class)
                    .getSingleResult();
        }
    }

    public static List<DriverTransportCountDto> getTransportsPerDriver() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT new org.example.dto.DriverTransportCountDto(" +
                    "t.driver.id, t.driver.name, COUNT(t)) " +
                    "FROM Transport t " +
                    "GROUP BY t.driver.id, t.driver.name " +
                    "ORDER BY COUNT(t) DESC",
                    DriverTransportCountDto.class
            ).getResultList();
        }
    }

    public static List<CompanyRevenueDto> getCompanyRevenueForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT new org.example.dto.CompanyRevenueDto(" +
                    "t.vehicle.company.id, t.vehicle.company.name, " +
                    "COUNT(t), COALESCE(SUM(t.price), 0)) " +
                    "FROM Transport t " +
                    "WHERE t.departureDate BETWEEN :startDate AND :endDate " +
                    "GROUP BY t.vehicle.company.id, t.vehicle.company.name " +
                    "ORDER BY SUM(t.price) DESC",
                    CompanyRevenueDto.class
            )
            .setParameter("startDate", startDate)
            .setParameter("endDate", endDate)
            .getResultList();
        }
    }

    public static List<DriverRevenueDto> getRevenuePerDriver() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT new org.example.dto.DriverRevenueDto(" +
                    "t.driver.id, t.driver.name, t.driver.company.name, " +
                    "COUNT(t), COALESCE(SUM(t.price), 0)) " +
                    "FROM Transport t " +
                    "GROUP BY t.driver.id, t.driver.name, t.driver.company.name " +
                    "ORDER BY SUM(t.price) DESC",
                    DriverRevenueDto.class
            ).getResultList();
        }
    }

}