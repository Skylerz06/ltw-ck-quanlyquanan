package ltw.ck.quanlyquanan.view;

import javax.swing.*;
import java.awt.*;

public class BaseSubView extends JPanel {

    private Runnable closeHandler = () -> {
    };

    public BaseSubView() {
        super(new BorderLayout());
    }

    public void setCloseHandler(Runnable closeHandler) {
        this.closeHandler = closeHandler == null ? () -> {
        } : closeHandler;
    }

    public void dispose() {
        closeHandler.run();
    }
}
