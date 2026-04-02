package ltw.ck.quanlyquanan.model.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ltw.ck.quanlyquanan.model.dao.HoaDonDAO;
import ltw.ck.quanlyquanan.model.entity.ChiTietHD;
import ltw.ck.quanlyquanan.model.entity.HoaDon;
import ltw.ck.quanlyquanan.model.util.JpaUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAOImpl implements HoaDonDAO {

    @Override
    public List<HoaDon> findAll() {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.createQuery("""
                    SELECT DISTINCT h
                    FROM HoaDon h
                    LEFT JOIN FETCH h.khachHang
                    LEFT JOIN FETCH h.nhanVien
                    LEFT JOIN FETCH h.ban
                    LEFT JOIN FETCH h.lstChiTietHoaDon c
                    LEFT JOIN FETCH c.monAn
                    ORDER BY h.maHd
                    """, HoaDon.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public HoaDon findById(Long id) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            List<HoaDon> result = em.createQuery("""
                    SELECT DISTINCT h
                    FROM HoaDon h
                    LEFT JOIN FETCH h.khachHang
                    LEFT JOIN FETCH h.nhanVien
                    LEFT JOIN FETCH h.ban
                    LEFT JOIN FETCH h.lstChiTietHoaDon c
                    LEFT JOIN FETCH c.monAn
                    WHERE h.maHd = :id
                    """, HoaDon.class)
                    .setParameter("id", id)
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public List<HoaDon> findByKhoangNgay(LocalDateTime from, LocalDateTime to) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.createQuery("""
                    SELECT DISTINCT h
                    FROM HoaDon h
                    LEFT JOIN FETCH h.khachHang
                    LEFT JOIN FETCH h.nhanVien
                    LEFT JOIN FETCH h.ban
                    LEFT JOIN FETCH h.lstChiTietHoaDon c
                    LEFT JOIN FETCH c.monAn
                    WHERE h.ngayLap BETWEEN :from AND :to
                    ORDER BY h.ngayLap DESC
                    """, HoaDon.class)
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<HoaDon> findByNhanVien(Long idNhanVien) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.createQuery("""
                    SELECT DISTINCT h
                    FROM HoaDon h
                    LEFT JOIN FETCH h.khachHang
                    LEFT JOIN FETCH h.nhanVien
                    LEFT JOIN FETCH h.ban
                    LEFT JOIN FETCH h.lstChiTietHoaDon c
                    LEFT JOIN FETCH c.monAn
                    WHERE h.nhanVien.maNV = :idNhanVien
                    ORDER BY h.maHd DESC
                    """, HoaDon.class)
                    .setParameter("idNhanVien", idNhanVien)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<HoaDon> findByBan(Long idBan) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            return em.createQuery("""
                    SELECT DISTINCT h
                    FROM HoaDon h
                    LEFT JOIN FETCH h.khachHang
                    LEFT JOIN FETCH h.nhanVien
                    LEFT JOIN FETCH h.ban
                    LEFT JOIN FETCH h.lstChiTietHoaDon c
                    LEFT JOIN FETCH c.monAn
                    WHERE h.ban.maBan = :idBan
                    ORDER BY h.maHd DESC
                    """, HoaDon.class)
                    .setParameter("idBan", idBan)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void save(HoaDon hoaDon) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(hoaDon);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(HoaDon hoaDon) {
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            HoaDon managedHoaDon = em.createQuery("""
                    SELECT DISTINCT h
                    FROM HoaDon h
                    LEFT JOIN FETCH h.lstChiTietHoaDon c
                    LEFT JOIN FETCH c.monAn
                    WHERE h.maHd = :id
                    """, HoaDon.class)
                    .setParameter("id", hoaDon.getMaHd())
                    .getSingleResult();

            managedHoaDon.setTrangThai(hoaDon.getTrangThai());
            managedHoaDon.setKhachHang(hoaDon.getKhachHang() == null ? null : em.getReference(hoaDon.getKhachHang().getClass(), hoaDon.getKhachHang().getMaKh()));
            managedHoaDon.setNhanVien(em.getReference(hoaDon.getNhanVien().getClass(), hoaDon.getNhanVien().getMaNV()));
            managedHoaDon.setBan(em.getReference(hoaDon.getBan().getClass(), hoaDon.getBan().getMaBan()));

            managedHoaDon.getLstChiTietHoaDon().clear();
            em.flush();

            List<ChiTietHD> chiTietMoi = new ArrayList<>();
            for (ChiTietHD chiTietHD : hoaDon.getLstChiTietHoaDon()) {
                ChiTietHD managedChiTiet = new ChiTietHD();
                managedChiTiet.setHoaDon(managedHoaDon);
                managedChiTiet.setMonAn(em.getReference(chiTietHD.getMonAn().getClass(), chiTietHD.getMonAn().getMaMon()));
                managedChiTiet.setSoLuong(chiTietHD.getSoLuong());
                chiTietMoi.add(managedChiTiet);
            }
            managedHoaDon.getLstChiTietHoaDon().addAll(chiTietMoi);

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
            HoaDon hoaDon = em.find(HoaDon.class, id);
            if (hoaDon != null) {
                em.remove(hoaDon);
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
