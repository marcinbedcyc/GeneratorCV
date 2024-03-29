package pl.remindapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import pl.remindapp.R;
import pl.remindapp.adapters.AbilityAdapter;
import pl.remindapp.cvObjects.Person;
import pl.remindapp.models.AbilityModel;

public class AbilitiesActivity extends AppCompatActivity {
    private ListView abilitiesListView;
    private AbilityAdapter abilityAdapter;
    private LinearLayout mainLayout;
    private Button addButton, nextButton;
    private ArrayList<AbilityModel> abilities;
    private Person user;
    private final int HOBBIES_CODE = 124;
    private final String USER_DATA = "user_data";

    @Override
    public void onBackPressed() {
        List<String> skills = new ArrayList<>();
        for(int i = 0; i < abilityAdapter.getCount(); i++){
            skills.add(abilityAdapter.getItem(i).toString());
        }
        user.setSkills(skills);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(USER_DATA, user);

        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_container_layout);

        user = (Person) getIntent().getSerializableExtra(USER_DATA);

        addButton = findViewById(R.id.addButton);
        nextButton = findViewById(R.id.nextButton);
        mainLayout = findViewById(R.id.abilitiesMainLayout);
        abilitiesListView = findViewById(R.id.listView);

        final TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(R.string.skills);

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


        abilitiesListView.setDivider(null);
        abilities = new ArrayList<AbilityModel>();
        for(String element : user.getSkills()){
            abilities.add(new AbilityModel(element));
        }
        abilityAdapter = new AbilityAdapter(this.getBaseContext(), R.layout.ability_item, abilities);

        abilitiesListView.setAdapter(abilityAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AbilitiesActivity.this);

                final View customLayout = getLayoutInflater().inflate(R.layout.add_ability_layout, null);

                final EditText input = customLayout.findViewById(R.id.addAbilityEditText);
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

                builder.setView(customLayout);
                final Dialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.gradient_rect));


                final Button confirmButton = customLayout.findViewById(R.id.addAbilityConfirmButton);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addElementToListView(input.getText().toString());
                        dialog.dismiss();
                    }
                });

                final Button cancelButton = customLayout.findViewById(R.id.addAbilityCancelButton);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });


                dialog.show();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> skills = new ArrayList<>();
                for(int i = 0; i < abilityAdapter.getCount(); i++){
                    skills.add(abilityAdapter.getItem(i).toString());
                }
                user.setSkills(skills);
                Intent intent = new Intent(AbilitiesActivity.this, HobbiesActivity.class);
                intent.putExtra(USER_DATA, user);
                startActivityForResult(intent, HOBBIES_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == HOBBIES_CODE && resultCode == RESULT_OK){
            user = (Person)data.getSerializableExtra(USER_DATA);

            abilities = new ArrayList<AbilityModel>();
            for(String element : user.getSkills()){
                abilities.add(new AbilityModel(element));
            }

            abilityAdapter = new AbilityAdapter(this.getBaseContext(), R.layout.ability_item, abilities);
            abilitiesListView.setAdapter(abilityAdapter);

        }
    }

    private void addElementToListView(String a){
        abilityAdapter.add(new AbilityModel(a));
        abilityAdapter.notifyDataSetChanged();
    }
}
