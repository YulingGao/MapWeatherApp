package edu.uiuc.cs427app;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddCityDialog extends AppCompatActivity {
    final private String TAG = AddCityDialog.class.getSimpleName();
    private String cityName = "";
    private boolean success = false;
    private boolean exist = false;
    AlertDialog.Builder builder;
    private Activity context;

    /**
     * @param context context
     * @param username username carried over
     * @param adapter adapter used to refresh the list
     * this method is to pop up dialog to enable users to enter the new add city
     */
    public AddCityDialog(Activity context, String username, customizedAdapter adapter) {
        this.context = context;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Add the city");
        View v = context.getLayoutInflater().inflate(R.layout.edittext, null);

        UserCityDB userCityDB = new UserCityDB(context);
        builder.setView(v)
            // Add action buttons
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            })
            .setPositiveButton("Add City", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // add location
                    // 1. get user id
                    EditText cityInput = (EditText) v.findViewById(R.id.editText);
                    cityName = cityInput.getText().toString();
                    Log.d(TAG, "userInput: " + cityName + " userName: " + username);
                    List<Address> listOfCity = getLocationFromAddress(cityName);
                    if (listOfCity.size() == 0) {
                        Toast.makeText( context, "The city name is invalid.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 2. add the input into the list of corresponding user
                    exist = userCityDB.isExist(username, cityName);
                    if(exist){
                        Toast.makeText( context, cityName+" exists", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        success = userCityDB.insertUserCity(username, cityName);
                        if(success){
                            Toast.makeText( context, cityName+" has been added", Toast.LENGTH_SHORT).show();
                        }
                    }

                    // 3. refresh the list
                    adapter.refresh(new ArrayList<>(userCityDB.getCityList(username)));
                }
            });
    }

    /**
     * pop up dialog
     */
    public void show() {
        builder.show();
    }

    /**
     * @param city city name
     * helper function to get latitude and longitude from a city name
     * @return
     */
    public List<Address> getLocationFromAddress(String city) {
        Geocoder coder = new Geocoder(this.context);
        List<Address> address;

        try {
            address = coder.getFromLocationName(city, 5);
            return address;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<Address>();
    }
}