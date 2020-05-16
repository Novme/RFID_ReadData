/*
 * MainFrame.java
 *
 * Created on 2016.8.19
 */

package com.yang.serialport.ui;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.yang.serialport.exception.NoSuchPort;
import com.yang.serialport.exception.NotASerialPort;
import com.yang.serialport.exception.PortInUse;
import com.yang.serialport.exception.SerialPortParameterFailure;
import com.yang.serialport.exception.TooManyListeners;
import com.yang.serialport.manage.SerialPortManager;
import com.yang.serialport.utils.ShowUtils;

/**
 * 主界面
 * 
 * @author yangle
 */
public class ReplyMainFrame extends JFrame {

	/**
	 * 程序界面宽度
	 */
	public static final int WIDTH = 500;

	/**
	 * 程序界面高度
	 */
	public static final int HEIGHT = 410;

	private JTextArea dataView = new JTextArea();
	private JScrollPane scrollDataView = new JScrollPane(dataView);
	private static String cardNumber;
	// 串口设置面板
	private JPanel serialPortPanel = new JPanel();// 创建一个串口设置的容器
	private JPanel replyPanel = new JPanel();// 创建一个响应的容器
	private JLabel serialPortLabel = new JLabel("串口");// 创建完的Label对象可以通过Container类中的add()方法
	private JLabel baudrateLabel = new JLabel("波特率");
	private JComboBox baudrateChoice = new JComboBox();// 波特率下拉框
	private JComboBox replyTypeChoice = new JComboBox();
	private JButton serialPortOperate = new JButton("打开串口");
	private JLabel cardNumberLabel = new JLabel("卡号");// 卡号标签
	private JTextField dataInput1 = new JTextField();// 用于配合dataInput显示提示信息
	private JTextField dataInput = new JTextField();// 用于输入卡号的单列文本框
	private JLabel replyLabel = new JLabel("响应");
	private JComboBox commChoice = new JComboBox();// COM下拉列表组建

	private List<String> commList = null;
	private SerialPort serialport; 
	public ReplyMainFrame() throws IOException {//构造方法
		initView();
		initComponents();
		actionListener();
		initData();
	}

	private void initView() throws IOException {
		// 关闭程序
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		// 禁止窗口最大化
		setResizable(false);

		// 设置程序窗口居中显示
		Point p = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getCenterPoint();
		setBounds(p.x - WIDTH / 2, p.y - HEIGHT / 2, WIDTH, HEIGHT);
		this.setLayout(null);

		setTitle("读写器");
	}

	private void initComponents() {
		// 数据显示
		dataView.setFocusable(false);
		scrollDataView.setBounds(10, 10, 475, 200);
		add(scrollDataView);

		// 串口设置
		serialPortPanel.setBorder(BorderFactory.createTitledBorder("串口设置"));
		serialPortPanel.setBounds(10, 220, 475, 60);
		serialPortPanel.setLayout(null);
		add(serialPortPanel);

		serialPortLabel.setForeground(Color.gray);// 串口标签设置
		serialPortLabel.setBounds(10, 25, 40, 20);
		serialPortPanel.add(serialPortLabel);
		commChoice.setFocusable(false);// 串口下拉列表设置
		commChoice.setBounds(35, 25, 60, 20);
		serialPortPanel.add(commChoice);

		baudrateLabel.setForeground(Color.gray);// 波特率标签设置
		baudrateLabel.setBounds(100, 25, 40, 20);
		serialPortPanel.add(baudrateLabel);
		baudrateChoice.setFocusable(false);// 波特率下拉列表设置
		baudrateChoice.setBounds(140, 25, 60, 20);
		serialPortPanel.add(baudrateChoice);

		serialPortOperate.setFocusable(false);// 打开串口
		serialPortOperate.setBounds(340, 25, 90, 20);// x y 长 宽
		serialPortPanel.add(serialPortOperate);

		// 响应设置
		replyPanel.setBorder(BorderFactory.createTitledBorder("响应设置"));
		replyPanel.setBounds(10, 305, 475, 60);
		replyPanel.setLayout(null);
		add(replyPanel);

		String info2 = "请输入响应的卡号<8位>";
		dataInput.setText(info2);
		dataInput1.addFocusListener(new MyFocusListener("", dataInput));// 添加焦点事件反映
		dataInput.addFocusListener(new MyFocusListener(info2, dataInput));
		cardNumberLabel.setForeground(Color.gray);// 卡号标签
		cardNumberLabel.setBounds(10, 25, 40, 20);
		replyPanel.add(cardNumberLabel);
		dataInput.setBounds(35, 25, 170, 20);// 卡号文本框设置
		replyPanel.add(dataInput);
		dataInput1.setBounds(0, 0, 0, 0);// 配合卡号文本框显示默认提示
		replyPanel.add(dataInput1);
		replyLabel.setForeground(Color.gray);// 响应标签设置
		replyLabel.setBounds(305, 25, 40, 20);
		replyPanel.add(replyLabel);
		replyTypeChoice.setFocusable(false);// 响应下拉列表设置
		replyTypeChoice.setBounds(335, 25, 120, 20);
		replyPanel.add(replyTypeChoice);

	}

	@SuppressWarnings("unchecked")
	private void initData() {
		commList = SerialPortManager.findPort();
		// 检查是否有可用串口，有则加入选项中
		if (commList == null || commList.size() < 1) {
			ShowUtils.warningMessage("没有搜索到有效串口！");
		} else {
			for (String s : commList) {
				commChoice.addItem(s);
			}
		}

		baudrateChoice.addItem("9600");
		baudrateChoice.addItem("19200");
		baudrateChoice.addItem("38400");
		baudrateChoice.addItem("57600");
		baudrateChoice.addItem("115200");

		replyTypeChoice.addItem("命令执行成功");
		replyTypeChoice.addItem("关闭串口失败");
		replyTypeChoice.addItem("静默失败");
		replyTypeChoice.addItem("没有读到标签/卡");
		replyTypeChoice.addItem("命令不支持");
		replyTypeChoice.addItem("命令不被允许");
		replyTypeChoice.addItem("Hardware Type not compatible");
	}

	private void actionListener() {
		serialPortOperate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ("打开串口".equals(serialPortOperate.getText())
						&& serialport == null) {
					openSerialPort(e);
				} else {
					closeSerialPort(e);
				}
			}
		});
	}

	/**
	 * 打开串口
	 * 
	 * @param evt
	 *            点击事件
	 */
	private void openSerialPort(java.awt.event.ActionEvent evt) {
		// 获取串口名称
		String commName = (String) commChoice.getSelectedItem();
		// System.out.println(commName);
		// 获取波特率
		int baudrate = 9600;
		String bps = (String) baudrateChoice.getSelectedItem();
		baudrate = Integer.parseInt(bps);

		// 检查串口名称是否获取正确
		if (commName == null || commName.equals("")) {
			ShowUtils.warningMessage("没有搜索到有效串口！");
		} else {
			try {
				serialport = SerialPortManager.openPort(commName, baudrate);
				if (serialport != null) {
					dataView.setText("串口已打开" + "\r\n");
					serialPortOperate.setText("关闭串口");
				}
			} catch (SerialPortParameterFailure e) {
				e.printStackTrace();
			} catch (NotASerialPort e) {
				e.printStackTrace();
			} catch (NoSuchPort e) {
				e.printStackTrace();
			} catch (PortInUse e) {
				e.printStackTrace();
				ShowUtils.warningMessage("串口已被占用！");
			}
		}

		try {
			SerialPortManager.addListener(serialport, new SerialListener());
		} catch (TooManyListeners e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭串口
	 * 
	 * @param evt
	 *            点击事件
	 */
	private void closeSerialPort(java.awt.event.ActionEvent evt) {
		SerialPortManager.closePort(serialport);
		dataView.setText("串口已关闭" + "\r\n");
		serialPortOperate.setText("打开串口");
	}

	private class SerialListener implements SerialPortEventListener {
		/**
		 * 处理监控到的串口事件
		 */
		public void serialEvent(SerialPortEvent serialPortEvent) {

			switch (serialPortEvent.getEventType()) {

			case SerialPortEvent.BI: // 10 通讯中断
				ShowUtils.errorMessage("与串口设备通讯中断");
				break;

			case SerialPortEvent.OE: // 7 溢位（溢出）错误

			case SerialPortEvent.FE: // 9 帧错误

			case SerialPortEvent.PE: // 8 奇偶校验错误

			case SerialPortEvent.CD: // 6 载波检测

			case SerialPortEvent.CTS: // 3 清除待发送数据

			case SerialPortEvent.DSR: // 4 待发送数据准备好了

			case SerialPortEvent.RI: // 5 振铃指示

			case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
				break;

			case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据
				byte[] data0 = null;
				try {
					if (serialport == null) {
						ShowUtils.errorMessage("串口对象为空！监听失败！");
					} else {
						String finalData = "EECCstatusCMD02lengthDATA00000D0A";
						String status;
						String CMD;
						String length;
						String DATA;
						// 读取串口数据
						data0 = SerialPortManager.readFromPort(serialport);
						String data = new String(data0);
						dataView.append("上位机请求数据：" + data + "\r\n");
						String temp = (String) replyTypeChoice
								.getSelectedItem();
						if ("命令执行成功".equals(temp)) {
							status = "00";
						} else if ("关闭串口失败".equals(temp)) {
							status = "01";
						} else if ("静默失败".equals(temp)) {
							status = "02";
						} else if ("没有读到标签/卡".equals(temp)) {
							status = "0F";
						} else if ("命令不支持".equals(temp)) {
							status = "10";
						} else if ("命令不被允许".equals(temp)) {
							status = "11";
						} else {
							status = "12";
						}
						// System.out.println(status);
						CMD = data.substring(4, 6);
						// System.out.println(CMD);
						if ("01".equals(CMD)) {
							length = "0002";
							DATA = "00" + data.substring(8, 10);// 两字节卡片类型
							// System.out.println(DATA);
							// String finalData
							// ="EECCstatusCMD02lengthDATA0000D0A";
							StringBuilder tempData = new StringBuilder(
									finalData);
							tempData.replace(21, 25, DATA);// 从后向前替换，因为替换的过程中，长度一直都在变化
							tempData.replace(15, 21, length);
							tempData.replace(10, 13, CMD);
							tempData.replace(4, 10, status);
							finalData = tempData.toString();
							// System.out.println(finalData);
							SerialPortManager.sendToPort(serialport, finalData);// 响应
						}
						if ("02".equals(CMD)) {
							length = "0004";
							if (dataInput.getText().toString().length() > 8) {
								dataView.append("卡号长度大于8位" + "\r\n");
							} else if (dataInput.getText().toString().length() < 8) {
								dataView.append("卡号长度小于8位" + "\r\n");
							} else {
								DATA = dataInput.getText().toString();// 4字节卡号
								// System.out.println(DATA);
								// String finalData
								// ="EECCstatusCMD02lengthDATA0000D0A";
								StringBuilder tempData = new StringBuilder(
										finalData);
								tempData.replace(21, 25, DATA);// 从后向前替换，因为替换的过程中，长度一直都在变化
								tempData.replace(15, 21, length);
								tempData.replace(10, 13, CMD);
								tempData.replace(4, 10, status);
								finalData = tempData.toString();
								// System.out.println(finalData);
								SerialPortManager.sendToPort(serialport,
										finalData);// 响应
							}
						}
						if ("03".equals(CMD)) {
							length = "0000";
							cardNumber = data.substring(14, 22);// 上位机选择的卡号
						}
						if ("04".equals(CMD)) {
							length = "0000";
						}
						if ("05".equals(CMD)) {
							length = "0011";
							BufferedReader br = new BufferedReader(
									new FileReader("simulationCard\\" + cardNumber + "\\"
											+ data.substring(14, 16) + ".txt"));// 模拟RFID读写器从标签中读数据

							DATA = br.readLine();
							// System.out.println(DATA);
							br.close();
							DATA = DATA.substring(16, 48);
							// System.out.println(DATA);
							// String finalData
							// ="EECCstatusCMD02lengthDATA0000D0A";
							StringBuilder tempData = new StringBuilder(
									finalData);
							tempData.replace(21, 25, DATA);// 从后向前替换，因为替换的过程中，长度一直都在变化
							tempData.replace(15, 21, length);
							tempData.replace(10, 13, CMD);
							tempData.replace(4, 10, status);
							finalData = tempData.toString();
							SerialPortManager.sendToPort(serialport, finalData);// 响应

						}
						if ("06".equals(CMD)) {
							length = "0000";
							// System.out.println(data.substring(14, 16));
							FileWriter fw = new FileWriter("simulationCard\\" + cardNumber + "\\"
									+ data.substring(14, 16) + ".txt");// 模拟RFID读写器向标签中写数据
							fw.write(data);
							fw.close();
						}
					}
				} catch (Exception e) {
					ShowUtils.errorMessage(e.toString());
					// 发生读取错误时显示错误信息后退出系统
					System.exit(0);
				}
				break;
			}
		}
	}

	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new ReplyMainFrame().setVisible(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}

class MyFocusListener implements FocusListener {
	String info;
	JTextField jtf;

	public MyFocusListener(String info, JTextField jtf) {
		this.info = info;
		this.jtf = jtf;
	}

	@Override
	public void focusGained(FocusEvent e) {// 获得焦点的时候,清空提示文字
		String temp = jtf.getText();
		if (temp.equals(info)) {
			jtf.setText("");
		}
	}

	@Override
	public void focusLost(FocusEvent e) {// 失去焦点的时候,判断如果为空,就显示提示文字
		String temp = jtf.getText();
		if (temp.equals("")) {
			jtf.setText(info);
		}
	}
}
