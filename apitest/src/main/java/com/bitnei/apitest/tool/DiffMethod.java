package com.bitnei.apitest.tool;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DiffMethod {

	   /**
	     * 返回当前数据类型
	     * @param source
	     * @return
	     */
	    public String getTypeValue(Object source){

	        if(source instanceof String){
	            return "String";
	        }

	        if(source instanceof Integer){
	            return "Integer";
	        }

	        if(source instanceof Float){
	            return "Float";
	        }

	        if(source instanceof Long){
	            return "Long";
	        }

	        if(source instanceof Double){
	            return "Double";
	        }

	        if(source instanceof Date){
	            return "Date";
	        }

	        if(source instanceof Boolean){
	            return "Boolean";
	        }

	        return "null";
	    }


	    /**
	     * 把Object变成JsonSchema
	     * @param source
	     * @return
	     */
	    public Object generateJsonSchema(Object source){

	        Object result = new Object();

	        //判断是否为JsonObject
	        if(source instanceof JSONObject){
	            JSONObject jsonResult = JSONObject.fromObject(result);
	            JSONObject sourceJSON = JSONObject.fromObject(source);
	            Iterator iterator = sourceJSON.keys();
	            while (iterator.hasNext()){
	                String key = (String) iterator.next();
	                Object nowValue = sourceJSON.get(key);

	                if(nowValue == null || nowValue.toString().equals("null")){
	                    jsonResult.put(key,"null");

	                }else if(isJsonObject(nowValue)){
	                    jsonResult.put(key,generateJsonSchema(nowValue));
	                }else if(isJsonArray(nowValue)){
	                    JSONArray tempArray = JSONArray.fromObject(nowValue);
	                    JSONArray newArray = new JSONArray();

	                    if(tempArray != null && tempArray.size() > 0 ){
	                        for(int i = 0 ;i < tempArray.size(); i++){
	                            newArray.add(generateJsonSchema(tempArray.get(i)));
	                        }
	                        jsonResult.put(key,newArray);
	                    }
	                }else if(nowValue instanceof List){
	                    List<Object> newList = new ArrayList<Object>();

	                    for(int i = 0;i<((List) nowValue).size();i++){
	                        newList.add(((List) nowValue).get(i));
	                    }

	                    jsonResult.put(key,newList);
	                }else {

	                    jsonResult.put(key,getTypeValue(nowValue));
	                }

	            }
	            return jsonResult;
	        }


	        if(source instanceof JSONArray){
	            JSONArray jsonResult = JSONArray.fromObject(source);
	            JSONArray tempArray = new JSONArray();
	            if(jsonResult != null && jsonResult.size() > 0){
	                for(int i = 0 ;i < jsonResult.size(); i++){
	                    tempArray.add(generateJsonSchema(jsonResult.get(i)));
	                }
	                return tempArray;
	            }

	        }

	        return getTypeValue(source);

	    }



	    /**
	     * JSON格式比对
	     * @param currentJSON
	     * @param expectedJSON
	     * @return
	     */
//	    public JSONObject diffJson(JSONObject currentJSON,JSONObject expectedJSON){
//
//	        JSONObject jsonDiff = new JSONObject();
//
//	        Iterator iterator = expectedJSON.keys();
//
//	        while (iterator.hasNext()){
//	            String key = (String)iterator.next();
//	            Object expectedValue = expectedJSON.get(key);
//	            Object currentValue = currentJSON.get(key);
//	            System.out.println("key+++++++++++++++++++++++"+key);
//	            System.out.println("expectedValue+++++++++++++++++++++++"+expectedValue);
//	            System.out.println("currentValue+++++++++++++++++++++++"+currentValue);
//	            if(!expectedValue.toString().equals(currentValue.toString())){
//	                JSONObject tempJSON = new JSONObject();
//	                tempJSON.put("value",currentValue);
//	                tempJSON.put("expected",expectedValue);
//	                jsonDiff.put(key,tempJSON);
//	            }
//	        }
//	        return jsonDiff;
//	    }


	    /**
	     * 判断是否为JSONObject
	     * @param value
	     * @return
	     */
	    public boolean isJsonObject(Object value){

	        try{
	            if(value instanceof JSONObject) {
	                return true;
	            }else {
	                return false;
	            }
	        }catch (Exception e){
	            return false;
	        }
	    }


	    /**
	     * 判断是否为JSONArray
	     * @param value
	     * @return
	     */
	    public boolean isJsonArray(Object value){

	        try{

	            if(value instanceof JSONArray){
	                return true;
	            }else {
	                return false;
	            }

	        }catch (Exception e){
	            return false;
	        }
	    }


	    /**
	     * JSON格式比对，值不能为空,且key需要存在
	     * @param current
	     * @param expected
	     * @return
	     */
	    public JSONObject diffFormatJson(Object current,Object expected){

	        JSONObject jsonDiff = new JSONObject();
            JSONObject tempJSON = new JSONObject();
	        if(isJsonObject(expected)) {

	            JSONObject expectedJSON = JSONObject.fromObject(expected);
	            JSONObject currentJSON = JSONObject.fromObject(current);

	            Iterator iterator = JSONObject.fromObject(expectedJSON).keys();

	            while (iterator.hasNext()) {
	                String key = (String) iterator.next();
	                Object expectedValue = expectedJSON.get(key);

	                //System.out.println("----------key ---------------"+key);
	                //System.out.println("----------expectedValue ---------------"+expectedValue);
	                if (!currentJSON.containsKey(key)) {
	                    tempJSON.put("actualKey", "不存在此" + key);
	                    tempJSON.put("expectedKey", key);
	                    jsonDiff.put(key, tempJSON);

	                }

	                if (currentJSON.containsKey(key)) {

	                    Object currentValue = currentJSON.get(key);
	                    //System.out.println("----------currentValue ---------------"+currentValue);

	                    if (expectedValue != null && currentValue == null || expectedValue.toString() != "null" && currentValue.toString() == "null") {
	                        tempJSON.put("actualValue", "null");
	                        tempJSON.put("expectedValue", expectedValue);
	                        jsonDiff.put(key, tempJSON);
	                    }

	                    if (expectedValue != null && currentValue != null) {
	                        if (isJsonObject(expectedValue) && !JSONObject.fromObject(expectedValue).isNullObject() || 
	                        		isJsonArray(expectedValue) && !JSONArray.fromObject(expectedValue).isEmpty()) {
	                            JSONObject getResultJSON = new JSONObject();
	                            getResultJSON = diffFormatJson(currentValue, expectedValue);
	                            if (getResultJSON != null) {
	                                jsonDiff.putAll(getResultJSON);
	                            }
	                        }
	                        if (expectedValue instanceof String) {
	                        	if(!expectedValue.equals(currentValue)) {
	        	                    tempJSON.put("currentValue is ", currentValue);
	        	                    tempJSON.put("expectedValue", expectedValue);
	        	                    jsonDiff.put(key, tempJSON);
	                        	}
	                        	
	                        }
	                        if (expectedValue instanceof Integer) {
	                        	if(!expectedValue.equals(currentValue)) {
	        	                    tempJSON.put("currentValue is ", currentValue);
	        	                    tempJSON.put("expectedValue is", expectedValue);
	        	                    jsonDiff.put(key, tempJSON);
	                        	}
	                        }
	                        if (expectedValue instanceof Float) {
	                        	//todo
	                        }
	                        if (expectedValue instanceof Long) {
	                        	//todo
	                        }
	                        if (expectedValue instanceof Double) {
	                        	//todo
	                        }
	                        if (expectedValue instanceof Date) {
	                        	//todo
	                        }
	                        if (expectedValue instanceof Boolean) {
	                        	if(!expectedValue.equals(currentValue)) {
	        	                    tempJSON.put("currentValue is ", currentValue);
	        	                    tempJSON.put("expectedValue is", expectedValue);
	        	                    jsonDiff.put(key, tempJSON);
	                        	}
	                        }
	                    }
	                }
	            }
	        }


	        if(isJsonArray(expected)){
	        	//System.out.println("----------isJsonArray ---------------");
	            JSONArray expectArray = JSONArray.fromObject(expected);
	            JSONArray currentArray = JSONArray.fromObject(current);

	            if(expectArray.size() != currentArray.size()){
	                tempJSON.put("actualLenth",currentArray.size());
	                tempJSON.put("expectLenth",expectArray.size());
	                jsonDiff.put("Length",tempJSON);
	            }

	            if(expectArray.size() != 0 && currentArray.size() != 0){
	                Object expectIndexValue = expectArray.get(0);
	                Object currentIndexValue = currentArray.get(0);

	                if(expectIndexValue != null && currentIndexValue != null){
	                    if (isJsonObject(expectIndexValue) && !JSONObject.fromObject(expectIndexValue).isNullObject() 
	                    		|| isJsonArray(expectIndexValue) && !JSONArray.fromObject(expectIndexValue).isEmpty()) {
	                        JSONObject getResultJSON = new JSONObject();
	                        getResultJSON = diffFormatJson(currentIndexValue, expectIndexValue);
	                        if (getResultJSON != null) {
	                            jsonDiff.putAll(getResultJSON);
	                        }
	                    }
	                }
	            }
	        }
	        

	        return jsonDiff;
	    }
	}

