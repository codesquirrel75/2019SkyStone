/*

***************  Code Designed by Team 107 Team Robotics *********************
***************  Edited for Team 1918 By Nate and Ken    *********************


 */

package com.example.vande.scouting2019;

import android.Manifest;
import android.graphics.Color;
import android.os.Environment;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.TextInputLayout;
import android.content.Intent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.FormatStringUtils;
import utils.PermissionUtils;
import utils.StringUtils;
import utils.ViewUtils;

import static android.R.attr.value;
import static com.example.vande.scouting2019.AutonActivity.AUTON_STRING_EXTRA;
import static com.example.vande.scouting2019.AutonActivity.MATCH_STRING_EXTRA;
import static com.example.vande.scouting2019.AutonActivity.TEAMNUMBER_STRING_EXTRA;


public class TeleopActivity extends AppCompatActivity implements View.OnKeyListener {
    /*This area sets and binds all of the variables that we will use in the auton activity*/


    @BindView(R.id.stone_counter_input_layout)
    public TextInputLayout stoneCounterInputLayout;

    @BindView(R.id.stone_counter_input)
    public TextInputEditText stoneCounterInput;

    @BindView(R.id.bridge_counter_input_layout)
    public TextInputLayout bridgeCounterInputLayout;

    @BindView(R.id.bridge_counter_input)
    public TextInputEditText bridgeCounterInput;


    @BindView(R.id.tel_moved_foundation_radio_group)
    public  RadioGroup telMovedFoundationRadioGroup;

    @BindView(R.id.tel_end_position_radio_group)
    public RadioGroup telEndPositionRadioGroup;

    @BindView(R.id.tel_capstone_radio_group)
    public RadioGroup telCapstoneRadioGroup;


    public String observations = "";

    @BindView(R.id.save_btn)
    public Button saveBtn;

    int stoneCounterValue = 0;
    int bridgeCounterValue = 0;

    public String auton;
    public String matchNumber;
    public String teamNumber;

    private ArrayList<CharSequence> teleopDataStringList;
/*
 *When this activity is first called,
 *we will call the activity_auton layout so we can display
 *the user interface
 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teleop);

        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        auton = bundle.getString(AUTON_STRING_EXTRA);
        matchNumber = bundle.getString(MATCH_STRING_EXTRA);
        teamNumber = bundle.getString(TEAMNUMBER_STRING_EXTRA);

        getSupportActionBar().setTitle("Match: " + matchNumber + " - Team: " + teamNumber);



        teleopDataStringList = new ArrayList<>();
        displayStoneCounterInput(stoneCounterValue);
        displayBridgeCounterInput(bridgeCounterValue);

    }


    /*If this activity is resumed from a paused state the data
     *will be set to what they previously were set to
     */
    @Override
    protected void onResume() {
        super.onResume();

        stoneCounterInput.setOnKeyListener(this);
        bridgeCounterInput.setOnKeyListener(this);
        telMovedFoundationRadioGroup.setOnKeyListener(this);
        telEndPositionRadioGroup.setOnKeyListener(this);
        telCapstoneRadioGroup.setOnKeyListener(this);

    }

    /*If this activity enters a paused state the data will be set to null*/
    @Override
    protected void onPause() {
        super.onPause();


        stoneCounterInput.setOnKeyListener(null);
        bridgeCounterInput.setOnKeyListener(null);
        telMovedFoundationRadioGroup.setOnKeyListener(null);
        telEndPositionRadioGroup.setOnKeyListener(null);
        telCapstoneRadioGroup.setOnKeyListener(null);

    }

    /* This method will display the options menu when the icon is pressed
     * and this will inflate the menu options for the user to choose
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /*This method will launch the correct activity
     *based on the menu option user presses
      */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_activity:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.send_data:
                startActivity(new Intent(this, SendDataActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setSpinnerError(Spinner spinner, String error){
        View selectedView = spinner.getSelectedView();
        if (selectedView instanceof TextView){
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("error");
            selectedTextView.setTextColor(Color.RED);
            selectedTextView.setText(error);

        }
    }

    public void decreaseStoneCounterInput(View view) {
        if (stoneCounterValue != 0) {
            stoneCounterValue = stoneCounterValue - 1;
            displayStoneCounterInput(stoneCounterValue);
        }
    }

    private void displayStoneCounterInput(int number) {
        stoneCounterInput.setText("" + number);
    }

    public void increaseStoneCounterInput(View view) {
        if (stoneCounterValue <= 7) {
            stoneCounterValue = stoneCounterValue + 1;
            displayStoneCounterInput(stoneCounterValue);
        }
    }
    public void decreaseBridgeCounterInput(View view) {
        if (bridgeCounterValue != 0) {
            bridgeCounterValue = bridgeCounterValue - 1;
            displayBridgeCounterInput(bridgeCounterValue);
        }
    }

    public void increaseBridgeCounterInput(View view) {
        if (bridgeCounterValue <= 7) {
            bridgeCounterValue = bridgeCounterValue + 1;
            displayBridgeCounterInput(bridgeCounterValue);
        }
    }
    private void displayBridgeCounterInput(int number) {
        bridgeCounterInput.setText("" + number);
    }

    // this method sets up a string for a group of checkBoxes




    /*This method will look at all of the text/number input fields and set error
    *for validation of data entry
     */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_SPACE && keyCode != KeyEvent.KEYCODE_TAB) {
            TextInputEditText inputEditText = (TextInputEditText) v;

            if (inputEditText != null) {

                switch (inputEditText.getId()) {

                   /* case R.id.teleop_cargo_ship_hatch_panel_input:
                        teleopCargoShipHatchPanelInputLayout.setError(null);
                        break;
                   */

                }
            }
        }
        return false;
    }

    /*
    * This method will verify that all fields are filled and highlight error to user
    * along with change focus to first blank input area. The radio button values are obtained
    * A file is created on the dvice to send the data to. We add the teleop data to the arraylist
    * delimited by commas. We create our message by concatenating the teleop data to the end of
    * the auton data. The data is then output to the file we created. We send a message to the user
    * about the saved message. We send a result back to the auton activity upon completion.
    * We then clear the data of the teleop activity and finish it to close and return
    * to the auton activty to clear its data*/

    public void saveData(View view) throws IOException {
        String state = Environment.getExternalStorageState();
        boolean allInputsPassed = false;

        /*if (StringUtils.isEmptyOrNull(getTextInputLayoutString(teleopCargoShipHatchPanelInputLayout))) {
            teleopCargoShipHatchPanelInputLayout.setError(getText(R.string.teleopCargoShipHatchPanelError));
            ViewUtils.requestFocus(teleopCargoShipHatchPanelInputLayout, this);
        }*/ if(false){
        } else {
            allInputsPassed = true;
        }
        if (!allInputsPassed) {
            return;
        }

        final RadioButton movedFoundationRadiobtn = findViewById(telMovedFoundationRadioGroup.getCheckedRadioButtonId());
        final RadioButton endPositionRadiobtn = findViewById(telEndPositionRadioGroup.getCheckedRadioButtonId());
        final RadioButton capstoneRadiobtn = findViewById(telCapstoneRadioGroup.getCheckedRadioButtonId());


        if(PermissionUtils.getPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File dir = new File(Environment.getExternalStorageDirectory() + "/Scouting");
                dir.mkdirs();

                File file = new File(dir, "Match" + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID) + ".csv");

                teleopDataStringList.add(getTextInputLayoutString(stoneCounterInputLayout));
                teleopDataStringList.add(getTextInputLayoutString(bridgeCounterInputLayout));
                teleopDataStringList.add(movedFoundationRadiobtn.getText().toString());
                teleopDataStringList.add(endPositionRadiobtn.getText().toString());
                teleopDataStringList.add(capstoneRadiobtn.getText().toString());

                teleopDataStringList.add(ScouterInitialsActivity.getInitials());

                String message = auton + "," + FormatStringUtils.addDelimiter(teleopDataStringList, ",") + "\n";

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                    fileOutputStream.write(message.getBytes());
                    fileOutputStream.close();

                    Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "IOException! Go talk to the programmers!", Toast.LENGTH_LONG).show();
                    Log.d("Scouting", e.getMessage());
                }
            } else {
                Toast.makeText(getApplicationContext(), "SD card not found", Toast.LENGTH_LONG).show();
            }

            Intent intent = getIntent();
            intent.putExtra("Key", value);
            setResult(RESULT_OK, intent);

            clearData(view);
            finish();
        }


    }

    /*The method will clear all the data in the text fields, checkboxes, and
    * set radio buttons to default*/
    public void clearData(View view) {

        stoneCounterValue = 0;
        bridgeCounterValue = 0;

        stoneCounterInput.setText("" + stoneCounterValue);
        bridgeCounterInput.setText("" + bridgeCounterValue);
        telMovedFoundationRadioGroup.check(R.id.tel_moved_foundation_no);
        telEndPositionRadioGroup.check(R.id.tel_end_position_no);
        telCapstoneRadioGroup.check(R.id.tel_capstone_no);

    }

    /* This method will change the text entered into the app into a string if it is not already*/
    private String getTextInputLayoutString(@NonNull TextInputLayout textInputLayout) {
        final EditText editText = textInputLayout.getEditText();
        return editText != null && editText.getText() != null ? editText.getText().toString() : "";
    }
}
