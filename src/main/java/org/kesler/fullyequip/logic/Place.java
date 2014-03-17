package org.kesler.fullyequip.logic;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.kesler.fullyequip.dao.AbstractEntity;
import org.kesler.fullyequip.gui.dict.DictEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* Место, где помещается оборудование (Филиал, склад, дирекция)
*/
@Entity
@Table(name = "Places")
public class Place extends DictEntity{

    @Column(name = "Name", length = 255)
	private String name;

    @Column(name = "WindowsCount")
	private Integer windowsCount;

    @Column(name = "Supervisor", length = 255)
	private String supervisor;

    @Column(name = "Address", length = 1024)
	private String address;

    @Column(name = "Contacts", length = 1024)
	private String contacts;

    @Column(name = "Storage")
    private Boolean storage;

    @OneToMany(mappedBy = "place", fetch = FetchType.EAGER)
//    @Fetch(FetchMode.SUBSELECT)
    private Set<Unit> units;


	public Place() {
        units = new HashSet<Unit>();
    }


	public String getName() {return name;}

	public void setName(String name) {this.name = name;}


	public Integer getWindowsCount() {return windowsCount;}

	public void setWindowsCount(Integer windowsCount) {this.windowsCount = windowsCount;}


	public String getSupervisor() {return supervisor;}

	public void setSupervisor(String supervisor) {this.supervisor = supervisor;}


	public String getAddress() {return address;}

	public void setAddress(String address) {this.address = address;}


	public String getContacts() {return contacts;}

	public void setContacts(String contacts) {this.contacts = contacts;}


    public Boolean isStorage() { return storage;}
    public void setStorage(Boolean storage) {this.storage = storage;}

    public Set<Unit> getUnits() {return units;}

    @Override
    public String toString() {
        return name;
    }

}