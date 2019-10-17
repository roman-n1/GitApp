package com.yarullin.roman.gitapp.features.search.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yarullin.roman.gitapp.R;
import com.yarullin.roman.gitapp.persistence.entity.ModelRepository;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class SearchRepoAdapter extends RealmRecyclerViewAdapter<ModelRepository, SearchRepoAdapter.SearchRepoViewHolder> {

    public SearchRepoAdapter(@Nullable OrderedRealmCollection<ModelRepository> data) {
        super(data, true);
    }

    @Override
    public SearchRepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repo, parent, false);
        return new SearchRepoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchRepoViewHolder holder, int position) {
        ModelRepository repository = getItem(position);
        if (repository != null && repository.getOwner() != null && repository.getOwner().getAvatarUrl() != null) {
            Picasso.with(holder.profileImage.getContext())
                    .load(repository.getOwner().getAvatarUrl())
                    .into(holder.profileImage);
        }
        holder.repoNameTv.setText(repository.getFullName());
        holder.repoDescriptionTv.setText(repository.getDescription());
    }

    class SearchRepoViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView repoNameTv;
        TextView repoDescriptionTv;

        SearchRepoViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            repoNameTv = itemView.findViewById(R.id.repo_name);
            repoDescriptionTv = itemView.findViewById(R.id.repo_description);
        }
    }
}
