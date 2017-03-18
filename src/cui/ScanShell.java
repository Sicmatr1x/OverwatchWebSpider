/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cui;

import data.SubjectArea;
import io.Reader;
import io.Writer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author sicmatr1x
 */
public class ScanShell {

	/**
	 * 用户命令
	 */
	private String cmd;
	/**
	 * 论坛主题区帖子列表对象
	 */
	private SubjectArea sa;
	/**
	 * CUI交互线程开启标记； 1开启；0关闭
	 */
	private static int flag = 1;

	// --------------------------------------------------------------------------
	// 命令列表
	// --------------------------------------------------------------------------
	/**
	 * 获取命令列表
	 */
	private void help() {
		System.out
				.println("====================================================");
		System.out.println("load 文件路径" + "->" + "读取对象文件");
		System.out.println("write" + "->" + "手动保存模式 序列化对象文件到默认文件夹（暂不可用）");
		System.out.println("set floder" + "->" + "设置序列化对象文件的默认文件夹");
		System.out.println("scan 论坛主题区网址" + "->" + "扫描论坛主题区帖子");
		System.out.println("scanall 扫描所有主题区");
		System.out
				.println("====================================================");
	}

	private void load(String arg) {
		if (arg == null) {
			throw new IllegalArgumentException();
		}
		try {
			Reader.in(new File(arg));
		} catch (IOException ex) {
			System.out.println("load():" + "读取文件失败");
			Logger.getLogger(ScanShell.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (ClassNotFoundException ex) {
			System.out.println("load():" + "对象序列化失败");
			Logger.getLogger(ScanShell.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	private void write() {
		System.out.println("暂不可用");
		// Writer.out(null, sa)
	}

	private void set(String op, String arg) {
		if (arg == null || op == null) {
			throw new IllegalArgumentException();
		}
		switch (op) {
		case "floder":
			SubjectArea.fileSavePath = arg;
			break;
		}

	}

	/**
	 * 爬取单个指定的讨论区
	 * @param arg 讨论区网址
	 */
	private void scan(String arg) {
		if (arg == null) {
			throw new IllegalArgumentException();
		}
		this.sa = new SubjectArea(arg);
		// this.sa.run();
		Thread th = new Thread(this.sa);
		th.start();
	}

	public int run() {
		String[] work = this.cmd.split(" ");

		try {
			switch (work[0]) {
			case "load":
				this.load(work[1]);
				break;
			case "write":
				this.write();
				break;
			case "set":
				this.set(work[1], work[2]);
				break;
			case "scan":
				this.scan(work[1]);
				ScanShell.flag = 0;
				break;
			case "scanall":
				for (int i = 0; i < ScanShell.areaList.length; i++) {
					this.scan(ScanShell.areaList[i]);
				}
				ScanShell.flag = 0;
				break;
			case "exit":
				return 0;
			}
		} catch (IllegalArgumentException ex) {
			System.out.println("命令参数错误");
			Logger.getLogger(SubjectArea.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return 1;
	}

	public static final String[] areaList = {
			"http://bbs.ow.blizzard.cn/forum.php?mod=forumdisplay&fid=58", // BUG反馈区*
			"http://bbs.ow.blizzard.cn/forum.php?mod=forumdisplay&fid=61", // 客户服务区*
			"http://bbs.ow.blizzard.cn/forum.php?mod=forumdisplay&fid=55", // 技术支持区*
			"http://bbs.ow.blizzard.cn/forum.php?mod=forumdisplay&fid=38", // 信息发布区*
			"http://bbs.ow.blizzard.cn/forum.php?mod=forumdisplay&fid=62", // 综合讨论区*
			"http://bbs.ow.blizzard.cn/forum.php?mod=forumdisplay&fid=63", // 汉化及配音讨论区
			"http://bbs.ow.blizzard.cn/forum.php?mod=forumdisplay&fid=65" // 战友招募区*
	};

	// --------------------------------------------------------------------------
	/**
	 * 开始多线程爬取全部讨论区
	 */
	public void scanList() {
		for (int i = 0; i < ScanShell.areaList.length; i++) {
			this.scan(ScanShell.areaList[i]);
		}
	}

	// --------------------------------------------------------------------------
	public static void main(String[] args) {
		ScanShell shell = new ScanShell();
		/*
		 * for (int i = 0; i < ScanShell.areaList.length; i++) {
		 * shell.scan(ScanShell.areaList[i]); }
		 */

		Scanner input = new Scanner(System.in);

		while (ScanShell.flag != 0) {
			System.out.print("user:");
			shell.cmd = input.nextLine();
			flag = shell.run();
		}
	}
}
