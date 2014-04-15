package org.kesler.fullyequip.logic;

import java.util.*;

import javax.persistence.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.kesler.fullyequip.gui.dict.DictEntity;


/**
* Единица оборудования
*/

@Entity
@Table(name = "Units")
public class Unit extends DictEntity{

	@Column(name="Name", length=255)
	private String name;

	@ManyToOne
	@JoinColumn(name="TypeID", nullable=false)
	private UnitType type;

    @Column(name = "InvReg")
    private Boolean invReg;

	@Column(name="InvNumber", length=50)
	private String invNumber;

	@Column(name="SerialNumber", length=50)
	private String serialNumber;

	@Column(name="Price")
	private Double price;

    @Column(name="Quantity")
    private Long quantity; // Количество техники без инвентарников -  пилоты, патчкорды

	@ManyToOne
	@JoinColumn(name="InvoiceID", nullable = false)
	private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "InvoicePositionID", nullable = false)
    private InvoicePosition invoicePosition;

	@ManyToOne
	@JoinColumn(name="PlaceID", nullable = false)
	private Place place;

    @ManyToOne
    @JoinColumn(name="StateID", nullable = false)
    private UnitState state;

    @OneToMany(mappedBy = "unit", fetch = EAGER, cascade = ALL, orphanRemoval = true)
    private Set<UnitMove> moves;


	public Unit() {
        moves = new HashSet<UnitMove>();
        quantity = 1L;
    }


	public String getName() {return name;}
	public void setName(String name) {this.name = name;}

	public UnitType getType() {return type;}
	public void setType(UnitType type) {this.type = type;}

    public Boolean getInvReg() {return invReg;}
    public Boolean isInvreg() {return invReg;}
    public void setInvReg(Boolean invReg) {this.invReg = invReg;}

	public String getInvNumber() {return invNumber;}
	public void setInvNumber(String invNumber) {this.invNumber = invNumber;}

	public String getSerialNumber() {return serialNumber;}
	public void setSerialNumber(String serialNumber) {this.serialNumber = serialNumber;}

	public Double getPrice() {return price;}
	public void setPrice(Double price) {this.price = price;}

	public Invoice getInvoice() {return invoice;}
	public void setInvoice(Invoice invoice) {this.invoice = invoice;}

    public InvoicePosition getInvoicePosition() {return invoicePosition;}
    public void setInvoicePosition(InvoicePosition invoicePosition) {this.invoicePosition = invoicePosition;}

	public Long getQuantity() {return quantity;}
	public void setQuantity(Long quantity) {this.quantity = quantity;}

	public Place getPlace() {return place;}
	public void setPlace(Place place) {
        // если присваивается то же место, то ничего не делаем
        if (this.place!=null && this.place.equals(place)) return;
        if (this.place!=null) this.place.getUnits().remove(this); // удаляемся из старого расположения
        this.place = place;                 // присваиваем новое расположение
        if (this.place!=null) this.place.getUnits().add(this);    // Добавляемся в новое расположение

        // Запоминаем это перемещение
        UnitMove move = new UnitMove();
        move.setUnit(this);
        move.setPlace(place);
        move.setMoveDate(new Date());
        moves.add(move);
    }

    public UnitState getState() {return state;}
    public void setState(UnitState state) {this.state = state;}

    public Set<UnitMove> getMoves() {return moves;}
    public void setMoves(Set<UnitMove> moves) {this.moves = moves;}

    @Override
    public String toString() {
        return name;
    }

}