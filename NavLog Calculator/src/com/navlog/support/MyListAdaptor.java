package com.navlog.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

import com.example.navlog.calculator.R;

public class MyListAdaptor extends ArrayAdapter<String> implements SectionIndexer 
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
			
			ch = ch.toUpperCase(Locale.getDefault());
	
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