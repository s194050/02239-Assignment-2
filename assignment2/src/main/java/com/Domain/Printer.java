package com.Domain;

import java.util.ArrayList;
import java.util.List;

public class Printer {

    private String printerName; // Name of printer
    private String status = "Idle"; // Status of printer
    private int jobNumber = 0; // Job number
    private List<Pair> queue = new ArrayList<>(); // Queue for storing jobs
    

    public Printer(String printerName) { // Constructor for printer
        this.printerName = printerName;

    }
    
    public String getPrinterName() { // Get printer name
        return printerName;
    }

    public String getStatus() { // Get printer status
        return status;
    }

    public void setPrinterName(String printerName) { // Set printer name
        this.printerName = printerName;
    }

    public String addToqueue(String filename) { // Add job to queue
        queue.add(new Pair(jobNumber, filename)); // Add to queue
        jobNumber++; // Increment job number
        return "File " + filename + " added to queue " + this.printerName+ " as job " + (jobNumber - 1);
    }

    String queueString = "";
        for (Pair pair : queue) {
            queueString += pair.getJob() + " " + pair.getFilename() + " ";
        }
        return queueString;   

}
