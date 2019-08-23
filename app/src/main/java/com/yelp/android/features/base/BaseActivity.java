package com.yelp.android.features.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LongSparseArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicLong;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.yelp.android.YelpApplication;
import com.yelp.android.R;
import com.yelp.android.features.common.ErrorView;
import com.yelp.android.injection.component.ActivityComponent;
import com.yelp.android.injection.component.ConfigPersistentComponent;
import com.yelp.android.injection.component.DaggerConfigPersistentComponent;
import com.yelp.android.injection.module.ActivityModule;

import javax.annotation.Nullable;

import timber.log.Timber;


/**
 * Abstract activity that every other Activity in this application must implement. It provides the
 * following functionality: - Handles creation of Dagger components and makes sure that instances of
 * ConfigPersistentComponent are kept across configuration changes. - Set up and handles a
 * GoogleApiClient instance that can be used to access the Google sign in api. - Handles signing out
 * when an authentication error event is received.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final LongSparseArray<ConfigPersistentComponent> componentsArray =
            new LongSparseArray<>();

    private long activityId;

    @Nullable
    @BindView(R.id.title) TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);

        setSupportActionBar(findViewById(R.id.toolbar));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(getSupportActionBar() !=null) getSupportActionBar().setElevation(0);
        }

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("");
        }

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        activityId =
                savedInstanceState != null
                        ? savedInstanceState.getLong(KEY_ACTIVITY_ID)
                        : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent;
        if (componentsArray.get(activityId) == null) {

            // manage token usage
            String token = YelpApplication.get(this).getComponent().apiManager().getToken();

            Timber.i("Creating new ConfigPersistentComponent id=%d", activityId);
            configPersistentComponent =
                    DaggerConfigPersistentComponent.builder()
                            .appComponent(YelpApplication.get(this).getComponent(token))
                            .build();

            componentsArray.put(activityId, configPersistentComponent);
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", activityId);
            configPersistentComponent = componentsArray.get(activityId);
        }
        ActivityComponent activityComponent =
                configPersistentComponent.activityComponent(new ActivityModule(this));
        inject(activityComponent);
        attachView();
        initComponents();
    }

    protected abstract int getLayout();

    protected abstract void inject(ActivityComponent activityComponent);

    protected abstract void attachView();

    protected abstract void detachPresenter();

    /**
     * Initialize view components here
     */
    protected abstract void initComponents();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, activityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", activityId);
            componentsArray.remove(activityId);
        }
        detachPresenter();
        super.onDestroy();
    }

    /**
     * Show toast message
     * @param message
     */
    protected void showMessage(int message) {
        Toast.makeText(this, getString(message), Toast.LENGTH_LONG).show();
    }

    /**
     * Show toast message
     * @param message
     */
    protected void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Class to simplify calling new Intent Activity
     * @param tClass class to call
     * @param extras bundle extras if any
     */
    protected void callActivity(Class<? extends BaseActivity> tClass, Bundle extras) {
        callActivity(tClass, extras, false);
    }

    /**
     * Class to simplify calling new Intent Activity
     * @param tClass class to call
     * @param extras bundle extras if any
     * @param animate
     */
    protected void callActivity(Class<? extends BaseActivity> tClass, Bundle extras, boolean animate) {
        Intent intent = new Intent(getApplicationContext(), tClass);
        if(extras != null) intent.putExtras(extras);
        startActivity(intent);
        if(animate)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//        overridePendingTransition(R.anim.slide_from_left,R.anim.to_right);
    }

    /**
     * Set action header title
     * Make sure TextView R.id.title is defined in your xml layout
     * @param title
     */
    protected void setHeaderText(String title) {
        if(titleView == null) return;
        titleView.setText(title);
    }

    /**
     * This handles the fragment switches and title changes.
     * @param fragment
     * @param title
     */
    public void moveToFragment(Fragment fragment, String title) {
        setHeaderText(title);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_layout, fragment, fragment.getClass().getSimpleName()).commit();
    }

    /**
     * Set action header title
     * @param title
     */
    public void setActionTitle(String title) {
        setTitle(title);
    }

    /**
     * This displays prompt to confirm user logout
     */
    public void confirmLogout() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.confirm_logout))
                .setPositiveButton(getString(R.string.dialog_action_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                }).setNegativeButton(getString(R.string.dialog_action_cancel), null).show();
    }

    /**
     * Called when the session is logged out.
     */
    public void logoutSession() {
        Toast.makeText(this, R.string.invalid_token, Toast.LENGTH_LONG).show();
        logout();
    }

    /**
     * Default logout. Ends the activity. Login class is then displayed.
     */
    public void logout() {
        finish();
    }

    /**
     * Default behavior for hiding/showing progress bar with recyclerview components
     * @param adapter
     * @param progressBar
     * @param swipeRefreshLayout
     * @param errorView
     * @param recyclerView
     * @param show
     */
    public void showProgress(RecyclerView.Adapter adapter, ProgressBar progressBar, SwipeRefreshLayout swipeRefreshLayout, ErrorView errorView, RecyclerView recyclerView, boolean show) {

        if(adapter == null || progressBar == null || swipeRefreshLayout == null || errorView == null || recyclerView == null) return;

        if (show) {
            if(adapter == null) {
                progressBar.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            } else if (recyclerView.getVisibility() == View.VISIBLE
                    && adapter.getItemCount() > 0) {
                swipeRefreshLayout.setRefreshing(true);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }
            errorView.setVisibility(View.GONE);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Default behavior for progress bar show/hide
     * @param progressBar
     * @param show
     */
    public void showProgress(ProgressBar progressBar, boolean show) {
        if(progressBar == null) return;
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Default behavior for showing/hide error view
     * @param error
     * @param recyclerView
     * @param swipeRefreshLayout
     * @param errorView
     */
    public void showError(Throwable error, RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout, ErrorView errorView) {
        if(recyclerView != null) recyclerView.setVisibility(View.GONE);
        if(swipeRefreshLayout != null) swipeRefreshLayout.setVisibility(View.GONE);
        if(errorView != null) errorView.setVisibility(View.VISIBLE);
        Timber.e(error, "There was an error retrieving the devices.");
    }

}
