package ltw.ck.quanlyquanan.model.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ltw.ck.quanlyquanan.model.dao.KhachHangDAO;
import ltw.ck.quanlyquanan.model.entity.KhachHang;
import ltw.ck.quanlyquanan.model.util.JpaUtil;

import java.util.List;

public class KhachHangDAOImpl implements KhachHangDAO {

    @Override
    public List<KhachHang> findAll(){
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try{
            return em.createQuery("""
                    SELECT k
                    FROM KhachHang k
                    LEFT JOIN FETCH k.loaiKhachHang
                    ORDER BY k.maKh
                    """, KhachHang.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public KhachHang findById(Long id) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            List<KhachHang> result = em.createQuery("""
                SELECT kh
                FROM KhachHang kh
                LEFT JOIN FETCH kh.loaiKhachHang
                WHERE kh.maKh = :id
                """, KhachHang.class)
                    .setParameter("id", id)
                    .getResultList();

            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public List<KhachHang> findByTenKh(String tenKh) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.createQuery("""
                SELECT kh
                FROM KhachHang kh
                LEFT JOIN FETCH kh.loaiKhachHang
                WHERE LOWER(kh.tenKh) LIKE LOWER(:tenKh)
                ORDER BY kh.tenKh
                """, KhachHang.class)
                    .setParameter("tenKh", "%" + tenKh + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void save(KhachHang khachHang) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            em.persist(khachHang);
            tx.commit();
        } catch (Exception e){
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(KhachHang khachHang) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            em.merge(khachHang);
            tx.commit();
        } catch (Exception e){
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
        try{
            tx.begin();
            KhachHang khachHang = em.find(KhachHang.class, id);
            if (khachHang != null) {
                em.remove(khachHang);
            }
            tx.commit();
        } catch (Exception e){
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}

