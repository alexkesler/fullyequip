package org.kesler.fullyequip.logic;

import org.kesler.fullyequip.gui.dict.DictEntity;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Определяет договор поставок
 */
@Entity
@Table(name = "Contracts")
public class Contract extends DictEntity{

    @Column(name = "Number", length = 50)
	private String number;

    @Column(name = "Date")
    @Temporal(TemporalType.TIMESTAMP)
	private Date date;

    @ManyToOne
    @JoinColumn(name = "SupplierID", nullable = false)
	private Supplier supplier;

    @Column(name = "ByAuction")
	private Boolean byAuction;

    @OneToOne
    @JoinColumn(name = "AuctionID")
	private Auction auction;

    @OneToMany(mappedBy = "contract", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Invoice> invoices;


	public Contract() {
        invoices = new HashSet<Invoice>();
    }


	public String getNumber() {return number;}
	public void setNumber(String number) {this.number = number;}


	public Date getDate() {return date;}
	public void setDate(Date date) {this.date = date;}


	public Supplier getSupplier() {return supplier;}
	public void setSupplier(Supplier supplier) {this.supplier = supplier;}


	public Boolean isByAuction() {return byAuction;}
    public Boolean getByAuction() {return byAuction;}
	public void setByAuction(Boolean byAuction) {this.byAuction = byAuction;}


	public Auction getAuction() {return auction;}
	public void setAuction(Auction auction) {this.auction = auction;}


    public Set<Invoice> getInvoices() {return invoices;}


    public Double computeTotal() {
        Double total = 0.0;
        for(Invoice invoice: invoices) total += invoice.computeTotal();

        return total;
    }

    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String supplierName = supplier == null ? "" : " c " + supplier.getName();
        return number + " от " + simpleDateFormat.format(date) +  supplierName;
    }

}