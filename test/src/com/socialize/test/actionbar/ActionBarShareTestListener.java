package com.socialize.test.actionbar;

import android.app.Activity;
import com.socialize.SocializeAccess;
import com.socialize.api.action.like.SocializeLikeUtils;
import com.socialize.api.action.share.SocialNetworkShareListener;
import com.socialize.api.action.share.SocializeShareUtils;
import com.socialize.entity.Entity;
import com.socialize.entity.Like;
import com.socialize.listener.like.LikeGetListener;
import com.socialize.test.util.TestUtils;
import com.socialize.ui.actionbar.ActionBarLayoutView;
import com.socialize.ui.actionbar.ActionBarView;
import com.socialize.ui.actionbar.OnActionBarEventListener;
import com.socialize.ui.actionbar.OnActionBarEventListener.ActionBarEvent;
import com.socialize.ui.share.ShareDialogListener;
import org.mockito.Mockito;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Deprecated
public class ActionBarShareTestListener extends ActionBarTest {
	
	@Override
	public boolean isManual() {
		return false;
	}

	public void testShareButtonCallsActionBarListener() throws Throwable {
		
        Activity activity = TestUtils.getActivity(this);
		
		final Like mockLike = new Like();
		mockLike.setEntity(entity);
		mockLike.setId(0L);
		
		SocializeLikeUtils likeUtils = new SocializeLikeUtils() {
			@Override
			public void getLike(Activity context, String entityKey, LikeGetListener listener) {
				listener.onGet(mockLike);
			}
        };
		
		SocializeShareUtils shareUtils = new SocializeShareUtils() {
			@Override
			public void showShareDialog(Activity context, Entity entity, int options, SocialNetworkShareListener socialNetworkListener, ShareDialogListener dialogListener) {
				
			}
		};
		
		SocializeAccess.setShareUtilsProxy(shareUtils);
		SocializeAccess.setLikeUtilsProxy(likeUtils);
		
		final ActionBarLayoutView actionBar = TestUtils.findView(activity, ActionBarLayoutView.class, 10000);	
		final ActionBarView actionBarView = TestUtils.findView(activity, ActionBarView.class, 10000);

        assertNotNull("Action bar was null", actionBar);
        assertNotNull("Action bar view was null", actionBarView);

		OnActionBarEventListener listener = Mockito.mock(OnActionBarEventListener.class);
		
		Mockito.when(listener.onClick(actionBarView, ActionBarEvent.SHARE)).thenReturn(false);
		
		actionBar.setOnActionBarEventListener(listener);	
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		// Junit test runs in non-ui thread
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				assertTrue(actionBar.getShareButton().performClick());
				latch.countDown();
			}
		});
		
		assertTrue(latch.await(10, TimeUnit.SECONDS));
	}
	
}
