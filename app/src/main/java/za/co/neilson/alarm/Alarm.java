/* Copyright 2014 Sheldon Neilson www.neilson.co.za
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package za.co.neilson.alarm;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import za.co.neilson.alarm.alert.AlarmAlertBroadcastReciever;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;

public class Alarm implements Serializable {

	public enum Difficulty{
		EASY,
		MEDIUM,
		HARD;
		
		@Override
		public String toString() {
			switch(this.ordinal()){
				case 0:
					return "Easy";
				case 1:
					return "Medium";
				case 2:
					return "Hard";
			}
			return super.toString();
		}
	}
	
	public enum Day{
		SUNDAY,
		MONDAY,
		TUESDAY,
		WEDNESDAY,
		THURSDAY,
		FRIDAY,
		SATURDAY;

		@Override
		public String toString() {
			switch(this.ordinal()){
				case 0:
					return "Sunday";
				case 1:
					return "Monday";
				case 2:
					return "Tuesday";
				case 3:
					return "Wednesday";
				case 4:
					return "Thursday";
				case 5:
					return "Friay";
				case 6:
					return "Saturday";
			}
			return super.toString();
		}
		
	}

	public enum Charity{
		WOUNDED,
		REDCROSS,
		DOCTORS,
		DEVELOPER;

		@Override
		public String toString(){
			switch(this.ordinal()){
				case 0:
					return "Wounded Warriors Project";
				case 1:
					return "American Red Cross";
				case 2:
					return "Doctors Without Borders";
				case 3:
					return "Developer";
			}
			return super.toString();
		}
	}


	private static final long serialVersionUID = 8699489847426803789L;
	private int id;
	private Boolean alarmActive = true;
	private Calendar alarmTime = Calendar.getInstance();
	private Day[] days = {Day.MONDAY,Day.TUESDAY,Day.WEDNESDAY,Day.THURSDAY,Day.FRIDAY,Day.SATURDAY,Day.SUNDAY};	
	private String alarmTonePath = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
	private Boolean vibrate = true;
	private String alarmName = "Alarm Clock";
	private Difficulty difficulty = Difficulty.EASY;
	private Double donateAmount = .50;
	private Charity[] charities = {Charity.WOUNDED, Charity.REDCROSS,Charity.DOCTORS, Charity.DEVELOPER};
	private Double tempDonateAmount = 0.0;
	
	public Alarm() {

	}

//	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
//		out.defaultWriteObject();
//		out.writeObject(getAlarmToneUri().getEncodedPath());
//	}

//	private void readObject(java.io.ObjectInputStream in) throws IOException {
//		try {
//			in.defaultReadObject();
//			this.setAlarmToneUri(Uri.parse(in.readObject().toString()));
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
//	}

	public Charity[] getCharities() {
		return charities;
	}

	/**
	 * @param set
	 *            the repeatDays to set
	 */
	public void setCharities(Charity[] charities) {
		this.charities = charities;
	}

	public void addCharity(Charity charity){
		boolean contains = false;
		for(Charity c : getCharities())
			if(c.equals(charity))
				contains = true;
		if(!contains){
			List<Charity> result = new LinkedList<Charity>();
			for(Charity d : getCharities())
				result.add(d);
			result.add(charity);
			setCharities(result.toArray(new Charity[result.size()]));
		}
	}

	public void removeCharity(Charity charity) {

		List<Charity> result = new LinkedList<Charity>();
		for(Charity c : getCharities())
			if(!c.equals(charity))
				result.add(c);
		setCharities(result.toArray(new Charity[result.size()]));
	}

	public String getCharitiesString() {
		StringBuilder charitiesStringBuilder = new StringBuilder();
		if(getCharities().length == Charity.values().length){
			charitiesStringBuilder.append("Every Charity");
		}else{
			Arrays.sort(getCharities(), new Comparator<Charity>() {
				@Override
				public int compare(Charity lhs, Charity rhs) {

					return lhs.ordinal() - rhs.ordinal();
				}
			});
			for(Charity c : getCharities()){
				switch(c){
					default:
						charitiesStringBuilder.append(c.toString().substring(0, 5));
						break;
				}
				charitiesStringBuilder.append(',');
			}
			charitiesStringBuilder.setLength(charitiesStringBuilder.length()-1);
		}

		return charitiesStringBuilder.toString();
	}

	public double getDonateAmount(){
		return donateAmount;
	}

	public void setDonateAmount(Double donateAmount){
		this.donateAmount = donateAmount;
	}

	public String getDonateAmountString(){
		return donateAmount.toString();
	}



	/**
	 * @return the alarmActive
	 */
	public Boolean getAlarmActive() {
		return alarmActive;
	}

	/**
	 * @param alarmActive
	 *            the alarmActive to set
	 */
	public void setAlarmActive(Boolean alarmActive) {
		this.alarmActive = alarmActive;
	}

	/**
	 * @return the alarmTime
	 */
	public Calendar getAlarmTime() {
		if (alarmTime.before(Calendar.getInstance()))
			alarmTime.add(Calendar.DAY_OF_MONTH, 1);
		while(!Arrays.asList(getDays()).contains(Day.values()[alarmTime.get(Calendar.DAY_OF_WEEK)-1])){
			alarmTime.add(Calendar.DAY_OF_MONTH, 1);			
		}
		return alarmTime;
	}

	/**
	 * @return the alarmTime
	 */
	public String getAlarmTimeString() {

		String time = "";
		if (alarmTime.get(Calendar.HOUR_OF_DAY) <= 9)
			time += "0";
		time += String.valueOf(alarmTime.get(Calendar.HOUR_OF_DAY));
		time += ":";

		if (alarmTime.get(Calendar.MINUTE) <= 9)
			time += "0";
		time += String.valueOf(alarmTime.get(Calendar.MINUTE));

		return time;
	}

	/**
	 * @param alarmTime
	 *            the alarmTime to set
	 */
	public void setAlarmTime(Calendar alarmTime) {
		this.alarmTime = alarmTime;
	}

	/**
	 * @param alarmTime
	 *            the alarmTime to set
	 */
	public void setAlarmTime(String alarmTime) {

		String[] timePieces = alarmTime.split(":");

		Calendar newAlarmTime = Calendar.getInstance();
		newAlarmTime.set(Calendar.HOUR_OF_DAY,
				Integer.parseInt(timePieces[0]));
		newAlarmTime.set(Calendar.MINUTE, Integer.parseInt(timePieces[1]));
		newAlarmTime.set(Calendar.SECOND, 0);
		setAlarmTime(newAlarmTime);		
	}

	/**
	 * @return the repeatDays
	 */
	public Day[] getDays() {
		return days;
	}

	/**
	 * @param set
	 *            the repeatDays to set
	 */
	public void setDays(Day[] days) {
		this.days = days;
	}

	public void addDay(Day day){
		boolean contains = false;
		for(Day d : getDays())
			if(d.equals(day))
				contains = true;
		if(!contains){
			List<Day> result = new LinkedList<Day>();
			for(Day d : getDays())
				result.add(d);
			result.add(day);
			setDays(result.toArray(new Day[result.size()]));
		}
	}
	
	public void removeDay(Day day) {
	    
		List<Day> result = new LinkedList<Day>();
	    for(Day d : getDays())
	        if(!d.equals(day))
	            result.add(d);
	    setDays(result.toArray(new Day[result.size()]));
	}
	
	/**
	 * @return the alarmTonePath
	 */
	public String getAlarmTonePath() {
		return alarmTonePath;
	}

	/**
	 * @param alarmTonePath the alarmTonePath to set
	 */
	public void setAlarmTonePath(String alarmTonePath) {
		this.alarmTonePath = alarmTonePath;
	}
	
	/**
	 * @return the vibrate
	 */
	public Boolean getVibrate() {
		return vibrate;
	}

	/**
	 * @param vibrate
	 *            the vibrate to set
	 */
	public void setVibrate(Boolean vibrate) {
		this.vibrate = vibrate;
	}

	public void setTempDonateAmount(double amount){
		this.tempDonateAmount = amount;
	}

	public double getTempDonateAmount(){
		return this.tempDonateAmount;
	}


	/**
	 * @return the alarmName
	 */
	public String getAlarmName() {
		return alarmName;
	}

	/**
	 * @param alarmName
	 *            the alarmName to set
	 */
	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRepeatDaysString() {
		StringBuilder daysStringBuilder = new StringBuilder();
		if(getDays().length == Day.values().length){
			daysStringBuilder.append("Every Day");		
		}else{
			Arrays.sort(getDays(), new Comparator<Day>() {
				@Override
				public int compare(Day lhs, Day rhs) {
					
					return lhs.ordinal() - rhs.ordinal();
				}
			});
			for(Day d : getDays()){
				switch(d){
				case TUESDAY:
				case THURSDAY:
//					daysStringBuilder.append(d.toString().substring(0, 4));
//					break;
					default:
						daysStringBuilder.append(d.toString().substring(0, 3));		
						break;
				}				
				daysStringBuilder.append(',');
			}
			daysStringBuilder.setLength(daysStringBuilder.length()-1);
		}
			
		return daysStringBuilder.toString();
	}

	public void schedule(Context context) {
		setAlarmActive(true);
		
		Intent myIntent = new Intent(context, AlarmAlertBroadcastReciever.class);
		myIntent.putExtra("alarm", this);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent,PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

		alarmManager.set(AlarmManager.RTC_WAKEUP, getAlarmTime().getTimeInMillis(), pendingIntent);					
	}
	
	public String getTimeUntilNextAlarmMessage(){
		long timeDifference = getAlarmTime().getTimeInMillis() - System.currentTimeMillis();
		long days = timeDifference / (1000 * 60 * 60 * 24);
		long hours = timeDifference / (1000 * 60 * 60) - (days * 24);
		long minutes = timeDifference / (1000 * 60) - (days * 24 * 60) - (hours * 60);
		long seconds = timeDifference / (1000) - (days * 24 * 60 * 60) - (hours * 60 * 60) - (minutes * 60);
		String alert = "Alarm will sound in ";
		if (days > 0) {
			alert += String.format(
					"%d days, %d hours, %d minutes and %d seconds", days,
					hours, minutes, seconds);
		} else {
			if (hours > 0) {
				alert += String.format("%d hours, %d minutes and %d seconds",
						hours, minutes, seconds);
			} else {
				if (minutes > 0) {
					alert += String.format("%d minutes, %d seconds", minutes,
							seconds);
				} else {
					alert += String.format("%d seconds", seconds);
				}
			}
		}
		return alert;
	}
}
