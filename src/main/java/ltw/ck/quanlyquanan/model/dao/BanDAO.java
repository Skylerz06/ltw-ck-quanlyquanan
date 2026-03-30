package ltw.ck.quanlyquanan.model.dao;

import java.util.List;
import ltw.ck.quanlyquanan.model.entity.Ban;

public interface BanDAO {
    List<Ban> findAll();
    Ban findById(Long id);
    Ban findByTenBan(String tenBan);

    void save(Ban entity);
    void update(Ban entity);
    void delete(Long id);
}
