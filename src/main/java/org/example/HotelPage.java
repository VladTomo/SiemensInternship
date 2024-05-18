package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HotelPage extends JFrame {
    private JPanel panelMaster = new JPanel();

    JLabel l=new JLabel();
    JLabel bookMsg=new JLabel();
    JTextField tf=new JTextField("");
    JButton b=new JButton();

    private List<Room> rooms = new ArrayList<Room>();

    public HotelPage(Hotel hotel) {
        this.setSize(600,600);
        this.setTitle(hotel.getName());
        panelMaster.setSize(600,600);
        rooms=hotel.getRooms();

        createTable dataTable=new createTable(rooms);
        JTable UITable=new JTable(dataTable);
        JScrollPane scroll=new JScrollPane(UITable);
        panelMaster.add(scroll,BorderLayout.CENTER);

        l.setText("Enter a room number: ");
        b.setText("Book");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(Room r:rooms){
                    if(r.getRoomNumber()==Integer.parseInt(tf.getText())){
                        System.out.println(r.getRoomNumber() + " " + r.isAvailable());
                        if(r.isAvailable()){
                            bookMsg.setText("Room " + r.getRoomNumber() + " has been booked");
                        }
                    }
                }
            }
        });
        tf.setColumns(4);
        panelMaster.add(l);
        panelMaster.add(tf);
        panelMaster.add(b);
        panelMaster.add(bookMsg);

        this.add(panelMaster);
        this.setVisible(true);
    }
}
