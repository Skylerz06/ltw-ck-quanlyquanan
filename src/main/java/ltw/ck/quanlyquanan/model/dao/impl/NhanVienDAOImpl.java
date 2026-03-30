package ltw.ck.quanlyquanan.model.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ltw.ck.quanlyquanan.model.dao.NhanVienDAO;
import ltw.ck.quanlyquanan.model.entity.NhanVien;
import ltw.ck.quanlyquanan.model.util.JpaUtil;

import java.util.List;

public class NhanVienDAOImpl implements NhanVienDAO {

    @Override
    public List<NhanVien> findAll() {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.createQuery("""
                    SELECT nv
                    FROM NhanVien nv
                    LEFT JOIN FETCH nv.taiKhoan
                    ORDER BY nv.maNV
                    """, NhanVien.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public NhanVien findById(Long id) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            List<NhanVien> result = em.createQuery("""
                    SELECT nv
                    FROM NhanVien nv
                    LEFT JOIN FETCH nv.taiKhoan
                    WHERE nv.maNV = :id
                    """, NhanVien.class)
                    .setParameter("id", id)
                    .getResultList();

            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public List<NhanVien> findByHoTen(String hoTen) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.createQuery("""
                    SELECT nv
                    FROM NhanVien nv
                    LEFT JOIN FETCH nv.taiKhoan
                    WHERE LOWER(nv.hoTen) LIKE LOWER(:hoTen)
                    ORDER BY nv.hoTen
                    """, NhanVien.class)
                    .setParameter("hoTen", "%" + hoTen + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public NhanVien findBySdt(String sdt) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            List<NhanVien> result = em.createQuery("""
                    SELECT nv
                    FROM NhanVien nv
                    LEFT JOIN FETCH nv.taiKhoan
                    WHERE nv.sdt = :sdt
                    """, NhanVien.class)
                    .setParameter("sdt", sdt)
                    .getResultList();

            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public void save(NhanVien entity) {
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
    public void update(NhanVien entity) {
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
            NhanVien nhanVien = em.find(NhanVien.class, id);
            if (nhanVien != null) {
                em.remove(nhanVien);
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