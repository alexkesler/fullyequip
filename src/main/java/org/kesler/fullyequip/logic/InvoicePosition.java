package org.kesler.fullyequip.logic;

import org.kesler.fullyequip.dao.AbstractEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

/**
 * Позиция в накладной
 */
@Entity
@Table(name="InvoicePositions")
public class InvoicePosition extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name = "InvoiceID", nullable = false)
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "TypeID", nullable = false)
    private UnitType unitType;

    @Column(name = "Quantity")
    private Long quantity;

    @Column(name = "InvReg")
    private Boolean invReg;

    @Column(name = "Price")
    private Double price;

    @OneToMany(mappedBy = "invoicePosition", fetch = EAGER, cascade = ALL, orphanRemoval = true)
    private Set<Unit> units;

    public InvoicePosition() {
        units = new HashSet<Unit>();
    }


    public Invoice getInvoice() {return invoice;}
    public void setInvoice(Invoice invoice) {this.invoice = invoice;}

    public UnitType getUnitType() {return unitType;}
    public void setUnitType(UnitType unitType) { this.unitType = unitType; }

    public Long getQuantity() {return quantity;}
    public void setQuantity(Long quantity) {this.quantity = quantity;}

    public Boolean getInvReg() {return invReg;}
    public Boolean isInvReg() {return invReg;}
    public void setInvReg(Boolean invReg) {this.invReg = invReg;}

    public Double getPrice() {return price;}
    public void setPrice(Double price) {this.price = price;}

    public Set<Unit> getUnits() {return units;}

}
