/**
 * 
 */
package com.android.imeetem.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.imeetem.IMEETEmSearchGridActivity;
import com.android.imeetem.MatchInfoActivity;
import com.android.imeetem.R;

import com.android.imeetem.util.IMEETEmConstants;
import com.android.imeetem.util.IMEETEmUtil;
import com.android.imeetem.util.MatchInfoBean;

/**
 * @author kneibarger
 * 
 */
public class IMEETEmMatchesGridAdapter extends ArrayAdapter<MatchInfoBean> {

	final private Context mContext;
	private int resourceId;
	private List<MatchInfoBean> data = new ArrayList<MatchInfoBean>();
	private int memId;


	public IMEETEmMatchesGridAdapter(Context context, int layoutResourceId,
			List<MatchInfoBean> data, int memId,
			IMEETEmSearchGridActivity activity) {
		super(context, layoutResourceId, data);
		this.mContext = context;
		this.resourceId = layoutResourceId;
		this.data = data;
		this.memId = memId;
		// prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		//this.activity = activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = convertView;
		ViewHolder holder = null;
		final IMEETEmUtil util = IMEETEmUtil.getInstance();

		if (itemView == null) {
			final LayoutInflater layoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemView = layoutInflater.inflate(resourceId, parent, false);

			holder = new ViewHolder();
			holder.imgItem = (ImageView) itemView.findViewById(R.id.imgItem);
			holder.txtItem = (TextView) itemView.findViewById(R.id.txtItem);
			// holder.nextEmButton =
			// (ImageButton)itemView.findViewById(R.id.next_em_button);
			itemView.setTag(holder);
		} else {
			holder = (ViewHolder) itemView.getTag();
		}

		MatchInfoBean match = getItem(position);

		// These vars are marked as final so that we can use them in an
		// implementation of an interface below (onClick event)
		// final String age = match.getMemberAge();
		// final String name = match.getMemberName();
		// final String impactId = match.getMemberId();
		// final Drawable image = match.getImageDrawable();
		// final String distance = match.getMemberDistance();
		//
		// final String hairColor = match.getMemberHairColor();
		// final String

		final MatchInfoBean matchInfo = match;
		// TODO: Add new data into MatchInfoBean and change the
		// setClickableForImageAndText method to pass in the
		// given bean..

		if (match != null && match.getSearchStatus() != null
				&& match.getSearchStatus().equalsIgnoreCase("I")) {
			Bitmap grayedOutPic = util.toGrayscale(match.getImageDrawable());
			holder.imgItem.setImageBitmap(grayedOutPic);
			holder.imgItem.setClickable(false);
			holder.txtItem.setText(match.getMemberAge() + " - "
					+ match.getMemberName());

			// Set the age - name section to gray
			itemView.setBackgroundColor(Color.LTGRAY);

		} else {
			holder.imgItem.setImageDrawable(match.getImageDrawable());
			holder.txtItem.setText(match.getMemberAge() + " - "
					+ match.getMemberName());

			holder.imgItem.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent i = util
							.setClickableForImageAndText(matchInfo, String
									.valueOf(memId),
									IMEETEmConstants.CALLED_FROM_BASIC_SEARCH,
									new Intent(v.getContext(),
											MatchInfoActivity.class), null,
									null);

					mContext.startActivity(i);

				}
			});
		}

		return itemView;
	}

	static class ViewHolder {
		ImageView imgItem;
		TextView txtItem;
	}

	@Override
	public MatchInfoBean getItem(int position) {
		return this.data.get(position);
	}
}
