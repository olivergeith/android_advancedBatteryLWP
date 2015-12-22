package de.geithonline.abattlwp;

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.geithonline.abattlwp.settings.Settings;

public class EasterEgg {
	private final Calendar date;
	private final boolean enabled;

	public EasterEgg() {
		date = new GregorianCalendar();
		enabled = Settings.isShowEasterEggs();
	}

	public boolean isSilviasBirthday() {
		return (enabled && date.get(Calendar.MONTH) == Calendar.FEBRUARY && date.get(Calendar.DAY_OF_MONTH) == 2);
	}

	public boolean isOliversBirthday() {
		return (enabled && date.get(Calendar.MONTH) == Calendar.JANUARY && date.get(Calendar.DAY_OF_MONTH) == 24);
	}

	public boolean isEikesBirthday() {
		return (enabled && date.get(Calendar.MONTH) == Calendar.MARCH && date.get(Calendar.DAY_OF_MONTH) == 29);
	}

	public boolean isAaronsBirthday() {
		return (enabled && date.get(Calendar.MONTH) == Calendar.FEBRUARY && date.get(Calendar.DAY_OF_MONTH) == 6);
	}

	public boolean isEmmysBirthday() {
		return (enabled && date.get(Calendar.MONTH) == Calendar.JUNE && date.get(Calendar.DAY_OF_MONTH) == 29);
	}

	public boolean isSomebodiesBirthday() {
		return isSilviasBirthday() //
				| isAaronsBirthday() //
				| isOliversBirthday() //
				| isEikesBirthday() //
				| isEmmysBirthday();
	}

	public boolean isDecember() {
		return (enabled && date.get(Calendar.MONTH) == Calendar.DECEMBER);
	}

	public boolean isXMas() {
		return (enabled && date.get(Calendar.MONTH) == Calendar.DECEMBER && date.get(Calendar.DAY_OF_MONTH) >= 24 && date.get(Calendar.DAY_OF_MONTH) <= 26);
	}

	public boolean isHalloween() {
		return (enabled && date.get(Calendar.MONTH) == Calendar.OCTOBER && date.get(Calendar.DAY_OF_MONTH) == 31);
	}
}
