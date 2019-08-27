package com.yelp;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.readystatesoftware.chuck.internal.ui.MainActivity;
import com.yelp.common.TestComponentRule;
import com.yelp.util.ErrorTestUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private final TestComponentRule componentRule =
            new TestComponentRule(InstrumentationRegistry.getTargetContext());
    private final ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    // TestComponentRule needs to go first to make sure the Dagger ApplicationTestComponent is set
    // in the Application before any Activity is launched.
    @Rule
    public TestRule chain = RuleChain.outerRule(componentRule).around(mainActivityTestRule);



    @Test
    public void checkErrorViewDisplays() {
        mainActivityTestRule.launchActivity(null);
        ErrorTestUtil.checkErrorViewsDisplay();
    }


}
