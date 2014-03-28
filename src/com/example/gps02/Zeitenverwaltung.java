/**
 * 
 */
package com.example.gps02;

import android.widget.Button;

/**
 * @author Dave
 *
 */
public class Zeitenverwaltung {
	private Button Zeitenanzeige;
	String Zeiten=new String();
	
	public Zeitenverwaltung(Button Anzeige){
		Zeitenanzeige=Anzeige;
	}
	
	public void Add_Zeit(String Zeit){
		Zeiten=Zeiten+Zeit+"\n";
		Zeitenanzeige.setText(Zeiten);
	}

}
