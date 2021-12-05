package Controllers;

import javaClasses.sqlManager;
import javaClasses.userData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;


public class SignUpUiController
{
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private DatePicker birthDate;
    @FXML
    private TextField contactNo;
    @FXML
    private TextField emailAddress;
    @FXML
    private TextField passwWord;


    @FXML
    private TextField unitHouseNo;
    @FXML
    private TextField streetD;
    @FXML
    private TextField citY;
    @FXML
    private TextField barangaY;
    @FXML
    private TextField provincE;
    @FXML
    private TextField regioN;
    @FXML
    private TextField postalCode;

    @FXML
    private Button continuE;
    @FXML
    private Button canceL;


    public void changeScrn(ActionEvent e) throws IOException
    {
        if(e.getSource().equals(continuE))
        {
           checkInvalidInput(e);
        }
        if(e.getSource().equals(canceL))
        {
            Parent viewParent = FXMLLoader.load(getClass().getResource("/fxml/LoginUi.fxml"));
            Scene viewScene = new Scene(viewParent);

            Stage srcWin = (Stage)((Node)e.getSource()).getScene().getWindow();
            srcWin.setScene(viewScene);
            srcWin.show();
        }
    }

    private void checkInvalidInput(ActionEvent e) throws IOException
    {
        String checkFname = firstName.getText().trim();
        String checkLname = lastName.getText().trim();
        String checkBday = "";
        String checkEAddress = emailAddress.getText().trim();
        String checkPassword = passwWord.getText().trim();
        int checkContactno = -1;

        int checkhouseNo = -1;
        String checkStreet = streetD.getText().trim();
        String checkCity = citY.getText().trim();
        String checkBarangay = barangaY.getText().trim();
        String checkProvince = provincE.getText().trim();
        String checkRegion = regioN.getText().trim();
        int checkPostalcode = -1;

        boolean gotoMain = true;
        if(checkFname ==""|| containsIllegalcharacters(checkFname))
        {
            gotoMain = false;
            System.out.println("First Name error");
        }
        if(checkLname =="" || containsIllegalcharacters(checkLname))
        {
            gotoMain = false;
            System.out.println("Last Name Error");
        }

        try{checkBday = birthDate.getValue().toString();}catch (Exception z)
        {
            gotoMain = false;
            System.out.println("Birthday Error");
        }

        try{checkContactno = Integer.parseInt(contactNo.getText().trim());}catch(Exception a)
        {
            gotoMain = false;
            System.out.println("Contact Error");
        }
        if(checkContactno < 0)
        {
            gotoMain = false;
            System.out.println("Contact Error < 0");
        }

        if(checkEAddress == "" || containsIllegalcharacters(checkEAddress))
        {
            gotoMain = false;
            System.out.println("Email Address error");
        }

        try{checkhouseNo = Integer.parseInt(unitHouseNo.getText().trim());}catch(Exception ab)
        {
            gotoMain = false;
            System.out.println("House no Error not a Number");
        }
        if(checkhouseNo < 0)
        {
            gotoMain = false;
            System.out.println("House no error");
        }

        if(checkStreet == "" || containsIllegalcharacters(checkStreet))
        {
            gotoMain = false;
            System.out.println("Street error");
        }
        if(checkCity == "" || containsIllegalcharacters(checkCity))
        {
            gotoMain = false;
            System.out.println("City Error");
        }
        if(checkBarangay == "" || containsIllegalcharacters(checkBarangay))
        {
            gotoMain = false;
            System.out.println("Barangay Error");
        }
        if(checkProvince == "" || containsIllegalcharacters(checkProvince))
        {
            gotoMain = false;
            System.out.println("Province error");
        }
        if(checkRegion == "" || containsIllegalcharacters(checkRegion))
        {
            gotoMain = false;
            System.out.println("Region Error");
        }

        try{checkPostalcode = Integer.parseInt(postalCode.getText().trim());}catch(Exception ac)
        {
            gotoMain = false;
            System.out.println("Postal code error exception");
        }
        if(checkPostalcode < 0)
        {
            gotoMain = false;
            System.out.println("Postal code is less than 0");
        }
        if(checkPassword == "")
        {
            gotoMain = false;
            System.out.println("Password is empty");
        }

        if(gotoMain)
        {
            String compiledHomeAddress = checkhouseNo + " " + checkStreet + " " + checkCity + " " + checkBarangay + " " + checkProvince
                    + " " + checkRegion + " " + checkPostalcode;

            userData newUser = new userData(checkFname,checkLname,checkBday,
                    checkContactno,checkEAddress, compiledHomeAddress, checkPassword);

            changetoMain(e, newUser);
        }
        else
        {
            launchInvalidwindow();
        }
    }

    public void changetoMain(ActionEvent e, userData insert) throws IOException
    {
        //Insertion to database of user data
        try
        {
            sqlManager insertOrder = new sqlManager();
            if(insertOrder.insertIntoUserData(insert))
            {
                //Load first the FXML
                FXMLLoader loadNew = new FXMLLoader();
                loadNew.setLocation(getClass().getResource("/fxml/Main.fxml"));

                //Get the controller
                Parent viewParent = loadNew.load();
                MainUiController mainUi = loadNew.getController();

                //Send data to new Controller
                mainUi.setCurrentuser(insert.getEmailAddress());
                Scene viewScene = new Scene(viewParent);

                //Switch view
                Stage srcWin = (Stage)((Node)e.getSource()).getScene().getWindow();
                srcWin.setScene(viewScene);
                srcWin.show();

            }
            else
            {

                launchInvalidwindow();
            }
        }catch (SQLException a)
        {
            a.printStackTrace();
            launchInvalidwindow();
        }
    }

    private void launchInvalidwindow() throws IOException
    {
        FXMLLoader loadthis = new FXMLLoader();
        loadthis.setLocation(getClass().getResource("/fxml/InvalidSignupDialogPrompt.fxml"));


        DialogPane loadtheError = loadthis.load();
        Dialog<ButtonType> dialog = new Dialog<>();
        Stage puticon = (Stage)dialog.getDialogPane().getScene().getWindow();
        puticon.getIcons().add(new Image(this.getClass().getResource("/Pictures/5.png").toString()));

        dialog.setDialogPane(loadtheError);
        dialog.setTitle("Error");

        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if(clickedButton.get() == ButtonType.CLOSE)
        {
            firstName.clear();
            lastName.clear();
            contactNo.clear();
            emailAddress.clear();

            unitHouseNo.clear();
            streetD.clear();
            citY.clear();
            barangaY.clear();
            regioN.clear();
            postalCode.clear();
            provincE.clear();
            dialog.close();
        }
    }

    private boolean containsIllegalcharacters(String candidate)
    {
        String[] arrayofIllegalCharacters = {"\"","/",":","*","?","<",">","|",";"};
        for(int i = 0; i < arrayofIllegalCharacters.length; i++)
        {
            if(candidate.contains(arrayofIllegalCharacters[i]))
            {
                return true;
            }
        }
        return false;
    }
}
