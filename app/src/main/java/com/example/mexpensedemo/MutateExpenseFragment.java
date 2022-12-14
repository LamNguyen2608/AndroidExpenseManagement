package com.example.mexpensedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.mexpensedemo.model.Expense;
import com.example.mexpensedemo.model.ExpenseViewModel;
import com.example.mexpensedemo.model.TripViewModel;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

public class MutateExpenseFragment extends AppCompatDialogFragment implements CurrencyConverter.OnInputListener {

    private static final int PERMISSION_REQUEST_CODE = 10000;
    private static final int IMAGE_CAPTURE_CODE = 10001;
    private static final int GET_FROM_GALLERY = 100002;
    private ExpenseViewModel expenseViewModel;
    private EditText enterExpenseName;
    private AutoCompleteTextView enterType;
    private TextView enterTime, enterdate;
    DatePickerDialog.OnDateSetListener dateSetListener;
    private EditText enterAmount;
    private EditText enterComment;
    private Expense expense = null;
    private String mutate = "";
    private String currentType = "Other";
    private int trip_id;
    private LinearLayout select_date;
    private LinearLayout select_time;
    private LinearLayout btn_takepicture, btn_uploadimage;
    private int hour, minute;
    private Button btn_convert, exp_mutate, exp_cancel;
    String[] expenseAttrs = {"Food", "Accommodation", "Transportation", "Equipment", "Outsource", "Other"};
    private Uri image_uri = null;
    private ImageView img_billing;
    private ProgressBar progressBar;
    private AwesomeValidation awesomeValidation;

    public MutateExpenseFragment(int trip_id) {
        this.trip_id = trip_id;
    }

    public MutateExpenseFragment(Expense expense, String mutate){
        this.expense = expense;
        this.mutate = mutate;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_new_expense, null);
        enterExpenseName = view.findViewById(R.id.inp_expname);
        enterAmount = view.findViewById(R.id.inp_expamt);
        enterComment = view.findViewById(R.id.inp_expcmt);
        select_date = view.findViewById(R.id.date_button);
        select_time = view.findViewById(R.id.time_button);
        enterTime = view.findViewById(R.id.time_expense_view);
        enterdate = view.findViewById(R.id.inp_date);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.getTime().getHours();
        int minute = calendar.getTime().getMinutes();
        enterdate.setText(day + "/" + month + "/" + year);
        enterTime.setText(hour + ":" + minute);
        enterType = view.findViewById(R.id.dropdown_expense_type_field);
        btn_convert = view.findViewById(R.id.btn_convert);
        btn_takepicture = view.findViewById(R.id.camera_button);
        btn_uploadimage = view.findViewById(R.id.gallery_button);
        img_billing = view.findViewById(R.id.expense_image);
        progressBar = view.findViewById(R.id.expense_progress);
        exp_mutate = view.findViewById(R.id.exp_btn_create);
        exp_cancel = view.findViewById(R.id.exp_btn_cancel);

        //Set currency converter
        btn_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CurrencyConverter(progressBar).show(getChildFragmentManager(), "currency converter");
            }
        });
        
        //Set Dropdown type
        ArrayAdapter<String> arrayAdapterExpenseType = new ArrayAdapter<String>(getActivity(), R.layout.list_item, expenseAttrs);
        enterType.setAdapter(arrayAdapterExpenseType);
        enterType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentType = adapterView.getItemAtPosition(i).toString();
            }
        });

        //Set date and calendar
        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });

        //Select Time
        select_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker(view);
            }
        });

         dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                enterdate.setText(date);
            }
        };

         //Upload Image
        btn_takepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED || getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_REQUEST_CODE);
                    }
                    else {
                        startcamera();
                    }
                }
            }
        });

        btn_uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        expenseViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity()
                .getApplication())
                .create(ExpenseViewModel.class);

        if (expense != null){
            enterExpenseName.setText(expense.getExpense_name());
            enterdate.setText(expense.getDate());
            enterTime.setText(expense.getTime());
            enterAmount.setText(String.valueOf(expense.getAmount()));
            enterComment.setText(expense.getComment());
            enterType.setText(expense.getExpense_type(), false);
            currentType = expense.getExpense_type();
            Picasso.with(getContext()).load(expense.getImage_uri()).into(img_billing);
        }

        awesomeValidation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
        awesomeValidation.setContext(getContext());
        awesomeValidation.addValidation(
                enterExpenseName,
                RegexTemplate.NOT_EMPTY,
                "Expense name is required");
        awesomeValidation.addValidation(
                enterAmount,
                RegexTemplate.NOT_EMPTY,
                "Expense amount is required"
        );

        switch (mutate) {
            case "edit_expense":
                exp_mutate.setText("update");
                exp_mutate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (awesomeValidation.validate()) {
                            String message = "Expense name: " + enterExpenseName.getText().toString() + "\n"
                                    + "Type: " + currentType + "\n"
                                    + "Amount: " + enterAmount.getText().toString() + "\n"
                                    + "Date: " + enterdate.getText().toString() + "\n"
                                    + "Time: " + enterTime.getText().toString() + "\n"
                                    + "Comment: " + enterComment.getText().toString();
                            androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                            builder1.setTitle("Update Expense Confirm");
                            builder1.setMessage(message);
                            builder1.setCancelable(true);

                            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Expense expense1 = new Expense();

                                    expense1.setId(expense.getId());
                                    expense1.setTrip_id(expense.getTrip_id());
                                    expense1.setExpense_type(currentType);
                                    expense1.setExpense_name(enterExpenseName.getText().toString());
                                    expense1.setDate(enterdate.getText().toString());
                                    expense1.setTime(enterTime.getText().toString());
                                    expense1.setAmount(Float.parseFloat(enterAmount.getText().toString()));
                                    expense1.setComment(enterComment.getText().toString());
                                    if (image_uri != null) {
                                        expense1.setImage_uri(String.valueOf(image_uri));
                                    } else {
                                        expense1.setImage_uri(expense.getImage_uri());
                                    }
                                    expense1.setAction("U");

                                    ExpenseViewModel.updateExpense(expense1);

                                    dialogInterface.cancel();
                                    dismiss();
                                }
                            });
                            androidx.appcompat.app.AlertDialog alert = builder1.create();
                            alert.show();
                        }
                    }
                });
                exp_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
                builder.setView(view)
                        .setTitle("Edit this expense");
                break;
            case "delete_expense":
                enterExpenseName.setEnabled(false);
                enterTime.setEnabled(false);
                enterAmount.setEnabled(false);
                enterComment.setEnabled(false);
                enterType.setEnabled(false);
                exp_mutate.setText("Delete");

                exp_mutate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                        builder1.setTitle("Delete Expense Confirm");
                        builder1.setMessage("Please confirm your deletion of " + enterExpenseName.getText().toString() +" expense");
                        builder1.setCancelable(true);

                        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                expense.setAction("U");
                                expense.setDelete(true);
                                ExpenseViewModel.updateExpense(expense);
                                dialogInterface.cancel();
                                dismiss();
                            }
                        });

                        androidx.appcompat.app.AlertDialog alert = builder1.create();
                        alert.show();
                    }
                });

                exp_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
                builder.setView(view)
                        .setTitle("Delete this expense");
                break;
            default:
                exp_mutate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (awesomeValidation.validate()) {
                            String message = "Expense name: " + enterExpenseName.getText().toString() + "\n"
                                    + "Type: " + currentType + "\n"
                                    + "Amount: " + enterAmount.getText().toString() + "\n"
                                    + "Date: " + enterdate.getText().toString() + "\n"
                                    + "Time: " + enterTime.getText().toString() + "\n"
                                    + "Comment: " + enterComment.getText().toString();
                            androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                            builder1.setTitle("Add Expense Confirm");
                            builder1.setMessage(message);
                            builder1.setCancelable(true);

                            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Expense expense1 = new Expense();

                                    expense1.setTrip_id(trip_id);
                                    expense1.setExpense_type(currentType);
                                    expense1.setExpense_name(enterExpenseName.getText().toString());
                                    expense1.setDate(enterdate.getText().toString());
                                    expense1.setTime(enterTime.getText().toString());
                                    expense1.setAmount(Float.parseFloat(enterAmount.getText().toString()));
                                    expense1.setComment(enterComment.getText().toString());
                                    expense1.setImage_uri(String.valueOf(image_uri));
                                    expense1.setAction("C");

                                    ExpenseViewModel.insert(expense1);
                                    dialogInterface.cancel();
                                    dismiss();
                                }
                            });
                            androidx.appcompat.app.AlertDialog alert = builder1.create();
                            alert.show();
                        }
                    }
                });

                exp_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
                builder.setView(view)
                        .setTitle("Add new expense");
        }
        return builder.create();
    }

    //Take picture activity
    private void startcamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCamera.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        getActivity().startActivityForResult(openCamera, IMAGE_CAPTURE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            image_uri = data.getData();
            Log.d("From gallery", "URI===>" + data.getData());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        img_billing.setImageURI(image_uri);
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                enterTime.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Set Time");
        timePickerDialog.show();
    }

    //Get currency from Currency Converter Fragment
    @Override
    public void sendInput(String input) {
        progressBar.setVisibility(View.GONE);
        Log.d("==>", "sendInput: got the input: " + input);
        enterAmount.setText(input);
    }


}