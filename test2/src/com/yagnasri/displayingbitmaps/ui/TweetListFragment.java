/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yagnasri.displayingbitmaps.ui;

import android.annotation.TargetApi;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.test.BuildConfig;
import com.example.test.R;
import com.yagnasri.displayingbitmaps.util.ImageCache;
import com.yagnasri.displayingbitmaps.util.ImageFetcher;
import com.yagnasri.displayingbitmaps.util.Utils;

/**
 * The main fragment that powers the ImageGridActivity screen. Fairly straight forward GridView
 * implementation with the key addition being the ImageWorker class w/ImageCache to load children
 * asynchronously, keeping the UI nice and smooth and caching thumbnails for quick retrieval. The
 * cache is retained over configuration changes like orientation change so the images are populated
 * quickly if, for example, the user rotates the device.
 */
public class TweetListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "ImageGridFragment";
    private static final String IMAGE_CACHE_DIR = "thumbs";

    private int mImageThumbSize;
    private int mImageThumbSpacing;
    private TweetAdapter mAdapter;
    private ImageFetcher mImageFetcher;

    /**
     * Empty constructor as per the Fragment documentation
     */
    public TweetListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

        

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize);
        mImageFetcher.setLoadingImage(R.drawable.ic_launcher);
        mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
        mAdapter = new TweetAdapter(getActivity(),mImageFetcher);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	
    	
    	
		//Set the Layout
//		setContentView(R.layout.tweetlist);
//		demoListView = (InfiniteScrollListView) this.findViewById(R.id.infinite_listview_infinitescrolllistview);
//		handler = new Handler();
//
//		demoListView = (InfiniteScrollListView) this.findViewById(R.id.infinite_listview_infinitescrolllistview);
//
//		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		demoListView.setLoadingView(layoutInflater.inflate(R.layout.loading_view_demo, null));
//		demoListAdapter = new DemoListAdapter();
//		PageLoader loader = new PageLoader(this, demoListAdapter, mClient);
//		demoListAdapter.setPageListener(loader);
//		
//		
//		demoListView.setAdapter(demoListAdapter);
//		// Display a toast when a list item is clicked
//		demoListView.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//				handler.post(new Runnable() {
//					@Override
//					public void run() {
//						Toast.makeText(AllInOneActivity.this, demoListAdapter.getItem(position) + " " + getString(R.string.app_name), Toast.LENGTH_SHORT).show();
//					}
//				});
//			}
//		});
		
		
		
		

        final View v = inflater.inflate(R.layout.tweetlist, container, false);
        final ListView mListView = (ListView) v.findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // Pause fetcher to ensure smoother scrolling when flinging
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Before Honeycomb pause image loading on scroll to help with performance
                    if (!Utils.hasHoneycomb()) {
                        mImageFetcher.setPauseWork(true);
                    }
                } else {
                    mImageFetcher.setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {
            }
        });

        // This listener is used to get the final width of the GridView and then calculate the
        // number of columns and the width of each column. The width of each column is variable
        // as the GridView has stretchMode=columnWidth. The column width is used to set the height
        // of each view so we get nice square thumbnails.
        mListView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @TargetApi(VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
//                        if (mAdapter.getNumColumns() == 0) {
//                            final int numColumns = (int) Math.floor(
//                            		mListView.getWidth() / (mImageThumbSize + mImageThumbSpacing));
//                            if (numColumns > 0) {
//                                final int columnWidth =
//                                        (mListView.getWidth() / numColumns) - mImageThumbSpacing;
//                                mAdapter.setNumColumns(numColumns);
//                                mAdapter.setItemHeight(columnWidth);
//                                if (BuildConfig.DEBUG) {
//                                    Log.d(TAG, "onCreateView - numColumns set to " + numColumns);
//                                }
//                                if (Utils.hasJellyBean()) {
//                                	mListView.getViewTreeObserver()
//                                            .removeOnGlobalLayoutListener(this);
//                                } else {
//                                    mListView.getViewTreeObserver()
//                                            .removeGlobalOnLayoutListener(this);
//                                }
//                            }
//                        }
                    }
                });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.onScrollNext();
        mImageFetcher.setExitTasksEarly(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        mImageFetcher.setPauseWork(false);
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN)
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//        final Intent i = new Intent(getActivity(), ImageDetailActivity.class);
//        i.putExtra(ImageDetailActivity.EXTRA_IMAGE, (int) id);
//        if (Utils.hasJellyBean()) {
//            // makeThumbnailScaleUpAnimation() looks kind of ugly here as the loading spinner may
//            // show plus the thumbnail image in GridView is cropped. so using
//            // makeScaleUpAnimation() instead.
//            ActivityOptions options =
//                    ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth(), v.getHeight());
//            getActivity().startActivity(i, options.toBundle());
//        } else {
//            startActivity(i);
//        }
    	//TODO when a tweet is clicked
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       // inflater.inflate(R.menu.main_menu, menu);
    	super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.clear_cache:
//                mImageFetcher.clearCache();
//                Toast.makeText(getActivity(), R.string.clear_cache_complete_toast,
//                        Toast.LENGTH_SHORT).show();
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }


}