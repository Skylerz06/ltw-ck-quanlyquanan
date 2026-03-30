package ltw.ck.quanlyquanan.model.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ltw.ck.quanlyquanan.model.dao.MonAnDAO;
import ltw.ck.quanlyquanan.model.entity.LoaiMonAn;
import ltw.ck.quanlyquanan.model.entity.MonAn;
import ltw.ck.quanlyquanan.model.util.JpaUtil;

import java.util.List;

public class MonAnDAOImpl implements MonAnDAO {

    @Override
    public List<MonAn> findAll() {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.createQuery("""
                    SELECT m
                    FROM MonAn m
                    LEFT JOIN FETCH m.loaiMonAn
                    ORDER BY m.maMon
                    """, MonAn.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public MonAn findById(Long id) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            List<MonAn> result = em.createQuery("""
                    SELECT m
                    FROM MonAn m
                    LEFT JOIN FETCH m.loaiMonAn
                    WHERE m.maMon = :id
                    """, MonAn.class)
                    .setParameter("id", id)
                    .getResultList();

            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public List<MonAn> findByTenMon(String tenMon) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.createQuery("""
                    SELECT m
                    FROM MonAn m
                    LEFT JOIN FETCH m.loaiMonAn
                    WHERE LOWER(m.tenMon) LIKE LOWER(:tenMon)
                    ORDER BY m.tenMon
                    """, MonAn.class)
                    .setParameter("tenMon", "%" + tenMon + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<MonAn> findByLoaiMonAn(LoaiMonAn loaiMonAn) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.createQuery("""
                    SELECT m
                    FROM MonAn m
                    LEFT JOIN FETCH m.loaiMonAn
                    WHERE m.loaiMonAn = :loaiMonAn
                    ORDER BY m.tenMon
                    """, MonAn.class)
                    .setParameter("loaiMonAn", loaiMonAn)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void save(MonAn entity) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(MonAn entity) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            MonAn monAn = em.find(MonAn.class, id);
            if (monAn != null) {
                em.remove(monAn);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}