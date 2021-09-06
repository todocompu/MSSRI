/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.SRIMS.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class UtilsConversiones {

	public static Object convertir(Object obj) {
		if (obj instanceof Integer || obj instanceof BigInteger) {
			return Integer.parseInt(obj.toString());
		} else if (obj instanceof String || obj instanceof Character || obj instanceof Byte) {
			return obj.toString().trim();
		} else if (obj instanceof Double) {
			return Double.parseDouble(obj.toString());
		} else if (obj instanceof Float) {
			return Float.parseFloat(obj.toString());
		} else if (obj instanceof BigDecimal) {
			return new BigDecimal(obj.toString());
		} else if (obj instanceof Boolean) {
			return Boolean.parseBoolean(obj.toString());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> Object convertir(List<T> list) {
		List<T> listAux = new ArrayList<T>();
		for (Object obj : list) {
			listAux.add((T) convertir(obj));
		}
		return listAux;
	}

	@SuppressWarnings("unchecked")
	public static <T> Object[] convertirListToArray(List<T> lista, int elementoDeLista) {
		try {
			Object obj = lista.get(elementoDeLista);
			Object[] array = (Object[]) obj;
			return array;
		} catch (ClassCastException cce1) {
			try {
				Object obj = lista.get(elementoDeLista);
				Object[] array = ((List<T>) obj).toArray();
				return array;
			} catch (ClassCastException cce2) {
				return null;
			}
		} catch (ArrayIndexOutOfBoundsException aiobe) {
			return null;
		} catch (NullPointerException npe) {
			return null;
		}
	}
}