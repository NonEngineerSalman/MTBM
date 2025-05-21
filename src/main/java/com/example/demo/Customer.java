package com.example.demo;

public class Customer {
    private int id;
    private String movieTitle, genre, tableNo, time, date, totalPayment, ticketNumber;

    public Customer(int id, String movieTitle, String genre, String tableNo, String time, String date, String totalPayment, String ticketNumber) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.genre = genre;
        this.tableNo = tableNo;
        this.time = time;
        this.date = date;
        this.totalPayment = totalPayment;
        this.ticketNumber = ticketNumber;
    }

    public int getId() { return id; }
    public String getMovieTitle() { return movieTitle; }
    public String getGenre() { return genre; }
    public String getTableNo() { return tableNo; }
    public String getTime() { return time; }
    public String getDate() { return date; }
    public String getTotalPayment() { return totalPayment; }
    public String getTicketNumber() { return ticketNumber; }
}
