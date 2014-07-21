package org.kesler.fullyequip.logic;


import org.kesler.fullyequip.dao.AbstractEntity;
import org.kesler.fullyequip.gui.dict.DictEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
* Вид техники (монитор, системный блок, тонкий клиент, маршрутизатор)
*/
@Entity
@Table(name = "UnitTypes")
public class UnitType extends AbstractEntity implements DictEntity{

    @Column(name = "Name", length = 255)
	private String name;


	public UnitType() {}


	public String getName() {return name;}

	public void setName(String name) {this.name = name;}

    @Override
    public String toString() {
        return name;
    }


}