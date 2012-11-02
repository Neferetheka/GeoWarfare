package com.aerilys.geowarfare.android.views.adapters;

import java.util.ArrayList;
import java.util.List;

import com.aerilys.geowarfare.android.R;
import com.aerilys.geowarfare.android.models.Success;
import com.aerilys.geowarfare.android.tools.FontManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SuccessAdapter extends BaseAdapter
{
	public List<Success> listeSuccess;
	LayoutInflater inflater;

	public SuccessAdapter(Context context, List<Success> listeSuccess)
	{
		inflater = LayoutInflater.from(context);
		this.listeSuccess = new ArrayList<Success>();
		
		for(Success success : listeSuccess)
		{
			if(!success.isHidden() || success.isDone())
				this.listeSuccess.add(success);
		}
	}

	public int getCount()
	{
		return listeSuccess.size();
	}

	public Object getItem(int index)
	{
		return listeSuccess.get(index);
	}

	public long getItemId(int arg0)
	{
		return arg0;
	}

	private class ViewHolder
	{
		TextView successNom;
		ImageView successImage;
		ImageView successDone;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listview_success, null);

			holder.successNom = (TextView) convertView.findViewById(R.id.succesNom);
			holder.successImage = (ImageView) convertView.findViewById(R.id.successImage);
			holder.successDone = (ImageView) convertView.findViewById(R.id.successDoneImage);
			holder.successNom.setTypeface(FontManager.Ubuntu);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.successNom.setText(listeSuccess.get(position).getNom());

		try
		{
			holder.successImage.setImageDrawable(parent.getContext().getResources()
					.getDrawable(listeSuccess.get(position).Image));
		}
		catch (Exception e)
		{
			holder.successImage.setImageDrawable(parent.getContext().getResources().getDrawable(R.drawable.question));
		}

		if (listeSuccess.get(position).isDone())
		{
			holder.successDone.setImageDrawable(parent.getContext().getResources().getDrawable(R.drawable.ic_action_done));
		}
		else
			holder.successDone.setImageDrawable(null);

		return convertView;
	}
}
