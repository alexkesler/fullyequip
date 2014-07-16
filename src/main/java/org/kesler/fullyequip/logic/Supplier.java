package org.kesler.fullyequip.logic;



import org.kesler.fullyequip.dao.AbstractEntity;
import org.kesler.fullyequip.gui.dict.DictEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
* Поставщик
*/
@Entity
@Table(name = "Suppliers")
public class Supplier extends AbstractEntity implements DictEntity {

    @Column(name = "Name", length = 512)
	private String name;

//    @Column(name = "Contacts", length = 1024)
//	private String contacts;


	public Supplier() {}


	public String getName() {return name;}

	public void setName(String name) {this.name = name;}


//	public String getContacts() {return contacts;}
//
//	public void setContacts(String contacts) {this.contacts = contacts;}


    @Override
    public String getDictName() {
        return name;
    }


}