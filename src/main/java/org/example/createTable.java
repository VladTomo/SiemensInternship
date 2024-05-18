package org.example;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class createTable extends AbstractTableModel {
    private final List<Room> rooms;
    private final String[] columnNames = {"RoomNumber", "RoomType", "Price", "isAvailable"};

    public createTable(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public int getRowCount() {
        return rooms.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Room r = rooms.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return r.getRoomNumber();
            case 1:
                return r.getType();
            case 2:
                return r.getPrice();
            case 3:
                return r.isAvailable();
            default:
                return null;
        }
    }

}
