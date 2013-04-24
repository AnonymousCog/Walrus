package com.navlog.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import com.example.navlog.calculator.R;


import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

public class AirplaneListActivity extends ListActivity {

	static final String[] Airplanes = new String[] { "My Plane - Airbus - A318",
		"Rental Plane - Astra - SPX", "Joe's Plane - Boeing - 767",
		"Sam's -Bombardier - CRJ","Company - Cessna - VII",
		"Company2 - Cessna - III", "Company - Cessna - XLS",
		"Company - Cessna - V", "Company - Cessna - X",
		"Company - Cessna - SII"};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinkedList<String> mLinked = new LinkedList<String>();
		for (int i = 0; i < Airplanes.length; i++) {
			mLinked.add(Airplanes[i]);
		}

		setListAdapter(new MyListAdaptor(this, mLinked));

		ListView lv = getListView();
		lv.setFastScrollEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(),
						((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.airplane_list, menu);
		return true;
	}
	
			class MyListAdaptor extends ArrayAdapter<String> implements SectionIndexer 
			{
		
				HashMap<String, Integer> alphaIndexer;
				String[] sections;
				
				public MyListAdaptor(Context context, LinkedList<String> items) 
				{
					super(context, R.layout.list_item, items);
				
					alphaIndexer = new HashMap<String, Integer>();
					int size = items.size();
				
					for (int x = 0; x < size; x++) 
					{
						String s = items.get(x);
				
						// get the first letter of the store
						String ch = s.substring(0, 1);
						// convert to uppercase otherwise lowercase a -z will be sorted
						// after upper A-Z
						ch = ch.toUpperCase();
				
						// HashMap will prevent duplicates
						alphaIndexer.put(ch, x);
					}
				
					Set<String> sectionLetters = alphaIndexer.keySet();
				
					// create a list from the set to sort
					ArrayList<String> sectionList = new ArrayList<String>(
							sectionLetters);
				
					Collections.sort(sectionList);
				
					sections = new String[sectionList.size()];
				
					sectionList.toArray(sections);
				}
				
				public int getPositionForSection(int section) 
				{
					return alphaIndexer.get(sections[section]);
				}
				
				public int getSectionForPosition(int position) 
				{
					return 0;
				}
				
				public Object[] getSections() 
				{
					return sections;
				}
			}
	

}
