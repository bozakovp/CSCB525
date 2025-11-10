package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.TransportCompany;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TransportCompanyDAO {
    
    public static void saveCompany(TransportCompany company) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(company);
            transaction.commit();
        }
    }

    public static TransportCompany getCompanyById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            TransportCompany company = session.get(TransportCompany.class, id);
            transaction.commit();
            return company;
        }
    }

    public static List<TransportCompany> getCompanies() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            List<TransportCompany> companies = session
                    .createQuery("FROM TransportCompany", TransportCompany.class)
                    .getResultList();
            transaction.commit();
            return companies;
        }
    }

    public static void updateCompany(TransportCompany company) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(company);
            transaction.commit();
        }
    }

    public static void deleteCompany(TransportCompany company) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(company);
            transaction.commit();
        }
    }

    public static List<TransportCompany> getCompaniesSortedByName(boolean ascending) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            String order = ascending ? "ASC" : "DESC";
            List<TransportCompany> companies = session
                    .createQuery("FROM TransportCompany ORDER BY name " + order, TransportCompany.class)
                    .getResultList();
            transaction.commit();
            return companies;
        }
    }

    public static List<TransportCompany> findByNamePattern(String pattern) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<TransportCompany> query = session.createQuery(
                "FROM TransportCompany WHERE name LIKE :pattern", 
                TransportCompany.class
            );
            query.setParameter("pattern", "%" + pattern + "%");
            List<TransportCompany> companies = query.getResultList();
            transaction.commit();
            return companies;
        }
    }

    public static BigDecimal calculateCompanyRevenue(Long companyId, LocalDate startDate, LocalDate endDate) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            String hql = """
                SELECT COALESCE(SUM(t.price), 0)
                FROM Transport t
                JOIN t.vehicle v
                WHERE v.company.id = :companyId
                AND t.departureDate >= :startDate
                AND t.departureDate <= :endDate
                """;
            Query<BigDecimal> query = session.createQuery(hql, BigDecimal.class);
            query.setParameter("companyId", companyId);
            query.setParameter("startDate", startDate.atStartOfDay());
            query.setParameter("endDate", endDate.plusDays(1).atStartOfDay());
            BigDecimal revenue = query.getSingleResult();
            transaction.commit();
            return revenue;
        }
    }
}

/*
Usage Examples:
// Create a company
TransportCompany company = new TransportCompany();
company.setName("Fast Logistics");
TransportCompanyDAO.saveCompany(company);

// Find companies by name
List<TransportCompany> companies = TransportCompanyDAO.findByNamePattern("Fast");

// Calculate revenue
BigDecimal revenue = TransportCompanyDAO.calculateCompanyRevenue(
    company.getId(), 
    LocalDate.now().minusMonths(1), 
    LocalDate.now()
);
*/