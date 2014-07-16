package org.kesler.fullyequip.logic;

import org.kesler.fullyequip.dao.AbstractEntity;
import org.kesler.fullyequip.gui.dict.DictEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "AuctionTypes")
public class AuctionType extends AbstractEntity implements DictEntity{

    @Column(name = "Name")
	private String name;

	public AuctionType() {}

	public String getName() {return name;}

	public void setName(String name) {this.name = name;}


    @Override
    public String getDictName() {
        return name;
    }


}