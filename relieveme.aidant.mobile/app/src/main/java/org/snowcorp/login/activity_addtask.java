package org.snowcorp.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.snowcorp.login.helper.DatabaseHandler;
import org.snowcorp.login.helper.Functions;
import org.snowcorp.login.helper.SessionManager;
import org.snowcorp.login.helper.StaticData;
import org.snowcorp.login.helper.TaskPlan;

import java.util.HashMap;
import java.util.Map;

public class activity_addtask extends Activity {
    private MaterialButton btnAddTask;
    private SessionManager session;
    private DatabaseHandler db;
    private HashMap<String,String> user = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        btnAddTask = findViewById(R.id.btn_add_task);


        DatabaseHandler db = new DatabaseHandler(activity_addtask.this);
        user = db.getUserDetails();

        // session manager
        session = new SessionManager(activity_addtask.this);

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from database
        String name = user.get("name");
        String email = user.get("email");
        String uid = user.get("uid");


        // String datestr = "" + day + "." + month + ", " + Constant.weeks[week-1];
        //dateTime.setText(datestr);

        //fragment = new EditFragment();
        // getFragmentManager().beginTransaction().replace(R.id.add_subtask_list, fragment).commit();

        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.FreqRept);
        //create a list of items for the spinner.
        String[] items = new String[]{"Jamais", "Toutes les heures", "Tous les jours", "Toutes les semaines", "Tous les mois", "Tous les ans"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        //get the spinner from the xml.
        Spinner dropdownTaskType = findViewById(R.id.TaskType);
        //create a list of items for the spinner.
        String[] itemsTaskType = new String[]{"Domicile", "Médicament", "Professionnel", "Familial", "Médical", "Administratif"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapterTaskType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsTaskType);
        //set the spinners adapter to the previously created one.
        dropdownTaskType.setAdapter(adapterTaskType);


        btnAddTask.setOnClickListener(v -> {

            EditText watch_user_Task_name = (EditText) findViewById(R.id.Task_name);
            String task_name = watch_user_Task_name.getText().toString();

            EditText watch_user_Task_description = (EditText) findViewById(R.id.Task_description);
            String task_description = watch_user_Task_description.getText().toString();

            EditText watch_user_Task_Date = (EditText) findViewById(R.id.Task_Date);
            String task_Date = watch_user_Task_Date.getText().toString();

            EditText watch_user_Task_Date_Time= (EditText) findViewById(R.id.Task_Date_Time);
            String task_Date_Time = watch_user_Task_Date_Time.getText().toString();

            EditText watch_user_TaskEndTime = (EditText) findViewById(R.id.TaskEndTime);
            String taskEndTime = watch_user_TaskEndTime.getText().toString();

            EditText watch_user_TaskEndTime_Time = (EditText) findViewById(R.id.TaskEndTime_Time);
            String taskEndTime_Time = watch_user_TaskEndTime_Time.getText().toString();

            Spinner spinner_TaskType = (Spinner) findViewById(R.id.TaskType);
            String taskType = spinner_TaskType.getSelectedItem().toString();

            Spinner spinner_FreqRept = (Spinner) findViewById(R.id.FreqRept);
            String freqRept = spinner_FreqRept.getSelectedItem().toString();


            if (task_name.matches("") || task_description.matches("") || task_Date.matches("") || taskEndTime.matches("")) {
                Toast.makeText(this, "Veuillez completer les informations", Toast.LENGTH_SHORT).show();
                return;
            } else {

                taskEndTime = taskEndTime+":"+ taskEndTime_Time;
                task_Date = task_Date+":"+ task_Date_Time;
                StaticData.Tasks = new TaskPlan();
                StaticData.Tasks.setTaskName(task_name);
                StaticData.Tasks.setTaskDesc(task_description);
                StaticData.Tasks.setTaskDate(task_Date);
                StaticData.Tasks.setEndTime(taskEndTime);
                StaticData.Tasks.setTaskType(taskType);
                StaticData.Tasks.setTaskRep(freqRept);

                registerWatchUser(task_name, task_description, task_Date, taskEndTime, GettaskType(taskType), GetFreqRep(freqRept), uid, StaticData.WatchUserId);
                Intent intent = new Intent(activity_addtask.this, HomeActivity.class);
                startActivity(intent);

            }


        });

    }

    private void registerWatchUser(final String task_name, final String task_description, final String task_Date, final String taskEndTime, final String taskType, final String freqRept, String idUser_fk, int idWatch_fk) {
        // Tag used to cancel the request
        String tag_string_req = "req_post_pushWatchUser";
        //showDialog();
        StringRequest req_AddTask = new StringRequest(Request.Method.POST,
                Functions.CREATE_TASK_URL, response -> {
            Toast.makeText(activity_addtask.this, response, Toast.LENGTH_LONG).show();

            try {
                if (response != null) {
                    JSONObject jObj = new JSONObject(response);
                    //boolean error = jObj.getBoolean("error");
                }

                Toast.makeText(activity_addtask.this, "la tache est bien rajouté!", Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(activity_addtask.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }, error -> {
            Log.e("req_post_pushWatchUser", "req_post_pushWatchUser: " + error.getMessage());
            Toast.makeText(activity_addtask.this, error.getMessage(), Toast.LENGTH_LONG).show();
            //hideDialog();
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("taskDescription", task_description);
                params.put("taskDate", task_Date);
                params.put("endTime", taskEndTime);
                params.put("typeTask", taskType);
                params.put("taskRepetition", freqRept);
                params.put("idUser_fk", idUser_fk);
                params.put("idWatch_fk", Integer.toString(idWatch_fk) );

                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(activity_addtask.this);
        queue.add(req_AddTask);

    }


    private String GetFreqRep(String value) {
        switch (value) {
            case "Jamais":
                return "once";
            case "Toutes les heures":
                return "hourly";
            case "Tous les jours":
                return "daily";
            case "Toutes les semaines":
                return "weekly";
            case "Tous les mois":
                return "mounthly";
            case "Tous les ans":
                return "yearly";
            default:
                value = "Invalid";
                return "Invalid";
        }
    }


    private String GettaskType(String value) {
        switch (value) {
            case "Domicile":
                return "domicile";
            case "Médicament":
                return "medoc";
            case "Professionnel":
                return "professionnel";
            case "Familial":
                return "familial";
            case "Médical":
                return "rdv";
            case "Administratif":
                return "service";
            default:
                value = "Invalid";
                return "Invalid";
        }
    }

    private void logoutUser() {
        session.setLogin(false);
        // Launching the login activity
        Functions logout = new Functions();
        logout.logoutUser(activity_addtask.this);
        Intent intent = new Intent(activity_addtask.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}

