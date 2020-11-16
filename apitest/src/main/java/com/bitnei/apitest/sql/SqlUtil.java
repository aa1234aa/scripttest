package com.bitnei.apitest.sql;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.bitnei.apitest.annotation.pro.FieldName;
import com.bitnei.apitest.annotation.pro.TableName;
import com.bitnei.apitest.annotation.pro.TempField;
import com.bitnei.apitest.pro.Po;
import com.bitnei.apitest.pro.Pram;



public class SqlUtil<T extends Po> {
	
	
	public Field getField(Class<?> t, String fieldName){
		Field[] fields = t.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		return null;
	}
	
	
	public List<Pram> getPramListOfSelect(Po po){
		List<Pram> list = new ArrayList<>();
		Class<? extends Po> thisClass = po.getClass();
	    Field[] fields = thisClass.getDeclaredFields();
	    for(Field f : fields){
	    	try {
	    		if (!f.isAnnotationPresent(TempField.class)) {
	    			String fName = f.getName();
	    			//�ж��Ƿ���boolean����
	    			String get = "get";
	    			String fieldType = f.getGenericType().toString();
	    			if (fieldType.indexOf("boolean") != -1 || fieldType.indexOf("Boolean") != -1) {
	    				get = "is";
					}
	    			if (f.isAnnotationPresent(FieldName.class)) {
	    				String fieldName = f.getAnnotation(FieldName.class).name();
						Pram pram = new Pram(fieldName + " as " + fName, thisClass.getMethod(get + fName.substring(0, 1).toUpperCase() + fName.substring(1)).invoke(po));
						list.add(pram);
	    			}else{
	    				String fieldName = toTableString(fName);
	    				Pram pram = new Pram(fieldName + " as " + fName, thisClass.getMethod(get + fName.substring(0, 1).toUpperCase() + fName.substring(1)).invoke(po));
						list.add(pram);
					}
				}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	    }
		return list;
	}
	
	/**
	 * ��ȡʵ�����Ӧ�ı���
	 * @param po
	 * @return
	 */
	public String getTableName(Po po){
		Class<? extends Po> c = po.getClass();
		if(c.isAnnotationPresent(TableName.class)){
			return c.getAnnotation(TableName.class).name();
		}else{
			String className = po.getClass().getSimpleName();
			String tName = toTableString(className);
			String poName = tName.substring(tName.length() - 2, tName.length());
			if("po".equals(poName)){
				tName = tName.substring(0,tName.length() - 3);
			}
			return tName;
		}
		
	}
	
	public static<T extends Po> List<Pram> getPramListofStatic(Po po){
		List<Pram> list = new ArrayList<>();
		Class<? extends Po> thisClass = po.getClass();
	    Field[] fields = thisClass.getDeclaredFields();
	    	try {
	    		for(Field f : fields){
		    		if(!f.getName().equalsIgnoreCase("ID") && !f.isAnnotationPresent(TempField.class)){
		    			String fName = f.getName();
		    			
		    			//�ж��Ƿ���boolean����
		    			String getf = "get";
		    			String fieldType = f.getGenericType().toString();
		    			if (fieldType.indexOf("boolean") != -1 || fieldType.indexOf("Boolean") != -1) {
		    				getf = "is";
						}
		    			if (f.isAnnotationPresent(FieldName.class)) {
		    				String fieldName = f.getAnnotation(FieldName.class).name();
		    				Method get = thisClass.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
		    				Object getValue = get.invoke(po);
		    				Pram pram = new Pram(fieldName, getValue);
							list.add(pram);
		    			}else{
		    				String fieldName = new SqlUtil<T>().toTableString(fName);
		    				Method get = thisClass.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
		    				Object getValue = get.invoke(po);
		    				Pram pram = new Pram(fieldName, getValue);
							list.add(pram);
						}
		    		}
	    		}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return list;
	}
	
	/**
	 * ͨ��Class��ȡ���ɶ�ӦSql�ֶ�
	 * @param po
	 * @return
	 */
	public List<Pram> getPramList(Class<T> po){
		List<Pram> list = new ArrayList<>();
		Class<? extends Po> thisClass = po;
	    Field[] fields = thisClass.getDeclaredFields();
	    	try {
	    		Object o = thisClass.newInstance();
	    		for(Field f : fields){
		    		if(!f.getName().equalsIgnoreCase("ID") && !f.isAnnotationPresent(TempField.class)){
		    			String fName = f.getName();
		    			
		    			//�ж��Ƿ���boolean����
		    			String getf = "get";
		    			String fieldType = f.getGenericType().toString();
		    			if (fieldType.indexOf("boolean") != -1 || fieldType.indexOf("Boolean") != -1) {
		    				getf = "is";
						}
		    			if (f.isAnnotationPresent(FieldName.class)) {
		    				String fieldName = f.getAnnotation(FieldName.class).name();
		    				Method get = thisClass.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
		    				Object getValue = get.invoke(o);
		    				Pram pram = new Pram(fieldName, getValue);
							list.add(pram);
		    			}else{
		    				String fieldName = toTableString(fName);
		    				Method get = thisClass.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
		    				Object getValue = get.invoke(o);
		    				Pram pram = new Pram(fieldName , getValue);
							list.add(pram);
						}
		    		}
	    		}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return list;
	}
	
	public List<Pram> getPramList(T po){
		List<Pram> list = new ArrayList<>();
		Class<? extends Po> thisClass = po.getClass();
	    Field[] fields = thisClass.getDeclaredFields();
	    	try {
	    		for(Field f : fields){
		    		if(!f.getName().equalsIgnoreCase("ID") && !f.isAnnotationPresent(TempField.class)){
		    			String fName = f.getName();
		    			
		    			//�ж��Ƿ���boolean����
		    			String getf = "get";
		    			String fieldType = f.getGenericType().toString();
		    			if (fieldType.indexOf("boolean") != -1 || fieldType.indexOf("Boolean") != -1) {
		    				getf = "is";
						}
		    			if (f.isAnnotationPresent(FieldName.class)) {
		    				String fieldName = f.getAnnotation(FieldName.class).name();
		    				Method get = thisClass.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
		    				Object getValue = get.invoke(po);
		    				Pram pram = new Pram(fieldName, getValue);
							list.add(pram);
		    			}else{
		    				String fieldName = toTableString(fName);
		    				Method get = thisClass.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
		    				Object getValue = get.invoke(po);
		    				Pram pram = new Pram(fieldName , getValue);
							list.add(pram);
						}
		    		}
	    		}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return list;
	}
	
	
	/**
	 * ͨ��Class��ȡ���ɶ�ӦSql��ѯ���ֶ�
	 * @param po
	 * @return
	 */
	public List<Pram> getPramListOfSelect(Class<T> po){
		List<Pram> list = new ArrayList<>();
		Class<? extends Po> thisClass = po;
	    Field[] fields = thisClass.getDeclaredFields();
	    	try {
	    		Object o = thisClass.newInstance();
	    		for(Field f : fields){
	    			if (!f.isAnnotationPresent(TempField.class)) {
	    				String fName = f.getName();
	    				//�ж��Ƿ���boolean����
		    			String getf = "get";
		    			String fieldType = f.getGenericType().toString();
		    			if (fieldType.indexOf("boolean") != -1 || fieldType.indexOf("Boolean") != -1) {
		    				getf = "is";
						}
		    			if (f.isAnnotationPresent(FieldName.class)) {
		    				String fieldName = f.getAnnotation(FieldName.class).name();
		    				Method get = thisClass.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
		    				Object getValue = get.invoke(o);
		    				Pram pram = new Pram(fieldName + " as " + fName, getValue);
							list.add(pram);
		    			}else{
		    				String fieldName = toTableString(fName);
		    				Method get = thisClass.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
		    				Object getValue = get.invoke(o);
		    				Pram pram = new Pram(fieldName + " as " + fName, getValue);
							list.add(pram);
						}
					}
	    		}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return list;
	}
	
	/**
	 * ͨ��Class��ȡ���ɶ�ӦSql��ѯ���ֶ�
	 * @param po
	 * @return
	 */
	public List<Pram> getPramListByBean(Class<T> po){
		List<Pram> list = new ArrayList<>();
		Class<?> thisClass = po;
	    Field[] fields = thisClass.getDeclaredFields();
	    try {
    		Object o = thisClass.newInstance();
    		for(Field f : fields){
	    		if(!f.getName().equalsIgnoreCase("ID") && !f.isAnnotationPresent(TempField.class)){
	    			
	    			String fName = f.getName();
	    			
	    			//�ж��Ƿ���boolean����
	    			String getf = "get";
	    			String fieldType = f.getGenericType().toString();
	    			if (fieldType.indexOf("boolean") != -1 || fieldType.indexOf("Boolean") != -1) {
	    				getf = "is";
					}
	    			if (f.isAnnotationPresent(FieldName.class)) {
	    				String fieldName = f.getAnnotation(FieldName.class).name();
	    				Method get = thisClass.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
	    				Object getValue = get.invoke(o);
	    				Pram pram = new Pram(fieldName + " as " + fName, getValue);
						list.add(pram);
	    			}else{
	    				String fieldName = toTableString(fName);
	    				Method get = thisClass.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
	    				Object getValue = get.invoke(o);
	    				Pram pram = new Pram(fieldName + " as " + fName, getValue);
						list.add(pram);
					}
	    			
	    		}
    		}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * ͨ��Class��ȡ���ɶ�ӦSql��ѯ���ֶ�
	 * @param po
	 * @return
	 */
	public List<Pram> getPramListByBeanOfSelect(Class<T> po){
		List<Pram> list = new ArrayList<>();
		Class<?> thisClass = po;
	    Field[] fields = thisClass.getDeclaredFields();
	    try {
    		Object o = thisClass.newInstance();
    		for(Field f : fields){
    			if (!f.isAnnotationPresent(TempField.class)) {
    				String fName = f.getName();
    				//�ж��Ƿ���boolean����
	    			String getf = "get";
	    			String fieldType = f.getGenericType().toString();
	    			if (fieldType.indexOf("boolean") != -1 || fieldType.indexOf("Boolean") != -1) {
	    				getf = "is";
					}
	    			if (f.isAnnotationPresent(FieldName.class)) {
	    				String fieldName = f.getAnnotation(FieldName.class).name();
	    				Method get = thisClass.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
	    				Object getValue = get.invoke(o);
	    				Pram pram = new Pram(fieldName + " as " + fName, getValue);
						list.add(pram);
	    			}else{
	    				String fieldName = toTableString(fName);
	    				Method get = thisClass.getMethod(getf + fName.substring(0, 1).toUpperCase() + fName.substring(1));
	    				Object getValue = get.invoke(o);
	    				Pram pram = new Pram(fieldName + " as " + fName, getValue);
						list.add(pram);
					}
				}
    		}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * ��ȡSql�ֶ���
	 * @param po
	 * @return
	 */
	public String getTableName(Class<T> po){
		if(po.isAnnotationPresent(TableName.class)){
			return po.getAnnotation(TableName.class).name();
		}else{
			String tName = toTableString(po.getSimpleName());
			String poName = tName.substring(tName.length() - 2, tName.length());
			if("po".equals(poName)){
				tName = tName.substring(0,tName.length() - 3);
			}
			return tName;
		}
	}
	
	/**
	 * ��ȡSql�ֶ���
	 * @param po
	 * @return
	 */
	public String getTableNameByBean(Class<T> po){
		if(po.isAnnotationPresent(TableName.class)){
			return po.getAnnotation(TableName.class).name();
		}else{
			String tName = toTableString(po.getSimpleName());
			if("po".equals(tName.substring(tName.length() - 3, tName.length() - 1))){
				tName = tName.substring(0,tName.length() - 3);
			}
			return tName;
		}
	}
	
	/**
	 * 获取实体类中的某个�??
	 * @param po
	 * @param fileName
	 * @return
	 */
	public static<T> Serializable getFileValue(Class<T> po, String fileName){
		try {
			Method method = po.getMethod("get" + fileName.substring(0, 1).toUpperCase() + fileName.substring(1));
			Object o = po.newInstance();
			Object invoke = method.invoke(o);
			return null == invoke ? null : (Serializable)invoke;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 取字段名
	 * @param po
	 * @param fileName
	 * @return
	 */
	public Serializable getFileValue(Po po, String fileName){
		try {
			Class<? extends Po> cla = po.getClass();
			Method method = cla.getMethod("get" + fileName.substring(0, 1).toUpperCase() + fileName.substring(1));
			Object o = po;
			Object invoke = method.invoke(o);
			return null == invoke ? null : (Serializable)invoke;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	
	/**
	 * 将某个�?��?�过反射强制赋给实体�?
	 * @param po
	 * @param fileName
	 * @param fileValue
	 * @return
	 */
	public static boolean setFileValue(Po po, String fileName, Serializable fileValue){
		Class<? extends Po> thisClass = po.getClass();
		try {
			if ("ID".equalsIgnoreCase(fileName)) {
				try {
					Field field = thisClass.getDeclaredField(fileName);
					String calssName = field.getType().getName();
					if (calssName.equals("int") || calssName.equals("java.lang.Integer")) {
						if (Integer.MAX_VALUE >  new Integer("" + fileValue)) {
							Integer val = new Integer("" + fileValue);
							Method method = thisClass.getMethod("set" + fileName.substring(0, 1).toUpperCase() + fileName.substring(1), field.getType());
							method.invoke(po, val);
							return true;
						}else{
							throw new AiyiIdTypeException("ID type is not a corresponding type at " + "set" + fileName.substring(0, 1).toUpperCase() + fileName.substring(1) + "\n"
									+ "the will give value type is " + fileValue.getClass().getName() + "\n" 
									+ "the filed type type is " + field.getType().getName());
						}
					}else if(calssName.equals("long") || calssName.equals("java.lang.Long")){
						Long val = new Long("" + fileValue);
						Method method = thisClass.getMethod("set" + fileName.substring(0, 1).toUpperCase() + fileName.substring(1), field.getType());
						method.invoke(po, val);
						return true;
					}else{
						Method method = thisClass.getMethod("set" + fileName.substring(0, 1).toUpperCase() + fileName.substring(1), field.getType());
						method.invoke(po, fileValue);
						return true;
					}
				} catch (AiyiIdTypeException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != fileValue) {
				try {
					Method method = null;
					if (fileValue instanceof String) {
						method = thisClass.getMethod("set" + fileName.substring(0, 1).toUpperCase() + fileName.substring(1), String.class);
					}else if(fileValue instanceof Integer){
						method = thisClass.getMethod("set" + fileName.substring(0, 1).toUpperCase() + fileName.substring(1), Integer.TYPE);
					}else if(fileValue instanceof Long){
						method = thisClass.getMethod("set" + fileName.substring(0, 1).toUpperCase() + fileName.substring(1), Long.TYPE);
					}else if(fileValue instanceof Double){
						method = thisClass.getMethod("set" + fileName.substring(0, 1).toUpperCase() + fileName.substring(1), Double.TYPE);
					}else if(fileValue instanceof Short){
						method = thisClass.getMethod("set" + fileName.substring(0, 1).toUpperCase() + fileName.substring(1), Short.TYPE);
					}
					
					
					method.invoke(po, fileValue);
				} catch (NoSuchMethodException e) {
					
					// TODO: handle exception
					try {
						Method method = thisClass.getMethod(
								"set" + fileName.substring(0, 1).toUpperCase() + fileName.substring(1), Boolean.TYPE);
						if (fileValue instanceof Integer) {
							method.invoke(po, (int) fileValue > 0 ? true : false);
						} 
					} catch (NoSuchMethodException e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
				}
			}
			return true;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	

	public String toTableString(String text){
		String tName = text.substring(0, 1).toLowerCase();
		for(int i = 1; i < text.length(); i++){
			if(!Character.isLowerCase(text.charAt(i))){
				tName += "_";
			}
			tName += text.substring(i, i + 1);
		}
		return tName.toLowerCase();
	}

	public String getTableNameByClazz(Class<? extends Po> po) {
		// TODO Auto-generated method stub
		if(po.isAnnotationPresent(TableName.class)){
			return po.getAnnotation(TableName.class).name();
		}else{
			String tName = toTableString(po.getSimpleName());
			if("po".equals(tName.substring(tName.length() - 3, tName.length() - 1))){
				tName = tName.substring(0,tName.length() - 3);
			}
			return tName;
		}
	}
	
}

