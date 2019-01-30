package id.pptik.bawaslubatch.view_models;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.pptik.bawaslubatch.R;
import id.pptik.bawaslubatch.models.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private List<Post> postList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView volunteer_photo;
        public TextView volunteer_postname, volunteer_postlocation, volunteer_postComment;

        public MyViewHolder(View itemView) {
            super(itemView);

            volunteer_photo = (ImageView) itemView.findViewById(R.id.card_photo);
            volunteer_postname = (TextView) itemView.findViewById(R.id.card_poster);
            volunteer_postlocation = (TextView) itemView.findViewById(R.id.card_location);
            volunteer_postComment = (TextView) itemView.findViewById(R.id.card_comment);
        }
    }

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post, parent, false);
        return new MyViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.MyViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.volunteer_postname.setText(post.getVolunteer_name());
        holder.volunteer_postlocation.setText(
                "Kode Provinsi " + post.getVolunteer_location_province() + "\n" +
                        "Kode Kelurahan " + post.getVolunteer_location_cityreg() + "\n" +
                        "Kode Kecamatan " + post.getVolunteer_location_district() + "\n"
        );
        holder.volunteer_postComment.setText("Komentar" + "\n\n" + post.getVolunteer_comment());
        Picasso.get().load(post.getVolunteer_photo()).into(holder.volunteer_photo);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

}