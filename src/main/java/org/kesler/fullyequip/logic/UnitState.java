package org.kesler.fullyequip.logic;

import org.kesler.fullyequip.dao.AbstractEntity;
import org.kesler.fullyequip.gui.dict.DictEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Отображает состояние единицы оборудования
 */
@Entity
@Table(name = "UnitStates")
public class UnitState extends AbstractEntity implements DictEntity{

    @Column(name = "Name", length = 50)
    private String name;

    @Column(name = "Initial")
    private Boolean initial;

    public UnitState() {
        initial = false;
    }


    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public Boolean getInitial() {return initial;}
    public Boolean isInitial() {return initial;}
    public void setInitial(Boolean initial) {this.initial = initial;}



    @Override
    public String getDictName() {
        return name;
    }

}
