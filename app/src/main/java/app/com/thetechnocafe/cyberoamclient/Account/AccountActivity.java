package app.com.thetechnocafe.cyberoamclient.Account;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import app.com.thetechnocafe.cyberoamclient.R;
import app.com.thetechnocafe.cyberoamclient.Utils.ValueUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountActivity extends AppCompatActivity implements IAccountView {
    private IAccountPresenter mPresenter;
    private Handler mUIUpdateHandler;
    private Runnable mUIRunnable;

    @BindView(R.id.logged_in_username)
    TextView mLoggedInUsername;
    @BindView(R.id.logout_button)
    Button mLogoutButton;
    @BindView(R.id.data_consumed_text_view)
    TextView mDataConsumedTextView;
    @BindView(R.id.logged_in_time)
    TextView mLoggedInTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //Bind butter knife
        ButterKnife.bind(this);

        //Initialize the presenter
        mPresenter = new AccountPresenter(this);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void onViewReady(boolean isLoggedIn) {
        setUpOnClickListeners();
        setUpAndStartUIHandler();
    }

    //Set up onClickListeners
    private void setUpOnClickListeners() {
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.logout();
            }
        });
    }

    @Override
    public void setInitialData(String username, double dataUsed, String loggedInTime) {
        mLoggedInUsername.setText(username);
        mDataConsumedTextView.setText(String.valueOf(dataUsed));
        mLoggedInTimeTextView.setText(loggedInTime);
    }

    @Override
    public void onLogout() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onViewReady();
    }

    @Override
    public void onBackPressed() {
        if (!mPresenter.isLoggedIn()) {
            super.onBackPressed();
        }
    }

    /**
     * Update the Data textView
     */
    private void setUpAndStartUIHandler() {
        mUIUpdateHandler = new Handler();

        //Create new runnable
        mUIRunnable = new Runnable() {
            @Override
            public void run() {
                //Ask presenter for refresh
                mPresenter.refreshUIDate();

                //Call the runnable again
                mUIUpdateHandler.postDelayed(mUIRunnable, ValueUtils.UI_UPDATE_INTERVAL);
            }
        };

        //Add to handler
        mUIUpdateHandler.postDelayed(mUIRunnable, ValueUtils.UI_UPDATE_INTERVAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Remove handler callback
        if (mUIUpdateHandler != null) {
            mUIUpdateHandler.removeCallbacks(mUIRunnable);
        }
    }
}
