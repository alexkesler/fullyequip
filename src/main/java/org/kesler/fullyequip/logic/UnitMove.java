package org.kesler.fullyequip.logic;

import org.kesler.fullyequip.dao.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Сущность для фиксации перемещений оборудования
 */
@Entity
public class UnitMove extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name = "UnitID", nullable = false)
    private Unit unit;

    @ManyToOne
    @JoinColumn(name = "PlaceID", nullable = false)
    private Place place;

    @Column(name = "MoveDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date moveDate;


    public UnitMove() {}


    public Unit getUnit() {return unit;}
    public void setUnit(Unit unit) {this.unit = unit;}


    public Place getPlace() {return place;}
    public void setPlace(Place place) {this.place = place;}


    public Date getMoveDate() {return moveDate;}
    public void setMoveDate(Date moveDate) {this.moveDate = moveDate;}


}
