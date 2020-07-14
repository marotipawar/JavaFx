package com.maroti;

import com.maroti.model.Reminder;
import com.maroti.model.ReminderData;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class AddNewReminderDialog {
    @FXML
    private TextField title;
    @FXML
    private TextArea desc;
    @FXML
    private DatePicker deadLine;

    public Reminder addReminder()
    {
        String t = title.getText().trim();
        String d = desc.getText().trim();
        LocalDate date = deadLine.getValue();
        Reminder rre=new Reminder(t,d,date);
        ReminderData.getInstance().addReminder(rre);
        return rre;
    }


    public TextField getTitle() {
        return title;
    }

    public void setTitle(TextField title) {
        this.title = title;
    }

    public TextArea getDesc() {
        return desc;
    }

    public void setDesc(TextArea desc) {
        this.desc = desc;
    }

    public DatePicker getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(DatePicker deadLine) {
        this.deadLine = deadLine;
    }
}
