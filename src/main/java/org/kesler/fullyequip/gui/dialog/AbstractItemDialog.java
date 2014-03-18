package org.kesler.fullyequip.gui.dialog;

import org.kesler.fullyequip.gui.dict.DictEntity;
import org.kesler.fullyequip.util.ResourcesUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Абстрактный диалог для редактирования элемента справочника
 * Диалог редактирования конкретного типа необходимо расширить из этого абстрактного диалога
 * @param <T> элемент справочника
 */
public abstract class AbstractItemDialog<T extends DictEntity> extends AbstractDialog{

    protected T item;

    /**
     * Конструктор для диалога создания элемента справочника
     * @param parentDialog родительский диалог
     */
    public AbstractItemDialog(JDialog parentDialog) {
        super(parentDialog, "Создать" ,true);

        createNewItem();
        createGUI();
        loadGUIFromItem();
        setLocationRelativeTo(parentDialog);

    }

    /**
     * Конструктор для диалога редактирования элемента
     * @param parentDialog родительский диалог
     * @param item элемент для редактирования
     */
    public AbstractItemDialog(JDialog parentDialog, T item) {
        super(parentDialog, "Изменить", true);
        this.item = item;

        createGUI();
        loadGUIFromItem();
        setLocationRelativeTo(parentDialog);

    }

    /**
     * Конструктор для диалога создания элемента с измененным заголовком
     * @param parentDialog родительский диалог
     * @param title заголовок
     */
    public AbstractItemDialog(JDialog parentDialog, String title) {
        super(parentDialog, title ,true);

        createNewItem();
        createGUI();
        loadGUIFromItem();
        setLocationRelativeTo(parentDialog);

    }

    /**
     * Конструктор для диалога редактирования элемента с измененным заголовком
     * @param parentDialog родительский диалог
     * @param title заголовок
     * @param item элемент для редактирования
     */
    public AbstractItemDialog(JDialog parentDialog, String title, T item) {
        super(parentDialog, title, true);
        this.item = item;

        createGUI();
        loadGUIFromItem();
        setLocationRelativeTo(parentDialog);
    }

    /**
     * Возвращает новый (измененный) элемент
     * @return новый (измененный) элемент
     */
    public T getItem() {return item;}

    /**
     * Абстрактный метод для создания новой сущности
     */
    protected abstract void createNewItem();

    /**
     * Абстрактный метод для создания панели редактирования сущности
     * @return панель для редактирования сущности
     */
    protected abstract JPanel createItemPanel();

    /**
     * Абстрактный метод для загрузки содержимого GUI из сущности
     */
    protected abstract void loadGUIFromItem();

    /**
     * Абстрактный метод для чтения сущности из GUI
     * @return результат чтения (все необходимые поля заданы)
     */
    protected abstract boolean readItemFromGUI();

    /**
     * Абстрактный метод для проверки того измемения сущности при редактировании
     * @return результат проверки измемения сущности
     */
    protected abstract boolean checkChanged();


    /**
     * Метод создания GUI
     */
    protected void createGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Панель данных
        JPanel dataPanel = createItemPanel();
        dataPanel.setBorder(BorderFactory.createEtchedBorder());

        // Панель кнопок
        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton("Ok");
        okButton.setIcon(ResourcesUtil.getIcon("accept.png"));
        this.getRootPane().setDefaultButton(okButton);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if(readItemFromGUI()) {
                    result = OK;
                    changed = checkChanged();
                    setVisible(false);
                }
            }
        });


        JButton cancelButton = new JButton("Отмена");
        cancelButton.setIcon(ResourcesUtil.getIcon("cancel.png"));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                result = CANCEL;
                setVisible(false);
            }
        });

        // Собираем панель кнопок
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Собираем панель данных
        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.setContentPane(mainPanel);
        this.pack();

    }

}