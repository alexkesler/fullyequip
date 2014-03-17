package org.kesler.fullyequip.gui.dialog;

import org.kesler.fullyequip.gui.dict.DictEntity;

import java.util.List;

/**
 * Created by alex on 16.03.14.
 */
public interface ListDialog<T extends DictEntity> {

    /**
     * Возвращает индекс выбранного элемента
     * @return
     */
    public int getSelectedIndex();

    /**
     * Устанавливает перечень элементов для отображения/ редактирования
     */
    public void setItems(List<T> items);



}
