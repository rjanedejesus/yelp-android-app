package com.yelp;

import com.yep.android.data.DataManager;
import com.yep.android.features.main.MainMvpView;
import com.yep.android.features.main.MainPresenter;
import com.yelp.util.RxSchedulersOverrideRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by ravindra on 24/12/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule overrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    MainMvpView mockMainMvpView;
    @Mock
    DataManager mockDataManager;
    private MainPresenter mainPresenter;

    @Before
    public void setUp() {
        mainPresenter = new MainPresenter(mockDataManager);
        mainPresenter.attachView(mockMainMvpView);
    }

    @After
    public void tearDown() {
        mainPresenter.detachView();
    }


}
