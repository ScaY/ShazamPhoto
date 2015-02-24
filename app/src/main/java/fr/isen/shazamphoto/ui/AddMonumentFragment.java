package fr.isen.shazamphoto.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fr.isen.shazamphoto.R;
import fr.isen.shazamphoto.database.Localization;
import fr.isen.shazamphoto.database.Monument;
import fr.isen.shazamphoto.utils.AddMonument;


public class AddMonumentFragment extends Fragment {


    public static AddMonumentFragment newInstance() {
        AddMonumentFragment fragment = new AddMonumentFragment();
        return fragment;
    }

    public AddMonumentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("New monument found");
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_monument, container, false);

        // Add the listener to close the keyboard
        final EditText editTextName = (EditText) view.findViewById(R.id.editText_name_monument);
        addListenerCloseKeyboard(editTextName);
        final EditText editTextDate = (EditText) view.findViewById(R.id.editText_date_monument);
        addListenerCloseKeyboard(editTextDate);
        final EditText editTextDescription = (EditText) view.findViewById(R.id.editText_description_monument);
        addListenerCloseKeyboard(editTextDescription);


        //Set the listener for the ADD button
        Button button = (Button) view.findViewById(R.id.button_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send the POST request to the serveur
                AddMonument addMonument = new AddMonument((UnidentifiedMonument)getActivity());
                addMonument.execute(new Monument(-1, "ISEN", "", "Description", 1959, 12, 1, new Localization(-1, 3, 50)));
            }
        });
        return view;
    }

    public void addListenerCloseKeyboard(final EditText editText){

        if(editText != null){
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId == EditorInfo.IME_ACTION_GO){
                        //Close the keyboard
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                    }
                    return false;
                }
            });
        }
    }


}
