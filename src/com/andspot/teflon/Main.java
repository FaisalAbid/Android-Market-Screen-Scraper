package com.andspot.teflon;

import java.io.IOException;

import com.andspot.jsonk.JSONException;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public static void main(String[] args) throws IOException, JSONException {
		Teflon t = new Teflon();
		System.out.print(t.FindByPackageName("com.zkytale.showtimes").toString());

	}

}
