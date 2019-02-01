package id.pptik.bawaslubatch.m_Recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.pptik.bawaslubatch.R;

import java.util.ArrayList;

/**
 * Created by Oclemy on 7/6/2016 for ProgrammingWizards Channel and http://www.camposha.com.
 */
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context c;
    ArrayList<String> users;

    public MyAdapter(Context c, ArrayList<String> users) {
        this.c = c;
        this.users = users;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(c).inflate(R.layout.cardview_post,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //BIND
        holder.nameTxt.setText(users.get(position));

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}