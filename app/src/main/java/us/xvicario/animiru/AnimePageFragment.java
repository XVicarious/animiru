package us.xvicario.animiru;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AnimePageFragment extends Fragment {

    @BindView(R.id.anime_title) TextView animeTitle;
    @BindView(R.id.anime_episodes) RecyclerView animeEpisodes;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.list_of_episodes, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);
        final String animeUrl = getArguments().getString("ANIME_URL");
        animeEpisodes.setLayoutManager(new LinearLayoutManager(getActivity()));
        animeEpisodes.setAdapter(new EpisodeAdapter(animeUrl));
        return fragmentView;
    }

    static class FetchEpisodesTask extends AsyncTask<String, Void, Anime> {

        @Override
        protected Anime doInBackground(String... strings) {
            Anime anime;
            try {
                anime = new Anime(new URL(strings[0]));
                NineAnime.fetchAnime(anime);
                return anime;
            } catch (MalformedURLException e) {
                // todo: handle this, lol
            }
            return null;
        }

    }

    static class EpisodeAdapter extends RecyclerView.Adapter {

        private Anime anime;

        EpisodeAdapter(String animeUrl) {
            try {
                anime = new FetchEpisodesTask().execute(animeUrl).get();
            } catch (InterruptedException e) {
                // todo: handle this, lol
            } catch (ExecutionException e) {
                // todo: handle this, lol
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new EpisodeViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            EpisodeViewHolder evh = (EpisodeViewHolder) holder;
            String episodeCode = (String) anime.episodeMap.keySet().toArray()[position];
            evh.animeEpisodeCode.setText(episodeCode);
            evh.animeSourceCount.setText(String.valueOf(anime.episodeMap.get(episodeCode).size()));
        }

        @Override
        public int getItemCount() {
            return anime.episodeMap.size();
        }

        static class EpisodeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            int lastUrlIndex = -1;

            TextView animeEpisodeCode;
            TextView animeSourceCount;

            EpisodeViewHolder(ViewGroup viewGroup) {
                super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.anime_episode, viewGroup, false));
                animeEpisodeCode = itemView.findViewById(R.id.anime_episode_code);
                animeSourceCount = itemView.findViewById(R.id.anime_source_count);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                // todo: make clicks work
            }

        }

    }

}
