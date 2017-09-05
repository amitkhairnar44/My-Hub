package soft.art.myhub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import soft.art.myhub.model.NoticeModel;

public class NoticeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    String userName;
    String date;

    RecyclerView noticeList;
    ArrayList<NoticeModel> arrayList;
    LinearLayoutManager linearLayoutManager;

    EditText noticeText;
    ImageView noticeButton;

    DatabaseReference databaseReference;

    NoticeModel noticeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userName = user.getDisplayName();

        noticeText = findViewById(R.id.notice_text);
        noticeButton = findViewById(R.id.notice_send);

        noticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = noticeText.getText().toString();
                date = "Today";
                addToDatabase(text,userName,date);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("faculty");

        noticeList = findViewById(R.id.notice_faculty_list);
        linearLayoutManager = new LinearLayoutManager(this);
        noticeList.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();

        FirebaseRecyclerAdapter<NoticeModel, NoticeViewHolder> recyclerAdapter =
                new FirebaseRecyclerAdapter<NoticeModel, NoticeViewHolder>(NoticeModel.class,R.layout.notice_list,NoticeViewHolder.class,databaseReference) {
                    @Override
                    protected void populateViewHolder(NoticeViewHolder viewHolder, NoticeModel model, int position) {
                        viewHolder.noticeText.setText(model.getNotice());
                        viewHolder.noticeName.setText(model.getFacultyname());
                        viewHolder.noticeDate.setText(model.getNoticeDate());
                    }
                };
        retrieveData();
        noticeList.setAdapter(recyclerAdapter);
    }

    public void addToDatabase(String notice,String name,String date)
    {
        NoticeModel model = new NoticeModel();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        date = currentDate.format(todayDate);

        model.setFacultyname(name);
        model.setNotice(notice);
        model.setNoticeDate(date);

        try {
            databaseReference.child("faculty").push().setValue(model, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    //Toast.makeText(, "", Toast.LENGTH_SHORT).show();
                    Log.d("Message:","Data added successfully");
                    noticeText.getText().clear();
                }
            });
        }
        catch (DatabaseException e)
        {
            Log.d("Error:",e.getMessage());
        }
    }

    public static class NoticeViewHolder extends RecyclerView.ViewHolder
    {
        TextView noticeText;
        TextView noticeName;
        TextView noticeDate;

        public NoticeViewHolder(View itemView) {
            super(itemView);

            noticeText = itemView.findViewById(R.id.notice_text);
            noticeName = itemView.findViewById(R.id.notice_name);
            noticeDate = itemView.findViewById(R.id.notice_date);
        }
    }

//    public void fetchData(DataSnapshot dataSnapshot)
//    {
//        NoticeModel model = dataSnapshot.getValue(NoticeModel.class);
//        System.out.println(dataSnapshot.getValue(NoticeModel.class));
//        System.out.println(arrayList.toString());
//    }

    public ArrayList<NoticeModel> retrieveData()
    {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //fetchData(dataSnapshot);
                noticeModel = dataSnapshot.getValue(NoticeModel.class);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //fetchData(dataSnapshot);
                noticeModel = dataSnapshot.getValue(NoticeModel.class);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return arrayList;
    }
}
