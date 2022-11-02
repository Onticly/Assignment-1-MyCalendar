/*
 *======================================================
 * CS141
 * Assignment 3: Calendar Part 3
 * author: Aaron Tripp
 *======================================================
 * Description: This program gives the user a menu with 
 * options to choose from. It should display a calendar 
 * of an entered date, todays date, as well as the 
 * previous and next months date. While giving the option
 * to continue using the menu or quitting. Gives the 
 * option to print a month to a file as well as displays
 * events that are read from a specific file.
 *======================================================
 * Comments: I wasn't really counting the hours but it 
 * had to of been around 14 hours of trial and error and
 * I still have yet to fully complete it.
 *======================================================
 */

import java.util.*;
import java.io.*;

public class MyCalendar {

   public static final int SIZE = 15;
   public static char LINE = '|';
   public static int dayVal = -1;
   public static String[][] eventArray;
   
   // Main method which gets and input from the user then displays the dates 
   // through a string as well as calls on drawMonth() to get the calendar body. 
   public static void main(String[] args)throws FileNotFoundException {
      Scanner scan = new Scanner (System.in);
      int day = 0;
      int month = 0;
      int width = SIZE * 7;
      boolean boolCal = true;
      System.out.println("Welcome to my calendar program!");
      eventFile("calendarEvents.txt");
      
      while (boolCal == true) {
         System.out.println("Please type a command");
         System.out.println(("    \"e\" to enter a date and display ") + 
         ("the corresponding calendar"));
         System.out.println(("    \"t\" to get todays date and display ") + 
         ("the today's calendar"));
         System.out.println("    \"n\" to display the next month");
         System.out.println("    \"p\" to display the previous month");
         System.out.println("    \"ev\" to plan an event");
         System.out.println("    \"fp\" to print a month to a file");
         System.out.println("    \"q\" to quit the program");
         
         String input = scan.nextLine();
         if (input.equals("e")) {
            System.out.print("What date would you like to look at (\"MM/DD\")? ");
            String date = scan.nextLine();
            dayVal = day = dayFromDate(date);
            month = monthFromDate(date);
            drawCalendar(month, day, System.out);
         }else if (input.equals("t")) {
            Calendar acal = Calendar.getInstance();
            dayVal = day = acal.get(Calendar.DATE);
            month = acal.get(Calendar.MONTH) + 1;
            drawCalendar(month, day, System.out);
         }else if (input.equals("n")) {
            if(month == 0) {
               System.out.println(("Please use a command to display ") + 
               ("a calendar first"));
               System.out.println();
            } else {
               if(month == 12) {
                  month = 1;
               }else {
                  month++;
               }
               drawCalendar(month, day, System.out);
            }
         }else if (input.equals("p")) {
            if(month == 0) {
               System.out.println(("Please use a command to display ") + 
               ("a calendar first"));
               System.out.println();
            } else {
               if (month == 1) {
                  month = 12;
               }else {
                  month--;
               }
               drawCalendar(month, day, System.out);
            }
         }else if (input.equals("ev")) {
            System.out.print("Enter an event (\"MM/DD event_title\"): ");
            String date = scan.next();
            String event = scan.nextLine();
            int d = dayFromDate(date);
            int m = monthFromDate(date);
            eventArray[m-1][d-1] = event;  
         }else if (input.equals("fp")) {
            Scanner s = new Scanner(System.in);
            System.out.print("\nEnter a month to print 1-12: ");
            month = s.nextInt();
            System.out.print("File name: ");
            String filename = s.next();
            PrintStream outFile = new PrintStream(new File(filename));
            drawCalendar(month, day, outFile);
            outFile.close();
         }else if (input.equals("q")) {
            boolCal = false;
         }else {
            System.out.println("Please enter a valid command.");
         }
      }
   }
   
   // Makes sure the default event file exists
   public static void eventFile(String filename)
   throws FileNotFoundException {
      eventArray = new String[12][];
      for(int i = 0; i < 12; i++)
         eventArray[i] = new String[maxDays(i)];
      Scanner input = new Scanner(new File(filename));
      while(input.hasNext()) {
         String date = input.next();
         String event = input.nextLine();
         int day = dayFromDate(date);
         int month = monthFromDate(date);
         eventArray[month-1][day-1] = event;
      }
      input.close();
   }
   
   // Displays the calender using the drawMonth and drawRow methods
   public static void drawCalendar(int month, int day, PrintStream out) {
      int startDay = startDay(month);
      int maxDays = maxDays(month);
      drawMonth(month, startDay, maxDays, out);
      displayDate(month, day, out);
   }
   
   // Just a method to get rid of redundancy
   // was goin to use this to get the weekday names centered for
   // each cell but couldn't get it exactly right for the scaling
   /*public static void blankSpace(int width, PrintStream out) {
      for (int blank = 0; blank < (width / 7) - 10; blank++) {
         out.print(" ");
      }
   }*/
   
   // Takes in an integer representing the month and displays the month
   // and a graphical representation of the calendar.
   public static void drawMonth(int month, int skipDays, int maxDays, PrintStream out) {
      int width = SIZE * 7;
      Ascii(out);
      for (int blank = 1; blank <= (width / 2); blank++) {
         out.print(" ");
      }
      out.println(month);
      for (int i = 1; i <= 7; i++) {
         for (int blank = 0; blank < (width / 7) - 3; blank++) {
            out.print(" ");
         }
         if (i == 1) {
            out.print("SUN");
         }else if (i == 2) {
            out.print("MON");
         }else if (i == 3) {
            out.print("TUE");
         }else if (i == 4) {
            out.print("WED");
         }else if (i == 5) {
            out.print("THU");
         }else if (i == 6) {
            out.print("FRI");
         }else if (i == 7) {
            out.print("SAT");
         }
      }
      out.println();
      
      int start = 1;
      while (start <= maxDays) {
         if(start == 1) {
            drawRow(month, maxDays, start, skipDays, out);
            start += 7 - skipDays;
         }else {
            drawRow(month, maxDays, start, 0, out);
            start += 7;
         }
      }
      for (int equal = 0; equal < width + 1; equal++) {
         out.print("=");
      }
      out.println();
   }

   // Dsiplays the rows and numbers of the calendar.
   public static void drawRow(int month, int maxMonthDays, int start, int skip, PrintStream out) {
      String string = "";
      int width = SIZE * 7;
      for (int equal = 0; equal < width + 1; equal++) {
         out.print("=");
      }
      out.println();
      
      int day = start;
      String blank = " ";
      out.print("|");
      for (int i = 1; i <= 7; i++) {
         string = "";
         if (i > skip && day <= maxMonthDays) {
            if(dayVal == day) {
               string += day++ + "**";
            }else {
               string += day++;
            }
         }
         while (string.length() < SIZE - 1)
         string += blank;
         string += LINE;
         out.print(string);
         
      }
      int height = SIZE / 2;
      day = start;
      for (int h = 2 ; h <= height; h++) {
         out.print("\n|");
         for (int i = 1; i <= 7; i++, day++) {
            string = "";
            if(h == 2 && i > skip && day <= maxMonthDays && eventArray[month-1][day-1] != null)
               string = eventArray[month-1][day-1];
            while (string.length() < SIZE - 1) 
               string += blank;
               string += LINE;
               out.print(string);
         }
      }
      out.print("\n");
   }
   
   // Gets the starting day of a month
   public static int startDay(int month) {
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.DAY_OF_MONTH, 1);
      cal.set(Calendar.MONTH, month - 1);
      return cal.get(Calendar.DAY_OF_WEEK) - 1;
   }
   
   // Gets the max number of days in a month
   public static int maxDays(int month) {
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.MONTH, month - 1);
      return cal.getActualMaximum(Calendar.DATE);
   }
   
   // Passes the month and the day as integer values and displays the date.
   public static void displayDate(int month, int day, PrintStream out) {
      out.println("Month: " + month);
      out.println("Day: " + day);
   }
   
   // Extracts an integer value for the month and returns it when passed 
   // a given date as a String.
   public static int monthFromDate(String date) {
      int slash = date.indexOf("/");
      String day = date.substring(0, slash);
      return Integer.parseInt(day);
   }
   
   // Extracts an integer value for the day and returns it when passed
   // a given date as a String.
   public static int dayFromDate(String date) {
      int slash = date.indexOf("/");
      String day = date.substring(slash + 1);
      return Integer.parseInt(day);
   }
   
   // Creates and displays some acsii art above the calender.
   public static void Ascii(PrintStream out) {
      for (int blank = 1; blank <= 7; blank++) {
         out.print(" ");
      }
      out.print("___");
      out.println();
      for (int blank = 1; blank <= 6; blank++) {
         out.print(" ");
      }
      out.print("|***|");
      out.println();
      for (int rep = 1; rep <= 2; rep++) {
         for (int blank = 1; blank <= 7; blank++) {
            out.print(" ");
         }
         out.print("|_|");
         out.println();
      }
      out.println("    ___|_|___");
      out.print("   <");
      for (int guard = 1; guard <= 4; guard++) {
         out.print("_+");
      }
      out.println("_>");
      for (int rep = 1; rep <= 4; rep++) {
         for (int blank = 1; blank <= 6; blank++) {
            out.print(" ");
         }
         out.print("| | |");
         out.println();
      }
      out.println("     _|_|_|_");
      out.println("   _/       \\_");
      out.println(" _/           \\_");
      out.print("|");
      for (int blank = 1; blank <= 15; blank++) {
            out.print(" ");
      }
      out.println("|");
   }
}