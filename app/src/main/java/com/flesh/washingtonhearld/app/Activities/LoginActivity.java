package com.flesh.washingtonhearld.app.Activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.flesh.washingtonhearld.app.Objects.Login.DtoLogin;
import com.flesh.washingtonhearld.app.Objects.Login.DtoLoginResponse;
import com.flesh.washingtonhearld.app.R;
import com.flesh.washingtonhearld.app.VolleyUtils.GsonRequest;
import com.flesh.washingtonhearld.app.WashingtonHearldSingleton;
import com.google.gson.GsonBuilder;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener{

        @InjectView(R.id.etLUsername)
        protected EditText etUsername;
        @InjectView(R.id.etPassword)
        protected EditText etPassword;
        @InjectView(R.id.btnLogin)
        protected Button loginButton;
        private String url;
        private RequestQueue queue;
        private  WashingtonHearldSingleton instance;


        public PlaceholderFragment() {

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            instance = WashingtonHearldSingleton.getInstance();
            queue = Volley.newRequestQueue(getActivity());
            url = getString(R.string.base_url) + getString(R.string.login);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_login, container, false);
            ButterKnife.inject(this, rootView);
            etUsername.setText("funderwood");
            etPassword.setText("password");
            loginButton.setOnClickListener(this);
            return rootView;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnLogin:
                    loginButton.setEnabled(false);
                    Login();
                    break;
            }

        }

        // LOGIN to the washington hearld system.
        private void Login() {
            DtoLogin login = new DtoLogin(etUsername.getText().toString(), etPassword.getText().toString());
            String loginString = new GsonBuilder().create().toJson(login);
            GsonRequest<DtoLoginResponse> loginRequest = new GsonRequest<DtoLoginResponse>
                    (Request.Method.POST, url, loginString, DtoLoginResponse.class, createLoginSuccessListener(), createErrorListener());
            queue.add(loginRequest);
        }

        private Response.Listener<DtoLoginResponse> createLoginSuccessListener() {
            return new Response.Listener<DtoLoginResponse>() {
                @Override
                public void onResponse(DtoLoginResponse response) {
                    instance.accessToken = response.getAccessToken();
                    instance.user = response.getUser();
                    Intent i = new Intent(getActivity(),HomeActivity.class);
                    getActivity().startActivity(i);
                }
            };
        }

        private Response.ErrorListener createErrorListener() {
            loginButton.setEnabled(true);
            return new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("Login", error.getLocalizedMessage());
                }
            };
        }
    }
}
