package org.snowcorp.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class activity_watch_data_user extends AppCompatActivity {
    private MaterialButton btnAddTask;
    private SessionManager session;
    private DatabaseHandler db;
    private HashMap<String,String> user = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_data_user);

        btnAddTask = findViewById(R.id.watch_user_validateinfo);

        DatabaseHandler db = new DatabaseHandler(activity_watch_data_user.this);
        user = db.getUserDetails();

        // session manager
        session = new SessionManager(activity_watch_data_user.this);

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from database
        String name = user.get("name");
        String email = user.get("email");
        String uid = user.get("uid");

        //get the spinner from the xml.
        Spinner dropdownLinkFamily = findViewById(R.id.watch_user_relation);
        //create a list of items for the spinner.
        String[] itemsTaskType = new String[]{"Papa", "Maman", "Grand-Père", "Grand-Mère", "Autre"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapterTaskType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsTaskType);
        //set the spinners adapter to the previously created one.
        dropdownLinkFamily.setAdapter(adapterTaskType);

        btnAddTask.setOnClickListener(v -> {

            EditText watch_user_NameEditText = (EditText) findViewById(R.id.watch_user_Name);
            String username = watch_user_NameEditText.getText().toString();
            EditText watch_user_Date_naisEditText = (EditText) findViewById(R.id.watch_user_Date_nais);
            String watch_user_Date_nais = watch_user_Date_naisEditText.getText().toString();
            Spinner spinner_relation = (Spinner)findViewById(R.id.watch_user_relation);
            String famLink = spinner_relation.getSelectedItem().toString();

            if (username.matches("") || watch_user_Date_nais.matches("")) {
                Toast.makeText(this, "Veuillez completer les informations : Nom et Date de Naissance", Toast.LENGTH_SHORT).show();
                return;
            } else {
                StaticData.WatchUser.setName(username);

                try {
                    Date dateNas = new SimpleDateFormat("dd-MM-yyyy").parse(watch_user_Date_nais);
                    StaticData.WatchUser.setBirthDate(dateNas);
                } catch (ParseException e) {

                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                registerWatchUser(StaticData.WatchUser.getUniqueCode(), StaticData.WatchUser.getName(), watch_user_Date_nais,GetFamLink(famLink), uid);

            }


        });
    }

        private void registerWatchUser(final String uniqueId, final String nameWatchUser, final String birthDate,final String familyLink, final String userId_fk) {
            // Tag used to cancel the request
            String tag_string_req = "req_post_pushWatchUser";
            //showDialog();
            StringRequest  req_registerWatchUser= new StringRequest(Request.Method.POST,
                    Functions.CREATE_WATCHUSER_URL , response  -> {
                Toast.makeText(activity_watch_data_user.this, response, Toast.LENGTH_LONG).show();

                try {
                    if (response != null) {
                        JSONObject jObj = new JSONObject(response);
                        StaticData.WatchUserId = jObj.getInt("lastInsertId");
                    }
                    Toast.makeText(activity_watch_data_user.this, "Compte bien rajouté!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(activity_watch_data_user.this, activity_addtask.class);
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(activity_watch_data_user.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }, error -> {
                Log.e("req_post_pushWatchUser", "req_post_pushWatchUser: " + error.getMessage());
               Toast.makeText(activity_watch_data_user.this, error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<>();
                    params.put("uniqueId", uniqueId);
                    params.put("nameWatchUser", nameWatchUser);
                    params.put("birthDate", birthDate);
                    params.put("familyLink", familyLink);
                    params.put("userId_fk", userId_fk);

                    return params;
                }

            };
            RequestQueue queue = Volley.newRequestQueue(activity_watch_data_user.this);
            queue.add(req_registerWatchUser);

    }


    private String GetFamLink(String value) {
        switch (value) {
            case "Papa":
                return "papa";
            case "Maman":
                return "maman";
            case "Grand-Père":
                return "grand-pere";
            case "Grand-Mère":
                return "grand-mere";
            case "Autre":
                return "autre";
            default:
                value = "Invalid";
                return "Invalid";
        }
    }

    private void logoutUser() {
        session.setLogin(false);
        // Launching the login activity
        Functions logout = new Functions();
        logout.logoutUser(activity_watch_data_user.this);
        Intent intent = new Intent(activity_watch_data_user.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}