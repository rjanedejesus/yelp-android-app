package com.yelp.android.features.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LongSparseArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yelp.android.YelpApplication;
import com.yelp.android.R;
import com.yelp.android.features.common.ErrorView;
import com.yelp.android.injection.component.ConfigPersistentComponent;
import com.yelp.android.injection.component.DaggerConfigPersistentComponent;
import com.yelp.android.injection.component.FragmentComponent;
import com.yelp.android.injection.module.FragmentModule;

import java.util.concurrent.atomic.AtomicLong;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Abstract Fragment that every other Fragment in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent are kept
 * across configuration changes.
 */
public abstract class BaseFragment extends Fragment {

    private static final String KEY_FRAGMENT_ID = "KEY_FRAGMENT_ID";
    private static final LongSparseArray<ConfigPersistentComponent> componentsArray =
            new LongSparseArray<>();
    private static final AtomicLong NEXT_ID = new AtomicLong(0);

    private long fragmentId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create the FragmentComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        fragmentId =
                savedInstanceState != null
                        ? savedInstanceState.getLong(KEY_FRAGMENT_ID)
                        : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent;
        if (componentsArray.get(fragmentId) == null) {

            // manage token usage
            String token = YelpApplication.get(getActivity()).getComponent().apiManager().getToken();

            configPersistentComponent =
                    DaggerConfigPersistentComponent.builder()
                            .appComponent(YelpApplication.get(getActivity()).getComponent(token))
                            .build();
            componentsArray.put(fragmentId, configPersistentComponent);
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", fragmentId);
            configPersistentComponent = componentsArray.get(fragmentId);
        }
        FragmentComponent fragmentComponent =
                configPersistentComponent.fragmentComponent(new FragmentModule(this));
        inject(fragmentComponent);
        attachView();
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, view);
        initComponents();
        return view;
    }

    protected abstract int getLayout();

    protected abstract void inject(FragmentComponent fragmentComponent);

    protected abstract void attachView();

    protected abstract void detachPresenter();

    /**
     * Initialize view components here
     */
    protected abstract void initComponents();

    protected abstract void logoutComponents();

    /**
     * Show message as toast
     * @param message
     */
    protected void showMessage(int message) {
        Toast.makeText(getContext(), getString(message), Toast.LENGTH_LONG).show();
    }

    /**
     * Show message as toast
     * @param message
     */
    protected void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_FRAGMENT_ID, fragmentId);
    }

    @Override
    public void onDestroy() {
        if (!getActivity().isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", fragmentId);
            componentsArray.remove(fragmentId);
        }
        detachPresenter();
        super.onDestroy();
    }

    /**
     * Call new activity. No animation.
     * @param tClass
     * @param extras
     */
    protected void callActivity(Class<? extends BaseActivity> tClass, Bundle extras) {
        callActivity(tClass, extras, false);
    }

    /**
     * Call new activity. No animation. With request code.
     * @param tClass
     * @param extras
     */
    protected void callActivity(Class<? extends BaseActivity> tClass, Bundle extras, int requestCode) {
        callActivity(tClass, extras, false, requestCode);
    }

    /**
     * Call new activity
     * @param tClass
     * @param extras
     * @param animate
     */
    protected void callActivity(Class<? extends BaseActivity> tClass, Bundle extras, boolean animate) {
        // this is the case with no result code
        callActivity(tClass, extras, animate, 0);
    }

    /**
     * Call new activity
     * @param tClass
     * @param extras
     * @param animate
     */
    protected void callActivity(Class<? extends BaseActivity> tClass, Bundle extras, boolean animate, int requestCode) {
        Intent intent = new Intent(getContext(), tClass);
        if(extras != null) intent.putExtras(extras);

        if(requestCode > 0) startActivityForResult(intent, requestCode);
        else startActivity(intent);

        //TODO disabled animation for now
//        if(animate)
//            getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//            getActivity().overridePendingTransition(R.anim.slide_from_left,R.anim.to_right);
    }

    /**
     * This handles the fragment switches
     * @param fragment
     */
    public void moveToFragment(Fragment fragment) {
        moveToFragmentLayout(fragment, R.id.content_layout);
    }

    /**
     * This handles the fragment switches
     * @param fragment
     */
    public void moveToFragment(Fragment fragment, int fragmentId) {
        moveToFragmentLayout(fragment, fragmentId);
    }

    /**
     * This handles the fragment switches. Specify layout id to put this fragment.
     */
    public void moveToFragmentLayout(Fragment fragment, int layoutId) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(layoutId, fragment, fragment.getClass().getSimpleName()).commit();
    }

    /**
     * This displays prompt to confirm user logout
     */
    public void confirmLogout() {
        new AlertDialog.Builder(getContext())
                .setMessage(getString(R.string.confirm_logout))
                .setPositiveButton(getString(R.string.dialog_action_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                        logoutComponents();
                    }
                }).setNegativeButton(getString(R.string.dialog_action_cancel), null).show();
    }

    /**
     * Called when the session is logged out.
     */
    public void logoutSession() {
        Toast.makeText(getActivity(), R.string.invalid_token, Toast.LENGTH_LONG).show();
        logout();
    }

    /**
     * Default logout. Ends the activity. Login class is then displayed.
     */
    public void logout() {
        getActivity().finish();
//        callActivity(LoginActivity.class, null);
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
