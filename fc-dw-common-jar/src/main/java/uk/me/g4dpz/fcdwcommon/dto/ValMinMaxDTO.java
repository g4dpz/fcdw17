// FUNcube Data Warehouse
// Copyright 2013 (c) David A.Johnson, G4DPZ, AMSAT-UK
// This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter
// to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.

package uk.me.g4dpz.fcdwcommon.dto;

import java.io.Serializable;

public class ValMinMaxDTO implements Serializable {

	private String name;
	private String value;
	private String min;
	private String max;

	public ValMinMaxDTO() {
	}

	public ValMinMaxDTO(final String name, final String value, final String min,
						final String max) {
		this.name = name;
		this.value = value;
		this.min = min;
		this.max = max;
	}

	public final String getName() {
		return name;
	}

	public final String getValue() {
		return value;
	}

	public final String getMin() {
		return min;
	}

	public final String getMax() {
		return max;
	}

}
