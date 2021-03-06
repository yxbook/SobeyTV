package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sobey.tvcust.R;
import com.sobey.tvcust.interfaces.CharSort;

import java.util.List;

public class ListAdapterComp extends BaseAdapter {
	private List<CharSort> results = null;
	private Context mContext;

	private boolean needNextButton;

	public List<CharSort> getResults() {
		return results;
	}

	public ListAdapterComp(Context mContext, List<CharSort> list,boolean needNextButton) {
		this.mContext = mContext;
		this.results = list;
		this.needNextButton = needNextButton;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<CharSort> list){
		this.results = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.results.size();
	}

	public CharSort getItem(int position) {
		return results.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_list_comp_office, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvNext = (TextView) view.findViewById(R.id.text_comp_next);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		if (results.get(position).getCar_title_html()!=null) {
			viewHolder.tvTitle.setText(Html.fromHtml(results.get(position).getCar_title_html()));
		}else {
			viewHolder.tvTitle.setText(results.get(position).getCar_title());
		}

		if (needNextButton) {
			viewHolder.tvNext.setVisibility(View.VISIBLE);
			viewHolder.tvNext.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (onNextClickListenner != null) onNextClickListenner.onNextClick(position);
				}
			});
		}else {
			viewHolder.tvNext.setVisibility(View.GONE);
		}

		//如果是总公司（id为-1）,则没有下级分类
		if (results.get(position).getId().equals(-1)){
			viewHolder.tvNext.setVisibility(View.GONE);
		}

		return view;
	}

	final static class ViewHolder {
		TextView tvTitle;
		TextView tvNext;
	}

	private OnNextClickListenner onNextClickListenner;
	public void setOnNextClickListenner(OnNextClickListenner onNextClickListenner) {
		this.onNextClickListenner = onNextClickListenner;
	}
	public interface OnNextClickListenner{
		void onNextClick(int position);
	}
}