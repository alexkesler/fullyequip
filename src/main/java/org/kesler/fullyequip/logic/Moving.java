package org.kesler.fullyequip.logic;

import org.kesler.fullyequip.dao.AbstractEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Класс для фиксации перемещения оборудования
 */
public class Moving extends AbstractEntity{

    @OneToMany(mappedBy = "moving",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Unit> units;

    @ManyToOne
    @JoinColumn(name = "NewPlaceID")
    private Place newPlace;

    @Column(name = "MovingDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date movingDate;

    @Column(name = "Complete")
    private Boolean complete;

    public Moving() {
        units = new ArrayList<Unit>();
    }

    public List<Unit> getUnits() {return units;}
    public void setUnits(List<Unit> units) {this.units = units;}

    public Place getNewPlace() {return newPlace;}
    public void setNewPlace(Place newPlace) {this.newPlace = newPlace;}

    public Date getMovingDate() {return movingDate;}
    public void setMovingDate(Date movingDate) {this.movingDate = movingDate;}

    public Boolean isComplete() {return complete;}
    public Boolean getComplete() {return complete;}
    public void setComplete(Boolean complete) {this.complete = complete;}

}
