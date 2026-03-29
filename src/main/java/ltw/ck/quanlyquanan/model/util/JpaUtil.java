package ltw.ck.quanlyquanan.model.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;

public class JpaUtil {

    private static final EntityManagerFactory EMF =
            Persistence.createEntityManagerFactory("quanan-pu");

    private JpaUtil() {
    }

    public static EntityManagerFactory getEmf() {
        return EMF;
    }

    public static void shutdown(){
        if (EMF != null && EMF.isOpen()) {
            EMF.close();
        }
    }
}
