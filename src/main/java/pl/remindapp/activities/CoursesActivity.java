package pl.remindapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import pl.remindapp.R;
import pl.remindapp.adapters.LifeEventAdapter;
import pl.remindapp.cvObjects.LifeEvent;
import pl.remindapp.cvObjects.Person;

public class CoursesActivity extends AppCompatActivity {
    private Person user;
    private LifeEventAdapter lifeEventAdapter;
    private LinearLayout mainLayout;
    private Button addButton, nextButton;
    private TextView titleTextView;
    private ListView listView;
    private LocalDate beginDate, endDate;
    private final int TEMPLATES_CODE = 128;
    private final String USER_DATA = "user_data";

    @Override
    public void onBackPressed() {
        List<LifeEvent> courses = new ArrayList<>();
        for (int i = 0; i < lifeEventAdapter.getCount(); i++) {
            courses.add(lifeEventAdapter.getItem(i));
        }
        user.setCourses(courses);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(USER_DATA, user);

        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == TEMPLATES_CODE && resultCode == RESULT_OK){
            user = (Person)data.getSerializableExtra(USER_DATA);
            lifeEventAdapter = new LifeEventAdapter(this, R.layout.life_event_list_item, user.getCourses());
            listView.setAdapter(lifeEventAdapter);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_view_container_layout);

        beginDate = endDate = null;
        user = (Person) getIntent().getSerializableExtra(USER_DATA);

        addButton = findViewById(R.id.addButton);
        nextButton = findViewById(R.id.nextButton);
        mainLayout = findViewById(R.id.abilitiesMainLayout);
        listView = findViewById(R.id.listView);

        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(R.string.courses);

        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int heightDiff = mainLayout.getRootView().getHeight() - mainLayout.getHeight();
                if(heightDiff > 216){
                    nextButton.setVisibility(View.INVISIBLE);
                    addButton.setVisibility(View.INVISIBLE);
                }
                else{
                    nextButton.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.VISIBLE);
                }
            }
        });
        lifeEventAdapter = new LifeEventAdapter(this, R.layout.life_event_list_item, user.getCourses());

        listView.setAdapter(lifeEventAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(CoursesActivity.this);

                final View customLayout = getLayoutInflater().inflate(R.layout.add_life_evenet_layout, null);

                final TextView title = customLayout.findViewById(R.id.addLifeEventTitle);
                title.setText(R.string.courses);

                final EditText titleEditText = customLayout.findViewById(R.id.addLifeEventTitleEditText);
                titleEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

                final EditText descriptionEditText = customLayout.findViewById(R.id.addLifeEventDescriptionEditText);
                descriptionEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

                builder.setView(customLayout);
                final Dialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.orange_gradient));


                final Button confirmButton = customLayout.findViewById(R.id.addLifeEventConfirmButton);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addElementToListView(titleEditText.getText().toString(), descriptionEditText.getText().toString());
                        dialog.dismiss();
                    }
                });

                final Button cancelButton = customLayout.findViewById(R.id.addLifeEventCancelButton);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                final TextView beginDateTextView = customLayout.findViewById(R.id.addLifeEventBeginDateTextView);
                final Button beginDateButton = customLayout.findViewById(R.id.addLifeEventBeginDateButton);
                beginDateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog;
                        if (beginDate == null)
                            datePickerDialog = new DatePickerDialog(CoursesActivity.this, AlertDialog.THEME_HOLO_DARK);
                        else
                            datePickerDialog = new DatePickerDialog(CoursesActivity.this, AlertDialog.THEME_HOLO_DARK, null, beginDate.getYear(), beginDate.getMonthValue() - 1, beginDate.getDayOfMonth());
                        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                beginDate = LocalDate.of(year, month + 1, dayOfMonth);
                                beginDateTextView.setText(beginDate.toString());
                            }
                        });
                        datePickerDialog.show();
                    }
                });

                final TextView endDateTextView = customLayout.findViewById(R.id.addLifeEventEndDateTextView);
                final Button endDateButton = customLayout.findViewById(R.id.addLifeEventEndDateButton);
                endDateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog;
                        if (endDate == null)
                            datePickerDialog = new DatePickerDialog(CoursesActivity.this, AlertDialog.THEME_HOLO_DARK);
                        else
                            datePickerDialog = new DatePickerDialog(CoursesActivity.this, AlertDialog.THEME_HOLO_DARK, null, endDate.getYear(), endDate.getMonthValue() - 1, endDate.getDayOfMonth());
                        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                endDate = LocalDate.of(year, month + 1, dayOfMonth);
                                endDateTextView.setText(endDate.toString());
                            }
                        });
                        datePickerDialog.show();
                    }
                });

                dialog.show();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<LifeEvent> courses = new ArrayList<>();
                for (int i = 0; i < lifeEventAdapter.getCount(); i++) {
                    courses.add(lifeEventAdapter.getItem(i));
                }
                user.setCourses(courses);
                Intent intent = new Intent(CoursesActivity.this, ChooseCvTemplateActivity.class);
                intent.putExtra(USER_DATA, user);
                startActivityForResult(intent, TEMPLATES_CODE);
            }
        });
    }

    private void addElementToListView(String title, String description) {
        lifeEventAdapter.add(new LifeEvent(beginDate, endDate, title, description));
        lifeEventAdapter.notifyDataSetChanged();
    }
}
