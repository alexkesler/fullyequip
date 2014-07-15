package org.kesler.fullyequip.logic;


import org.kesler.fullyequip.gui.dict.DictEntity;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

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
    @OneToMany(mappedBy = "invoice", fetch = EAGER, cascade = ALL, orphanRemoval = true)
    private Set<Unit> units;

    @OneToMany(mappedBy = "invoice", fetch = EAGER, cascade = ALL, orphanRemoval = true)
    private Set<InvoicePosition> positions;

	
	public Invoice() {
        units = new HashSet<Unit>();
        positions = new HashSet<InvoicePosition>();
    }

	
	public String getNumber() {return number;}
	public void setNumber(String number) {this.number = number;}


	public Date getDate() {return date;}
	public void setDate(Date date) {this.date = date;}


	public Contract getContract() {return contract;}
	public void setContract(Contract contract) {this.contract = contract;}

    public Set<Unit> getUnits() {return units;}

    public Set<InvoicePosition> getPositions() {return positions;}

    public Double computeTotal() {
        Double total = 0.0;
        for(InvoicePosition invoicePosition: positions) total += invoicePosition.computeTotal();

        return total;
    }


    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return number + " от " +  simpleDateFormat.format(date);
    }
 
}