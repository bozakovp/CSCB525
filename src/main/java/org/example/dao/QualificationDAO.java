package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Qualification;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 * Simple static DAO for managing qualifications (keeps pattern consistent with other DAOs)
 */
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

    public static List<Qualification> findByNameContaining(String namePattern) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Query<Qualification> q = session.createQuery("FROM Qualification WHERE name LIKE :p", Qualification.class);
            q.setParameter("p", "%" + namePattern + "%");
            List<Qualification> result = q.getResultList();
            tx.commit();
            return result;
        }
    }

    public static Set<Qualification> findByTransportType(Long transportTypeId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            String hql = "SELECT q FROM TransportType tt JOIN tt.requiredQualifications q WHERE tt.id = :tid";
            List<Qualification> result = session.createQuery(hql, Qualification.class).setParameter("tid", transportTypeId).getResultList();
            tx.commit();
            return new HashSet<>(result);
        }
    }

    public static Set<Qualification> findByEmployee(Long employeeId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            String hql = "SELECT q FROM TransportEmployee e JOIN e.qualifications q WHERE e.id = :eid";
            List<Qualification> result = session.createQuery(hql, Qualification.class).setParameter("eid", employeeId).getResultList();
            tx.commit();
            return new HashSet<>(result);
        }
    }

    public static List<Long> findEmployeesWithAllQualifications(Set<Long> qualificationIds) {
        if (qualificationIds == null || qualificationIds.isEmpty()) return List.of();
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            String hql = "SELECT e.id FROM TransportEmployee e JOIN e.qualifications q WHERE q.id IN (:ids) GROUP BY e.id HAVING COUNT(DISTINCT q.id) = :count";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("ids", qualificationIds);
            query.setParameter("count", (long) qualificationIds.size());
            List<Long> result = query.getResultList();
            tx.commit();
            return result;
        }
    }
}