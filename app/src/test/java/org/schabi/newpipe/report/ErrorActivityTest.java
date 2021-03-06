package org.schabi.newpipe.report;

import android.app.Activity;

import org.junit.Test;
import com.yolomate.Zentube.MainActivity;
import com.yolomate.Zentube.RouterActivity;
import com.yolomate.Zentube.fragments.detail.VideoDetailFragment;
import com.yolomate.Zentube.report.ErrorActivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link ErrorActivity}
 */
public class ErrorActivityTest {
    @Test
    public void getReturnActivity() throws Exception {
        Class<? extends Activity> returnActivity;
        returnActivity = ErrorActivity.getReturnActivity(MainActivity.class);
        assertEquals(MainActivity.class, returnActivity);

        returnActivity = ErrorActivity.getReturnActivity(RouterActivity.class);
        assertEquals(RouterActivity.class, returnActivity);

        returnActivity = ErrorActivity.getReturnActivity(null);
        assertNull(returnActivity);

        returnActivity = ErrorActivity.getReturnActivity(Integer.class);
        assertEquals(MainActivity.class, returnActivity);

        returnActivity = ErrorActivity.getReturnActivity(VideoDetailFragment.class);
        assertEquals(MainActivity.class, returnActivity);
    }



}