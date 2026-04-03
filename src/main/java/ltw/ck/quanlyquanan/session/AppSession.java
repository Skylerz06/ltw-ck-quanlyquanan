package ltw.ck.quanlyquanan.session;

import ltw.ck.quanlyquanan.model.entity.NhanVien;
import ltw.ck.quanlyquanan.model.entity.TaiKhoan;

public final class AppSession {

    private static TaiKhoan currentTaiKhoan;

    private AppSession() {
    }

    public static void setCurrentTaiKhoan(TaiKhoan taiKhoan) {
        currentTaiKhoan = taiKhoan;
    }

    public static TaiKhoan getCurrentTaiKhoan() {
        return currentTaiKhoan;
    }

    public static NhanVien getCurrentNhanVien() {
        return currentTaiKhoan == null ? null : currentTaiKhoan.getNhanVien();
    }

    public static void clear() {
        currentTaiKhoan = null;
    }
}
