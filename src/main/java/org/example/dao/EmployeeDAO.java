package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.TransportEmployeeDto;
import org.example.dto.TransportEmployeeWithQualificationsDto;
import org.example.entity.TransportEmployee;
import org.example.entity.Qualification;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class EmployeeDAO {
    
    public static void saveEmployee(TransportEmployee employee) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(employee);
            transaction.commit();
        }
    }

    public static TransportEmployee getEmployeeById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            TransportEmployee employee = session.get(TransportEmployee.class, id);
            transaction.commit();
            return employee;
        }
    }

    public static TransportEmployee getEmployeeByIdWithQualifications(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<TransportEmployee> query = session.createQuery(
                "SELECT DISTINCT e FROM TransportEmployee e " +
                "LEFT JOIN FETCH e.qualifications " +
                "WHERE e.id = :id",
                TransportEmployee.class
            );
            query.setParameter("id", id);
            TransportEmployee employee = query.uniqueResultOptional().orElse(null);
            transaction.commit();
            return employee;
        }
    }



    public static void updateEmployee(TransportEmployee employee) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(employee);
            transaction.commit();
        }
    }

    public static void deleteEmployee(TransportEmployee employee) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(employee);
            transaction.commit();
        }
    }





    public static void addQualificationToEmployee(Long employeeId, Long qualificationId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            
            TransportEmployee employee = session.get(TransportEmployee.class, employeeId);
            Qualification qualification = session.get(Qualification.class, qualificationId);
            
            if (employee != null && qualification != null) {
                employee.getQualifications().add(qualification);
                session.update(employee);
            }
            
            transaction.commit();
        }
    }

    public static void removeQualificationFromEmployee(Long employeeId, Long qualificationId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            
            TransportEmployee employee = session.get(TransportEmployee.class, employeeId);
            Qualification qualification = session.get(Qualification.class, qualificationId);
            
            if (employee != null && qualification != null) {
                employee.getQualifications().remove(qualification);
                session.update(employee);
            }
            
            transaction.commit();
        }
    }

    // Overloaded method for backward compatibility - finds all available drivers for a given time period without qualification filtering or pagination
    public static List<TransportEmployee> getAvailableDrivers(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate) {
        return getAvailableDrivers(startDate, endDate, null, null);
    }

    // Find available drivers for a given time period who have appropriate qualifications - checks if driver is not assigned to any transport during the period and has valid qualifications
    public static List<TransportEmployee> getAvailableDrivers(
            java.time.LocalDateTime startDate,
            java.time.LocalDateTime endDate,
            Long requiredQualificationId,
            Integer maxResults) {
        
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date must not be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must not be after end date");
        }

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            
            // Improved query to handle overlapping transports and check qualifications
            String hql = """
                SELECT DISTINCT e FROM TransportEmployee e
                LEFT JOIN e.qualifications q
                WHERE e NOT IN (
                    SELECT DISTINCT t.driver
                    FROM Transport t
                    WHERE (
                        (t.departureDate <= :endDate AND t.arrivalDate >= :startDate)
                        OR (t.departureDate BETWEEN :startDate AND :endDate)
                        OR (t.arrivalDate BETWEEN :startDate AND :endDate)
                    )
                )
                """;

            // Add qualification filter if required
            if (requiredQualificationId != null) {
                hql += " AND :qualificationId IN (SELECT q.id FROM e.qualifications q)";
            }
            
            hql += " ORDER BY e.name"; // Add ordering for consistency

            Query<TransportEmployee> query = session.createQuery(hql, TransportEmployee.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            
            if (requiredQualificationId != null) {
                query.setParameter("qualificationId", requiredQualificationId);
            }

            // Set max results if specified
            if (maxResults != null && maxResults > 0) {
                query.setMaxResults(maxResults);
            }

            List<TransportEmployee> availableDrivers = query.getResultList();
            transaction.commit();
            return availableDrivers;
        }
    }

    // DTO-based methods
    public static List<TransportEmployeeDto> getEmployeesByCompanyDto(Long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Query<TransportEmployeeDto> query = session.createQuery(
                "SELECT new org.example.dto.TransportEmployeeDto(" +
                "e.id, e.name, e.company.id, e.company.name) " +
                "FROM TransportEmployee e " +
                "WHERE e.company.id = :companyId",
                TransportEmployeeDto.class
            );
            query.setParameter("companyId", companyId);
            return query.getResultList();
        }
    }
}