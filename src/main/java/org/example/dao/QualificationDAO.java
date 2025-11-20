package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Qualification;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

// Simple static DAO for managing qualifications (keeps pattern consistent with other DAOs)
public class QualificationDAO {

    public static void saveQualification(Qualification qualification) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(qualification);
            tx.commit();
        }
    }

    public static Qualification getQualificationById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Qualification q = session.get(Qualification.class, id);
            tx.commit();
            return q;
        }
    }

    public static List<Qualification> getQualifications() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            List<Qualification> list = session.createQuery("FROM Qualification", Qualification.class).getResultList();
            tx.commit();
            return list;
        }
    }

    public static void updateQualification(Qualification qualification) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(qualification);
            tx.commit();
        }
    }

    public static void deleteQualification(Qualification qualification) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(qualification);
            tx.commit();
        }
    }
}