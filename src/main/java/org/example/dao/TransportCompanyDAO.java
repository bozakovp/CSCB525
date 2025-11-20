package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.TransportCompanyDto;
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





    // DTO-based methods
    public static List<TransportCompanyDto> getCompaniesDto() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT new org.example.dto.TransportCompanyDto(c.id, c.name) " +
                "FROM TransportCompany c",
                TransportCompanyDto.class
            ).getResultList();
        }
    }
}