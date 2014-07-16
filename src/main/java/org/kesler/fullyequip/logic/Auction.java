package org.kesler.fullyequip.logic;

import org.kesler.fullyequip.dao.AbstractEntity;
import org.kesler.fullyequip.gui.dict.DictEntity;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "Auctions")
public class Auction extends AbstractEntity implements DictEntity {

    @Column(name="Name", length = 512)
	private String name;

    @ManyToOne
    @JoinColumn(name = "TypeID", nullable = false)
	private AuctionType type;

    @Column(name = "Number", length = 50)
	private String number;

    @Column(name = "Price")
	private Float price;

    @Column(name = "HoldingDate")
    @Temporal(TemporalType.TIMESTAMP)
	private Date holdingDate;


    @OneToOne(mappedBy = "auction")
    private Contract contract;

	public Auction() {}

	public String getName() {return name;}

	public void setName(String name) {this.name = name;}


	public AuctionType getType() {return type;}

	public void setType(AuctionType type) {this.type = type;}


	public String getNumber() {return number;}

	public void setNumber(String number) {this.number = number;}


	public Float getPrice() {return price;}

	public void setPrice(Float price) {this.price = price;}


	public Date getHoldingDate() {return holdingDate;}

	public void setHoldingDate(Date holdingDate) {this.holdingDate = holdingDate;}


    public Contract getContract() {return contract;}

    public void setContract(Contract contract) {this.contract = contract;}

    @Override
    public String getDictName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return name + " от " + simpleDateFormat.format(holdingDate);
    }

}