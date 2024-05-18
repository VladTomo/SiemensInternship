package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;

import static java.lang.Math.*;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import java.awt.event.ActionEvent;

import java.awt.*;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Scanner;


public class Main {

    static List<JButton> listHotelButtons=new ArrayList<>();

    public static List<Hotel> readFromJson(String nameOfFile) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Hotel[] hotelListVector = mapper.readValue(new File(nameOfFile), Hotel[].class);
            List<Hotel> hotelList = Arrays.asList(hotelListVector);
            return hotelList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ActionListener showMyPos(List<Double> coordinates, JFrame frame) {
        JLabel ip = new JLabel();
        ip.setText("Your position: " + coordinates);
        ip.setBounds(190, 100, 250, 20);
        frame.add(ip);

        frame.revalidate();
        frame.repaint();

        return null;
    }

    public static void defaultUI() {
        // Here you enter your IP
        List<Double> coordinates = getCoordinates("193.226.6.226");

        JFrame frame = new JFrame("Hotel Reservation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setBackground(Color.lightGray);

        showMyPos(coordinates, frame);

        JLabel msg=new JLabel("Enter radius in km: ");
        msg.setBounds(220,220,150,20);

        JTextField tf = new JTextField();
        tf.setBounds(200, 250, 150, 20);

        JButton sH = new JButton("Show hotels");
        sH.setBounds(400, 250, 150, 20);

        JButton reset = new JButton("Reset");
        reset.setBounds(400, 10, 150, 20);
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                defaultUI();
            }
        });

        showHotels(coordinates, frame, tf, sH);

        frame.add(sH);
        frame.add(msg);
        frame.add(tf);
        frame.add(reset);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private static void showHotels(List<Double> coordinates, JFrame frame, JTextField tf, JButton sH) {
        sH.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    for(JButton hotelButton : listHotelButtons) {
                        frame.remove(hotelButton);
                    }
                    listHotelButtons.clear();

                    JLabel h = new JLabel("Hotels found in your radius:");
                    h.setBounds(200, 300, 200, 20);
                    frame.add(h);

                    List<Hotel> hotels = readFromJson(
                            "C:\\Users\\Vlad Tomo\\IdeaProjects\\Siemens\\src\\main\\java\\org\\example\\hotels.json");
                    int hotelsFound = 0;
                    int yAxis = 330;
                    for (Hotel hotel : hotels) {
                        Double hotelLat = (double) hotel.getLatitude();
                        Double hotelLon = (double) hotel.getLongitude();

                        double calcDist = distanceToHotel(coordinates.get(0), coordinates.get(1), hotelLat, hotelLon);
                        if (calcDist <= Double.parseDouble(tf.getText())) {
                            JButton entry = new JButton(hotel.getName());
                            listHotelButtons.add(entry);
                            hotelsFound++;
                        }
                    }
                    if (hotelsFound == 0) {
                        JLabel entry = new JLabel("No hotels found in your radius");
                        entry.setBounds(200, yAxis, 200, 20);
                    }else{
                        for(JButton button : listHotelButtons) {
                            button.setBounds(200, yAxis, 200, 20);
                            yAxis += 30;
                            frame.add(button);
                            button.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    for(Hotel h:hotels){
                                        if(h.getName()==button.getText()){
                                            HotelPage hotelPage=new HotelPage(h);
                                        }

                                    }

                                }
                            });
                        }
                    }

                } catch (NumberFormatException f) {
                    f.printStackTrace();
                }

                frame.revalidate();
                frame.repaint();
            }
        });
    }

    public static void main(String[] args) {
        defaultUI();

        //calculate the current user position
        List<Double> coordinates=getCoordinates("193.226.6.226");
        System.out.println("User Coordinates (lat,long): "+coordinates);

        //convert user position to meters
        List<Double> dist=toMeters(coordinates);
        System.out.println("User Distances (in meters): " + dist);

//        Uncomment to enter the radius manually
//        System.out.println("Enter radius in km: ");
//        Scanner in=new Scanner(System.in);
//        double radius=in.nextDouble();

        //for all the hotels to show up
        double radius=2;

//        no hotels
//        double radius=0.1;

        List<Hotel> hotels=
         readFromJson("C:\\Users\\Vlad Tomo\\IdeaProjects\\Siemens\\src\\main\\java\\org\\example\\hotels.json"
         );
        int hotelsFound = 0;
        for(Hotel hotel:hotels){
            Double hotelLat= (double) hotel.getLatitude();
            Double hotelLon= (double) hotel.getLongitude();
            List<Double> hotelCoordinates= Arrays.asList(hotelLat,hotelLon);
            List<Double> hotelDist=toMeters(hotelCoordinates);

//            Uncomment to find each hotel's position in meters
//            System.out.println("Distances of " + hotel.getName() + ": " + hotelDist);

            double calcDist=distanceToHotel(coordinates.get(0),coordinates.get(1),hotelLat, hotelLon);
            if(calcDist<=radius){
                System.out.println(hotel.getName() + " is " + String.format("%.2f",calcDist) + " km away ");
                hotelsFound++;
            }
        }
        if(hotelsFound==0)
            System.out.println("No hotels found");
    }

    //finding the current user position in coordinates
    private static List<Double> getCoordinates(String ip) {
        // Reference: https://www.baeldung.com/geolocation-by-ip-with-maxmind
        File database = new File(
                "C:\\Users\\Vlad Tomo\\IdeaProjects\\Siemens\\src\\main\\java\\org\\example\\GeoLite2-City.mmdb");
        try {
            DatabaseReader dbReader = new DatabaseReader.Builder(database).build();

            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = dbReader.city(ipAddress);

            Double latitude = response.getLocation().getLatitude();
            Double longitude = response.getLocation().getLongitude();

            List<Double> coordinates = Arrays.asList(latitude, longitude);
            return coordinates;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (GeoIp2Exception e) {
            throw new RuntimeException(e);
        }
    }

    //convert coordinates to meters
    private static List<Double> toMeters(List<Double> coordinates) {
        // Reference: https://en.wikipedia.org/wiki/Geographic_coordinate_system
        Double distLat = 110.6 * coordinates.get(0);// in km
        Double distLon = 111.3 * coordinates.get(1);// in km

        List<Double> dist = Arrays.asList(distLat * 1000, distLon * 1000);// in meters
        return dist;
    }

    //calculating the distance from user to hotel
    public static double distanceToHotel(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);
        double R = 6371;

        return R * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1));
    }
}