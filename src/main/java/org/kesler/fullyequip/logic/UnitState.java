package org.kesler.fullyequip.logic;

import org.kesler.fullyequip.gui.dict.DictEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Отображает состояние единицы оборудования
 */
@Entity
@Table(name = "UnitStates")
public class UnitState extends DictEntity{

    @Column(name = "Name", length = 50)
    private String name;

    public UnitState() {}


    public String getName() {return name;}
    public void setName(String name) {this.name = name;}


    @Override
    public String toString() {
        return name;
    }

}
