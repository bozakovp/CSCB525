package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.TransportCompanies;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ExampleDao {
    public static void saveCompany(TransportCompanies company) {
       try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
           Transaction transaction = session.beginTransaction();
           session.save(company);
           transaction.commit();
       }
    }

    public static void createCompany(TransportCompanies company) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(company);
            transaction.commit();
        }
    }
    public static TransportCompanies getCompanyById(long id) {
        TransportCompanies company;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            company = session.get(TransportCompanies.class, id);
            transaction.commit();
        }
        return company;
    }

    public static List<TransportCompanies> getCompanies() {
        List<TransportCompanies> companies;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            companies = session
                    .createQuery("Select c From Company c", TransportCompanies.class)
                    .getResultList();
            transaction.commit();
        }
        return companies;
    }

    public static void updateCompany(TransportCompanies company) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(company);
            transaction.commit();
        }
    }

    public static void deleteCompany(TransportCompanies company) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(company);
            transaction.commit();
        }
    }
}