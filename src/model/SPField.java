package model;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SPField {
    private String name;
    private String type;
    private String defaultValue;

    public SPField(String name, String type, String defaultValue) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
    }


    public SPField(String name, String type) {
        this.name = name;
        this.type = type;
        defaultValue = getDefaultValue(type);
    }


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SPField SPField = (model.SPField) o;
        return Objects.equals(name, SPField.name);
    }

    @Override
    public String toString() {
        return "model.SPField{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                '}';
    }

    public static SPField getViewPartFromRow(TableModel tableModel, int row) {
        String name = tableModel.getValueAt(row, 0).toString();
        String type = tableModel.getValueAt(row, 1).toString();
        String defaultValue = tableModel.getValueAt(row, 2).toString();
        return new SPField(name, type, defaultValue);
    }

    public static List<SPField> getViewPartListFromTable(JTable jTable) {
        TableModel model = jTable.getModel();
        List<SPField> SPFieldList = new ArrayList<>();
        for (int row = 0; row < model.getRowCount(); row++) {
            SPField SPField = getViewPartFromRow(model, row);
            SPFieldList.add(SPField);
        }
        return SPFieldList;

    }

    public static String getDefaultValue(String type) {
        switch (type) {
            case "String":
                return "\"\"";
            case "boolean":
                return "false";
            case "int":
            case "long":
                return "-1";
            default:
                return "-1f";

        }
    }

    public static String[] getDataTypes() {
        return new String[]{"String", "boolean", "int", "long", "float"};
    }
}
