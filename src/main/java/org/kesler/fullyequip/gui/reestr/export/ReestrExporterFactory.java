package org.kesler.fullyequip.gui.reestr.export;

/**
 * Created by alex on 13.06.14.
 */
public class ReestrExporterFactory {

    public static ReestrExporter createReestrExporter(ReestrExportEnum exportEnum) {
        switch (exportEnum) {
             case SELECTED_COLUMNS:
                return new SelectedColumnsReestrExporter();
            default:
                return null;
        }
    }
}
