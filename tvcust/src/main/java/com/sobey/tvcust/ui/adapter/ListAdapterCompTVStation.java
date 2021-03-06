package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;


import com.sobey.tvcust.R;
import com.sobey.tvcust.interfaces.CharSort;

import java.util.List;

public class ListAdapterCompTVStation extends BaseAdapter implements SectionIndexer {
	private List<CharSort> results = null;
	private Context mContext;

	public ListAdapterCompTVStation(Context mContext, List<CharSort> list) {
		this.mContext = mContext;
		this.results = list;
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
		final CharSort mContent = results.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_list_comp_tvstation, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.line = (ImageView) view.findViewById(R.id.line);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
			viewHolder.line.setVisibility(View.GONE);
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
			viewHolder.line.setVisibility(View.VISIBLE);
		}
	
		if (results.get(position).getCar_title_html()!=null) {
			viewHolder.tvTitle.setText(Html.fromHtml(results.get(position).getCar_title_html()));
		}else {
			viewHolder.tvTitle.setText(results.get(position).getCar_title());
		}
		return view;

	}
	


	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		ImageView line;
	}


	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return results.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = results.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}