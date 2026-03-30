package ltw.ck.quanlyquanan.model.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

import ltw.ck.quanlyquanan.model.dao.ChiTietHoaDonDAO;
import ltw.ck.quanlyquanan.model.entity.ChiTietHD;
import ltw.ck.quanlyquanan.model.util.JpaUtil;

public class ChiTietHoaDonDAOImpl implements ChiTietHoaDonDAO {

    @Override
    public List<ChiTietHD> findByHoaDon(Long maHd) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.createQuery("""
                    SELECT c
                    FROM ChiTietHD c
                    WHERE c.hoaDon.maHd = :maHd
                    """, ChiTietHD.class)
                    .setParameter("maHd", maHd)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public ChiTietHD findById(Long id) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.find(ChiTietHD.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public ChiTietHD findByHoaDonAndMonAn(Long maHD, Long maMon) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            List<ChiTietHD> list = em.createQuery("""
                    SELECT c
                    FROM ChiTietHD c
                    WHERE c.hoaDon.maHd = :maHD AND c.monAn.maMon = :maMon
                    """, ChiTietHD.class)
                    .setParameter("maHD", maHD)
                    .setParameter("maMon", maMon)
                    .getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            em.close();
        }
    }





    @Override
    public void save(ChiTietHD chitiethd){
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            em.persist(chitiethd);
            tx.commit();
        } catch (Exception e){
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override public void update(ChiTietHD chitiethd) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            em.merge(chitiethd);
            tx.commit();
        } catch (Exception e){
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override public void delete(Long id) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            ChiTietHD chitiethd = em.find(ChiTietHD.class, id);
            if (chitiethd != null)
                em.remove(chitiethd);
            tx.commit();
        } catch (Exception e){
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
