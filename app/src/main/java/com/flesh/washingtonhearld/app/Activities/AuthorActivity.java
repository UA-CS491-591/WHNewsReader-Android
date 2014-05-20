package com.flesh.washingtonhearld.app.Activities;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.flesh.washingtonhearld.app.Adapters.StoryAdapter;
import com.flesh.washingtonhearld.app.Objects.DtoStory;
import com.flesh.washingtonhearld.app.Objects.User;
import com.flesh.washingtonhearld.app.R;
import com.flesh.washingtonhearld.app.WH_Constants;
import com.flesh.washingtonhearld.app.WashingtonHearldSingleton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

public class AuthorActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);

        User Author = getIntent().getParcelableExtra(WH_Constants.AUTHOR_KEY);
        RestoreActionBar();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, PlaceholderFragment.fragmentInstance(Author))
                    .commit();
        }
    }

    private void RestoreActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
               finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String AUTHOR_KEY = "Author for Arguments";
        private static final String STORIES_KEY = "Stories for Arguments";
        private User Author;

        @InjectView(R.id.cimg_Author)
        protected CircleImageView imgAuthor;
        @InjectView(R.id.tvAuthorPosition)
        protected TextView tvAuthorPosition;
        @InjectView(R.id.lvAuthorStories)
        protected ListView lvAuthorStories;

        private String url;
        private RequestQueue queue;
        private StoryAdapter adapter;
        ArrayList<DtoStory> stories = new ArrayList<DtoStory>();

        public static Fragment fragmentInstance(User author) {
            Fragment frag = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putParcelable(AUTHOR_KEY, author);
            frag.setArguments(args);
            return frag;
        }


        public PlaceholderFragment() {

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if(savedInstanceState!=null){
                if(savedInstanceState.containsKey(AUTHOR_KEY)){
                    Author = savedInstanceState.getParcelable(AUTHOR_KEY);
                }
            }else{
                Author = getArguments().getParcelable(AUTHOR_KEY);
            }
            url = getString(R.string.base_url) + getString(R.string.stories_by_author) + getString(R.string.token_key) +
                    WashingtonHearldSingleton.getInstance().accessToken + "&" + getString(R.string.author_id_key)+Author.getId();
            adapter = new StoryAdapter(getActivity(), stories);
            queue = Volley.newRequestQueue(getActivity());
            if(savedInstanceState!=null){
                if(savedInstanceState.containsKey(STORIES_KEY)){
                    stories = savedInstanceState.getParcelableArrayList(STORIES_KEY);
                    setUI(stories);
                }
            }else{
                GetAuthorStories();
            }
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_author, container, false);
            ButterKnife.inject(this,rootView);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            lvAuthorStories.setAdapter(adapter);
            SetUI(Author);
        }

        private void SetUI(User author) {
            getActivity().getActionBar().setTitle(author.getFirstName()+ " "+ author.getLastName());
            Picasso.with(getActivity()).load(author.getImageUrl()).placeholder(R.drawable.ic_profile_placeholder).noFade().into(imgAuthor);
            tvAuthorPosition.setText(author.getPosition());
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putParcelable(AUTHOR_KEY, Author);
            outState.putParcelableArrayList(STORIES_KEY, stories);
        }

        // Get Most Recent Stories
        private void GetAuthorStories() {
            JsonArrayRequest storiesRequest = new JsonArrayRequest(url, getStoriesSuccessListener(), createErrorListener());
            queue.add(storiesRequest);
        }

        private Response.Listener<JSONArray> getStoriesSuccessListener() {
            return new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    stories = new ArrayList<DtoStory>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            stories.add(convertStory(response.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    setUI(stories);
                }
            };
        }

        private void setUI(ArrayList<DtoStory> stories) {
            refreshArrayAdapterWithNewObjects(adapter, stories, true);
        }

        private DtoStory convertStory(JSONObject obj) {
            String story = obj.toString();
            return new Gson().fromJson(story, DtoStory.class);
        }

        private Response.ErrorListener createErrorListener() {
            return new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("Recent", error.getLocalizedMessage());
                }
            };
        }


        /**
         * Refreshes ArrayAdapter with objects given.
         *
         * @param aa
         * @param objs
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        public void refreshArrayAdapterWithNewObjects(ArrayAdapter aa, ArrayList objs, Boolean clearWhatisInAdapter) {
            if (clearWhatisInAdapter) {
                aa.clear();
            }
            try {
                for (int i = 0; i < objs.size(); i++) {
                    aa.add(objs.get(i));
                }
                aa.notifyDataSetChanged();
            } catch (NullPointerException e) {

                Toast.makeText(getActivity(),
                        "No data please check internet conntection",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
