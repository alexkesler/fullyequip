package org.kesler.fullyequip.logic;


import org.kesler.fullyequip.gui.dict.DictEntity;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
* Накладная
*/
@Entity
@Table(name = "Invoices")
public class Invoice extends DictEntity{

    @Column(name = "Number", length = 255)
	private String number;

    @Column(name = "Date")
    @Temporal(TemporalType.TIMESTAMP)
	private Date date;

    @ManyToOne
    @JoinColumn(name = "ContractID",nullable = false)
	private Contract contract;

    // при удалении из списка - удаляем сущность из БД
    @OneToMany(mappedBy = "invoice", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Unit> units;

	
	public Invoice() {
        units = new HashSet<Unit>();
    }

	
	public String getNumber() {return number;}
	public void setNumber(String number) {this.number = number;}


	public Date getDate() {return date;}
	public void setDate(Date date) {this.date = date;}


	public Contract getContract() {return contract;}
	public void setContract(Contract contract) {this.contract = contract;}

    public Set<Unit> getUnits() {return units;}
    public void setUnits(Set<Unit> units) {this.units = units;}


    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return number + " от " +  simpleDateFormat.format(date);
    }
 
}