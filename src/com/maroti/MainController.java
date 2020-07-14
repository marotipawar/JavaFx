package com.maroti;

import com.maroti.model.Reminder;
import com.maroti.model.ReminderData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;

public class MainController {

    @FXML
    private BorderPane mainControllerWindow;
    @FXML
    private ListView<Reminder> listView;
    @FXML
    private TextArea desc;
    @FXML
    private Label deadLine;
    @FXML
    private ContextMenu delContextMenu;

    private ObservableList<Reminder> reminderList;
    @FXML
    private MenuItem newItemMenu;

    @FXML
    private ToggleButton todayEvent;

    private FilteredList<Reminder> fr;

    @FXML
    public void initialize() throws IOException {
        delContextMenu = new ContextMenu();
        MenuItem deleteReminder = new MenuItem("Delete");

        deleteReminder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Reminder reminder=listView.getSelectionModel().getSelectedItem();

                deleteReminder(reminder);



            }
        });


        delContextMenu.getItems().setAll(deleteReminder);
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Reminder>() {
            @Override
            public void changed(ObservableValue<? extends Reminder> observableValue, Reminder reminder, Reminder t1) {
                if(t1!=null)
                {
                   Reminder rem=listView.getSelectionModel().getSelectedItem();
                   desc.setText(rem.getDesc());
                   deadLine.setText("Date : "+rem.getDate().toString());
                }
            }
        });
       //ReminderData.getInstance().setReminders(getReminderList());
            fr= new FilteredList<Reminder>(ReminderData.getInstance().getReminders(), new Predicate<Reminder>() {
            @Override
            public boolean test(Reminder reminder) {
                return true;
            }
        });

                SortedList < Reminder > sortedList = new SortedList<Reminder>(fr, new Comparator<Reminder>() {
                    @Override
                    public int compare(Reminder o1, Reminder o2) {

                        return o1.getDate().compareTo(o2.getDate());
                    }
                });
        
        listView.setItems(sortedList);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.getSelectionModel().selectFirst();

        listView.setCellFactory(new Callback<ListView<Reminder>, ListCell<Reminder>>() {
            @Override
            public ListCell<Reminder> call(ListView<Reminder> reminderListView) {
                ListCell<Reminder> cell = new ListCell<>(){
                    @Override
                    protected void updateItem(Reminder reminder, boolean b) {
                        super.updateItem(reminder, b);
                        if(b)
                        {
                            setText(null);
                        }else{
                            setText(reminder.getTitle());
                        }
                    }
                };

                cell.emptyProperty().addListener((obs, wasEmpty, isEmpty)->{
                    if(isEmpty)
                    {
                        cell.setContextMenu(null);

                    }else {
                            cell.setContextMenu(delContextMenu);
                    }

                });
                return cell;
            }
        });

        newItemMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addNewReminder();
            }
        });



    }


    public ObservableList<Reminder> getReminderList()
    {
        Reminder re1 = new Reminder("BirthDay", "Birthday Celebration", LocalDate.of(1990, Month.APRIL, 2));
        Reminder re2 = new Reminder("Anniversary", "Anniversary Celebration", LocalDate.of(1990, Month.APRIL, 5));
        Reminder re3 = new Reminder("Visit", "Visit to Google office", LocalDate.of(1990, Month.APRIL, 9));
        Reminder re4 = new Reminder("Meeting", "Meeting about Corona virus", LocalDate.of(1990, Month.APRIL, 14));
        Reminder re5 = new Reminder("Hospital", "Monthly Checkup", LocalDate.of(1990, Month.APRIL, 18));
        Reminder re6 = new Reminder("Loan", "Paid monthly loan", LocalDate.of(1990, Month.APRIL, 20));
        reminderList= FXCollections.observableArrayList();
        reminderList.add(re1);
        reminderList.add(re2);
        reminderList.add(re3);
        reminderList.add(re4);
        reminderList.add(re5);
        reminderList.add(re6);
        return reminderList;

    }
    @FXML
    public void addNewReminder()
    {
        Dialog<ButtonType> newAddDialog = new Dialog<ButtonType>();
        newAddDialog.initOwner(mainControllerWindow.getScene().getWindow());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addNewReminderDialog.fxml"));
        try{
            newAddDialog.getDialogPane().setContent(loader.load());
            System.out.println("------------");


        }catch(Exception e)
        {
            e.printStackTrace();
        }

        newAddDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        newAddDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result =newAddDialog.showAndWait();
        if(result.get().equals(ButtonType.OK))
        {
            AddNewReminderDialog ad=loader.getController();
            Reminder rd=ad.addReminder();
            listView.setItems(ReminderData.getInstance().getReminders());
            listView.getSelectionModel().select(rd);


        }else{
            System.out.println("CANCEl");
        }


    }
    @FXML
    public void handleKeyPressed(KeyEvent keyEvent)
    {
        Reminder reminder=listView.getSelectionModel().getSelectedItem();
        if(reminder!=null)
        {
            if(keyEvent.getCode().equals(KeyCode.DELETE))
            {
                deleteReminder(reminder);
            }
        }
    }

    @FXML
    public void deleteReminder(Reminder rem)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Reminder");
        alert.setContentText("Delete the Reminder");
        Optional<ButtonType> result=alert.showAndWait();
        if(result.isPresent() && result.get().equals(ButtonType.OK))
        {
            Iterator<Reminder> itr=ReminderData.getInstance().getReminders().iterator();
            while (itr.hasNext())
            {
                Reminder r = itr.next();
                if(r.equals(rem))
                {
                    itr.remove();
                    listView.setItems(ReminderData.getInstance().getReminders());

                }
            }
        }

    }
    @FXML
    public void handleDeleteReminder()
    {
        Reminder rrr=listView.getSelectionModel().getSelectedItem();
        deleteReminder(rrr);
    }

    @FXML
    public void handleTodayEvent()
    {
        if(todayEvent.isSelected())
        {
            fr.setPredicate(new Predicate<Reminder>() {
                @Override
                public boolean test(Reminder reminder) {
                    return reminder.getDate().equals(LocalDate.now());
                }
            });
        }else{
            fr.setPredicate(new Predicate<Reminder>() {
                @Override
                public boolean test(Reminder reminder) {
                    return true;
                }
            });

        }
    }
}
