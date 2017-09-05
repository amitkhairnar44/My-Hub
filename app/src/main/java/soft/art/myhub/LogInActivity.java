package soft.art.myhub;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    CardView facultyLogin;
    CardView studentLogin;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    AlertDialog facultyCodeDialog;
    AlertDialog studentCodeDialog;

    EditText facultyCodeEditText;
    EditText studentCodeEditText;
    String fCode;
    String fCodeArray[];
    String sCode;
    String sCodeArray[];
    public static final String userTypePref = "usertypepref";
    public static final String facultyPref = "facultypref";
    public static final String studentPref = "studentpref";
    public static final String facultycode = "facultycode";
    public static final String studentcode = "studentcode";
    public static final String userType = "usertype";
    public static final String student = "student";
    public static final String faculty = "faculty";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        facultyLogin = findViewById(R.id.faculty_login);
        studentLogin = findViewById(R.id.student_login);


        View facultyDialogView = View.inflate(this,R.layout.faculty_code_dialog,null);
        facultyCodeEditText = facultyDialogView.findViewById(R.id.faculty_code_text);

        View studentDialogView = View.inflate(this,R.layout.student_code_dialog,null);
        studentCodeEditText = studentDialogView.findViewById(R.id.student_code_text);

        final SharedPreferences facultySharedPreferences = getSharedPreferences(facultyPref,MODE_PRIVATE);
        final SharedPreferences studentSharedPreferences = getSharedPreferences(studentPref,MODE_PRIVATE);
        final SharedPreferences userTypePreferences = getSharedPreferences(userTypePref,MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        currentUser= mAuth.getCurrentUser();

        if (currentUser != null)
        {
            if (userTypePreferences.getString(userType,"")!= null && userTypePreferences.getString(userType,"").equals(faculty))
            {
                Intent facultyIntent = new Intent(LogInActivity.this,NoticeActivity.class);
                startActivity(facultyIntent);
                finish();
            }
            if (userTypePreferences.getString(userType,"")!= null && userTypePreferences.getString(userType,"").equals(student))
            {
                Intent studentIntent = new Intent(LogInActivity.this,MainActivity.class);
                startActivity(studentIntent);
                finish();
            }

        }


        fCodeArray = getResources().getStringArray(R.array.pref_faculty_codes);
        sCodeArray = getResources().getStringArray(R.array.pref_student_codes);

        //AlertDialog for faculty login code
        facultyCodeDialog = new AlertDialog.Builder(this).create();
        facultyCodeDialog.setView(facultyDialogView);
        facultyCodeDialog.setTitle("Faculty Code");
        facultyCodeDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });

        //AlertDialog for student login code
        studentCodeDialog = new AlertDialog.Builder(this).create();
        studentCodeDialog.setView(studentDialogView);
        studentCodeDialog.setTitle("Student Code");
        studentCodeDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });

        facultyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facultyCodeDialog.show();
                facultyCodeDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fCode = facultyCodeEditText.getText().toString();
                        Toast.makeText(LogInActivity.this, fCode, Toast.LENGTH_SHORT).show();

                        for (String aFCodeArray : fCodeArray) {
                            if (fCode.equals(aFCodeArray)) {
                                Toast.makeText(LogInActivity.this, "Code is correct", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor userEditor = userTypePreferences.edit();
                                userEditor.putString(userType, faculty);
                                userEditor.commit();

                                SharedPreferences.Editor facultyCodeEditor = facultySharedPreferences.edit();
                                facultyCodeEditor.putString(facultycode, fCode);
                                facultyCodeEditor.commit();

                                Intent intent = new Intent(LogInActivity.this, GoogleLoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        facultyCodeDialog.dismiss();
                    }
                });
            }
        });

        studentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentCodeDialog.show();
                studentCodeDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sCode = studentCodeEditText.getText().toString();
                        for (String aSCodeArray : sCodeArray)
                        {
                            if (sCode.equals(aSCodeArray))
                            {
                                SharedPreferences.Editor userEditor = userTypePreferences.edit();
                                userEditor.putString(userType, student);
                                userEditor.commit();

                                SharedPreferences.Editor studentCodeEditor = studentSharedPreferences.edit();
                                studentCodeEditor.putString(studentcode,sCode);
                                studentCodeEditor.commit();

                                Intent intent = new Intent(LogInActivity.this,GoogleLoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });
            }
        });

    }
}
