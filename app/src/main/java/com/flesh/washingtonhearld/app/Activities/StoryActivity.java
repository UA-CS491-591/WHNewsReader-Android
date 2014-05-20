package com.flesh.washingtonhearld.app.Activities;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.flesh.washingtonhearld.app.MyDateUtils;
import com.flesh.washingtonhearld.app.Objects.DtoStory;
import com.flesh.washingtonhearld.app.R;
import com.flesh.washingtonhearld.app.VolleyUtils.GsonRequest;
import com.flesh.washingtonhearld.app.WH_Constants;
import com.flesh.washingtonhearld.app.WashingtonHearldSingleton;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        getActionBar().setTitle("");
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, PlaceholderFragment.fragmentInstance(getIntent().getStringExtra("Story")))
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public static class PlaceholderFragment extends Fragment {

        private static final String TEXT_SIZE_KEY = "text size key";

        public static class TextSize {
            static int TextSmall = 0;
            static int TextMedium = 1;
            static int TextLarge = 2;
        }

        private static final String STORY = "Story in args";
        @InjectView(R.id.tvStoryBody)
        protected TextView tvStoryBody;
        @InjectView(R.id.tvStoryAuthor)
        protected TextView tvStoryAuthor;
        @InjectView(R.id.tvHeader)
        protected TextView tvStoryHeader;
        @InjectView(R.id.tvTitle)
        protected TextView tvStoryTitle;
        @InjectView(R.id.tvSubtitle)
        protected TextView tvStorySubTitle;
        @InjectView(R.id.storyHeaderImg)
        protected ImageView imgStory;
        @InjectView(R.id.pbStory)
        protected ProgressBar pbStory;
        @InjectView(R.id.authorClick)
        protected RelativeLayout authorClick;
        private RequestQueue queue;
        private WashingtonHearldSingleton instance;
        private String url;
        private Point p;
        private DtoStory mStory;
        private int mTextSize = 0;

        public static PlaceholderFragment fragmentInstance(String storyId) {
            PlaceholderFragment frag = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(STORY, storyId);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            instance = WashingtonHearldSingleton.getInstance();
            queue = Volley.newRequestQueue(getActivity());
            url = getString(R.string.base_url) + getString(R.string.story_id)
                    + getString(R.string.token_key) + instance.accessToken + "&"
                    + getString(R.string.story_key) + getArguments().getString(STORY);
            p = getWindowDisplaySize(getActivity().getWindowManager());

        }

        public PlaceholderFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.story, menu);
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            if (id == R.id.action_text_size) {
                if (mTextSize < 2) {
                    mTextSize++;
                } else {
                    mTextSize = 0;
                }
                SetTextSize(mTextSize);
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_story, container, false);
            ButterKnife.inject(this, rootView);
            SetTextSize(mTextSize);
            return rootView;
        }

        private void SetTextSize(int mTextSize) {
            if (mTextSize == TextSize.TextSmall) {
                tvStoryBody.setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
            } else if (mTextSize == TextSize.TextMedium) {
                tvStoryBody.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
            } else if (mTextSize == TextSize.TextLarge) {
                tvStoryBody.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
            }
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            if (savedInstanceState == null) {
                GetStory();
            } else {
                if (savedInstanceState.containsKey(STORY))
                    mStory = savedInstanceState.getParcelable(STORY);
                if (savedInstanceState.containsKey(TEXT_SIZE_KEY))
                    mTextSize = savedInstanceState.getInt(TEXT_SIZE_KEY);
                SetUI(mStory);
                SetTextSize(mTextSize);
            }
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putParcelable(STORY, mStory);
            outState.putInt(TEXT_SIZE_KEY, mTextSize);
        }

        // LOGIN to the washington hearld system.
        private void GetStory() {
            GsonRequest<DtoStory> storyRequest = new GsonRequest<DtoStory>
                    (Request.Method.GET, url, "", DtoStory.class, createStorySuccessListener(), createErrorListener());
            queue.add(storyRequest);
        }

        private Response.Listener<DtoStory> createStorySuccessListener() {
            return new Response.Listener<DtoStory>() {
                @Override
                public void onResponse(DtoStory response) {
                    mStory = response;
                    SetUI(mStory);
                }
            };
        }

        private void SetUI(final DtoStory story) {
            pbStory.setVisibility(View.GONE);
            tvStoryAuthor.setText(story.getAuthor().getFirstName() + story.getAuthor().getLastName());
            tvStoryTitle.setText(story.getTitle());
            tvStorySubTitle.setText(story.getSubtitle());
            tvStoryBody.setText(story.getBody());
            tvStoryHeader.setText(story.getCategory().getName() + " | " + MyDateUtils.TimeFromTodayAccuracyToTheMinute(story.getDatePublished()));
            Picasso.with(getActivity()).load(story.getImageUrl()).resize(p.x, p.x).centerInside().into(imgStory);
            getActivity().getActionBar().setTitle(story.getCategory().getName());
            getActivity().getActionBar().setSubtitle(story.getCategory().getDescription());
            authorClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), AuthorActivity.class);
                    i.putExtra(WH_Constants.AUTHOR_KEY, story.getAuthor());
                    getActivity().startActivity(i);
                }
            });
        }

        private Response.ErrorListener createErrorListener() {
            return new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("Story", error.getLocalizedMessage());
                }
            };
        }


        private static Point getWindowDisplaySize(WindowManager wm) {
            Display display;
            Point WindowDisplaySize = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                display = wm.getDefaultDisplay();
                display.getSize(WindowDisplaySize);
            } else {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(displaymetrics);
                int height = displaymetrics.heightPixels;
                int width = displaymetrics.widthPixels;
                WindowDisplaySize.set(width, height);
            }
            return WindowDisplaySize;
        }

    }


}
