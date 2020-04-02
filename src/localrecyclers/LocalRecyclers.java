package localrecyclers;

// With Sample Modularisation
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.SpringLayout;
// ActionListener Imports 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// SQLite Imports
import java.sql.SQLException;
import java.sql.ResultSet;
import static javax.swing.JOptionPane.showMessageDialog;
// Import (include) the java library for managing array sorting 
import java.util.Arrays;

/**
 * @author Harrison Howard
 */
public class LocalRecyclers extends Frame implements ActionListener, WindowListener {

    // Create new SQLite
    SQLite sql = new SQLite();

    int maxEntries = 1000000;
    int numberOfEntries = 0;
    int currentEntry = 1;

    Label lblTitle, lblBusnName, lblAddress, lblPhone, lblWebsite, lblRecycles, lblRecyclersList, lblFind;
    TextField txtBusnName, txtAddress, txtPhone, txtWebsite, txtFind, txtFilter;
    TextArea txtaraRecycles, txtaraRecyclersList;
    Button btnFind, btnExit, btnFirst, btnPrev, btnNext, btnLast, btnSortBusnName, btnSearchBusnName, btnFilterBy;

    MenuBar myMenuBar;
    Menu menuFile, menuEdit;
    MenuItem itemNew, itemSave, itemDelete, itemClear, itemRestore;

    String[] sortArray = new String[maxEntries];

    public static void main(String[] args) {
        try {
            // Establish a connection
            new LocalRecyclers().sql.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        Frame myFrame = new LocalRecyclers();
        myFrame.setSize(530, 540);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        myFrame.setLocation(dim.width / 2 - myFrame.getSize().width / 2, dim.height / 2 - myFrame.getSize().height / 2);
        myFrame.setResizable(false);
        myFrame.setVisible(true);
    }

    public LocalRecyclers() {
        setTitle("Local Recyclers");
        //setBackground(Color.yellow);

        SpringLayout myLayout = new SpringLayout();
        setLayout(myLayout);

        // Call the methods below to instantiate and place the various screen components
        LocateLabels(myLayout);
        LocateTextFields(myLayout);
        LocateTextArea(myLayout);
        LocateButtons(myLayout);

        // Create menu bar and all components
        LocateMenuBar();
        LocateMenu();
        LocateMenuItem();
        setMenuBar(myMenuBar);

        this.addWindowListener(this);
    }

    public void LocateMenuBar() {
        myMenuBar = new MenuBar();
    }

    public void LocateMenu() {
        menuFile = LocateAMenu(myMenuBar, menuFile, "File");
        menuEdit = LocateAMenu(myMenuBar, menuEdit, "Edit");
    }

    public void LocateMenuItem() {
        // MENU FILE ITEMS
        itemNew = LocateAMenuItem(menuFile, itemNew, "New Record");
        itemSave = LocateAMenuItem(menuFile, itemSave, "Save Record");
        itemDelete = LocateAMenuItem(menuFile, itemDelete, "Delete Record");

        // MENU EDIT ITEMS
        itemClear = LocateAMenuItem(menuEdit, itemClear, "Clear All");
        itemRestore = LocateAMenuItem(menuEdit, itemRestore, "Restore Entry");
    }

    public Menu LocateAMenu(MenuBar menuBar, Menu menu, String menuName) {
        menu = new Menu(menuName);
        menuBar.add(menu);
        return menu;
    }

    public MenuItem LocateAMenuItem(Menu menu, MenuItem menuItem, String menuItemName) {
        menuItem = new MenuItem(menuItemName);
        menu.add(menuItem);
        menuItem.addActionListener(this);
        return menuItem;
    }

    //------------------------------------------------------------------------------------------
    // Method that manages the adding of multiple Labels to the screen.
    // Each line requests the LocateALabel method to instantiate, add and place a specific Label  
    public void LocateLabels(SpringLayout myLabelLayout) {
        lblTitle = LocateALabel(myLabelLayout, lblTitle, "Local Recycler Contacts", 40, 15, false, new Dimension(0, 0));
        lblBusnName = LocateALabel(myLabelLayout, lblBusnName, "Business Name:", 40, 50, true, new Dimension(105, 20));
        lblAddress = LocateALabel(myLabelLayout, lblAddress, "Address:", 40, 78, true, new Dimension(105, 20));
        lblPhone = LocateALabel(myLabelLayout, lblPhone, "Phone:", 40, 106, true, new Dimension(105, 20));
        lblWebsite = LocateALabel(myLabelLayout, lblWebsite, "Website:", 40, 134, true, new Dimension(105, 20));
        lblRecycles = LocateALabel(myLabelLayout, lblRecycles, "Recycles:", 40, 162, true, new Dimension(105, 20));
        lblRecyclersList = LocateALabel(myLabelLayout, lblRecyclersList, "Recyclers List:", 40, 280, true, new Dimension(438, 20));
        lblFind = LocateALabel(myLabelLayout, lblFind, "Find: ", 350, 25, true, new Dimension(42, 20));
    }

    /**
     * Method with low coupling and high cohesion for adding individual labels:
     * - reduces overall code, especially in the LocateLabels method. - makes
     * this method re-usable with minimal adjustment as it is moved from one
     * program to another.
     */
    public Label LocateALabel(SpringLayout myLabelLayout, Label myLabel, String LabelCaption, int x, int y, boolean background, Dimension dim) {
        myLabel = new Label(LabelCaption);
        if (background == true) {
            myLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 11));
            myLabel.setBackground(new Color(0, 105, 93));
            myLabel.setForeground(Color.white);
            myLabel.setPreferredSize(dim);
        } else {
            myLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
            myLabel.setForeground(new Color(3, 115, 39));
        }
        add(myLabel);
        myLabelLayout.putConstraint(SpringLayout.WEST, myLabel, x, SpringLayout.WEST, this);
        myLabelLayout.putConstraint(SpringLayout.NORTH, myLabel, y, SpringLayout.NORTH, this);
        return myLabel;
    }

    //------------------------------------------------------------------------------------------
    // Method that manages the adding of multiple TextFields to the screen.
    // Each line requests the LocateATextField method to instantiate, add and place a specific TextField  
    public void LocateTextFields(SpringLayout myTextFieldLayout) {
        txtBusnName = LocateATextField(myTextFieldLayout, txtBusnName, 20, 150, 50);
        txtAddress = LocateATextField(myTextFieldLayout, txtAddress, 20, 150, 78);
        txtPhone = LocateATextField(myTextFieldLayout, txtPhone, 20, 150, 106);
        txtWebsite = LocateATextField(myTextFieldLayout, txtWebsite, 20, 150, 134);
        txtFind = LocateATextField(myTextFieldLayout, txtFind, 10, 397, 25);

        txtFilter = LocateATextField(myTextFieldLayout, txtFilter, 6, 415, 250);
    }

    /**
     * Method with low coupling and high cohesion for adding individual text
     * boxes: - reduces overall code, especially in the LocateTextFields method.
     * - makes this method re-usable with minimal adjustment as it is moved from
     * one program to another.
     */
    public TextField LocateATextField(SpringLayout myTextFieldLayout, TextField myTextField, int width, int x, int y) {
        myTextField = new TextField(width);
        add(myTextField);
        myTextFieldLayout.putConstraint(SpringLayout.WEST, myTextField, x, SpringLayout.WEST, this);
        myTextFieldLayout.putConstraint(SpringLayout.NORTH, myTextField, y, SpringLayout.NORTH, this);
        return myTextField;
    }

    public void LocateTextArea(SpringLayout myTextAreaLayout) {
        txtaraRecycles = LocateATextArea(myTextAreaLayout, txtaraRecycles, 20, 2, 150, 162, true);
        txtaraRecyclersList = LocateATextArea(myTextAreaLayout, txtaraRecyclersList, 60, 9, 40, 310, false);
    }

    public TextArea LocateATextArea(SpringLayout myTextAreaLayout, TextArea myTextArea, int width, int height, int x, int y, boolean edit) {
        myTextArea = new TextArea("", height, width, myTextArea.SCROLLBARS_NONE);
        myTextArea.setEditable(edit);
        add(myTextArea);
        myTextAreaLayout.putConstraint(SpringLayout.WEST, myTextArea, x, SpringLayout.WEST, this);
        myTextAreaLayout.putConstraint(SpringLayout.NORTH, myTextArea, y, SpringLayout.NORTH, this);
        return myTextArea;
    }

    //------------------------------------------------------------------------------------------
    // Method that manages the adding of multiple Buttons to the screen.
    // Each line requests the LocateAButton method to instantiate, add and place a specific Button  
    public void LocateButtons(SpringLayout myButtonLayout) {
        btnFind = LocateAButton(myButtonLayout, btnFind, "Find", 350, 50, 140, 25);

        btnExit = LocateAButton(myButtonLayout, btnExit, "Exit", 350, 210, 140, 25);
        btnFirst = LocateAButton(myButtonLayout, btnFirst, "|<", 350, 165, 30, 25);
        btnPrev = LocateAButton(myButtonLayout, btnPrev, "<", 387, 165, 30, 25);
        btnNext = LocateAButton(myButtonLayout, btnNext, ">", 423, 165, 30, 25);
        btnLast = LocateAButton(myButtonLayout, btnLast, ">|", 460, 165, 30, 25);

        btnSortBusnName = LocateAButton(myButtonLayout, btnSortBusnName, "Sort by Busn Name", 40, 250, 120, 25);
        btnSearchBusnName = LocateAButton(myButtonLayout, btnSearchBusnName, "Binary Search by Busn Name", 165, 250, 180, 25);
        btnFilterBy = LocateAButton(myButtonLayout, btnFilterBy, "Filter By", 350, 250, 60, 25);
    }

    /**
     * Method with low coupling and high cohesion for adding individual buttons:
     * - reduces overall code, especially in the LocateButtons method. - makes
     * this method re-usable with minimal adjustment as it is moved from one
     * program to another.
     */
    public Button LocateAButton(SpringLayout myButtonLayout, Button myButton, String ButtonCaption, int x, int y, int w, int h) {
        myButton = new Button(ButtonCaption);
        add(myButton);
        myButton.addActionListener(this);
        myButtonLayout.putConstraint(SpringLayout.WEST, myButton, x, SpringLayout.WEST, this);
        myButtonLayout.putConstraint(SpringLayout.NORTH, myButton, y, SpringLayout.NORTH, this);
        myButton.setPreferredSize(new Dimension(w, h));
        return myButton;
    }

    public void actionPerformed(ActionEvent e) {
        // BUTTON FIRST
        if (e.getSource() == btnFirst) {
            // The currentEntry variable is used to define which record will be displayed
            //     on screen.
            // In this instance, set the currentEntry to 0 (ie: the index of the first entry)
            currentEntry = 1;

            // The displayEntry method will display the currentEntry on the screen
            // In this instance, display the first entry (currentEntry = 1) on the screen.   
            displayEntry(currentEntry);
        }

        // BUTTON PREVIOUS
        if (e.getSource() == btnPrev) {
            // Only go to the previous record if we have a previous entry in the array...
            if (currentEntry > 1) {
                // Reduce the value of currentEntry by 1
                currentEntry--;
                // Display the current entry
                displayEntry(currentEntry);
            }
        }

        // BUTTON NEXT
        if (e.getSource() == btnNext) {
            // Only go the next record if we have a next existing entry in the array...    
            // NOTE: the use of numberOfEntries as opposed to maxEntries.
            if (currentEntry < numberOfEntries) {
                // Increase the value of currentEntry by 1
                currentEntry++;
                // Display the current entry
                displayEntry(currentEntry);
            }
        }

        // BUTTON LAST
        if (e.getSource() == btnLast) {
            currentEntry = numberOfEntries;
            displayEntry(currentEntry);
        }

        // ITEM NEW
        if (e.getSource() == itemNew) {
            // Only if the array is large enough to store another record...
            if (numberOfEntries < 1) {
                numberOfEntries++;
                txtBusnName.setEditable(true);
                txtAddress.setEditable(true);
                txtPhone.setEditable(true);
                txtWebsite.setEditable(true);
                txtaraRecycles.setEditable(true);
            } else if (numberOfEntries > 0 && (txtBusnName.getText().length() < 1
                    && txtAddress.getText().length() < 1
                    && txtPhone.getText().length() < 1
                    && txtWebsite.getText().length() < 1
                    && txtaraRecycles.getText().length() < 1)) {
                showMessageDialog(null, "New field is still empty. Can not create another");
            } else if (numberOfEntries < maxEntries - 1) {
                // Increment the numberOfEntries
                numberOfEntries++;
                // Set the current entry to the new record
                currentEntry = numberOfEntries;
                // Blank out any existing data in the 3 arrays, ready
                // for adding the new record.
                try {
                    sql.setBusnInfo(currentEntry, "", "", "", "", "");
                } catch (ClassNotFoundException | SQLException err) {
                    err.printStackTrace();
                }
                // Display this new blank entry on screen
                displayEntry(currentEntry);
            }
        }

        // ITEM SAVE
        if (e.getSource() == itemSave) {
            saveEntry(currentEntry);
            displayEntry(currentEntry);
        }

        // ITEM DELETE
        if (e.getSource() == itemDelete) {
            if (numberOfEntries > 0) {
                // Remove entry
                try {
                    sql.deleteEntry(currentEntry);
                } catch (ClassNotFoundException | SQLException err) {
                    err.printStackTrace();
                }
                // Reduce the current total number of entries stored in the array by one.
                // Then check if the current entry is now further down the array than
                //      the last entry.  If so, reduce the value of currentEntry by 1.
                numberOfEntries--;
                if (currentEntry > numberOfEntries) {
                    currentEntry = numberOfEntries;
                }
                // Display the currentEntry
                displayEntry(currentEntry);

                if (numberOfEntries < 1) {
                    txtBusnName.setEditable(false);
                    txtBusnName.setText(" ");
                    txtBusnName.setText("");
                    txtAddress.setEditable(false);
                    txtAddress.setText(" ");
                    txtAddress.setText("");
                    txtPhone.setEditable(false);
                    txtPhone.setText(" ");
                    txtPhone.setText("");
                    txtWebsite.setEditable(false);
                    txtWebsite.setText(" ");
                    txtWebsite.setText("");
                    txtaraRecycles.setEditable(false);
                    txtaraRecycles.setText(" ");
                    txtaraRecycles.setText("");
                }
            } else {
                showMessageDialog(null, "There is nothing to delete");
            }
        }

        // BUTTON FIND
        if (e.getSource() == btnFind) {
            if (numberOfEntries > 0) {
                try {
                    // Declare a boolean valuable: found (to remember whether
                    //         the required entry has been found yet.)
                    boolean found = false;
                    // Declare a counter (i)
                    int i = 1;
                    // While there are more entries to check and the 
                    // 'search' entry has not been found... 
                    while (i <= numberOfEntries && found == false) {
                        // If the current PCName is equal to the 'search' entry...
                        ResultSet result = sql.getBusnName(i);
                        if (result.next()) {
                            if (result.getString("busnname").toLowerCase()
                                    .indexOf(txtFind.getText().toLowerCase()) > -1) {
                                // Set found = true
                                found = true;
                            }
                        }
                        // Increment the counter (i)
                        // so the loop will move onto the next record
                        i++;
                    }
                    // If the entry was found
                    // then set the value of currentEntry and then display the entry.
                    if (found) {
                        currentEntry = i - 1;
                        displayEntry(currentEntry);
                    }
                } catch (ClassNotFoundException | SQLException err) {
                    err.printStackTrace();
                }
            } else {
                txtFind.setText(" ");
                txtFind.setText("");
                showMessageDialog(null, "There is nothing to find");
            }
        }

        // BUTTON SORT
        if (e.getSource() == btnSortBusnName || e.getSource() == btnSearchBusnName) {
            if (numberOfEntries > 0) {
                // Update sort array
                if (sortArray[0] == null) {
                    try {
                        for (int i = 1; i <= numberOfEntries; i++) {
                            ResultSet result = sql.getBusnName(i);
                            if (result.next()) {
                                String busnname = result.getString("busnname");
                                if (busnname.length() < 1) {
                                    busnname = "null";
                                }
                                sortArray[i - 1] = busnname;
                            }
                        }
                    } catch (ClassNotFoundException | SQLException err) {
                        err.printStackTrace();
                    }
                    Arrays.sort(sortArray, 0, numberOfEntries);
                }

                txtaraRecyclersList.setText("Sorted Business Names:\n");
                txtaraRecyclersList.append("--------------------------\n");
                for (int i = 0; i < numberOfEntries; i++) {
                    txtaraRecyclersList.append(sortArray[i] + "\n");
                }
                txtaraRecyclersList.setCaretPosition(0);
            } else {
                txtaraRecyclersList.setText("Sorted Business Names:\n");
                txtaraRecyclersList.append("--------------------------\n");
            }
        }

        // BUTTON BINARY SEARCH
        if (e.getSource() == btnSearchBusnName) {
            if (numberOfEntries > 0) {
                int result = Arrays.binarySearch(sortArray, 0, numberOfEntries, txtFilter.getText());
                // Note:  \n - go to a new line in the TextArea
                txtaraRecyclersList.append("\nBinary Search result = " + result);
                txtaraRecyclersList.setCaretPosition(0);
            } else {
                txtaraRecyclersList.append("\nBinary Search result = -1");
            }
        }

        // BUTTON FILTER
        if (e.getSource() == btnFilterBy) {
            txtaraRecyclersList.setText(txtFilter.getText() + " \n");
            txtaraRecyclersList.append("---------------------------"
                    + "------------------------------ \n");
            try {
                switch (txtFilter.getText().toLowerCase()) {
                    case "busn name":
                        for (int i = 1; i <= numberOfEntries; i++) {
                            ResultSet result = sql.getBusnName(i);
                            if (result.next()) {
                                txtaraRecyclersList.append(result.getString("busnname")
                                        + "\n\n");
                            }
                        }
                        break;
                    case "address":
                        for (int i = 1; i <= numberOfEntries; i++) {
                            ResultSet result = sql.getAddress(i);
                            if (result.next()) {
                                txtaraRecyclersList.append(result.getString("address")
                                        + "\n\n");
                            }
                        }
                        break;
                    case "phone":
                        for (int i = 1; i <= numberOfEntries; i++) {
                            ResultSet result = sql.getPhone(i);
                            if (result.next()) {
                                txtaraRecyclersList.append(result.getString("phone")
                                        + "\n\n");
                            }
                        }
                        break;
                    case "website":
                        for (int i = 1; i <= numberOfEntries; i++) {
                            ResultSet result = sql.getWebsite(i);
                            if (result.next()) {
                                txtaraRecyclersList.append(result.getString("website")
                                        + "\n\n");
                            }
                        }
                        break;
                    case "recycles":
                        for (int i = 1; i <= numberOfEntries; i++) {
                            ResultSet result = sql.getRecycles(i);
                            if (result.next()) {
                                txtaraRecyclersList.append(result.getString("recycles")
                                        + "\n\n");
                            }
                        }
                        break;
                    default:
                        txtaraRecyclersList.setText("Busn Name; Address; Phone; Website; Recycles \n");
                        if (txtFilter.getText().length() > 0) {
                            txtaraRecyclersList.append("Text detected in Filter by. Filter by is invalid. Use names above ^\n");
                        }
                        // Use .append to add a line under the headings
                        txtaraRecyclersList.append("---------------------------"
                                + "------------------------------ \n");
                        // Loop through the various records and add each one 
                        // to a new line within the TextArea
                        ResultSet result = sql.getBusnInfo();
                        while (result.next()) {
                            txtaraRecyclersList.append(result.getString("busnname")
                                    + "; " + result.getString("address")
                                    + "; " + result.getString("phone")
                                    + "; " + result.getString("website")
                                    + "; " + result.getString("recycles")
                                    + "\n\n");
                        }
                        break;
                }
            } catch (ClassNotFoundException | SQLException err) {
                err.printStackTrace();
            }
            txtaraRecyclersList.setCaretPosition(0);
        }

        // ITEM CLEAR
        if (e.getSource() == itemClear) {
            txtBusnName.setText(" ");
            txtBusnName.setText("");
            txtAddress.setText(" ");
            txtAddress.setText("");
            txtPhone.setText(" ");
            txtPhone.setText("");
            txtWebsite.setText(" ");
            txtWebsite.setText("");
            txtaraRecycles.setText(" ");
            txtaraRecycles.setText("");
        }

        // ITEM RESTORE
        if (e.getSource() == itemRestore) {
            displayEntry(currentEntry);
        }

        // BUTTON EXIT
        if (e.getSource() == btnExit) {
            // Call the method below that writes the data back to the data file
            checkEmptyEntries();
            System.exit(0);
        }
    }
    // Manage responses to the various Window events

    public void windowClosing(WindowEvent we) {
        // Call the method below that writes the data back to the data file
        checkEmptyEntries();
        System.exit(0);
    }

    public void windowIconified(WindowEvent we) {
    }

    public void windowOpened(WindowEvent we) {
        // Call the method below that reads the data from the data file (when the Frame first opens)
        setNumberOfEntries();
        // Display the first data entry (record) in the Frame
        displayEntry(currentEntry);
    }

    public void windowClosed(WindowEvent we) {
    }

    public void windowDeiconified(WindowEvent we) {
    }

    public void windowActivated(WindowEvent we) {
    }

    public void windowDeactivated(WindowEvent we) {
    }

    public void displayEntry(int index) {
        if (numberOfEntries > 0) {
            try {
                // Get first BusnInfo
                ResultSet result = sql.getBusnInfo(index);
                // Get first result and set it
                if (result.next()) {
                    txtBusnName.setText(result.getString("busnname"));
                    txtAddress.setText(result.getString("address"));
                    txtPhone.setText(result.getString("phone"));
                    txtWebsite.setText(result.getString("website"));
                    txtaraRecycles.setText(result.getString("recycles"));
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveEntry(int index) {
        if (numberOfEntries > 0 && (txtBusnName.getText().length() < 1
                && txtAddress.getText().length() < 1
                && txtPhone.getText().length() < 1
                && txtWebsite.getText().length() < 1
                && txtaraRecycles.getText().length() < 1)) {
            showMessageDialog(null, "One or all of the fields are empty, can not save");
        } else if (numberOfEntries > 0) {
            try {
                sql.setBusnInfo(index,
                        txtBusnName.getText(),
                        txtAddress.getText(),
                        txtPhone.getText(),
                        txtWebsite.getText(),
                        txtaraRecycles.getText());
                checkEmptyEntries();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        } else {
            showMessageDialog(null, "There is nothing to save");
        }
    }

    public void setNumberOfEntries() {
        try {
            // Get all BusnInfo
            ResultSet result = sql.getBusnInfo();
            // Loop through result TO GET CURRENT ENTRIES AMOUNT
            while (result.next()) {
                numberOfEntries++;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        // If no entries, set fields as uneditable
        if (numberOfEntries < 1) {
            txtBusnName.setEditable(false);
            txtAddress.setEditable(false);
            txtPhone.setEditable(false);
            txtWebsite.setEditable(false);
            txtaraRecycles.setEditable(false);
        }
    }

    public void checkEmptyEntries() {
        try {
            for (int i = 1; i <= numberOfEntries; i++) {
                ResultSet result = sql.getBusnInfo(i);
                if (result.next()) {
                    if ((result.getString("busnname").length() < 1)
                            && (result.getString("address").length() < 1)
                            && (result.getString("phone").length() < 1)
                            && (result.getString("website").length() < 1)
                            && (result.getString("recycles").length() < 1)) {
                        sql.deleteEntry(i);
                    } else if ((result.getString("busnname").length() < 1)
                            || (result.getString("address").length() < 1)
                            || (result.getString("phone").length() < 1)
                            || (result.getString("website").length() < 1)
                            || (result.getString("recycles").length() < 1)) {
                        sql.setBusnInfo(i,
                                result.getString("busnname").length() < 1 ? "null" : result.getString("busnname"),
                                result.getString("address").length() < 1 ? "null" : result.getString("address"),
                                result.getString("phone").length() < 1 ? "null" : result.getString("phone"),
                                result.getString("website").length() < 1 ? "null" : result.getString("website"),
                                result.getString("recycles").length() < 1 ? "null" : result.getString("recycles"));
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
