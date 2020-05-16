/*
 * MainFrame.java
 *
 * Created on 2016.8.19
 */

package com.yang.serialport.ui;

import getAddress.GetData;
import getAddress.MatchChinese;
import getAddress.MatchCode;
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
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
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

import sendDataToServer.SendToServer;

import com.yang.serialport.exception.NoSuchPort;
import com.yang.serialport.exception.NotASerialPort;
import com.yang.serialport.exception.PortInUse;
import com.yang.serialport.exception.SendDataToSerialPortFailure;
import com.yang.serialport.exception.SerialPortOutputStreamCloseFailure;
import com.yang.serialport.exception.SerialPortParameterFailure;
import com.yang.serialport.exception.TooManyListeners;
import com.yang.serialport.manage.SerialPortManager;
import com.yang.serialport.utils.ShowUtils;

import conversion_of_number_systems.Binary2str;
import conversion_of_number_systems.Sixteen2Two;
import conversion_of_number_systems.Str2Binary;
import conversion_of_number_systems.Ten2TwoStr;
import conversion_of_number_systems.Two2Sixteen;

/**
 * 主界面
 * 
 * @author yangle
 */
public class MainFrame extends JFrame implements ItemListener {

	/**
	 * 程序界面宽度
	 */
	public static final int WIDTH = 500;

	/**
	 * 程序界面高度
	 */
	public static final int HEIGHT = 700;
	public static int contral;// 用来记录请求数据时，“0”返回数据是订单号，“1”返回的是实时数据
	public static int serverContral = 0;// 用来判断向服务器发送的数据是否完整，如果数据不完整便不发送
	public static int readOrWriterContral = 0;// 当和为10时才能执行读数据或写数据操作
	public static String temp;// 定义全局变量temp作
	public static String cardNumber = null;
	public static String cardType = null;
	public static String finalOrderNumber = null;// 最终要发送给server的订单号
	public static String finalAddress = null;// 最终要发送给server的地址
	public static String finalStatus = null;// 最终要发给server的签收状态
	public static String finalName = null;// 最终要发给server的扫描员姓名
	public static String finalPassword = null;// 最终要发给server的扫描员密码
	public static String dataForServer = null;// finalOrdeNumber + finalAddress
												// + finalStatus
	private JTextArea dataView = new JTextArea();
	private JScrollPane scrollDataView = new JScrollPane(dataView);

	// 串口设置面板
	private JPanel serialPortPanel = new JPanel();// 创建一个容器
	private JLabel serialPortLabel = new JLabel("串口");// 创建完的Label对象可以通过Container类中的add()方法
	private JLabel baudrateLabel = new JLabel("波特率");
	private JComboBox commChoice = new JComboBox();// 下拉列表组建
	private JComboBox baudrateChoice = new JComboBox();
	private JButton serialPortOperate = new JButton("打开串口");

	// 操作读数据面板
	private JPanel operatePanel = new JPanel();//创建读写数据操作之前容器
	private JPanel operatePanel1 = new JPanel();//创建读写数据容器
	private JPanel operatePanel11 = new JPanel();//创建读数据容器
	private JPanel operatePanel12 = new JPanel();//创建写数据容器

	private JLabel orderNumber = new JLabel("订单号");
	private JTextField dataInput2 = new JTextField();// 单行文本框组建 订单号
	private JTextField keyInput = new JTextField(); // 单行文本框 密钥 限定12位十进制数
	private JTextField dataInput3 = new JTextField();// 单行文本框组建为了使dataInput1,dataInput2,同时显示默认提示信息，不需要太关心

	private JButton sendData1 = new JButton("请求类型");
	private JButton sendData2 = new JButton("寻卡");
	private JButton sendData3 = new JButton("选卡");
	private JButton sendData4 = new JButton("认证密钥");
	private JButton sendData5 = new JButton("读订单号信息");
	private JButton sendData6 = new JButton("开始写数据");
	private JButton sendData7 = new JButton("读实时信息");
	private JButton sendDataToServer = new JButton("向服务器发送数据");
	private JLabel scanName = new JLabel("姓名");
	private JLabel scanPassword = new JLabel("密码");
	private JTextField nameInput = new JTextField();
	private JTextField passwordInput = new JTextField();
	private JComboBox s1 = new JComboBox();// 下拉列表组建
	private JComboBox s2 = new JComboBox();// 下拉列表组建
	private JComboBox s3 = new JComboBox();// 下拉列表组建
	private JComboBox s4 = new JComboBox();// 下拉列表组建
	private JComboBox s5 = new JComboBox();// 下拉列表组建
	private JComboBox s6Type = new JComboBox();// 模式下拉列表组建
	private JComboBox s6Province = new JComboBox();// 省下拉列表组
	private JComboBox s6City = new JComboBox();// 市下拉列表组建
	private JComboBox s6County = new JComboBox();// 县下拉列表组建
	private JLabel status = new JLabel("订单签收状态");
	private JComboBox s6Status = new JComboBox();// 订单签收状态下拉表
	private JLabel express = new JLabel("快号");
	private JComboBox s6Express = new JComboBox();// 数据初始化/数据更新下拉表
	

	private List<String> commList = null;
	private SerialPort serialport;

	public MainFrame() throws IOException {
		initView();
		initComponents();
		actionListener();
		initData();
		s6Province.addItemListener(this);// 为实现省市县三级联动，为下拉框添加监听事件
		s6City.addItemListener(this);// 为实现省市县三级联动，为下拉框添加监听事件
		s6Express.addItemListener(this);// 为实现快号和订单签收状态联动
		// s1.addItemListener(this);//为实现s1和s2,s3,s4,s5,s6的联动
	}

	private void initBox() throws IOException, IOException {
		s1.addItem("不在选择模式未休眠的卡");
		s1.addItem("在选择模式未休眠的卡");
		s1.addItem("不在选择模式所有的卡");
		s1.addItem("在选择模式请求所有的卡");

		s2.addItem("不在选择模式");
		s2.addItem("在选择模式");

		s3.addItem("不在选择模式");
		s3.addItem("在选择模式");

		s4.addItem("不在选择模式A");
		s4.addItem("不在选择模式B");
		s4.addItem("在选择模式A");
		s4.addItem("在选择模式B");

		s5.addItem("不在选择模式");
		s5.addItem("在选择模式");

		s6Type.addItem("不在选择模式");
		s6Type.addItem("在选择模式");

		s6Express.addItem("初始化卡数据");
		s6Express.addItem("更新卡数据");

		s6Province.addItem("请选择省份");
		s6City.addItem("请选择市");
		s6County.addItem("请选择县");

		s6Status.addItem("未签收");

		HashMap<String, String> hm = GetData
				.getSingleData("address\\province.txt");// 获取省份信息
		for (String key : hm.keySet()) {// 将省份信息添加到下拉框
			s6Province.addItem(hm.get(key));
		}
	}

	@Override
	public void itemStateChanged(java.awt.event.ItemEvent e) {// 为了实现省市县三级联动和快号和订单签收状态联动
		// TODO Auto-generated method stub
		String temp = ((JComboBox) e.getSource()).getSelectedItem().toString();// 获取下拉框信息

		if (temp.equals("更新卡数据")) {
			s6Status.removeItemListener(this);
			s6Status.removeAllItems();
			s6Status.addItemListener(this);
			s6Status.addItem("未签收");
			s6Status.addItem("签收");
		}
		if (temp.equals("初始化卡数据")) {
			s6Status.removeItemListener(this);
			s6Status.removeAllItems();
			s6Status.addItemListener(this);
			s6Status.addItem("未签收");
		}

		String tempProvince = null;
		String tempCity = null;
		HashMap<String, String> province = null;
		try {
			province = GetData.getSingleData("address\\province.txt");// 获取省份信息,返回HashMap类型的数据
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (String key : province.keySet()) {// 遍历省份信息
			if (temp.equals(province.get(key))) {// 匹配省份信息，实现，省_市的联动
				s6City.removeItemListener(this);
				s6City.removeAllItems();
				s6City.addItemListener(this);
				try {
					HashMap<String, String> city = GetData
							.getSingleData("address\\" + province.get(key)
									+ "_市.txt");// 获取对应省下所有市的信息
					for (String key1 : city.keySet()) {
						s6City.addItem(city.get(key1));
					}
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if ("请选择省份".equals(temp)) {
				s6City.removeItemListener(this);
				s6City.removeAllItems();
				s6City.addItemListener(this);
				s6City.addItem("请选择市");
				s6County.addItem("请选择县");
			}
		}
		tempProvince = (String) s6Province.getSelectedItem();
		tempCity = (String) s6City.getSelectedItem();

		HashMap<String, String> city = null;
		if ("请选择省份".equals(tempProvince)) {

		} else {
			try {
				city = GetData.getSingleData("address\\" + tempProvince
						+ "_市.txt");// 获取对应省下所有市的信息
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			for (String key : city.keySet()) {// 遍历对应的省下所有的市
				// System.out.println(city.get(key));
				if (temp.equals(city.get(key))) {
					HashMap<String, String> county = null;// 获取市_县信息
					try {
						county = GetData.getSingleData("address\\"
								+ tempProvince + "_" + tempCity + ".txt");
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					s6County.removeItemListener(this);
					s6County.removeAllItems();
					s6County.addItemListener(this);
					for (String key1 : county.keySet()) {// 遍历市_县内容

						s6County.addItem(county.get(key1));
						// System.out.println(county.get(key));
					}
				}
			}
		}

	}

	private void initView() throws IOException {
		initBox();// wei
		// 关闭程序
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		// 禁止窗口最大化
		setResizable(false);

		// 设置程序窗口居中显示
		Point p = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getCenterPoint();
		setBounds(p.x - WIDTH / 2, p.y - HEIGHT / 2, WIDTH, HEIGHT);
		this.setLayout(null);

		setTitle("上位机");
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
		commChoice.setBounds(35, 25, 100, 20);
		serialPortPanel.add(commChoice);

		baudrateLabel.setForeground(Color.gray);// 波特率标签设置
		baudrateLabel.setBounds(180, 25, 40, 20);
		serialPortPanel.add(baudrateLabel);

		baudrateChoice.setFocusable(false);// 波特率下拉列表设置
		baudrateChoice.setBounds(220, 25, 100, 20);
		serialPortPanel.add(baudrateChoice);

		serialPortOperate.setFocusable(false);// 打开串口
		serialPortOperate.setBounds(375, 25, 90, 20);// x y 长 宽
		serialPortPanel.add(serialPortOperate);

		// 读数据/写数据之前要操作的界面
		operatePanel
				.setBorder(BorderFactory.createTitledBorder("读数据/写数据之前要操作"));
		operatePanel.setBounds(10, 305, 475, 110);
		operatePanel.setLayout(null);
		add(operatePanel);

		sendData1.setFocusable(false);// 请求卡类型按钮设置
		sendData1.setBounds(115, 25, 90, 20);
		operatePanel.add(sendData1);
		s1.setFocusable(false);// s1下拉列表设置
		s1.setBounds(10, 25, 100, 20);
		operatePanel.add(s1);

		sendData2.setFocusable(false);// 寻卡按钮设置
		sendData2.setBounds(375, 25, 90, 20);
		operatePanel.add(sendData2);
		s2.setFocusable(false);// s2下拉列表设置
		s2.setBounds(270, 25, 100, 20);
		operatePanel.add(s2);

		sendData3.setFocusable(false);// 选卡按钮设置
		sendData3.setBounds(115, 70, 90, 20);
		operatePanel.add(sendData3);
		s3.setFocusable(false);// s3下拉列表设置
		s3.setBounds(10, 70, 100, 20);
		operatePanel.add(s3);

		String info1 = "输入12位认证密钥";
		keyInput.setText(info1);
		keyInput.addFocusListener(new MyFocusListener(info1, keyInput));
		sendData4.setFocusable(false);// 认证密钥按钮设置
		sendData4.setBounds(375, 70, 90, 20);
		operatePanel.add(sendData4);
		keyInput.setBounds(270, 55, 100, 20);
		operatePanel.add(keyInput);
		s4.setFocusable(false);// s4下拉列表设置
		s4.setBounds(270, 80, 100, 20);
		operatePanel.add(s4);

		// 读数据/写数据界面
		operatePanel1.setBorder(BorderFactory.createTitledBorder("读/写"));
		operatePanel1.setBounds(10, 440, 475, 210);
		operatePanel1.setLayout(null);
		add(operatePanel1);
		
		operatePanel11.setBorder(BorderFactory.createTitledBorder("读数据"));//读数据界面
		operatePanel11.setBounds(5,20,465,80);
		operatePanel11.setLayout(null);
		operatePanel1.add(operatePanel11);
		
		

		s5.setFocusable(false);// s5下拉列表设置
		s5.setBounds(5, 20, 90, 20);
		operatePanel11.add(s5);
		sendData5.setFocusable(false);// 读订单号数据按钮设置
		sendData5.setBounds(100, 20, 115, 20);
		operatePanel11.add(sendData5);
		sendData7.setFocusable(false);// 读实时信息按钮
		sendData7.setBounds(215, 20, 105, 20);
		operatePanel11.add(sendData7);
		
		String name = "请输入姓名";
		nameInput.setText(name);
		nameInput.addFocusListener(new MyFocusListener("", nameInput));// 添加焦点事件反映
		nameInput.addFocusListener(new MyFocusListener(name, nameInput));
		
		String password = "请输入密码";
		passwordInput.setText(password);
		passwordInput.addFocusListener(new MyFocusListener("", passwordInput));// 添加焦点事件反映
		passwordInput.addFocusListener(new MyFocusListener(password, passwordInput));
		
		scanName.setForeground(Color.gray);
		scanName.setBounds(5,45,40,20);
		operatePanel11.add(scanName);
		nameInput.setBounds(35,45,120,20);
		operatePanel11.add(nameInput);
		
		scanPassword.setForeground(Color.gray);
		scanPassword.setBounds(165,45,40,20);
		operatePanel11.add(scanPassword);
		passwordInput.setBounds(195,45,126,20);
		operatePanel11.add(passwordInput);
		
		sendDataToServer.setFocusable(false);// 向服务器发送数据请求按钮
		sendDataToServer.setBounds(320, 35, 140, 20);
		operatePanel11.add(sendDataToServer);
		
		
		operatePanel12.setBorder(BorderFactory.createTitledBorder("写数据"));//写数据界面
		operatePanel12.setBounds(5,105,465,95);
		operatePanel12.setLayout(null);
		operatePanel1.add(operatePanel12);
		
		String info2 = "初始化时输入更新时不输入";
		dataInput2.setText(info2);
		dataInput3.addFocusListener(new MyFocusListener("", dataInput3));// 添加焦点事件反映
		dataInput2.addFocusListener(new MyFocusListener(info2, dataInput2));

		express.setForeground(Color.gray);
		express.setBounds(115, 20, 40, 20);
		operatePanel12.add(express);

		s6Express.setFocusable(false);
		s6Express.setBounds(140, 20, 120, 20);
		operatePanel12.add(s6Express);

		orderNumber.setForeground(Color.gray);// 订单号标签设置
		orderNumber.setBounds(270, 20, 40, 20);
		operatePanel12.add(orderNumber);
		dataInput2.setBounds(310, 20, 150, 20);// 订单号 文本输入框设置
		operatePanel12.add(dataInput2);
		dataInput3.setBounds(0, 0, 0, 0);// 为了使dataInput1,dataInput2同时显示默认提示
		operatePanel.add(dataInput3);

		s6Type.setFocusable(false);// s5Type下拉表设置
		s6Type.setBounds(5, 20, 100, 20);
		operatePanel12.add(s6Type);
		s6Province.setFocusable(false);// s6Provice下拉表设置
		s6Province.setBounds(5, 45, 147, 20);
		operatePanel12.add(s6Province);
		s6City.setFocusable(false);// s6City下拉表设置
		s6City.setBounds(160, 45, 147, 20);
		operatePanel12.add(s6City);
		s6County.setFocusable(false);// s6County下拉表设置
		s6County.setBounds(315, 45, 145, 20);
		operatePanel12.add(s6County);
		status.setForeground(Color.gray);// 订单签收状态标签设置
		status.setBounds(5, 70, 80, 20);
		operatePanel12.add(status);
		s6Status.setFocusable(false);// 订单签收状态下拉表设置
		s6Status.setBounds(85, 70, 80, 20);
		operatePanel12.add(s6Status);
		sendData6.setFocusable(false); // 写数据按钮设置
		sendData6.setBounds(185, 70, 100, 20);
		operatePanel12.add(sendData6);
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

		sendData1.addActionListener(new ActionListener() {// 添加请求类型按钮监控事件

					@Override
					public void actionPerformed(ActionEvent e) {
						sendData1(e);
					}
				});

		sendData2.addActionListener(new ActionListener() {// 添加寻卡按钮监控事件

					@Override
					public void actionPerformed(ActionEvent e) {
						sendData2(e);
					}
				});

		sendData3.addActionListener(new ActionListener() {// 添加选卡按钮监控事件

					@Override
					public void actionPerformed(ActionEvent e) {
						sendData3(e);
					}
				});

		sendData4.addActionListener(new ActionListener() {// 添加认证密钥按钮监控事件

					@Override
					public void actionPerformed(ActionEvent e) {
						sendData4(e);
					}
				});

		sendData5.addActionListener(new ActionListener() {// 添加读订单号数据按钮监控事件

					@Override
					public void actionPerformed(ActionEvent e) {
						sendData5(e);
					}
				});

		sendData6.addActionListener(new ActionListener() {// 添加写数据按钮监控事件

					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							sendData6(e);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
		sendData7.addActionListener(new ActionListener() {// 添加读实时数据按钮监控事件

					@Override
					public void actionPerformed(ActionEvent e) {
						sendData7(e);
					}
				});
		sendDataToServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendDataToServer(e);
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

	/**
	 * 发送数据
	 * 
	 * @param evt
	 *            点击事件
	 */
	private void sendData1(java.awt.event.ActionEvent evt) {//请求类型
		String name = (String) s1.getSelectedItem();
		String data = null;
		if ("在选择模式未休眠的卡".equals(name)) {
			temp = name.substring(0, 1);
			data = "EECC01020100010000000D0A";
		} else if ("不在选择模式所有的卡".equals(name)) {
			temp = name.substring(0, 1);
			data = "EECC01020000010100000D0A";
		} else if ("不在选择模式未休眠的卡".equals(name)) {
			temp = name.substring(0, 1);
			data = "EECC01020000010000000D0A";
		} else {
			temp = name.substring(0, 1);// 在选择模式请求所有的卡
			data = "EECC01020100010100000D0A";
		}
		try {
			SerialPortManager.sendToPort(serialport, data);
		} catch (SendDataToSerialPortFailure e) {
			e.printStackTrace();
		} catch (SerialPortOutputStreamCloseFailure e) {
			e.printStackTrace();
		}
		readOrWriterContral += 1;
	}

	private void sendData2(java.awt.event.ActionEvent evt) {//寻卡
		String name = (String) s2.getSelectedItem();
		String data = null;
		if ("不在选择模式".equals(name) && temp.equals(name.substring(0, 1))) {
			data = "EECC020200000000000D0A";
			try {
				SerialPortManager.sendToPort(serialport, data);
			} catch (SendDataToSerialPortFailure e) {
				e.printStackTrace();
			} catch (SerialPortOutputStreamCloseFailure e) {
				e.printStackTrace();
			}
			readOrWriterContral += 2;
		} else if ("在选择模式".equals(name) && temp.equals(name.substring(0, 1))) {
			data = "EECC020201000000000D0A";
			try {
				SerialPortManager.sendToPort(serialport, data);
			} catch (SendDataToSerialPortFailure e) {
				e.printStackTrace();
			} catch (SerialPortOutputStreamCloseFailure e) {
				e.printStackTrace();
			}
			readOrWriterContral += 2;
		} else {
			dataView.append("不在选择模式 与 在选择模式不匹配，请重新选择" + "\r\n");
		}

	}

	private void sendData3(java.awt.event.ActionEvent evt) {//选卡
		String name = (String) s3.getSelectedItem();
		String data = null;
		if ("不在选择模式".equals(name) && temp.equals(name.substring(0, 1))) {
			data = "EECC0302000004cardNumber00000D0A";
			StringBuffer sb = new StringBuffer(data);
			sb.replace(14, 24, cardNumber);
			data = sb.toString();
			try {
				SerialPortManager.sendToPort(serialport, data);
			} catch (SendDataToSerialPortFailure e) {
				e.printStackTrace();
			} catch (SerialPortOutputStreamCloseFailure e) {
				e.printStackTrace();
			}
			readOrWriterContral += 3;
		} else if ("在选择模式".equals(name) && temp.equals(name.substring(0, 1))) {
			data = "EECC0302010004cardNumber00000D0A";
			StringBuffer sb = new StringBuffer(data);
			sb.replace(14, 24, cardNumber);
			data = sb.toString();
			try {
				SerialPortManager.sendToPort(serialport, data);
			} catch (SendDataToSerialPortFailure e) {
				e.printStackTrace();
			} catch (SerialPortOutputStreamCloseFailure e) {
				e.printStackTrace();
			}
			readOrWriterContral += 3;
		} else {
			dataView.append("不在选择模式 与 在选择模式不匹配，请重新选择" + "\r\n");
		}

	}

	private void sendData4(java.awt.event.ActionEvent evt) {//认证密钥
		String name = (String) s4.getSelectedItem();
		String data = null;
		if ("不在选择模式A".equals(name) && temp.equals(name.substring(0, 1))) {
			data = "EECC0402000018typeExpressKey4cardNumber00000D0A";
			if (keyInput.getText().toString().length() > 12) {
				dataView.append("密钥长度大于12位，重新输入"+ "\r\n");
			} else if (keyInput.getText().toString().length() < 12) {
				dataView.append("密钥长度小于12位，重新输入"+ "\r\n");
			} else {
				StringBuffer sb = new StringBuffer(data);
				cardNumber = cardNumber + cardNumber + cardNumber + cardNumber;
				String key = keyInput.getText().toString();
				String express = "31";
				String type = "01";
				sb.replace(28, 39, cardNumber);// 从后向前替换
				sb.replace(25, 28, key);
				sb.replace(18, 25, express);
				sb.replace(14, 18, type);
				data = sb.toString();
				try {
					SerialPortManager.sendToPort(serialport, data);
				} catch (SendDataToSerialPortFailure e) {
					e.printStackTrace();
				} catch (SerialPortOutputStreamCloseFailure e) {
					e.printStackTrace();
				}
				readOrWriterContral += 4;
			}
		} else if ("不在选择模式B".equals(name) && temp.equals(name.substring(0, 1))) {
			data = "EECC0402000018typeExpressKey4cardNumber00000D0A";
			if (keyInput.getText().toString().length() > 12) {
				dataView.append("密钥长度大于12位，重新输入"+ "\r\n");
			} else if (keyInput.getText().toString().length() < 12) {
				dataView.append("密钥长度小于12位，重新输入"+ "\r\n");
			} else {
				StringBuffer sb = new StringBuffer(data);
				cardNumber = cardNumber + cardNumber + cardNumber + cardNumber;
				String key = keyInput.getText().toString();
				String express = "31";
				String type = "01";
				sb.replace(28, 39, cardNumber);// 从后向前替换
				sb.replace(25, 28, key);
				sb.replace(18, 25, express);
				sb.replace(14, 18, type);
				data = sb.toString();
				try {
					SerialPortManager.sendToPort(serialport, data);
				} catch (SendDataToSerialPortFailure e) {
					e.printStackTrace();
				} catch (SerialPortOutputStreamCloseFailure e) {
					e.printStackTrace();
				}
				readOrWriterContral += 4;
			}
		} else if ("在选择模式A".equals(name) && temp.equals(name.substring(0, 1))) {
			data = "EECC0402010018typeExpressKey4cardNumber00000D0A";
			if (keyInput.getText().toString().length() > 12) {
				dataView.append("密钥长度大于12位，重新输入"+ "\r\n");
			} else if (keyInput.getText().toString().length() < 12) {
				dataView.append("密钥长度小于12位，重新输入"+ "\r\n");
			} else {
				StringBuffer sb = new StringBuffer(data);
				cardNumber = cardNumber + cardNumber + cardNumber + cardNumber;
				String key = keyInput.getText().toString();
				String express = "31";
				String type = "01";
				sb.replace(28, 39, cardNumber);// 从后向前替换
				sb.replace(25, 28, key);
				sb.replace(18, 25, express);
				sb.replace(14, 18, type);
				data = sb.toString();
				try {
					SerialPortManager.sendToPort(serialport, data);
				} catch (SendDataToSerialPortFailure e) {
					e.printStackTrace();
				} catch (SerialPortOutputStreamCloseFailure e) {
					e.printStackTrace();
				}
				readOrWriterContral += 4;
			}
		} else if ("在选择模式B".equals(name) && temp.equals(name.substring(0, 1))) {
			data = "EECC0402010018typeExpressKey4cardNumber00000D0A";
			if (keyInput.getText().toString().length() > 12) {
				dataView.append("密钥长度大于12位，重新输入"+ "\r\n");
			} else if (keyInput.getText().toString().length() < 12) {
				dataView.append("密钥长度小于12位，重新输入"+ "\r\n");
			} else {
				StringBuffer sb = new StringBuffer(data);
				cardNumber = cardNumber + cardNumber + cardNumber + cardNumber;
				String key = keyInput.getText().toString();
				String express = "31";
				String type = "01";
				sb.replace(28, 39, cardNumber);// 从后向前替换
				sb.replace(25, 28, key);
				sb.replace(18, 25, express);
				sb.replace(14, 18, type);
				data = sb.toString();
				try {
					SerialPortManager.sendToPort(serialport, data);
				} catch (SendDataToSerialPortFailure e) {
					e.printStackTrace();
				} catch (SerialPortOutputStreamCloseFailure e) {
					e.printStackTrace();
				}
				readOrWriterContral += 4;
			}
		} else {
			dataView.append("不在选择模式 与 在选择模式不匹配，请重新选择" + "\r\n");
		}

	}

	private void sendData5(java.awt.event.ActionEvent evt) {// 读取订单号事件

		if (readOrWriterContral == 10) {
			String name = (String) s5.getSelectedItem();
			String data = null;
			if ("不在选择模式".equals(name) && temp.equals(name.substring(0, 1))) {
				data = "EECC05020000010100000D0A";
				try {
					SerialPortManager.sendToPort(serialport, data);
				} catch (SendDataToSerialPortFailure e) {
					e.printStackTrace();
				} catch (SerialPortOutputStreamCloseFailure e) {
					e.printStackTrace();
				}
				contral = 0;// 标记，为了区分读写器响应数据为订单号
			} else if ("在选择模式".equals(name)
					&& temp.equals(name.substring(0, 1))) {
				data = "EECC05020100010100000D0A";
				try {
					SerialPortManager.sendToPort(serialport, data);
				} catch (SendDataToSerialPortFailure e) {
					e.printStackTrace();
				} catch (SerialPortOutputStreamCloseFailure e) {
					e.printStackTrace();
				}
				contral = 0;// 标记，为了区分读写器响应数据为订单号
			} else {
				dataView.append("不在选择模式 与 在选择模式不匹配，请重新选择" + "\r\n");
			}
			readOrWriterContral = 0;
		} else {
			dataView.append("没有执行必要操作，请重新执行“请求类型”，“寻卡”，“选卡”，“认证密钥”" + "\r\n");
			readOrWriterContral = 0;
		}
	}

	private void sendData7(java.awt.event.ActionEvent evt) {// 读取实时信息事件
		if (readOrWriterContral == 10) {
			String name = (String) s5.getSelectedItem();
			String data = null;
			if ("不在选择模式".equals(name) && temp.equals(name.substring(0, 1))) {
				data = "EECC05020000018000000D0A";
				try {
					SerialPortManager.sendToPort(serialport, data);
				} catch (SendDataToSerialPortFailure e) {
					e.printStackTrace();
				} catch (SerialPortOutputStreamCloseFailure e) {
					e.printStackTrace();
				}
				contral = 1;// 标记，为了区分读写器响应数据为实时信息
			} else if ("在选择模式".equals(name)
					&& temp.equals(name.substring(0, 1))) {
				data = "EECC05020100018000000D0A";
				try {
					SerialPortManager.sendToPort(serialport, data);
				} catch (SendDataToSerialPortFailure e) {
					e.printStackTrace();
				} catch (SerialPortOutputStreamCloseFailure e) {
					e.printStackTrace();
				}
				contral = 1;// 标记，为了区分读写器响应数据为实时信息
			} else {
				dataView.append("不在选择模式 与 在选择模式不匹配，请重新选择" + "\r\n");
			}
			readOrWriterContral = 0;
		} else {
			dataView.append("没有执行必要操作，请重新执行“请求类型”，“寻卡”，“选卡”，“认证密钥”" + "\r\n");
			readOrWriterContral = 0;
		}

	}

	private void sendDataToServer(java.awt.event.ActionEvent evt) {//向服务器发送数据
		dataForServer = nameInput.getText().toString() + "," + passwordInput.getText().toString() + "," + dataForServer;
		/*
		 * arr[0]表示扫描员姓名
		 * arr[1]表示扫描员密码
		 * arr[2]表示订单号
		 * arr[3]表示省
		 * arr[4]表示市
		 * arr[5]表示县
		 * arr[6]表示签收状态
		 */
		String [] arr = dataForServer.split(",");
		HashMap<String, String> hm1 = new HashMap<>();//name
		HashMap<String, String> hm2 = new HashMap<>();//password
		HashMap<String, String> hm3 = new HashMap<>();//id
		HashMap<String, String> hm4 = new HashMap<>();//address
		HashMap<String, String> hm5 = new HashMap<>();//status
		hm1.put("username",arr[0]);
		hm2.put("passwd",arr[1]);
		hm3.put("order_id",arr[2]);
		hm4.put("real_time_address",arr[3] + "," + arr[4] + "," + arr[5]);
		hm5.put("signFor",(arr[6] == "签收" ? "true":"false"));
		/*for (String string : arr) {
			System.out.println(string);
		}*/
		if (serverContral == 2) {
			dataView.append("发送失败：原因未读取实时信息" + "\r\n");
		} else if (serverContral == 3) {
			dataView.append("发送失败：原因未读取订单号信息" + "\r\n");
		} else if (serverContral == 5) {
			/*SendToServer.sendDataToServer("http://localhost:8080/Group8_server/ScannerLoginServlet",hm1);//登陆name
			SendToServer.sendDataToServer("http://localhost:8080/Group8_server/ScannerLoginServlet",hm2);//登陆password
			SendToServer.sendDataToServer("http://localhost:8080/Group8_server/ChangeAddressServlet",hm3);//修改id
			SendToServer.sendDataToServer("http://localhost:8080/Group8_server/ChangeAddressServlet",hm4);//修改address
			SendToServer.sendDataToServer("http://localhost:8080/Group8_server/ExpressSignInServlet",hm5);//签收状态
*/			dataView.append("发送成功：发送的数据为" + dataForServer + "\r\n");
			serverContral = 0;
		} else {
			dataView.append("发送失败：需要重新获“取订单号信息”和“实时信息”" + "\r\n");
			serverContral = 0;
		}

	}

	private void sendData6(java.awt.event.ActionEvent evt) throws IOException {
		// String data = dataInput.getText().toString();
		if (readOrWriterContral == 10) {
			if (dataInput2.getText().toString().length() < 12) {
				dataView.append("您输入的订单号少于12位" + "\r\n");
			} else if (dataInput2.getText().toString().length() > 18) {
				dataView.append("您输入的订单号多于18位" + "\r\n");
			} else {
				String finalData = "EECC0602select0011data00000D0A";// 要发送的所有数据
				String data;// finalData中的data
				String select = (String) s6Type.getSelectedItem() == "不在选择模式" ? "00"
						: "01";// 不在选择模式返回“00”，在选择模式返回“01”
				String express0 = (String) s6Express.getSelectedItem();
				String express;// 快号
				String orderNumber;// 订单号（12 ~ 18位数字字符串）
				if ("初始化卡数据".equals(express0)) {
					express0 = "1";// 将初始化信息（订单号，收货地址，签收状态）存储在快号1开始的地方
				} else {
					express0 = "128";// 将跟新（订单号实时地址，签收状态）存储在快号65开始的地方
				}
				express = Ten2TwoStr.decimal2Binary(Integer.parseInt(express0));// 快号转二进制字符串

				if (express.length() == 4) {// 当快号小于15时在前面自动补齐4位二进制“0”
					express = "0000" + express;
				}
				String number = dataInput2.getText().toString();
				String orderLength = "";
				if (number.length() == 12) {
					orderLength = "1001";// 1001为9，不是等于13；为什么这样表示？因为数据位占16个字节，128位，
											// （订单号（108位）+地址信息（15位）+订单签收状态（1位））=
											// 124位，只剩下4位
					number = "123456" + number;
				} else if (number.length() == 13) {
					orderLength = "1010";
					number = "12345" + number;
				} else if (number.length() == 14) {
					orderLength = "1011";
					number = "1234" + number;
				} else if (number.length() == 15) {
					orderLength = "1100";
					number = "123" + number;
				} else if (number.length() == 16) {
					orderLength = "1101";
					number = "12" + number;
				} else if (number.length() == 17) {
					orderLength = "1110";
					number = "1" + number;
				} else if (number.length() == 18) {
					orderLength = "1111";
				}

				orderNumber = Str2Binary.toBinary(number);// 订单号转二进制字符串，初始化数据时才向读卡器写入订单号
				String address = MatchChinese.getBinary(
						(String) s6Province.getSelectedItem(),
						(String) s6City.getSelectedItem(),
						(String) s6County.getSelectedItem());// 省市县转二进制字符串
				String status = (String) s6Status.getSelectedItem() == "未签收" ? "0"
						: "1";// 未签收返回“0”，签收返回“1”
				data = express + orderLength + orderNumber + address + status;// finalData中的data（二进制）
				data = Two2Sixteen.binaryStrToHexStr(data);// 将data（二进制）转成
															// data（十六进制）
				StringBuilder sb = new StringBuilder(finalData);
				sb.replace(18, 22, data);// 先替换后面的，否则前面讲影响从字符串长度
				sb.replace(8, 14, select);
				// sb.replace(16, 20, data);
				finalData = sb.toString();
				try {

					SerialPortManager.sendToPort(serialport, finalData);// 发送请求

				} catch (SendDataToSerialPortFailure e) {
					e.printStackTrace();
				} catch (SerialPortOutputStreamCloseFailure e) {
					e.printStackTrace();
				}
			}
			readOrWriterContral = 0;
		} else {
			dataView.append("没有执行必要操作，请重新执行“请求类型”，“寻卡”，“选卡”，“认证密钥”" + "\r\n");
			readOrWriterContral = 0;
		}

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
				byte[] data = null;
				try {
					if (serialport == null) {
						ShowUtils.errorMessage("串口对象为空！监听失败！");
					} else {
						// 读取串口数据
						data = SerialPortManager.readFromPort(serialport);
						String tempData = new String(data);
						dataView.append("读卡器回应数据：" + tempData + "\r\n");

						if (tempData.substring(4, 6).equals("01")) {
							dataView.append("错误提示：" + "关闭串口失败" + "\r\n");
						} else if (tempData.substring(4, 6).equals("02")) {
							dataView.append("错误提示：" + "静默失败 " + "\r\n");
						} else if (tempData.substring(4, 6).equals("0F")) {
							dataView.append("错误提示：" + "没有读到标签/卡 " + "\r\n");
						} else if (tempData.substring(4, 6).equals("10")) {
							dataView.append("错误提示：" + "命令不支持 " + "\r\n");
						} else if (tempData.substring(4, 6).equals("11")) {
							dataView.append("错误提示：" + "命令不被允许 " + "\r\n");
						} else if (tempData.substring(4, 6).equals("12")) {
							dataView.append("错误提示："
									+ "Hardware Type not compatible" + "\r\n");
						} else if (tempData.substring(4, 6).equals("00")) {

							if (tempData.substring(6, 8).equals("01")) {// 请求卡类型相应数据
								cardType = tempData.substring(14, 18);
								dataView.append("读卡器回应的卡片类型为：" + cardType
										+ "\r\n");
							} else if (tempData.substring(6, 8).equals("02")) {// 寻卡返回卡号数据
								cardNumber = tempData.substring(14, 22);
								dataView.append("读卡器回应的卡号为：" + cardNumber
										+ "\r\n");
							} else if (tempData.substring(6, 8).equals("03")) {// 选卡，无响应
								dataView.append("选卡时，读卡器无数据响应！" + "\r\n");
							} else if (tempData.substring(6, 8).equals("04")) {// 认证密钥，无响应
								dataView.append("认证密钥时，读卡器无数据响应！" + "\r\n");
							} else if (tempData.substring(6, 8).equals("05")) {// 读数据响应
								if (contral == 0) {// 表示读写器响应数据为订单号
									int orderNumberLength = Integer.parseInt(
											tempData.substring(14, 15), 16) + 3;// 为什么加3，因为传过来的值比真实的值小3
									System.out.println(orderNumber);
									String orderNumber = tempData.substring(15,
											42);
									orderNumber = Sixteen2Two
											.spaceAt6(Sixteen2Two
													.hexString2binaryString(
															orderNumber)
													.substring(
															(18 - orderNumberLength) * 6));
									orderNumber = Binary2str
											.toString(orderNumber);
									dataView.append("解析后的订单号为：" + orderNumber
											+ "\r\n");
									finalOrderNumber = orderNumber;
									serverContral += 2;// 为了控制是否可以向server发送数据，serverContral
														// == 5是，表示数据（订单号 +
														// 实时信息）完整
								} else if (contral == 1) {// 表示读写器响应数据为实时信息
									String orderNumber = tempData.substring(42,
											46);// 16进制字符串
									orderNumber = Sixteen2Two
											.hexString2binaryString(orderNumber);// 转成2进制字符串
									String province = orderNumber.substring(0,
											5);// 获取省份2进制编码
									String city = orderNumber.substring(5, 10);// 获取市的2进制编码
									String county = orderNumber.substring(10,
											15);// 获取县的2进制编码
									String type = orderNumber.substring(15)
											.equals("1") ? "已签收" : "未签收";// 获取订单信息的2进制编码
									String address = MatchCode.getAddress(
											province, city, county);
									finalAddress = address;
									finalStatus = type;
									dataView.append("解析后的实时信息：" + address + ","
											+ type + "\r\n");
									serverContral += 3;// 为了控制是否可以向server发送数据，serverContral
														// == 5是，表示数据（订单号 +
														// 实时信息）完整
								}
								if (serverContral == 5) {// 表示：“请求订单号” 和
															// “请求实时信息”
															// 各执行了一次，信息已经获取完整
									dataForServer = finalOrderNumber + ","
											+ finalAddress + "," + finalStatus;
								}
							}
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
					new MainFrame().setVisible(true);
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