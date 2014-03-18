package org.kesler.fullyequip.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;


import org.hibernate.FetchMode;
import org.hibernate.annotations.*;
import org.kesler.fullyequip.dao.AbstractEntity;
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
	@JoinColumn(name="PlaceID", nullable = false)
	private Place place;

    @ManyToOne
    @JoinColumn(name="StateID")
    private UnitState state;

    @OneToMany(mappedBy = "unit", fetch = FetchType.EAGER, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
    @Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    private List<UnitMove> moves;


	public Unit() {
        moves = new ArrayList<UnitMove>();
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

    public List<UnitMove> getMoves() {return moves;}
    public void setMoves(List<UnitMove> moves) {this.moves = moves;}

    @Override
    public String toString() {
        return name;
    }

}