package de.geithonline.abattlwp;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class EasterEgg {
	private final Calendar date;

	public EasterEgg() {
		date = new GregorianCalendar();
	}

	public boolean isSilviasBirthday() {
		return (date.get(Calendar.MONTH) == Calendar.FEBRUARY && date.get(Calendar.DAY_OF_MONTH) == 2);
	}

	public boolean isOliversBirthday() {
		return (date.get(Calendar.MONTH) == Calendar.JANUARY && date.get(Calendar.DAY_OF_MONTH) == 24);
	}

	public boolean isEikesBirthday() {
		return (date.get(Calendar.MONTH) == Calendar.MARCH && date.get(Calendar.DAY_OF_MONTH) == 29);
	}

	public boolean isAaronsBirthday() {
		return (date.get(Calendar.MONTH) == Calendar.FEBRUARY && date.get(Calendar.DAY_OF_MONTH) == 6);
	}

	public boolean isEmmysBirthday() {
		return (date.get(Calendar.MONTH) == Calendar.JUNE && date.get(Calendar.DAY_OF_MONTH) == 29);
	}

	public boolean isSomebodiesBirthday() {
		return isSilviasBirthday() //
				| isAaronsBirthday() //
				| isOliversBirthday() //
				| isEikesBirthday() //
				| isEmmysBirthday();
	}

	public boolean isDecember() {
		return (date.get(Calendar.MONTH) == Calendar.DECEMBER);
	}

	public boolean isHalloween() {
		return (date.get(Calendar.MONTH) == Calendar.OCTOBER && date.get(Calendar.DAY_OF_MONTH) == 31);
	}

}
