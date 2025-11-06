package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
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

    public static List<TransportEmployee> getEmployees() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            List<TransportEmployee> employees = session
                    .createQuery("FROM TransportEmployee", TransportEmployee.class)
                    .getResultList();
            transaction.commit();
            return employees;
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

    public static List<TransportEmployee> getEmployeesByCompany(Long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<TransportEmployee> query = session.createQuery(
                "FROM TransportEmployee WHERE company.id = :companyId", 
                TransportEmployee.class
            );
            query.setParameter("companyId", companyId);
            List<TransportEmployee> employees = query.getResultList();
            transaction.commit();
            return employees;
        }
    }

    public static List<TransportEmployee> getEmployeesByQualification(Long qualificationId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<TransportEmployee> query = session.createQuery(
                "SELECT DISTINCT e FROM TransportEmployee e " +
                "JOIN e.qualifications q " +
                "WHERE q.id = :qualificationId",
                TransportEmployee.class
            );
            query.setParameter("qualificationId", qualificationId);
            List<TransportEmployee> employees = query.getResultList();
            transaction.commit();
            return employees;
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

    /**
     * Overloaded method for backward compatibility.
     * Finds all available drivers for a given time period without qualification filtering or pagination.
     */
    public static List<TransportEmployee> getAvailableDrivers(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate) {
        return getAvailableDrivers(startDate, endDate, null, null);
    }

    public static List<TransportEmployee> searchEmployeesByName(String namePattern) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<TransportEmployee> query = session.createQuery(
                "FROM TransportEmployee WHERE name LIKE :pattern",
                TransportEmployee.class
            );
            query.setParameter("pattern", "%" + namePattern + "%");
            List<TransportEmployee> employees = query.getResultList();
            transaction.commit();
            return employees;
        }
    }

    /**
     * Find available drivers for a given time period who have appropriate qualifications.
     * This method checks:
     * 1. Driver is not assigned to any transport during the specified period
     * 2. Driver has valid qualifications
     * 3. Driver belongs to an active company
     *
     * @param startDate The start date-time of the period (inclusive)
     * @param endDate The end date-time of the period (inclusive)
     * @param requiredQualificationId Optional qualification ID that the driver must have
     * @param maxResults Maximum number of results to return (for pagination)
     * @return List of available drivers
     * @throws IllegalArgumentException if startDate is after endDate or dates are null
     */
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
                WHERE e.company.active = true
                AND e NOT IN (
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
}

/*
Example usage:
// Create a new employee
TransportEmployee employee = new TransportEmployee();
employee.setName("John Doe");
employee.setCompany(company);  // company is a TransportCompany instance
EmployeeDAO.saveEmployee(employee);

// Add qualification to employee
EmployeeDAO.addQualificationToEmployee(employee.getId(), qualificationId);

// Find employees by company
List<TransportEmployee> companyEmployees = EmployeeDAO.getEmployeesByCompany(company.getId());

// Search employees by name
List<TransportEmployee> searchResults = EmployeeDAO.searchEmployeesByName("John");

// Find available drivers for a transport
List<TransportEmployee> availableDrivers = EmployeeDAO.getAvailableDrivers(
    LocalDateTime.now(),
    LocalDateTime.now().plusDays(1)
);
*/