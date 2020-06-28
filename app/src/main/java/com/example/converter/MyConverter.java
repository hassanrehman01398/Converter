package com.example.converter;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.rcarvalho.unitconverter.R;

import java.util.ArrayList;

public class MyConverter extends Fragment {


    String decimalPrecision = "%.4f";


    private final ArrayList<String> profiles = new ArrayList<>();
    private final ArrayList<String> units = new ArrayList<>();
    private final ArrayList<Double> unitValues = new ArrayList<>();
    private BaseUnit activeUnits;

    EditText input;
    Spinner profile;
    Spinner baseUnit;
    Spinner resultUnit;
    Button convert;
    TextView resultLabel;

    Button btnShowSaved;
    Button btnShowcurrency;


    @Override
    public void onActivityCreated(Bundle savedInstanceState)

    {
        super.onActivityCreated(savedInstanceState);


        linkUI();
        loadProfiles();


        int xmlUnitNamesReference = getStringArrayResourceID("units" + this.profiles.get(0));
        int xmlUnitValuesReference = getStringArrayResourceID("units" + this.profiles.get(0)
                + "Values");

        if (xmlUnitNamesReference == 0 || xmlUnitValuesReference == 0)
        {
            showDialog(getResources().getString(R.string.errorMsgBoxTitle),
                    getResources().getString(R.string.unitsAndOrValuesNotFoundError), true);
        }
        else
        {
            int xmlUnitBaseReference = getStringArrayResourceID("units" + this.profiles.get(0)
                    +"Base");


            activeUnits = createBaseUnit(xmlUnitNamesReference, xmlUnitValuesReference,
                    xmlUnitBaseReference);

            if (activeUnits != null)
            {


                setupSpinner(activeUnits, baseUnit, 0);
                setupSpinner(activeUnits, resultUnit, 1);

                setupProfileListener();
                setupBaseUnitSpinnerListener();
                setupResultUnitSpinnerListener();
                setupButtonListener();
                setupTextInputListener();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_unit_converter, container, false);
    }


    private void convert(double amount, String inputAmount, int baseIndex, int resultIndex,
                         boolean messageBox)

    {
        if(resultUnit.getSelectedItem().equals("Kelvin")){
Double outputAmount=(Double.parseDouble(inputAmount) + 459.67) *5/9;
            String message = inputAmount + " " + activeUnits.getUnitNames()[baseIndex] + " " +
                    getResources().getString(R.string.isEqualTo) + " " +
                    outputAmount + " " + activeUnits.getUnitNames()[resultIndex];

            resultLabel.setText(message);


            SharedPref.SaveInPref(getActivity(), message);

            if (messageBox) {
                showDialog(getResources().getString(R.string.conversionMsgBoxTitle), message, false);
            }

        }
        else {

            activeUnits.setValue(amount, baseIndex);


            double result = activeUnits.convert(resultIndex);


            String outputAmount;
            if ((result - Math.floor(result)) > 0.0000001) {
                outputAmount = String.format(decimalPrecision, result);
            } else {
                outputAmount = String.format("%.0f", result);
            }
            String message = inputAmount + " " + activeUnits.getUnitNames()[baseIndex] + " " +
                    getResources().getString(R.string.isEqualTo) + " " +
                    outputAmount + " " + activeUnits.getUnitNames()[resultIndex];

            resultLabel.setText(message);


            SharedPref.SaveInPref(getActivity(), message);

            if (messageBox) {
                showDialog(getResources().getString(R.string.conversionMsgBoxTitle), message, false);
            }
        }
    }

    private BaseUnit createBaseUnit(int xmlUnitNamesReference, int xmlUnitValuesReference,
                                    int xmlBaseUnitReference)

    {
        String[] units = getResources().getStringArray(xmlUnitNamesReference);
        String[] unitBaseValuesStrings = getResources().getStringArray(xmlUnitValuesReference);

        if (units.length == unitBaseValuesStrings.length && units.length > 1)
        {

            double[] unitBaseValues = new double[unitBaseValuesStrings.length];

            for (int i = 0; i < unitBaseValuesStrings.length; i++) {
                try
                {
                    unitBaseValues[i] = Double.parseDouble(unitBaseValuesStrings[i]);

                }
                catch (NumberFormatException e)
                {

                    showDialog(getResources().getString(R.string.errorMsgBoxTitle),
                            getResources().getString(R.string.nonParsableUnitValueError), true);
                }
            }

            if (xmlBaseUnitReference != 0)
            {
                String[] baseNumber = getResources().getStringArray(xmlBaseUnitReference);

                try
                {

                    double baseNumberValue = Double.parseDouble(baseNumber[0]);
                    return new BaseUnit(units, unitBaseValues, 0, baseNumberValue);

                }
                catch (NumberFormatException e)
                {

                    showDialog(getResources().getString(R.string.errorMsgBoxTitle),
                            getResources().getString(R.string.nonParsableUnitValueError), true);
                }

                return null;
            }
            else
            {

                return new BaseUnit(units, unitBaseValues, 0, 0);
            }
        }
        else if (units.length < 2)

        {
            showDialog(getResources().getString(R.string.errorMsgBoxTitle),
                    getResources().getString(R.string.notEnoughUnitsError), true);
            return null;
        }
        else
        {
            showDialog(getResources().getString(R.string.errorMsgBoxTitle),
                    getResources().getString(R.string.unitsAndUnitValuesDontMatchError), true);
            return null;
        }
    }

    private int getStringArrayResourceID(String resourceIDString)
    {
        return getResources().getIdentifier(resourceIDString, "array",
                this.getActivity().getPackageName());
    }

    private void linkUI()

    {
        input=(EditText) getActivity(). findViewById(R.id.input);
        profile = (Spinner) getActivity(). findViewById(R.id.spinnerCategory);
        baseUnit = (Spinner) getActivity(). findViewById(R.id.spinnerUnitsBase);
        resultUnit = (Spinner) getActivity(). findViewById(R.id.spinnerUnitsResult);
        resultLabel = ((TextView) getActivity(). findViewById (R.id.txtResult));
        convert = (Button) getActivity(). findViewById(R.id.btnConvert);
        btnShowSaved=getActivity().findViewById(R.id.btnShowSaved);
        //if(profile.getSelectedItem().toString().equals("Currency")){
            baseUnit.setEnabled(false);

       // }
//        btnShowcurrency=getActivity().findViewById(R.id.btnConvert2);
//
//btnShowcurrency.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        startActivity(new Intent(getActivity(),MainActivity1.class));
//
//    }
//});
        btnShowSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),SavedResults.class));

            }
        });
    }

    private void loadProfiles()
    {
        String[] profiles = getResources().getStringArray(R.array.profiles);
        for (String profile : profiles)
        {
            this.profiles.add(profile);
        }
    }

    private void setupBaseUnitSpinnerListener()

    {
        baseUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                try {
                    //store the value the user wants to convert
                    double amount = Double.parseDouble(input.getText().toString());

                    //perform a conversion without a confirmation dialogue
                    convert(amount, input.getText().toString(), position,
                            resultUnit.getSelectedItemPosition(), false);
                } catch (NumberFormatException e) {

                    resultLabel.setText(getResources().getString(R.string.defaultResult));
                }
            }

            @Override

            public void onNothingSelected(AdapterView<?> parentView) {
                resultLabel.setText(getResources().getString(R.string.defaultResult));
            }
        });
    }

    private void setupButtonListener()

    {
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    double amount = Double.parseDouble(input.getText().toString());


                    convert(amount, input.getText().toString(), baseUnit.getSelectedItemPosition(),
                            resultUnit.getSelectedItemPosition(), true);

                } catch (NumberFormatException e) {

                    showDialog(getResources().getString(R.string.noticeMsgBoxTitle),
                            getResources().getString(R.string.noNumberError), false);
                }

            }
        });
    }

    private void setupProfileListener()

    {

        profile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int xmlUnitNamesReference = getStringArrayResourceID("units" +
                        profiles.get(position));
                int xmlUnitValuesReference = getStringArrayResourceID("units" +
                        profiles.get(position) + "Values");


                if (xmlUnitNamesReference != 0 && xmlUnitValuesReference != 0) {
                   int xmlUnitBaseReference = getStringArrayResourceID("units" +
                            profiles.get(position) + "Base");

                    activeUnits = createBaseUnit(xmlUnitNamesReference, xmlUnitValuesReference,
                            xmlUnitBaseReference);

                    if (activeUnits != null) {

                        setupSpinner(activeUnits, baseUnit, 0);
                        setupSpinner(activeUnits, resultUnit, 1);
                    }


                } else {

                    showDialog(getResources().getString(R.string.errorMsgBoxTitle),
                            getResources().getString(R.string.nonParsableUnitValueError), true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });
    }

    private void setupResultUnitSpinnerListener()
    {
        resultUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id)
            {
                try
                {
                    double amount = Double.parseDouble(input.getText().toString());

                    convert(amount, input.getText().toString(), baseUnit.getSelectedItemPosition(),
                            position, false);
                }
                catch (NumberFormatException e)
                {
                    resultLabel.setText(getResources().getString(R.string.defaultResult));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

                resultLabel.setText(getResources().getString(R.string.defaultResult));
            }

        });
    }

    private void setupSpinner(BaseUnit units, Spinner spinner, int defaultSelection)
    {

        String[]unitNames = activeUnits.getUnitNames();
        ArrayAdapter<String> spinnerArrayAdapter;

        spinnerArrayAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, unitNames);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setSelection(defaultSelection);
    }

    private void setupTextInputListener()
   {

        input.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE))
                {
                    try
                    {
                        double amount = Double.parseDouble(input.getText().toString());

                        convert(amount, input.getText().toString(),
                                baseUnit.getSelectedItemPosition(),
                                resultUnit.getSelectedItemPosition(), false);

                        return false;

                    }
                    catch (NumberFormatException e)
                    {

                        resultLabel.setText(getResources().getString(R.string.defaultResult));
                        return false;
                    }
                }
                return false;
            }
        });
    }

    private void showDialog(String title, String message, boolean terminateApp)

    {
        AlertDialog ad = new AlertDialog.Builder(this.getActivity()).create();
        ad.setCancelable(false);
        ad.setTitle(title);
        ad.setMessage(message);

        if (terminateApp)
        {
            ad.setButton(getResources().getString(R.string.msgBoxCloseButtonTitle),
            new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    System.exit(0);
                }
            });
        }
        else
        //Dismiss the dialogue upon pressing the Close button
        {
            ad.setButton(getResources().getString(R.string.msgBoxCloseButtonTitle),
            new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });
        }
        ad.show();
    }
}