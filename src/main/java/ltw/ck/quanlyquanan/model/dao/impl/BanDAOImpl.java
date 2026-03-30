package ltw.ck.quanlyquanan.model.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ltw.ck.quanlyquanan.model.dao.BanDAO;
import ltw.ck.quanlyquanan.model.entity.Ban;
import ltw.ck.quanlyquanan.model.util.JpaUtil;

import java.util.List;

public class BanDAOImpl implements BanDAO {

    @Override
    public void save(Ban ban){
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            em.persist(ban);
            tx.commit();
        } catch (Exception e){
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Ban> findAll(){
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try{
            return em.createQuery("""
                    SELECT b
                    FROM Ban b
                    ORDER BY b.maBan
                    """,
                    Ban.class
                    ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Ban findById(Long id){
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try{
            return em.find(Ban.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public Ban findByTenBan(String tenBan){
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        try {
            List<Ban> list = em.createQuery("""
                    SELECT b
                    FROM Ban b
                    WHERE b.tenBan LIKE :tenBan
                    """, Ban.class)
                    .setParameter("tenBan", "%" + tenBan + "%")
                    .getResultList();

                    return list.isEmpty() ? null : list.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Ban ban){
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            em.merge(ban);
            tx.commit();
        } catch (Exception e){
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id){
        EntityManager em = JpaUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            Ban ban = em.find(Ban.class, id);
            if (ban != null)
                em.remove(ban);
            tx.commit();
        } catch (Exception e){
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }



}
