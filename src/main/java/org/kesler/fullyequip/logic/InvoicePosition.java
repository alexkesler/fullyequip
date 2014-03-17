package org.kesler.fullyequip.logic;

import org.kesler.fullyequip.dao.AbstractEntity;

import java.util.Set;

/**
 * Позиция в накладной
 */
public class InvoicePosition extends AbstractEntity{

    private Invoice invoice;

    private UnitType unitType;
    private Long quantity;
    private Boolean invReg;
    private Double price;

    private Set<Unit> units;



}
