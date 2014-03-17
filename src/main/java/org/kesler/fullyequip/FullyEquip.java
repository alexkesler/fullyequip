package org.kesler.fullyequip;


import com.alee.laf.WebLookAndFeel;
import org.kesler.fullyequip.gui.main.MainViewController;
import org.kesler.fullyequip.util.OptionsUtil;

import javax.swing.*;

public class FullyEquip {

	public static void main(String args[]) {

        OptionsUtil.readOptions();
        WebLookAndFeel.install();
        AppStarter starter = new AppStarter();
        SwingUtilities.invokeLater(starter);

	}
}

class AppStarter extends Thread {
    @Override
    public void run() {
        MainViewController.getInstance().showView();
    }
}
