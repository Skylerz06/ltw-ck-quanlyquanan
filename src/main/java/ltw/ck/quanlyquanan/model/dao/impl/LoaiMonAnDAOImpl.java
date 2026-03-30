package ltw.ck.quanlyquanan.model.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ltw.ck.quanlyquanan.model.dao.LoaiMonAnDAO;
import ltw.ck.quanlyquanan.model.entity.LoaiMonAn;
import ltw.ck.quanlyquanan.model.util.JpaUtil;

import java.util.List;

public class LoaiMonAnDAOImpl implements LoaiMonAnDAO {

    @Override
    public List<LoaiMonAn> findAll() {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.createQuery("""
                    SELECT l
                    FROM LoaiMonAn l
                    ORDER BY l.tenLoai
                    """, LoaiMonAn.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public LoaiMonAn findById(Long id) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.find(LoaiMonAn.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public LoaiMonAn findByTenLoaiMon(String tenLoaiMon) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            List<LoaiMonAn> result = em.createQuery("""
                    SELECT l
                    FROM LoaiMonAn l
                    WHERE LOWER(l.tenLoai) = LOWER(:tenLoaiMon)
                    """, LoaiMonAn.class)
                    .setParameter("tenLoaiMon", tenLoaiMon)
                    .getResultList();

            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public void save(LoaiMonAn entity) {
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
    public void update(LoaiMonAn entity) {
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
            LoaiMonAn loaiMonAn = em.find(LoaiMonAn.class, id);
            if (loaiMonAn != null) {
                em.remove(loaiMonAn);
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