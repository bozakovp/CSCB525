package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.TransportType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class TransportTypeDAO {
    public static void saveTransportType(TransportType type) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(type);
            tx.commit();
        }
    }

    public static void updateTransportType(TransportType type) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(type);
            tx.commit();
        }
    }

    public static void deleteTransportType(TransportType type) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(type);
            tx.commit();
        }
    }

    public static TransportType getById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            TransportType type = session.get(TransportType.class, id);
            tx.commit();
            return type;
        }
    }

    public static TransportType getByIdWithQualifications(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Query<TransportType> query = session.createQuery(
                "SELECT DISTINCT tt FROM TransportType tt " +
                "LEFT JOIN FETCH tt.requiredQualifications " +
                "WHERE tt.id = :id",
                TransportType.class
            );
            query.setParameter("id", id);
            TransportType type = query.uniqueResultOptional().orElse(null);
            tx.commit();
            return type;
        }
    }

    public static TransportType findByName(String name) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Query<TransportType> q = session.createQuery(
                "FROM TransportType WHERE name = :name",
                TransportType.class
            );
            q.setParameter("name", name);
            TransportType result = q.uniqueResultOptional().orElse(null);
            tx.commit();
            return result;
        }
    }

    public static TransportType findByNameWithQualifications(String name) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Query<TransportType> query = session.createQuery(
                "SELECT DISTINCT tt FROM TransportType tt " +
                "LEFT JOIN FETCH tt.requiredQualifications " +
                "WHERE tt.name = :name",
                TransportType.class
            );
            query.setParameter("name", name);
            TransportType result = query.uniqueResultOptional().orElse(null);
            tx.commit();
            return result;
        }
    }

    public static List<TransportType> getAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            List<TransportType> list = session
                .createQuery("FROM TransportType", TransportType.class)
                .getResultList();
            tx.commit();
            return list;
        }
    }
}
