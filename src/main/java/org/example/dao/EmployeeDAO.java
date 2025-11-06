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

    public static List<TransportEmployee> getAvailableDrivers(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            // Find drivers that don't have any transports scheduled in the given time period
            String hql = """
                FROM TransportEmployee e
                WHERE e NOT IN (
                    SELECT DISTINCT t.driver
                    FROM Transport t
                    WHERE (t.departureDate BETWEEN :startDate AND :endDate)
                    OR (t.arrivalDate BETWEEN :startDate AND :endDate)
                )
                """;
            Query<TransportEmployee> query = session.createQuery(hql, TransportEmployee.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
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