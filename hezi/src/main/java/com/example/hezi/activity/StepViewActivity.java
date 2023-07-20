package com.example.hezi.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hezi.R;
import com.example.hezi.view.StepView;

import java.util.Arrays;
import java.util.List;

public class StepViewActivity extends AppCompatActivity {

    private StepView mStepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_view);
        mStepView = findViewById(R.id.step_view);
        List<String> steps = Arrays.asList("", "", "", "");
        mStepView.setSteps(steps);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.next) {
            int nextStep = mStepView.getCurrentStep() + 1;
            if (nextStep > mStepView.getStepCount()) {
                nextStep = 1;
            }
            mStepView.selectedStep(nextStep);
        }
    }
}