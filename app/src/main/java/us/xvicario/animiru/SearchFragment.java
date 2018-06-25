package us.xvicario.animiru;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends Fragment {

    @BindView(R.id.search_text) EditText searchText;
    @BindView(R.id.search_button) ImageButton searchButton;
    @BindView(R.id.search_results) RecyclerView searchResults;

    AnimeListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.search_anime, container, false);
        ButterKnife.bind(this, fragmentView);
        searchResults.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        listAdapter = new AnimeListAdapter();
        searchResults.setAdapter(listAdapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NineAnime.searchAnime(searchText.getText().toString());
            }
        });
        return fragmentView;
    }

}
