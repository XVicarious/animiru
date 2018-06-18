package us.xvicario.animiru;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;

class AnimeListAdapter extends RecyclerView.Adapter<AnimeListAdapter.AnimeViewHolder> {

    public ArrayList<Anime> anime;

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnimeViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeViewHolder holder, int position) {
        holder.animeName.setText(anime.get(position).title);
    }

    @Override
    public int getItemCount() {
        return anime.size();
    }

    class AnimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //@BindView(R.id.anime_poster)
        //ImageView animePoster;
        @BindView(R.id.anime_title)
        TextView animeName;

        AnimeViewHolder(ViewGroup viewGroup) {
            super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.anime_result, viewGroup, false));
            //animePoster = itemView.findViewById(R.id.anime_poster);
            animeName = itemView.findViewById(R.id.anime_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            Fragment animePageFragment = new AnimePageFragment();
            FragmentManager fragmentManager = ((MainActivity) this.itemView.getContext()).getFragmentManager();
            FragmentTransaction transaction;
            transaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("ANIME_URL", anime.get(position).url.toString());
            animePageFragment.setArguments(bundle);
            transaction.add(R.id.fragment_container, animePageFragment, "ANIME_EPISODES");
            transaction.addToBackStack(null);
            transaction.commit();
            //new FetchAnimeTask().execute(anime.get(position));
        }

        private class FetchAnimeTask extends AsyncTask<Anime, Void, Void> {

            @Override
            protected Void doInBackground(Anime... animes) {
                NineAnime.fetchAnime(animes[0]);
                return null;
            }
        }

    }

}
