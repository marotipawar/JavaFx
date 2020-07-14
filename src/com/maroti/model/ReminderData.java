package com.maroti.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

public class ReminderData {

    private ObservableList<Reminder> reminders;
    private static ReminderData rd= new ReminderData();
    private String fileName="remindersData.txt";

    private ReminderData()
    {

    }

    public static ReminderData getInstance()
    {
        return rd;
    }

    public ObservableList<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(ObservableList<Reminder> reminders) {
        this.reminders = reminders;
    }


    public void addReminder(Reminder r)
    {
        getReminders().add(r);
    }

    public void loadData() throws IOException {
        reminders= FXCollections.observableArrayList();
        Path path = Paths.get(fileName);
        BufferedReader br=Files.newBufferedReader(path);
        String input;
        try {
            while((input=br.readLine())!=null)
            {
                String[] itemPeace = input.split("\t");
                String title=itemPeace[0];
                String desc= itemPeace[1];
                String deadLine=itemPeace[2];
                reminders.add(new Reminder(title,desc, LocalDate.parse(deadLine)));

            }
       }catch(IOException e){
            e.printStackTrace();
        }finally {
            if(br!=null)
            {
                br.close();
            }
        }
 }

 public void storeData()throws IOException
 {
     Path path = Paths.get(fileName);
     BufferedWriter bw =Files.newBufferedWriter(path);
     try{
         Iterator<Reminder> itr =reminders.iterator();
         while(itr.hasNext())
         {
             Reminder rem =itr.next();
             bw.write(String.format("%s\t%s\t%s", rem.getTitle(), rem.getDesc(), rem.getDate()));
             bw.newLine();
             bw.flush();
         }

     }catch (IOException e)
     {
         e.printStackTrace();
     }finally {
         if(bw!=null)
         {
             bw.close();

         }
     }

 }

}
