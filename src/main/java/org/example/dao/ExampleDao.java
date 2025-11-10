package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.TransportCompany;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ExampleDao {
    public static void saveCompany(TransportCompany company) {
       try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
           Transaction transaction = session.beginTransaction();
           session.save(company);
           transaction.commit();
       }
    }

    public static void createCompany(TransportCompany company) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(company);
            transaction.commit();
        }
    }
    public static TransportCompany getCompanyById(long id) {
        TransportCompany company;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            company = session.get(TransportCompany.class, id);
            transaction.commit();
        }
        return company;
    }

    public static List<TransportCompany> getCompanies() {
        List<TransportCompany> companies;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
        companies = session
            .createQuery("FROM TransportCompany", TransportCompany.class)
                    .getResultList();
            transaction.commit();
        }
        return companies;
    }

    public static void updateCompany(TransportCompany company) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(company);
            transaction.commit();
        }
    }

    public static void deleteCompany(TransportCompany company) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(company);
            transaction.commit();
        }
    }
}