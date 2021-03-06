package soft.art.myhub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import soft.art.myhub.model.NoticeModel;

public class MainActivity extends AppCompatActivity {

    RecyclerView noticeList;
    ArrayAdapter<NoticeModel> arrayAdapter;
    ArrayList<NoticeModel> arrayList;
    LinearLayoutManager linearLayoutManager;

    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        name = user.getDisplayName();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("faculty");

        noticeList = findViewById(R.id.notice_list);
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

    public void fetchData(DataSnapshot dataSnapshot)
    {
        //arrayList.clear();
        NoticeModel model = dataSnapshot.getValue(NoticeModel.class);
        System.out.println(dataSnapshot.getValue(NoticeModel.class));
        //arrayList.add(model);
        System.out.println(arrayList.toString());
    }

    public ArrayList<NoticeModel> retrieveData()
    {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
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
