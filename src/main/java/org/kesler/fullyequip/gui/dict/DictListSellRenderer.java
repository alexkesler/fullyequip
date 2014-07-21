package org.kesler.fullyequip.gui.dict;

import javax.swing.*;
import java.awt.*;

/**
 * Created by alex on 21.07.14.
 */
public class DictListSellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Object newValue = value;
        if (value instanceof DictEntity && value!=null) newValue = ((DictEntity)value).toString();
        return super.getListCellRendererComponent(list, newValue, index, isSelected, cellHasFocus);
    }

}
