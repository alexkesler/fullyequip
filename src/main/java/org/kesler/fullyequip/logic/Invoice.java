package org.kesler.fullyequip.logic;


import org.kesler.fullyequip.dao.AbstractEntity;
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
public class Invoice extends AbstractEntity implements DictEntity{

    @Column(name = "Number", length = 255)
	private String number;

    @Column(name = "Date")
    @Temporal(TemporalType.TIMESTAMP)
	private Date date;

    @ManyToOne
    @JoinColumn(name = "ContractID",nullable = false)
	private Contract contract;

    @OneToMany(mappedBy = "invoice", fetch = EAGER, cascade = ALL, orphanRemoval = true)
    private Set<InvoicePosition> positions;

	
	public Invoice() {
        positions = new HashSet<InvoicePosition>();
    }

	
	public String getNumber() {return number;}
	public void setNumber(String number) {this.number = number;}

	public Date getDate() {return date;}
	public void setDate(Date date) {this.date = date;}

	public Contract getContract() {return contract;}
	public void setContract(Contract contract) {this.contract = contract;}

    public Set<InvoicePosition> getPositions() {return positions;}

    public Double computeTotal() {
        Double total = 0.0;
        for(InvoicePosition invoicePosition: positions) total += invoicePosition.computeTotal();
        return total;
    }

    @Override
    public String getDictName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return number + " от " +  simpleDateFormat.format(date);
    }
 
}