package com.flesh.washingtonhearld.app.Activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.flesh.washingtonhearld.app.Adapters.StoryAdapter;
import com.flesh.washingtonhearld.app.Objects.DtoStory;
import com.flesh.washingtonhearld.app.R;
import com.flesh.washingtonhearld.app.WashingtonHearldSingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import uk.co.senab.actionbarpulltorefresh.library.viewdelegates.AbsListViewDelegate;

import static android.widget.AdapterView.OnItemClickListener;


public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements OnItemClickListener, OnRefreshListener {

        @InjectView(R.id.ptr_layout)
        protected PullToRefreshLayout mPullToRefreshLayout;
        private String url;
        private RequestQueue queue;
        @InjectView(R.id.gvStories)
        protected GridView gvStories;
        private StoryAdapter adapter;
        ArrayList<DtoStory> stories = new ArrayList<DtoStory>();

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            url = getString(R.string.base_url) + getString(R.string.recent) + getString(R.string.token_key) + WashingtonHearldSingleton.getInstance().accessToken;
            adapter = new StoryAdapter(getActivity(), stories);
            queue = Volley.newRequestQueue(getActivity());
            if (savedInstanceState == null) {
                GetMostRecentStories();
            }else{
                if(savedInstanceState.containsKey("Stories")) {
                    stories = savedInstanceState.getParcelableArrayList("Stories");
                    setUI(stories);
                }else{
                    GetMostRecentStories();
                }
            }
        }


        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putParcelableArrayList("Stories", stories);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            ButterKnife.inject(this, rootView);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            gvStories.setAdapter(adapter);
            gvStories.setOnItemClickListener(this);
            ViewGroup viewGroup = (ViewGroup) view;

            // We need to create a PullToRefreshLayout manually
            mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());

            // We can now setup the PullToRefreshLayout
            ActionBarPullToRefresh.from(getActivity())

                    // We need to insert the PullToRefreshLayout into the Fragment's ViewGroup
                    .insertLayoutInto(viewGroup)

                            // We need to mark the ListView and it's Empty View as pullable
                            // This is because they are not dirent children of the ViewGroup
                    .theseChildrenArePullable(gvStories, gvStories.getEmptyView())

                            // We can now complete the setup as desired
                    .listener(this)
            .options(Options.create()
                    // Here we make the refresh scroll distance to 75% of the GridView height
                    .scrollDistance(.5f).build())
            .setup(mPullToRefreshLayout);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String storyId = stories.get(position).getStoryId();
            Intent i = new Intent(getActivity(), StoryActivity.class);
            i.putExtra("Story", storyId);
            startActivity(i);
        }

        // Get Most Recent Stories
        private void GetMostRecentStories() {
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
            if(mPullToRefreshLayout!=null) {
                if (mPullToRefreshLayout.isRefreshing()) {
                    mPullToRefreshLayout.setRefreshComplete();
                }
            }
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


        @Override
        public void onRefreshStarted(View view) {
            GetMostRecentStories();
        }
    }
}
