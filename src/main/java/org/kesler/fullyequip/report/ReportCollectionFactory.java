package org.kesler.fullyequip.report;

import org.kesler.fullyequip.logic.Contract;
import org.kesler.fullyequip.logic.model.DefaultModel;

import java.util.List;


public class ReportCollectionFactory {

    public static List<Contract> createContractCollection() {


        DefaultModel<Contract> contractsModel = new DefaultModel<Contract>(Contract.class);
        contractsModel.readItemsFromDB();
        List<Contract> contracts = contractsModel.getAllItems();

        return contracts;

    }

}
