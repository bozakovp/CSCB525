package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.QualificationDto;
import org.example.entity.Qualification;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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

    // DTO-based methods
    public static List<QualificationDto> getQualificationsDto() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT new org.example.dto.QualificationDto(q.id, q.name, q.description) " +
                "FROM Qualification q",
                QualificationDto.class
            ).getResultList();
        }
    }

    public static QualificationDto getQualificationByIdDto(Long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Query<QualificationDto> query = session.createQuery(
                "SELECT new org.example.dto.QualificationDto(q.id, q.name, q.description) " +
                "FROM Qualification q " +
                "WHERE q.id = :id",
                QualificationDto.class
            );
            query.setParameter("id", id);
            return query.uniqueResultOptional().orElse(null);
        }
    }
}