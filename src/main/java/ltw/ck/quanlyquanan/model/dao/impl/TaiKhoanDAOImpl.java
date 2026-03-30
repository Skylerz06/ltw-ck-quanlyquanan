package ltw.ck.quanlyquanan.model.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ltw.ck.quanlyquanan.model.dao.TaiKhoanDAO;
import ltw.ck.quanlyquanan.model.entity.TaiKhoan;
import ltw.ck.quanlyquanan.model.util.JpaUtil;

import java.util.List;

public class TaiKhoanDAOImpl implements TaiKhoanDAO {

    @Override
    public List<TaiKhoan> findAll() {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.createQuery("""
                    SELECT tk
                    FROM TaiKhoan tk
                    LEFT JOIN FETCH tk.nhanVien
                    ORDER BY tk.maTK
                    """, TaiKhoan.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public TaiKhoan findById(Long id) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            List<TaiKhoan> result = em.createQuery("""
                    SELECT tk
                    FROM TaiKhoan tk
                    LEFT JOIN FETCH tk.nhanVien
                    WHERE tk.maTK = :id
                    """, TaiKhoan.class)
                    .setParameter("id", id)
                    .getResultList();

            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public TaiKhoan findByTenDangNhap(String tenDangNhap) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            List<TaiKhoan> result = em.createQuery("""
                    SELECT tk
                    FROM TaiKhoan tk
                    LEFT JOIN FETCH tk.nhanVien
                    WHERE LOWER(tk.tenDangNhap) = LOWER(:tenDangNhap)
                    """, TaiKhoan.class)
                    .setParameter("tenDangNhap", tenDangNhap)
                    .getResultList();

            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public TaiKhoan checkLogin(String tenDangNhap, String matKhau) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            List<TaiKhoan> result = em.createQuery("""
                    SELECT tk
                    FROM TaiKhoan tk
                    LEFT JOIN FETCH tk.nhanVien
                    WHERE tk.tenDangNhap = :tenDangNhap
                    AND tk.matKhau = :matKhau
                    """, TaiKhoan.class)
                    .setParameter("tenDangNhap", tenDangNhap)
                    .setParameter("matKhau", matKhau)
                    .getResultList();

            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public void save(TaiKhoan entity) {
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
    public void update(TaiKhoan entity) {
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
            TaiKhoan taiKhoan = em.find(TaiKhoan.class, id);
            if (taiKhoan != null) {
                em.remove(taiKhoan);
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