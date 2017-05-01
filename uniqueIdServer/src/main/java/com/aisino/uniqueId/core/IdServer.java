package com.aisino.uniqueId.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aisino.uniqueId.util.SystemConfig;

/** Snowflake */
public class IdServer {
	// private final long twepoch = 1288834974657L;
	private final long twepoch = SystemConfig.twepoch;
	private final long workerIdBits = SystemConfig.workerIdBits;// 工程Id4位
	private final long datacenterIdBits = SystemConfig.datacenterIdBits;// 机房Id2位
	private final long busiIdBits = SystemConfig.busiIdBits;// 业务ID4位
	private final long maxWorkerId = -1L ^ (-1L << workerIdBits); // 31
	private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits); // 31
	private final long sequenceBits = SystemConfig.sequenceBits;
	private final long workerIdShift = sequenceBits;
	private final long datacenterIdShift = sequenceBits + workerIdBits;// 左移动12位
	private final long busiShift = sequenceBits + workerIdBits
			+ datacenterIdBits;// 左移18位
	private final long timestampLeftShift = sequenceBits + workerIdBits
			+ datacenterIdBits + busiIdBits;// 左移22位
	private final long sequenceMask = -1L ^ (-1L << sequenceBits);

	private long workerId;
	private long datacenterId;
	private long sequence = 0L;
	private long lastTimestamp = -1L;// 上次时间戳
	private Lock lock = new ReentrantLock();

	private static Logger logger = LogManager.getLogger();

	public IdServer(long workerId, long datacenterId) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format(
					"worker Id can't be greater than %d or less than 0",
					maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(String.format(
					"datacenter Id can't be greater than %d or less than 0",
					maxDatacenterId));
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	public long nextId(long busi) {
		long id = -1;
		lock.lock();
		try {
			long timestamp = timeGen();
			if (timestamp < lastTimestamp) {
				throw new Exception(
						String.format(
								"Clock moved backwards.  Refusing to generate id for %d milliseconds",
								lastTimestamp - timestamp));
			}else if (lastTimestamp == timestamp) {//如果是同一毫秒内生成的，则进行毫秒内序列
				sequence = (sequence + 1) & sequenceMask;
				System.out.print("h");
				// 当前毫秒截内序列溢出，即超过了4095
				if (sequence == 0) {// 这里等于0的情况是已经递增到最大值了即4096&4095=0，这时候需要重新获取下一个时间戳
					// 阻塞到下一个毫秒，获得新的时间截
					timestamp = tilNextMillis(lastTimestamp);
				}
			} else {
				// 时间截改变，毫秒内序列重置，相当于当前的时间截又会有对应的4095个序列值生成
				sequence = 0L;
				// 这里还继续自增，相当于下一个时间截沿用当前的时间截内序列，会减少生成的ID个数
				// sequence = (sequence + 1) & sequenceMask;
			}
			lastTimestamp = timestamp;
			id = ((timestamp - twepoch) << timestampLeftShift)
					| (busi << busiShift) | (datacenterId << datacenterIdShift)
					| (workerId << workerIdShift) | sequence;// 22 | 18 | 16 |
																// 12 | 最后的位数

		} catch (Exception e) {
			logger.error("exception:", e);
		} finally {
			lock.unlock();
		}
		return id;
	}

	/**
	 * 
	 * <p>
	 * 阻塞到下一个毫秒，一直到获得新的时间截
	 * </p>
	 * 
	 * @param lastTimestamp
	 * @return long
	 * @author: 王沾
	 * @date: Created on 2017-4-18 下午6:52:20
	 */
	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();// 当前时间戳
		while (timestamp <= lastTimestamp) {// 如果当前的时间戳小于上次的时间戳，那么就继续取当前的时间戳并返回
			timestamp = timeGen();
		}
		return timestamp;
	}

	/**
	 * 
	 * <p>
	 * 获取当前时间的毫秒数
	 * </p>
	 * 
	 * @return long
	 * @author: 王沾
	 * @date: Created on 2017-4-18 下午6:51:26
	 */
	protected long timeGen() {
		return System.currentTimeMillis();
	}

	public static void main(String[] args) throws Exception {
//		long beginlongtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
//				.parse("2016-01-01 00:00:00:000").getTime();
//		long endlongtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
//				"1970-01-01 08:00:00:000").getTime();
//
//		long beginid = ((beginlongtime) << 22);
//
//		long endid = ((endlongtime) << 22);
//		Calendar c = Calendar.getInstance();
//		System.out.println(beginlongtime);
//		// System.out.println(-1L ^ (-1L << 12));// 2的12次方减一
//		// System.out.println(Long.toHexString(-1L));
//		// System.out.println(4096 & 4095);
//		System.out.println(System.currentTimeMillis()-beginlongtime);
//		System.out.println(new Date(1480396604543L));
//		System.out.println(endlongtime);
		for (int i = 0; i < 10; i++) {
			
			IdServer id=new IdServer(0, 0);
			System.out.println(id.nextId(2));
		}

		// System.out.println(Long.toBinaryString(-1));
		/*
		 * long begin = System.currentTimeMillis();
		 * System.out.println(begin+";"+Long.toBinaryString(begin));
		 * Thread.sleep(2000); long end = System.currentTimeMillis();
		 * System.out.println(end+";"+Long.toBinaryString(end));
		 * System.out.println(Long.toBinaryString(2000));
		 */
		// IdServer idWorker = new IdServer(0, 0);
		// long id = idWorker.nextId(0L);

		/*
		 * IdServer idWorker = new IdServer(0, 0); SecureRandom sr = new
		 * SecureRandom(); for (int i = 0; i < 1; i++) { long id =
		 * idWorker.nextId(0L);
		 * 
		 * System.out.println("id:"+id+";id%8:"+(id%8)+";binary:"+Long.
		 * toBinaryString(id)); }
		 */
	}
}