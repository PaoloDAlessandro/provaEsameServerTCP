package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import com.google.gson.Gson;



public class ClientHandler implements Runnable {
    Socket clientSocket;
    BufferedReader in;
    PrintWriter out;
    static ArrayList<City> cities = new ArrayList<City>();
    DatabaseHandler dbh = null;

    ClientHandler (Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run () {
        this.buildCities();
        this.inizializeClientHandler();
        this.inizializeDatabaseHandler();
        try {
            this.executeClientHandler();
        } catch (SocketException e) {
            System.out.println("error");
        }
    }

    void inizializeClientHandler () {
        try {
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("reader failed" + e);
        }

        out = null;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void executeClientHandler() throws SocketException {
        Gson gson = new Gson();
        String s;
        while (true) {
            s = receive();
            System.out.println(s);
            try {
                switch (s) {
                    default:
                        out.println(s + " is not a command");
                        break;
                    case "hottest":
                        out.println(gson.toJson(searchMaxTemp()));
                        break;

                    case "all":
                        out.println(dbh.selectAll());
                        break;

                    case "sorted_by_temp":
                        out.println(dbh.sort_by_temp());
                        break;

                    case "sorted_by_name":
                        sort_by_name();
                        out.println(dbh.sort_by_name());
                        //out.println(gson.toJson(cities));
                        break;

                    case "add":
                        insertData();
                        break;

                    case "remove":
                        removeData();
                        break;

                    case "search":
                        out.println(searchCity());
                        break;
                }

            } catch (NullPointerException e) {
                System.out.println("Client: " + clientSocket.getLocalAddress() + " disconnected from the server");
                break;
            }

            if (s == "") break;
        }
    }

    void inizializeDatabaseHandler() {
        dbh = new DatabaseHandler();
    }


    String receive() {
        String s = "";
        try {
            s = in.readLine();
        } catch (IOException e) {
            //tryReconnect();
        }
        return s;
    }

    private void tryReconnect() {
        try {
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildCities() {
        cities.add(new City(3,"Toronto",15.9));
        cities.add(new City(33,"Milan",25.94));
        cities.add(new City(55,"Rome",35.4));
    }

    void insertData() {
        String city_name = "";
        Double temp = 0.0;
        out.println("Type city: ");
        try {
            city_name = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("Type temp: ");
        try {
            temp = Double.parseDouble(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        dbh.insertCity(city_name, temp);
        out.println(city_name + " has been added");
    }

    String searchCity() {
        String city_name = "";

        try {
            out.println("Type city to search for: ");
            city_name = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result = dbh.searchCity(city_name);

        return result;

    }

    void removeData() {
        String city_name = "";

        try {
            out.println("Type city to be deleted: ");
            city_name = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dbh.removeCity(city_name);
        out.println(city_name + " has been removed");
    }

    City searchMaxTemp() {
        sort_by_temp();
        return cities.get(0);
    }

    void sort_by_temp() {
        cities.sort((o1, o2) -> {
            if (o1.getTemp() < o2.getTemp())
                return 1;
            if (o1.getTemp() > o2.getTemp())
                return -1;
            return 0;
        });
    }

    void sort_by_name() {
        cities.sort((o1, o2) -> {
            return o1.getName().compareTo(o2.getName());
        });
    }


}
