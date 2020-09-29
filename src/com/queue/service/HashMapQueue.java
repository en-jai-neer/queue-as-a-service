package com.queue.service;

import java.util.HashMap;

public class HashMapQueue {
	private static HashMap <String, QueueInstance> map = null;
	
	public static HashMap<String, QueueInstance> getHashMapQueue(){
		if(map == null){
			map = new HashMap <String, QueueInstance>();
		}
		return map;
	}
}
