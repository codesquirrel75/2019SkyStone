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


    @BindView(R.id.defense_effectiveness)
    public RadioGroup defenseEffectiveness;

    @BindView(R.id.end_game_location_spinner)
    public Spinner endGameLocationSpinner;

    @BindView(R.id.cycle_time_spinner)
    public Spinner cycleTimeSpinner;

    @BindView(R.id.overall_effectiveness_radio_group)
    public RadioGroup overallEffectivenessRadioGoup;


    @BindView(R.id.trained_drive_team_radio_group)
    public RadioGroup trainedDriveTeamRadioGoup;

    @BindView(R.id.observ_cargo_pickup)
    public CheckBox observCargoPickup;

    @BindView(R.id.observ_died_back)
    public CheckBox observDiedBack;

    @BindView(R.id.observ_died_mid)
    public CheckBox observDiedMid;

    @BindView(R.id.observ_dns)
    public CheckBox observDns;

    @BindView(R.id.observ_fast)
    public CheckBox observFast;

    @BindView(R.id.observ_fell_apart)
    public CheckBox observFellApart;

    @BindView(R.id.observ_fell_over)
    public CheckBox observFellOver;


    @BindView(R.id.observ_jerky)
    public CheckBox observJerky;

    @BindView(R.id.observ_not_much)
    public CheckBox observNotMuch;

    @BindView(R.id.observ_penalties)
    public CheckBox observPenalties;

    @BindView(R.id.observ_played_defense)
    public CheckBox observPlayedDefense;

    @BindView(R.id.observ_slow)
    public CheckBox observSlow;

    @BindView(R.id.observ_slowed_by_robot)
    public CheckBox observSlowedByRobot;

    @BindView(R.id.observ_smooth)
    public CheckBox observsmooth;

    @BindView(R.id.summary_input)
    public EditText summaryInput;

    @BindView(R.id.issues_input)
    public EditText issuesInput;

    public String observations = "";

    @BindView(R.id.save_btn)
    public Button saveBtn;

    int teleopCargoShipHatchPanel = 0;
    int teleopCargoShipCargo = 0;
    int teleopHatchPanelTop = 0;
    int teleopHatchPanelMiddle = 0;
    int teleopHatchPanelBottom =0;
    int teleopCargotop = 0;
    int teleopCargoMiddle = 0;
    int teleopCargoBottom = 0;





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




        //  --- End Game Location spinner ---

        Spinner endgamelocationspinner = (Spinner) findViewById(R.id.end_game_location_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> endgamelocationadapter = ArrayAdapter.createFromResource(this,
                R.array.endgame_location, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        endgamelocationadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        endgamelocationspinner.setAdapter(endgamelocationadapter);


        //  --- Cycle Time spinner ---

        Spinner cycletimespinner = (Spinner) findViewById(R.id.cycle_time_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> cycletimeadapter = ArrayAdapter.createFromResource(this,
                R.array.cycle_time_spinner, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        cycletimeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        cycletimespinner.setAdapter(cycletimeadapter);

    }


    /*If this activity is resumed from a paused state the data
     *will be set to what they previously were set to
     */
    @Override
    protected void onResume() {
        super.onResume();


        defenseEffectiveness.setOnKeyListener(this);
        endGameLocationSpinner.setOnKeyListener(this);
        cycleTimeSpinner.setOnKeyListener(this);
        overallEffectivenessRadioGoup.setOnKeyListener(this);

        trainedDriveTeamRadioGoup.setOnKeyListener(this);
        observsmooth.setOnKeyListener(this);
        observSlowedByRobot.setOnKeyListener(this);
        observSlow.setOnKeyListener(this);
        observPlayedDefense.setOnKeyListener(this);
        observPenalties.setOnKeyListener(this);
        observNotMuch.setOnKeyListener(this);
        observJerky.setOnKeyListener(this);

        observFellOver.setOnKeyListener(this);
        observFellApart.setOnKeyListener(this);
        observFast.setOnKeyListener(this);
        observDns.setOnKeyListener(this);
        observDiedMid.setOnKeyListener(this);
        observDiedBack.setOnKeyListener(this);
        observCargoPickup.setOnKeyListener(this);

        summaryInput.setOnKeyListener(this);
        issuesInput.setOnKeyListener(this);

    }

    /*If this activity enters a paused state the data will be set to null*/
    @Override
    protected void onPause() {
        super.onPause();


        defenseEffectiveness.setOnKeyListener(null);
        endGameLocationSpinner.setOnKeyListener(null);
        cycleTimeSpinner.setOnKeyListener(null);
        overallEffectivenessRadioGoup.setOnKeyListener(null);
        trainedDriveTeamRadioGoup.setOnKeyListener(null);
        observsmooth.setOnKeyListener(null);
        observSlowedByRobot.setOnKeyListener(null);
        observSlow.setOnKeyListener(null);
        observPlayedDefense.setOnKeyListener(null);
        observPenalties.setOnKeyListener(null);
        observNotMuch.setOnKeyListener(null);
        observJerky.setOnKeyListener(null);
        observFellOver.setOnKeyListener(null);
        observFellApart.setOnKeyListener(null);
        observFast.setOnKeyListener(null);
        observDns.setOnKeyListener(null);
        observDiedMid.setOnKeyListener(null);
        observDiedBack.setOnKeyListener(null);
        observCargoPickup.setOnKeyListener(null);
        summaryInput.setOnKeyListener(null);
        issuesInput.setOnKeyListener(null);

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




    // this method sets up a string for a group of checkBoxes

    public void setString(View view) {
        Boolean checked = ((CheckBox) view).isChecked();
        String s1;

        switch (view.getId()) {
            case R.id.observ_cargo_pickup:
                s1 = observCargoPickup.getText().toString() + " |";
                if (checked) {
                    if (observations.isEmpty()) {
                        observations = s1;
                    } else {
                        observations = observations + s1;
                    }
                } else {
                    if (observations.contains(s1)) {
                        int start = observations.indexOf(s1);
                        observations = observations.substring(0, start) + observations.substring(start + s1.length());
                    }
                }
                break;
            case R.id.observ_died_back:
                s1 = observDiedBack.getText().toString() + " |";
                if (checked) {
                    if (observations.isEmpty()) {
                        observations = s1;
                    } else {
                        observations = observations + s1;
                    }
                } else {
                    if (observations.contains(s1)) {
                        int start = observations.indexOf(s1);
                        observations = observations.substring(0, start) + observations.substring(start + s1.length());
                    }
                }
                break;
            case R.id.observ_died_mid:
                s1 = observDiedMid.getText().toString() + " |";
                if (checked) {
                    if (observations.isEmpty()) {
                        observations = s1;
                    } else {
                        observations = observations + s1;
                    }
                } else {
                    if (observations.contains(s1)) {
                        int start = observations.indexOf(s1);
                        observations = observations.substring(0, start) + observations.substring(start + s1.length());
                    }
                }
                break;
            case R.id.observ_dns:
                s1 = observDns.getText().toString() + " |";
                if (checked) {
                    if (observations.isEmpty()) {
                        observations = s1;
                    } else {
                        observations = observations + s1;
                    }
                } else {
                    if (observations.contains(s1)) {
                        int start = observations.indexOf(s1);
                        observations = observations.substring(0, start) + observations.substring(start + s1.length());
                    }
                }
                break;
            case R.id.observ_fast:
                s1 = observFast.getText().toString() + " |";
                if (checked) {
                    if (observations.isEmpty()) {
                        observations = s1;
                    } else {
                        observations = observations + s1;
                    }
                } else {
                    if (observations.contains(s1)) {
                        int start = observations.indexOf(s1);
                        observations = observations.substring(0, start) + observations.substring(start + s1.length());
                    }
                }
                break;
            case R.id.observ_fell_apart:
                s1 = observFellApart.getText().toString() + " |";
                if (checked) {
                    if (observations.isEmpty()) {
                        observations = s1;
                    } else {
                        observations = observations + s1;
                    }
                } else {
                    if (observations.contains(s1)) {
                        int start = observations.indexOf(s1);
                        observations = observations.substring(0, start) + observations.substring(start + s1.length());
                    }
                }
                break;
            case R.id.observ_fell_over:
                s1 = observFellOver.getText().toString() + " |";
                if (checked) {
                    if (observations.isEmpty()) {
                        observations = s1;
                    } else {
                        observations = observations + s1;
                    }
                } else {
                    if (observations.contains(s1)) {
                        int start = observations.indexOf(s1);
                        observations = observations.substring(0, start) + observations.substring(start + s1.length());
                    }
                }
                break;
            case R.id.observ_jerky:
                s1 = observJerky.getText().toString() + " |";
                if (checked) {
                    if (observations.isEmpty()) {
                        observations = s1;
                    } else {
                        observations = observations + s1;
                    }
                } else {
                    if (observations.contains(s1)) {
                        int start = observations.indexOf(s1);
                        observations = observations.substring(0, start) + observations.substring(start + s1.length());
                    }
                }
                break;
            case R.id.observ_not_much:
                s1 = observNotMuch.getText().toString() + " |";
                if (checked) {
                    if (observations.isEmpty()) {
                        observations = s1;
                    } else {
                        observations = observations + s1;
                    }
                } else {
                    if (observations.contains(s1)) {
                        int start = observations.indexOf(s1);
                        observations = observations.substring(0, start) + observations.substring(start + s1.length());
                    }
                }
                break;
            case R.id.observ_penalties:
                s1 = observPenalties.getText().toString() + " |";
                if (checked) {
                    if (observations.isEmpty()) {
                        observations = s1;
                    } else {
                        observations = observations + s1;
                    }
                } else {
                    if (observations.contains(s1)) {
                        int start = observations.indexOf(s1);
                        observations = observations.substring(0, start) + observations.substring(start + s1.length());
                    }
                }
                break;
            case R.id.observ_played_defense:
                s1 = observPlayedDefense.getText().toString() + " |";
                if (checked) {
                    if (observations.isEmpty()) {
                        observations = s1;
                    } else {
                        observations = observations + s1;
                    }
                } else {
                    if (observations.contains(s1)) {
                        int start = observations.indexOf(s1);
                        observations = observations.substring(0, start) + observations.substring(start + s1.length());
                    }
                }
                break;
            case R.id.observ_slow:
                s1 = observSlow.getText().toString() + " |";
                if (checked) {
                    if (observations.isEmpty()) {
                        observations = s1;
                    } else {
                        observations = observations + s1;
                    }
                } else {
                    if (observations.contains(s1)) {
                        int start = observations.indexOf(s1);
                        observations = observations.substring(0, start) + observations.substring(start + s1.length());
                    }
                }
                break;
            case R.id.observ_slowed_by_robot:
                s1 = observSlowedByRobot.getText().toString() + " |";
                if (checked) {
                    if (observations.isEmpty()) {
                        observations = s1;
                    } else {
                        observations = observations + s1;
                    }
                } else {
                    if (observations.contains(s1)) {
                        int start = observations.indexOf(s1);
                        observations = observations.substring(0, start) + observations.substring(start + s1.length());
                    }
                }
                break;
            case R.id.observ_smooth:
                s1 = observsmooth.getText().toString() + " |";
                if (checked) {
                    if (observations.isEmpty()) {
                        observations = s1;
                    } else {
                        observations = observations + s1;
                    }
                } else {
                    if (observations.contains(s1)) {
                        int start = observations.indexOf(s1);
                        observations = observations.substring(0, start) + observations.substring(start + s1.length());
                    }
                }
                break;

        }
    }


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

        final RadioButton defenseEffectivenessRadiobtn = findViewById(defenseEffectiveness.getCheckedRadioButtonId());
        final RadioButton overallEffectivenessRadiobtn = findViewById(overallEffectivenessRadioGoup.getCheckedRadioButtonId());

        final RadioButton trainedDriveTeamRadiobtn = findViewById(trainedDriveTeamRadioGoup.getCheckedRadioButtonId());



        if(PermissionUtils.getPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File dir = new File(Environment.getExternalStorageDirectory() + "/Scouting");
                dir.mkdirs();

                File file = new File(dir, "Match" + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID) + ".csv");


                teleopDataStringList.add(defenseEffectivenessRadiobtn.getText().toString());
                teleopDataStringList.add(endGameLocationSpinner.getSelectedItem().toString());
                teleopDataStringList.add(cycleTimeSpinner.getSelectedItem().toString());
                teleopDataStringList.add(overallEffectivenessRadiobtn.getText().toString());

                teleopDataStringList.add(trainedDriveTeamRadiobtn.getText().toString());
                teleopDataStringList.add(observations);
                teleopDataStringList.add(summaryInput.getText().toString());
                teleopDataStringList.add(issuesInput.getText().toString());

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

        defenseEffectiveness.clearCheck();
        endGameLocationSpinner.setSelection(0);
        cycleTimeSpinner.setSelection(0);

        overallEffectivenessRadioGoup.clearCheck();

        trainedDriveTeamRadioGoup.clearCheck();
        observsmooth.setChecked(false);
        observSlowedByRobot.setChecked(false);
        observSlow.setChecked(false);
        observPlayedDefense.setChecked(false);
        observPenalties.setChecked(false);
        observNotMuch.setChecked(false);
        observJerky.setChecked(false);

        observFellOver.setChecked(false);
        observFellApart.setChecked(false);
        observFast.setChecked(false);
        observDns.setChecked(false);
        observDiedMid.setChecked(false);
        observDiedBack.setChecked(false);
        observCargoPickup.setChecked(false);

        summaryInput.setText(null);
        issuesInput.setText(null);

    }

    /* This method will change the text entered into the app into a string if it is not already*/
    private String getTextInputLayoutString(@NonNull TextInputLayout textInputLayout) {
        final EditText editText = textInputLayout.getEditText();
        return editText != null && editText.getText() != null ? editText.getText().toString() : "";
    }
}
