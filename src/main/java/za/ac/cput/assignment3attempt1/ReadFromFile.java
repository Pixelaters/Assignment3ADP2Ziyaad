/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.cput.assignment3attempt1;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

/**
 *
 * @author Ziyaad Petersen 219083479
 */
public class ReadFromFile {
    //creating global variables

    private ObjectInputStream input;
    //creating array formattedDate customer and supplier
    ArrayList<Customer> CustArray = new ArrayList<>();
    ArrayList<Supplier> SuppArray = new ArrayList<>();
    int CustomersCanRent;
    int CustomersCannotRent;
    int age[];

    //opens ser file
    public void openFile() {
        try {
            input = new ObjectInputStream(new FileInputStream("stakeholder.ser"));
            System.out.println("open for reading");
        } catch (IOException ioe) {
            System.out.println("error reading file" + ioe.getMessage());
        }
    }

    //adds all stakeholders to corresponding arrays
    public void readFile() {

        try {
            //creating temp variable of type stakeholder
            Stakeholder temp;

            while (true) {
                temp = (Stakeholder) input.readObject();
                //if stakeholder(temp) is a customer it will add it to the customer array
                if (temp instanceof Customer) {
                    CustArray.add((Customer) temp); //casting it to Customer
                    //if stakeholder(temp) is a supplier it will add it to the customer array
                } else if (temp instanceof Supplier) {
                    SuppArray.add((Supplier) temp); //casting it to supplier

                }

            }
        } catch (EOFException ex) {
            System.out.println("EOF reached"+ ex.getMessage());
        } catch (ClassNotFoundException cnfe) {
            System.out.println("class not found"+ cnfe.getMessage());
        } catch (FileNotFoundException fnfe) {
            System.out.println("file not found"+ fnfe.getMessage());
        } catch (IOException ioe) {
            System.out.println("IO exception"+ ioe.getMessage());
        }

    }

    //prints all customers to console
    public void printCustomer() {
        System.out.println("Customers");
        for (int i = 0; i < CustArray.size(); i++) {
            System.out.println(CustArray.get(i));

        }
    }

    //prints all suppliers to console
    public void printSupplier() {
        System.out.println("Suppliers");
        for (int i = 0; i < SuppArray.size(); i++) {
            System.out.println(SuppArray.get(i));
        }
    }

    //closes file
    public void closeFile() {
        try {
            input.close();
        } catch (IOException ioe) {
            System.out.println("error closing ser file" + ioe.getMessage());
        }
    }

    //age calculation
    public void age() {
        age = new int[CustArray.size()];
        for (int i = 0; i < CustArray.size(); i++) {
            LocalDate lDate = LocalDate.parse(CustArray.get(i).getDateOfBirth());
            int year = lDate.getYear();
            age[i] = 2021 - year;
        }
    }

    //checks if a customer can rent
    public void canRent() {
        for (int i = 0; i < CustArray.size(); i++) {
            if (CustArray.get(i).getCanRent() == true) {
                CustomersCanRent++;
            } else if (CustArray.get(i).getCanRent() == false) {
                CustomersCannotRent++;
            }
        }
    }

    //sorting customer by ID
    public void sort() {
        CustArray.sort(Comparator.comparing(Stakeholder::getStHolderId));
    }
    
    //formatting the date of birth to dd mmm yyyy
    public void formatDOB() {
        try{
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); //current date format

        for (int i = 0; i < CustArray.size(); i++) {
            String DOB = CustArray.get(i).getDateOfBirth(); //getting the Date of birth of each customer
            Date formattedDate = format1.parse(DOB); //parsing the current date format to formattedDate
            DateFormat format2 = new SimpleDateFormat("dd MMM yyyy"); //desired date format
            CustArray.get(i).setDateOfBirth(format2.format(formattedDate)); //changing it to desired format
            }
        }
        catch(ParseException pe){
            System.out.println("parse exception" + pe.getMessage());
        }
    }

    //sorting supplier by Name
    public void sortSupplier() {
        SuppArray.sort(Comparator.comparing(Supplier::getName));
    }

    //creating customerFile
    public void CreateCustomerFile(){
        try {
            FileWriter fw = new FileWriter("Customer.txt"); //reading file
            BufferedWriter bw = new BufferedWriter(fw); //making read file to buffered writer
            bw.write("==================== CUSTOMERS ====================");
            bw.newLine();
            bw.write("ID\tName\tSurname\t\tDate of birth\tAge");
            bw.newLine();
            bw.write("===================================================");
            bw.newLine();
            for (int i = 0; i < CustArray.size(); i++) {
                //creating if statement to properly align content as tabbing wasnt correct with different char sizes
                //displaying content
                if (CustArray.get(i).getSurName().length() < 8) {
                    bw.write(CustArray.get(i).getStHolderId() + "\t" + CustArray.get(i).getFirstName() + "\t" + CustArray.get(i).getSurName() + "\t\t" + CustArray.get(i).getDateOfBirth() + "\t" + age[i] + "\t");
                    bw.newLine();
                } else {
                    bw.write(CustArray.get(i).getStHolderId() + "\t" + CustArray.get(i).getFirstName() + "\t" + CustArray.get(i).getSurName() + "\t" + CustArray.get(i).getDateOfBirth() + "\t" + age[i] + "\t");
                    bw.newLine();
                }
            }
            //displaying customers that can and cannot rent
            bw.newLine();
            bw.write("Number of customers that can rent:");
            bw.write(String.valueOf("\t" + CustomersCanRent)); //displays null when you write an int therfore i passed it to string
            bw.newLine();
            bw.write("Number of customers who cannot rent: ");
            bw.write(String.valueOf("\t" + CustomersCannotRent)); //displays null when you write an int therfore i passed it to string
            bw.close();
        } catch (EOFException ex) {
            System.out.println("EOF reached" + ex.getMessage());
        } catch (FileNotFoundException fnfe) {
            System.out.println("file not found" + fnfe.getMessage());
        } catch (IOException ioe) {
            System.out.println("IO exception" + ioe.getMessage());
        }
    }

    public void CreateSupplierFile() {
        try {
            //same format as customer
            FileWriter fw = new FileWriter("Supplier.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("=========================== Supplier ============================");
            bw.newLine();
            bw.write("ID\tName\t\t\tProd Type\tDescription");
            bw.newLine();
            bw.write("=================================================================");
            for (int i = 0; i < SuppArray.size(); i++) {
                if (SuppArray.get(i).getName().length() > 12) {
                    bw.newLine();
                    bw.write(SuppArray.get(i).getStHolderId() + "\t" + SuppArray.get(i).getName() + "\t" + SuppArray.get(i).getProductType() + "\t\t" + SuppArray.get(i).getProductDescription() + "\t");

                } else {
                    bw.newLine();
                    bw.write(SuppArray.get(i).getStHolderId() + "\t" + SuppArray.get(i).getName() + "\t\t" + SuppArray.get(i).getProductType() + "\t\t" + SuppArray.get(i).getProductDescription() + "\t");

                }
            }

            bw.close();
        } catch (EOFException ex) {
            System.out.println("EOF reached" + ex.getMessage());
        } catch (FileNotFoundException fnfe) {
            System.out.println("file not found" + fnfe.getMessage());
        } catch (IOException ioe) {
            System.out.println("IO exception" + ioe.getMessage());
        }

    }

    public static void main(String[] args)  {
        //calling all functions
        ReadFromFile obj1 = new ReadFromFile();

        obj1.openFile();
        obj1.readFile();
        obj1.sort();
        obj1.age();
        obj1.formatDOB();
        obj1.sortSupplier();

        obj1.printCustomer();
        obj1.printSupplier();
        obj1.canRent();

        obj1.closeFile();
        obj1.CreateCustomerFile();
        obj1.CreateSupplierFile();

    }
}
