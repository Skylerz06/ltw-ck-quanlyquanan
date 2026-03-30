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
                    """, KhachHang.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public KhachHang findById(Long id){
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try{
            return em.find(KhachHang.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<KhachHang> findByTenKh(String tenKh){
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.createQuery("""
                    SELECT k
                    FROM KhachHang k
                    WHERE k.tenKh LIKE :tenKh
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
        } finally {
            em.close();
        }
    }
}
