package ltw.ck.quanlyquanan.model.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.List;
import ltw.ck.quanlyquanan.model.dao.HoaDonDAO;
import ltw.ck.quanlyquanan.model.entity.HoaDon;
import ltw.ck.quanlyquanan.model.util.JpaUtil;

public class HoaDonDAOImpl implements HoaDonDAO{

    @Override
    public List<HoaDon> findAll() {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.createQuery("""
                    
                            SELECT h
                    FROM HoaDon h
                    ORDER BY h.maHd
                    """, HoaDon.class)
                    .getResultList();
            } finally {
                em.close();
            }
    }

    @Override
    public HoaDon findById(Long id) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.find(HoaDon.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<HoaDon> findByKhoangNgay(LocalDateTime from, LocalDateTime to){
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.createQuery("""
                    SELECT h
                    FROM HoaDon h
                    WHERE h.ngayLap BETWEEN :from AND :to
                    """, HoaDon.class)
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<HoaDon> findByNhanVien(Long idNhanVien){
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.createQuery("""
                    SELECT h
                    FROM HoaDon h
                    WHERE h.nhanVien.maNV = :idNhanVien
                    """, HoaDon.class)
                    .setParameter("idNhanVien", idNhanVien)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<HoaDon> findByBan(Long idBan){
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.createQuery("""
                    SELECT h
                    FROM HoaDon h
                    WHERE h.ban.maBan = :idBan
                    """, HoaDon.class)
                    .setParameter("idBan", idBan)
                    .getResultList();
        } finally {
            em.close();
        }
    }


    @Override public void save(HoaDon hoadon) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            em.persist(hoadon);
            tx.commit();
        } catch (Exception e){
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override public void update(HoaDon hoadon) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            em.merge(hoadon);
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
            HoaDon hoadon = em.find(HoaDon.class, id);
            if (hoadon != null)
                em.remove(hoadon);
            tx.commit();
        } catch (Exception e){
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
