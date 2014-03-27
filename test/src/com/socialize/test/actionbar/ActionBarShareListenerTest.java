/*
 * Copyright (c) 2012 Socialize Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.socialize.test.actionbar;

import android.app.Activity;
import android.app.Dialog;
import com.socialize.ConfigUtils;
import com.socialize.ShareUtils;
import com.socialize.SocializeAccess;
import com.socialize.api.action.like.SocializeLikeUtils;
import com.socialize.config.SocializeConfig;
import com.socialize.entity.Like;
import com.socialize.listener.like.LikeGetListener;
import com.socialize.test.util.TestUtils;
import com.socialize.ui.actionbar.ActionBarLayoutView;
import com.socialize.ui.actionbar.ActionBarView;
import com.socialize.ui.actionbar.OnActionBarShareEventListener;
import com.socialize.ui.share.SharePanelView;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * @author Jason Polites
 *
 */
@Deprecated
public class ActionBarShareListenerTest extends ActionBarTest {

	@Override
	public boolean isManual() {
		return false;
	}
	
	@Override
	protected boolean overrideShareUtils() {
		return false;
	}

	public void testOnActionBarShareEventListenerIsCalledOnDialogDisplay() throws Throwable {

        Activity activity = TestUtils.getActivity(this);

        SocializeConfig config = ConfigUtils.getConfig(activity);

        config.setProperty(SocializeConfig.FACEBOOK_APP_ID, "");
		config.setProperty(SocializeConfig.TWITTER_CONSUMER_KEY, "");
		config.setProperty(SocializeConfig.TWITTER_CONSUMER_SECRET, "");
		config.setProperty(SocializeConfig.SOCIALIZE_REQUIRE_AUTH, "false");
		
		ShareUtils.preloadShareDialog(activity);
		ShareUtils.preloadLinkDialog(activity);
		
		final ActionBarView actionBar = TestUtils.findView(activity, ActionBarView.class, 5000);
		final ActionBarLayoutView actionBarView = TestUtils.findView(activity, ActionBarLayoutView.class, 10000);

        assertNotNull("Action bar was null", actionBar);
        assertNotNull("Action bar view was null", actionBarView);
		
		final Like like = new Like();
		
		like.setId(-1L);
		like.setEntity(entity);
		
		final CountDownLatch shareLatch = new CountDownLatch(1);
		final CountDownLatch dialogLatch = new CountDownLatch(1);
		
		final SocializeLikeUtils mockLikeUtils = new SocializeLikeUtils() {
			@Override
			public void getLike(Activity context, String entityKey, LikeGetListener listener) {
				listener.onGet(like);
			}
		};
		
		actionBar.setOnActionBarEventListener(new OnActionBarShareEventListener() {

			@Override
			public void onShow(Dialog dialog, SharePanelView dialogView) {
				TestUtils.addResult(0, dialog);
                TestUtils.addResult(1, dialogView);
				dialogLatch.countDown();
			}
		});
		
		SocializeAccess.setLikeUtilsProxy(mockLikeUtils);
		
		// Simulate share
		this.runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				assertTrue(actionBarView.getShareButton().performClick());
				shareLatch.countDown();
			}
		});
		
		assertTrue(shareLatch.await(30, TimeUnit.SECONDS));

		dialogLatch.await(30, TimeUnit.SECONDS);
		
		SharePanelView dialogView = TestUtils.getResult(1);
        Dialog dialog = TestUtils.getResult(0);
		
		assertNotNull(dialogView);
        assertNotNull(dialog);

        dialog.dismiss();
	}
}
