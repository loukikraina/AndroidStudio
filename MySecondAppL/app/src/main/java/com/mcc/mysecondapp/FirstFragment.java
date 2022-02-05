package com.mcc.mysecondapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.mcc.mysecondapp.databinding.FragmentFirstBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private EditText fname, lname, rollNo, dept, phone, email;
    private Button sendDatabtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    student student;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("student");
        student = new student();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname = binding.fname.getText().toString();
                String lname = binding.lname.getText().toString();
                String rollNo = binding.rollno.getText().toString();
                String dept = binding.dept.getText().toString();
                String phone = binding.phone.getText().toString();
                String email = binding.email.getText().toString();
                if (TextUtils.isEmpty(fname) && TextUtils.isEmpty(lname) && TextUtils.isEmpty(rollNo)
                        && TextUtils.isEmpty(phone) && TextUtils.isEmpty(dept) && TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), "Please add some data!", Toast.LENGTH_SHORT).show();
                } else {
                    addDatatoFirebase(fname, lname, rollNo, email, phone, dept);
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);
                }
            }
        });
    }
    private void addDatatoFirebase(String fname, String lname, String rno,String email, String phone, String dept) {
        student.setStudentFName(fname);
        student.setStudentLName(lname);
        student.setRollNo(rno);
        student.setEmail(email);
        student.setPhone(phone);
        student.setDept(dept);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.setValue(student);

                // after adding this data we are showing toast message.
                Toast.makeText(getActivity(), "Data added!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Fail to add data! " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}