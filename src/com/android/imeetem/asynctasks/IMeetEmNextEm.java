package com.android.imeetem.asynctasks;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import com.android.imeetem.IMEETEmSearchGridActivity;
import com.android.imeetem.R;
import com.android.imeetem.adapters.IMEETEmMatchesGridAdapter;
import com.android.imeetem.services.beans.IMEETEmSearchResultsBean;
import com.android.imeetem.services.impl.IMEETEmServicesUtilImpl;
import com.android.imeetem.util.IMEETEmConstants;
import com.android.imeetem.util.IMEETEmUtil;
import com.android.imeetem.util.MatchInfoBean;

public class IMeetEmNextEm extends AsyncTask<IMEETEmSearchResultsBean, Integer, String> {

	private static final String TAG = "IMeetEmNextEm";
	private IMEETEmServicesUtilImpl serviceUtil = IMEETEmServicesUtilImpl.getInstance();
	private int memId;
	private String face_id;
	private String lat;
	private String lon;
	List<IMEETEmSearchResultsBean> resultList = new ArrayList<IMEETEmSearchResultsBean>();
	List<MatchInfoBean> matchInfo = new ArrayList<MatchInfoBean>();
	private IMEETEmMatchesGridAdapter mAdapter;
	private GridView gridview;
	private Context mContext;
	private ProgressDialog progressDialog;
	private IMEETEmSearchGridActivity activity;

	public IMeetEmNextEm(int memId, String face_id, String lat, String lon,
			IMEETEmMatchesGridAdapter mAdapter, GridView gridview, Context mContext) {

		this.memId = memId;
		this.face_id = face_id;
		this.lat = lat;
		this.lon = lon;
		this.mAdapter = mAdapter;
		this.gridview = gridview;
		this.mContext = mContext;

	}

	// @Override
	/*
	 * protected void onPreExecute() {
	 * 
	 * progressDialog = new ProgressDialog(mContext);
	 * progressDialog.setCanceledOnTouchOutside(false);
	 * progressDialog.setMessage("Loading Next 6 matches...");
	 * progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	 * progressDialog.show();
	 * 
	 * super.onPreExecute(); }
	 */

	@Override
	protected String doInBackground(IMEETEmSearchResultsBean... params) {

		String returnString = "Got Matches";
		IMEETEmUtil util = IMEETEmUtil.getInstance();

		try {
			resultList = serviceUtil.nextEmBtn(String.valueOf(memId), face_id, lat, lon);

			if (resultList == null || resultList != null
					&& resultList.get(0).getNoMatches().equalsIgnoreCase(IMEETEmConstants.NO_MATCHES_FOR_YOU)) {
				returnString = resultList.get(0).getNoMatches();
			} else {

				// Build a list of MatchInfo beans used in postExecute to update
				// ListView
				matchInfo = new ArrayList<MatchInfoBean>();
				String name = null;
				String age = null;

				for (int i = 0; i < resultList.size(); i++) {

					MatchInfoBean bean = MatchInfoBean.getInstance();
					IMEETEmSearchResultsBean result = resultList.get(i);
					name = result.getSearchResultMemName();
					age = result.getSearchResultMemAge();

					System.out.println("\n\n *********** RESULTS In NextEm Async Task!***************\n "
							+ result.toString() + "\n\n\n");
					bean.setMemberName(name);
					bean.setMemberAge(age);
					bean.setImageUrl(result.getSearchResultMemImgLoc());
					bean.setImageDrawable(util.createDrawableImg(mContext, result.getSearchResultMemImg()));
					bean.setSearchStatus(result.getSearchResultStatus());
					bean.setMemberId(result.getSearchResultMemId());
					matchInfo.add(bean);
				}
				Log.d(TAG, "Got Matches, no matter what... " + matchInfo.size());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnString;
	}

	@Override
	protected void onPostExecute(String result) {

		if (!result.equals(null)) {
			// TODO: We will need to handle the message
			// "Sorry, cannot find any matches" -kneibar 1/26/2014
			if (!result.equals(IMEETEmConstants.NO_MATCHES_FOR_YOU)) {

				Log.d(TAG, "\n-------------------- Set the Grid Adapter in Next Em! ------------------- \n");
				mAdapter = new IMEETEmMatchesGridAdapter(mContext, R.layout.matches_grid, matchInfo, memId,
						activity);
				gridview.setAdapter(mAdapter);
			} else {
				Log.d(TAG, "\n-------------------- No Matches found! ------------------- \n");
				mAdapter = new IMEETEmMatchesGridAdapter(mContext, R.layout.no_matches, matchInfo, memId,
						activity);
				gridview.setAdapter(mAdapter);
			}
		} else {
			Log.d(TAG, "We were not able to get any more matches for some reason ");
		}
		System.out.println("DONE: Got result onPostExecute: " + result);
	}

}
