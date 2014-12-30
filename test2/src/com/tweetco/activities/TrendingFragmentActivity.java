package com.tweetco.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.tweetco.R;
import com.tweetco.tweetlist.HomeFeedMode;
import com.tweetco.tweetlist.TrendingFeedMode;
import com.tweetco.tweets.TweetCommonData;
import com.tweetco.utility.UiUtility;
import com.yagnasri.displayingbitmaps.ui.TweetAdapter;
import com.yagnasri.displayingbitmaps.ui.TweetListFragment;

public class TrendingFragmentActivity extends FragmentActivity 
{
	private String mTag = null;
	
	
	/**
     * Empty constructor as per the Fragment documentation
     */
    public TrendingFragmentActivity() {}
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.trendingfragmentactivity);
       
        mTag = getIntent().getExtras().getString(Constants.TREND_TAG_STR);
    	
    	if(UiUtility.getView(this, R.id.trendingTweetsListFragmentContainer) != null)
		{
			final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            TweetListFragment tweetListFragment = new TweetListFragment();
            Bundle bundle = new Bundle();
			TrendingFeedMode mode = new TrendingFeedMode(mTag);
			bundle.putParcelable(Constants.TWEET_LIST_MODE, mode);
			
            bundle.putBoolean("hideFooter", true);
            tweetListFragment.setArguments(bundle);
            ft.replace(R.id.trendingTweetsListFragmentContainer, tweetListFragment);
            ft.commit();
		}
    }
}