import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.*;
import java.time.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.time.format.DateTimeFormatter;
import javax.swing.Timer;


interface SuperHome{
    void operate(String value);
    void timeUpdate(String startHour,String startMinute,String endHour,String endMinute);
}
class SmartHomeController{

    private SuperHome[] superHomeArray;
    int nextIndex;
    private int index;

    private String value;
    private String startHour;
    private String startMinute;
    private String endHour;
    private String endMinute;

    SmartHomeController(){
        superHomeArray= new SuperHome[100];
    }

    public void add(SuperHome superHome){
        System.out.println(nextIndex);
        superHomeArray[nextIndex++]=superHome;
    }

    public void setBtnValue(String value){
        if(!this.value.equals(value)){
            this.value=value;
            notifyObjects();
        }
    }
    public void notifyObjects(){
        for (int i = 0; i < nextIndex; i++){
            superHomeArray[i].operate(value);
        }
    }
    public void setIndex(int index){
        this.index=index;
        System.out.println(index);
    }

    public void setTime(String startHour,String startMinute,String endHour,String endMinute){
        if(this.startHour!=startHour ||this.startMinute!=startMinute||this.endHour!=endHour||this.endMinute!=endMinute){
            this.startHour=startHour;
            this.startMinute=startMinute;
            this.endHour=endHour;
            this.endMinute=endMinute;
            notifyTime();
        }
    }
    public void notifyTime(){
        superHomeArray[index].timeUpdate(startHour,startMinute,endHour,endMinute);
    }
}

class Switch extends JFrame{
    private JLabel lblTime;

    private JToggleButton btnOnOff;
    private JButton btnSettings;
    private JSpinner spnHour;
    private JSpinner spnMinutes;

    public boolean clickONOFF;
    public int number;

    SmartHomeController controlRoom=new SmartHomeController();

    public Switch(SmartHomeController smartHomeControl){
        setSize(300,150);
        setTitle("Switch");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //setLocationRelativeTo(null);
        setLocation(new Point(150, 350));
        setVisible(true);

        JPanel southPanel=new JPanel(new FlowLayout(FlowLayout.CENTER));

        clickONOFF= false;
        btnOnOff=new JToggleButton("OFF");
        btnOnOff.setFont(new Font("",1,15));

        ItemListener itemListener = new ItemListener() {

            public void itemStateChanged(ItemEvent itemEvent) {
                if (btnOnOff.isSelected()) {
                    btnOnOff.setText("OFF");
                    System.out.println("\nYou selected OFF");
                    smartHomeControl.setBtnValue(btnOnOff.getText());

                } else {
                    btnOnOff.setText("ON");
                    System.out.println("\nYou selected ON");
                    smartHomeControl.setBtnValue(btnOnOff.getText());
                }
            }
        };
        btnOnOff.addItemListener(itemListener);

        add("North",btnOnOff);

        btnSettings=new JButton("Settings");
        btnSettings.setFont(new Font("",1,15));
        btnSettings.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                new Controller(smartHomeControl).setVisible(true);
            }
        });
        add("Center",btnSettings);

        Timer timer = new Timer(1000, e -> updateLocalTime());
        timer.start();
        lblTime=new JLabel();
        lblTime.setFont(new Font("",1,15));
        southPanel.add("South",lblTime);
        add("South",southPanel);


    }
    private void updateLocalTime() {
        // Get the current local time
        LocalTime currentTime = LocalTime.now();

        // Format the time using a DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        String formattedTime = currentTime.format(formatter);

        // Update the JLabel text
        lblTime.setText("Time"+formattedTime);
    }

}

class Controller extends JFrame{

    private DefaultListModel model;

    private JList list;
    private TimeSettingWindow[] timeSettingWindowsArray;

    SmartHomeController smartHomeController;

    public Controller(SmartHomeController smartHomeController){
        this.smartHomeController=smartHomeController;
        setSize(400,250);
        setTitle("Controller");
        setDefaultCloseOperation(2);
        setLocation(100,80);

        JPanel panel = new JPanel(new GridLayout(4,0));

        model= new DefaultListModel();

        model.addElement("TV Dining Room");
        model.addElement("TV Living Room");

        model.addElement("Speaker Living Room");
        model.addElement("Window Living Room");


        list= new JList(model);

        add(list);

        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && !list.isSelectionEmpty()) {

                    String name=""+list.getSelectedValue();
                    System.out.println(name);
                    // if (name=="TV Living Room"){
                    //    new TimeSettingWindow(name,smartHomeController).setVisible(true);
                    //}else if (name=="TV Dining Room"){
                    //     new TimeSettingWindow(name,smartHomeController).setVisible(true);
                    // }else if (name=="Speaker Living Room"){
                    //    new TimeSettingWindow(name,smartHomeController).setVisible(true);
                    //}else if (name=="Window Living Room") {
                    //    new WindowLivingRoom(smartHomeController).setVisible(true);
                    new TimeSettingWindow(name,smartHomeController).setVisible(true);
                    //}

                    // new TimeSettingWindow(name,smartHomeController).setVisible(true);
                    //int index=list.getSelectedIndex();
                    smartHomeController.setIndex(list.getSelectedIndex());
                }
            }
        });
    }
}
class TimeSettingWindow extends JFrame {

    SmartHomeController smartHomeController;
    private TimeModel timeModel;
    private int nextIndex;
    private JLabel start;
    private JLabel minute;
    private JLabel hour;
    private JLabel minute1;
    private JLabel hour1;
    private JLabel end;

    private JSpinner spnStartHour;
    private JSpinner spnStartMinutes;
    private JSpinner spnEndHour;
    private JSpinner spnEndMinutes;

    private DefaultListModel<TimeModel> li;
    private JButton setOrEdit;
    private JList list;

    public TimeSettingWindow(String name,SmartHomeController smartHomeController){
        this.smartHomeController=smartHomeController;
        setSize(600,250);
        setTitle(name);
        setDefaultCloseOperation(1);
        setLocation(850,50);
        setVisible(true);

        JPanel southPanel = new JPanel(new FlowLayout());

        SpinnerModel valueStartHour = new SpinnerNumberModel(0,0,23,1);
        SpinnerModel valueStartMinute = new SpinnerNumberModel(0,0,59,1);
        SpinnerModel valueEndHour = new SpinnerNumberModel(0,0,23,1);
        SpinnerModel valueEndMinute = new SpinnerNumberModel(0,0,59,1);

        spnStartHour= new JSpinner(valueStartHour);
        spnStartMinutes= new JSpinner(valueStartMinute);
        spnEndHour= new JSpinner(valueEndHour);
        spnEndMinutes= new JSpinner(valueEndMinute);

        start=new JLabel("Start : ");
        start.setFont(new Font("",1,15));

        hour=new JLabel("Hour:");
        hour.setFont(new Font("",1,15));

        minute=new JLabel(" Minute:");
        minute.setFont(new Font("",1,15));

        hour1=new JLabel("Hour:");
        hour1.setFont(new Font("",1,15));

        minute1=new JLabel(" Minute:");
        minute1.setFont(new Font("",1,15));

        end=new JLabel("   End : ");
        end.setFont(new Font("",1,15));

        li= new DefaultListModel<>();
        list=new JList(li);
        list.getSelectionModel().addListSelectionListener(e->{
            if (!e.getValueIsAdjusting() && !list.isSelectionEmpty()) {
                setOrEdit.setText("Edit");
                timeModel= (TimeModel) list.getSelectedValue();

                valueStartHour.setValue(Integer.parseInt(timeModel.getStartHour()));
                valueStartMinute.setValue(Integer.parseInt(timeModel.getStartMinute()));
                valueEndHour.setValue(Integer.parseInt(timeModel.getEndHour()));
                valueEndMinute.setValue(Integer.parseInt(timeModel.getEndMinute()));

                setOrEdit.addActionListener(f -> {
                    int index = list.getSelectedIndex();
                    if (index >= 0) {

                        li.remove(li.getSize() - 1);
                        list.repaint();
                    }
                    setOrEdit.setText("Set");
                });

            }
        });

        setOrEdit = new JButton("Set");
        setOrEdit.setFont( new Font("",1,15));
        setOrEdit.addActionListener(evt -> {
            setOrEdit.setText("Set");

            String sHour=""+spnStartHour.getValue();
            String sMinuter=""+spnStartMinutes.getValue();
            String eHour=""+spnEndHour.getValue();
            String eMinute=""+spnEndMinutes.getValue();

            li.addElement(new TimeModel(sHour,sMinuter,eHour,eMinute));
            smartHomeController.setTime(sHour,sMinuter,eHour,eMinute);
        });



        southPanel.add(start);
        southPanel.add(hour);
        southPanel.add(spnStartHour);
        southPanel.add(minute);
        southPanel.add(spnStartMinutes);
        southPanel.add(end);
        southPanel.add(hour1);
        southPanel.add(spnEndHour);
        southPanel.add(minute1);
        southPanel.add(spnEndMinutes);
        southPanel.add(setOrEdit);
        add(list);

        add("South",southPanel);
    }
}

//class WindowLivingRoom extends TimeSettingWindow{
// SmartHomeController smartHomeController;
// public WindowLivingRoom(SmartHomeController smartHomeController){
//     this.smartHomeController=smartHomeController;
//    setSize(600,250);
//    setTitle("WindowLivingRoom");
//   setDefaultCloseOperation(1);
//   setLocation(850,250);
//   setVisible(true);
//}
//}

class TimeModel{

    private String startHour;
    private String startMinute;
    private String endHour;
    private String endMinute;

    public TimeModel(String startHour,String startMinute,String endHour,String endMinute){
        this.startHour=startHour;
        this.startMinute=startMinute;
        this.endHour=endHour;
        this.endMinute=endMinute;
    }

    public void setStartHour(){
        this.startHour=startHour;
    }
    public void setStartMinute(){
        this.startMinute=startMinute;
    }
    public void setEndHour(){
        this.endHour=endHour;
    }
    public void setEndMinute(){
        this.endMinute=endMinute;
    }
    public String getStartHour(){
        return startHour;
    }
    public String getStartMinute(){
        return startMinute;
    }
    public String getEndHour(){
        return endHour;
    }
    public String getEndMinute(){
        return endMinute;
    }

    @Override
    public String toString(){
        return "Starts at :  "+startHour+"."+startMinute+"   Ends at : "+endHour+"."+endMinute;
    }
}

class TVLiving extends JFrame implements SuperHome{

    private int startHour,startMinute,endHour,endMinute;
    private JLabel lblStartTime;
    private JLabel lblEndTime;
    private JLabel label;
    private JPanel panel;
    private Timer timer;

    public TVLiving(){
        setSize(300,100);
        setTitle("TVLiving");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocation(500,500);
        setVisible(true);

        panel=new JPanel();
        label=new JLabel("OFF");
        label.setFont(new Font("",1,20));
        panel.add(label);
        add("Center",panel);
    }

    @Override
    public void operate(String value){
        System.out.println(value);
        if(value.equals("ON")){
            System.out.println("Value passed as ON");
            label.setText("OFF");
        }else{
            label.setText("ON");
        }
    }

    @Override
    public void timeUpdate(String startHour,String startMinute,String endHour,String endMinute){
        this.startHour=Integer.parseInt(startHour);
        this.startMinute=Integer.parseInt(startMinute);
        this.endHour=Integer.parseInt(endHour);
        this.endMinute=Integer.parseInt(endMinute);

        lblStartTime=new JLabel();
        lblStartTime.setText(String.format("%02d:%02d:00",this.startHour,this.startMinute));
        lblEndTime=new JLabel();
        lblEndTime.setText(String.format("%02d:%02d:00",this.endHour,this.endMinute));

        System.out.println("Speaker"+startHour+startMinute+"."+endHour+endMinute);
        timer=new javax.swing.Timer(1000,e->{
            timeChecking();
        });timer.start();
    }

    public void timeChecking() {
        LocalTime currentTime = LocalTime.now();
        if (currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(lblStartTime.getText())) {
            label.setText("ON");
        } else if (currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(lblEndTime.getText())) {
            label.setText("OFF");
        }

    }
}
class TVDining extends JFrame implements SuperHome{
    private int startHour,startMinute,endHour,endMinute;
    private JLabel lblStartTime;
    private JLabel lblEndTime;
    private JLabel label;
    private JPanel panel;
    private Timer timer;

    public TVDining(){
        setSize(300,100);
        setTitle("TVDining");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocation(500,400);
        setVisible(true);

        panel=new JPanel();
        label=new JLabel("OFF");
        label.setFont(new Font("",1,20));
        panel.add(label);
        add("Center",panel);
    }

    @Override
    public void operate(String value){
        System.out.println(value);
        if(value.equals("ON")){
            System.out.println("Value passed as ON");
            label.setText("OFF");
        }else{
            label.setText("ON");
        }

    }

    @Override
    public void timeUpdate(String startHour,String startMinute,String endHour,String endMinute){
        this.startHour=Integer.parseInt(startHour);
        this.startMinute=Integer.parseInt(startMinute);
        this.endHour=Integer.parseInt(endHour);
        this.endMinute=Integer.parseInt(endMinute);

        lblStartTime=new JLabel();
        lblStartTime.setText(String.format("%02d:%02d:00",this.startHour,this.startMinute));
        lblEndTime=new JLabel();
        lblEndTime.setText(String.format("%02d:%02d:00",this.endHour,this.endMinute));

        System.out.println("Speaker"+startHour+startMinute+"."+endHour+endMinute);
        Timer timer=new javax.swing.Timer(1000,e->{
            timeChecking();
        });timer.start();
    }

    public void timeChecking() {
        LocalTime currentTime = LocalTime.now();
        if (currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(lblStartTime.getText())) {
            label.setText("ON");

        } else if (currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(lblEndTime.getText())) {
            label.setText("OFF");
        }

    }
}

class Window extends JFrame implements SuperHome{
    private int startHour,startMinute,endHour,endMinute;
    private JLabel lblStartTime;
    private JLabel lblEndTime;
    private JLabel label;
    private JPanel panel;
    private Timer timer;

    public Window(){
        setSize(300,100);
        setTitle("Window");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocation(500,600);
        setVisible(true);

        panel=new JPanel();
        label=new JLabel("OFF");
        label.setFont(new Font("",1,20));
        panel.add(label);
        add("Center",panel);
    }

    @Override
    public void operate(String value){
        System.out.println(value);
        if(value.equals("ON")){
            System.out.println("Value passed as ON");
            label.setText("OFF");
        }else{
            label.setText("ON");
        }

    }

    @Override
    public void timeUpdate(String startHour,String startMinute,String endHour,String endMinute){
        this.startHour=Integer.parseInt(startHour);
        this.startMinute=Integer.parseInt(startMinute);
        this.endHour=Integer.parseInt(endHour);
        this.endMinute=Integer.parseInt(endMinute);

        lblStartTime=new JLabel();
        lblStartTime.setText(String.format("%02d:%02d:00",this.startHour,this.startMinute));
        lblEndTime=new JLabel();
        lblEndTime.setText(String.format("%02d:%02d:00",this.endHour,this.endMinute));

        System.out.println("window"+startHour+startMinute+"."+endHour+endMinute);
        timer=new javax.swing.Timer(1000,e->{
            timeChecking();
        });timer.start();
    }

    public void timeChecking() {
        LocalTime currentTime = LocalTime.now();
        if (currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(lblStartTime.getText())) {
            label.setText("ON");

        } else if (currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(lblEndTime.getText())) {
            label.setText("OFF");
        }

    }
}

class Speaker extends JFrame implements SuperHome{
    private int startHour,startMinute,endHour,endMinute;
    private JLabel lblStartTime;
    private JLabel lblEndTime;
    private JLabel label;
    private JPanel panel;
    private Timer timer;

    public Speaker(){
        setSize(300,100);
        setTitle("Speaker");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocation(500,300);
        setVisible(true);

        panel=new JPanel();
        label=new JLabel("OFF");
        label.setFont(new Font("",1,20));
        panel.add(label);
        add("Center",panel);
    }

    @Override
    public void operate(String value){
        System.out.println(value);
        if(value.equals("ON")){
            System.out.println("Value passed as ON");
            label.setText("OFF");
        }else{
            label.setText("ON");
        }

    }

    @Override
    public void timeUpdate(String startHour,String startMinute,String endHour,String endMinute){
        this.startHour=Integer.parseInt(startHour);
        this.startMinute=Integer.parseInt(startMinute);
        this.endHour=Integer.parseInt(endHour);
        this.endMinute=Integer.parseInt(endMinute);

        lblStartTime=new JLabel();
        lblStartTime.setText(String.format("%02d:%02d:00",this.startHour,this.startMinute));
        lblEndTime=new JLabel();
        lblEndTime.setText(String.format("%02d:%02d:00",this.endHour,this.endMinute));

        System.out.println("Speaker"+startHour+startMinute+"."+endHour+endMinute);
        timer=new javax.swing.Timer(1000,e->{
            timeChecking();
        });
        timer.start();
    }

    public void timeChecking() {
        LocalTime currentTime = LocalTime.now();
        if (currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(lblStartTime.getText())) {
            label.setText("ON");

        } else if (currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")).equals(lblEndTime.getText())) {
            label.setText("OFF");
        }

    }
}


public class Main{
    public static void main(String args[]){
        SmartHomeController smartHomeControl= new SmartHomeController();

        smartHomeControl.add(new TVDining());
        smartHomeControl.add(new TVLiving());
        smartHomeControl.add(new Speaker());
        smartHomeControl.add(new Window());

        new Switch(smartHomeControl);

    }
}
