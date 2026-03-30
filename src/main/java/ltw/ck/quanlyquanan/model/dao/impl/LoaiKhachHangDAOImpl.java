package ltw.ck.quanlyquanan.model.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import ltw.ck.quanlyquanan.model.dao.LoaiKhachHangDAO;
import ltw.ck.quanlyquanan.model.entity.LoaiKH;
import ltw.ck.quanlyquanan.model.util.JpaUtil;

public class LoaiKhachHangDAOImpl implements LoaiKhachHangDAO {

    @Override
    public List<LoaiKH> findAll() {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT l FROM LoaiKH l ORDER BY l.maLoaiKh",
                    LoaiKH.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public LoaiKH findById(Long id) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.find(LoaiKH.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public LoaiKH findByTenLoaiKh(String tenLoaiKh) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            List<LoaiKH> list = em.createQuery(
                    "SELECT l FROM LoaiKH l WHERE l.tenLoaiKh = :ten",
                    LoaiKH.class
            ).setParameter("ten", tenLoaiKh).getResultList();

            return list.isEmpty() ? null : list.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public void save(LoaiKH loaikh) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(loaikh);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(LoaiKH loaikh) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(loaikh);
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
            LoaiKH loaikh = em.find(LoaiKH.class, id);
            tx.begin();
            if (loaikh != null)
                em.remove(loaikh);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}