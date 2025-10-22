package com.example.lab3;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab3.adapter.StudentAdapter;
import com.example.lab3.helper.StudentDatabaseHelper;
import com.example.lab3.model.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    private EditText etMssv, etHoten, etLop;
    private Button btnInsert, btnDelete, btnUpdate, btnQuery;
    private RecyclerView rvStudents;
    private StudentAdapter adapter;
    private List<Student> students;
    private StudentDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        // Initialize views
        etMssv = findViewById(R.id.et_mssv);
        etHoten = findViewById(R.id.et_hoten);
        etLop = findViewById(R.id.et_lop);
        btnInsert = findViewById(R.id.btn_insert);
        btnDelete = findViewById(R.id.btn_delete);
        btnUpdate = findViewById(R.id.btn_update);
        btnQuery = findViewById(R.id.btn_query);
        rvStudents = findViewById(R.id.rv_students);

        // Initialize database
        dbHelper = new StudentDatabaseHelper(this);
        dbHelper.copyDatabaseFromAssets();
        
        // Open database to keep connection for Database Inspector
        dbHelper.openDatabase();
        
        // Debug database
        dbHelper.debugDatabase();

        // Setup RecyclerView
        students = new ArrayList<>();
        adapter = new StudentAdapter(students, student -> {
            // Khi click vào sinh viên, fill vào form để edit
            etMssv.setText(student.getMssv());
            etHoten.setText(student.getHoten());
            etLop.setText(student.getLop());
        });
        rvStudents.setLayoutManager(new LinearLayoutManager(this));
        rvStudents.setAdapter(adapter);

        // Load all students
        loadStudents();

        // Button listeners
        btnInsert.setOnClickListener(v -> insertStudent());
        btnDelete.setOnClickListener(v -> deleteStudent());
        btnUpdate.setOnClickListener(v -> updateStudent());
        btnQuery.setOnClickListener(v -> queryStudent());
    }

    private void insertStudent() {
        String mssv = etMssv.getText().toString().trim();
        String hoten = etHoten.getText().toString().trim();
        String lop = etLop.getText().toString().trim();

        if (mssv.isEmpty() || hoten.isEmpty() || lop.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        Student student = new Student(mssv, hoten, lop);
        long result = dbHelper.insertStudent(student);
        
        if (result != -1) {
            Toast.makeText(this, "Thêm sinh viên thành công!", Toast.LENGTH_SHORT).show();
            clearFields();
            loadStudents();
        } else {
            Toast.makeText(this, "Lỗi: MSSV đã tồn tại hoặc lỗi database!", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteStudent() {
        String mssv = etMssv.getText().toString().trim();

        if (mssv.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập MSSV cần xóa!", Toast.LENGTH_SHORT).show();
            return;
        }

        int result = dbHelper.deleteStudent(mssv);
        
        if (result > 0) {
            Toast.makeText(this, "Xóa sinh viên thành công!", Toast.LENGTH_SHORT).show();
            clearFields();
            loadStudents();
        } else {
            Toast.makeText(this, "Không tìm thấy MSSV: " + mssv, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateStudent() {
        String mssv = etMssv.getText().toString().trim();
        String hoten = etHoten.getText().toString().trim();
        String lop = etLop.getText().toString().trim();

        if (mssv.isEmpty() || hoten.isEmpty() || lop.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        Student student = new Student(mssv, hoten, lop);
        int result = dbHelper.updateStudent(student);
        
        if (result > 0) {
            Toast.makeText(this, "Cập nhật sinh viên thành công!", Toast.LENGTH_SHORT).show();
            clearFields();
            loadStudents();
        } else {
            Toast.makeText(this, "Không tìm thấy MSSV: " + mssv, Toast.LENGTH_SHORT).show();
        }
    }

    private void queryStudent() {
        String mssv = etMssv.getText().toString().trim();

        if (mssv.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập MSSV cần tìm!", Toast.LENGTH_SHORT).show();
            return;
        }

        Student student = dbHelper.getStudentByMssv(mssv);
        if (student != null) {
            etHoten.setText(student.getHoten());
            etLop.setText(student.getLop());
            Toast.makeText(this, "Tìm thấy sinh viên!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Không tìm thấy sinh viên!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadStudents() {
        Log.d("StudentActivity", "loadStudents() called");
        students.clear();
        List<Student> loadedStudents = dbHelper.getAllStudents();
        students.addAll(loadedStudents);
        adapter.notifyDataSetChanged();
        Log.d("StudentActivity", "RecyclerView updated with " + students.size() + " students");
    }

    private void clearFields() {
        etMssv.setText("");
        etHoten.setText("");
        etLop.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close database when activity is destroyed (optional)
        // Comment this line if you want to keep database open for debugging
        // if (dbHelper != null) {
        //     dbHelper.close();
        // }
    }
}

