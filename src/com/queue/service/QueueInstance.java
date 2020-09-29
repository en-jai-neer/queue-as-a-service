package com.queue.service;

import java.util.LinkedList;
import java.util.Queue;

public class QueueInstance {
	private long size = 0;
	private Queue<String> queue = null;
	
	public QueueInstance() {
		queue = new LinkedList<>();
	}
	
	public long getSize() {
		return size;
	}
	
	public void setSize(long size) {
		this.size = size;
	}
	
	public Queue<String> getQueue () {
		return queue;
	}
	
	public void setQueue(Queue<String> queue) {
		this.queue = queue;
	}

	@Override
	public String toString() {
		return "QueueInstance [size=" + size + ", queue=" + queue + "]";
	}
}
