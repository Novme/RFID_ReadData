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
 * ������
 * 
 * @author yangle
 */
public class MainFrame extends JFrame implements ItemListener {

	/**
	 * ���������
	 */
	public static final int WIDTH = 500;

	/**
	 * �������߶�
	 */
	public static final int HEIGHT = 700;
	public static int contral;// ������¼��������ʱ����0�����������Ƕ����ţ���1�����ص���ʵʱ����
	public static int serverContral = 0;// �����ж�����������͵������Ƿ�������������ݲ������㲻����
	public static int readOrWriterContral = 0;// ����Ϊ10ʱ����ִ�ж����ݻ�д���ݲ���
	public static String temp;// ����ȫ�ֱ���temp��
	public static String cardNumber = null;
	public static String cardType = null;
	public static String finalOrderNumber = null;// ����Ҫ���͸�server�Ķ�����
	public static String finalAddress = null;// ����Ҫ���͸�server�ĵ�ַ
	public static String finalStatus = null;// ����Ҫ����server��ǩ��״̬
	public static String finalName = null;// ����Ҫ����server��ɨ��Ա����
	public static String finalPassword = null;// ����Ҫ����server��ɨ��Ա����
	public static String dataForServer = null;// finalOrdeNumber + finalAddress
												// + finalStatus
	private JTextArea dataView = new JTextArea();
	private JScrollPane scrollDataView = new JScrollPane(dataView);

	// �����������
	private JPanel serialPortPanel = new JPanel();// ����һ������
	private JLabel serialPortLabel = new JLabel("����");// �������Label�������ͨ��Container���е�add()����
	private JLabel baudrateLabel = new JLabel("������");
	private JComboBox commChoice = new JComboBox();// �����б��齨
	private JComboBox baudrateChoice = new JComboBox();
	private JButton serialPortOperate = new JButton("�򿪴���");

	// �������������
	private JPanel operatePanel = new JPanel();//������д���ݲ���֮ǰ����
	private JPanel operatePanel1 = new JPanel();//������д��������
	private JPanel operatePanel11 = new JPanel();//��������������
	private JPanel operatePanel12 = new JPanel();//����д��������

	private JLabel orderNumber = new JLabel("������");
	private JTextField dataInput2 = new JTextField();// �����ı����齨 ������
	private JTextField keyInput = new JTextField(); // �����ı��� ��Կ �޶�12λʮ������
	private JTextField dataInput3 = new JTextField();// �����ı����齨Ϊ��ʹdataInput1,dataInput2,ͬʱ��ʾĬ����ʾ��Ϣ������Ҫ̫����

	private JButton sendData1 = new JButton("��������");
	private JButton sendData2 = new JButton("Ѱ��");
	private JButton sendData3 = new JButton("ѡ��");
	private JButton sendData4 = new JButton("��֤��Կ");
	private JButton sendData5 = new JButton("����������Ϣ");
	private JButton sendData6 = new JButton("��ʼд����");
	private JButton sendData7 = new JButton("��ʵʱ��Ϣ");
	private JButton sendDataToServer = new JButton("���������������");
	private JLabel scanName = new JLabel("����");
	private JLabel scanPassword = new JLabel("����");
	private JTextField nameInput = new JTextField();
	private JTextField passwordInput = new JTextField();
	private JComboBox s1 = new JComboBox();// �����б��齨
	private JComboBox s2 = new JComboBox();// �����б��齨
	private JComboBox s3 = new JComboBox();// �����б��齨
	private JComboBox s4 = new JComboBox();// �����б��齨
	private JComboBox s5 = new JComboBox();// �����б��齨
	private JComboBox s6Type = new JComboBox();// ģʽ�����б��齨
	private JComboBox s6Province = new JComboBox();// ʡ�����б���
	private JComboBox s6City = new JComboBox();// �������б��齨
	private JComboBox s6County = new JComboBox();// �������б��齨
	private JLabel status = new JLabel("����ǩ��״̬");
	private JComboBox s6Status = new JComboBox();// ����ǩ��״̬������
	private JLabel express = new JLabel("���");
	private JComboBox s6Express = new JComboBox();// ���ݳ�ʼ��/���ݸ���������
	

	private List<String> commList = null;
	private SerialPort serialport;

	public MainFrame() throws IOException {
		initView();
		initComponents();
		actionListener();
		initData();
		s6Province.addItemListener(this);// Ϊʵ��ʡ��������������Ϊ��������Ӽ����¼�
		s6City.addItemListener(this);// Ϊʵ��ʡ��������������Ϊ��������Ӽ����¼�
		s6Express.addItemListener(this);// Ϊʵ�ֿ�źͶ���ǩ��״̬����
		// s1.addItemListener(this);//Ϊʵ��s1��s2,s3,s4,s5,s6������
	}

	private void initBox() throws IOException, IOException {
		s1.addItem("����ѡ��ģʽδ���ߵĿ�");
		s1.addItem("��ѡ��ģʽδ���ߵĿ�");
		s1.addItem("����ѡ��ģʽ���еĿ�");
		s1.addItem("��ѡ��ģʽ�������еĿ�");

		s2.addItem("����ѡ��ģʽ");
		s2.addItem("��ѡ��ģʽ");

		s3.addItem("����ѡ��ģʽ");
		s3.addItem("��ѡ��ģʽ");

		s4.addItem("����ѡ��ģʽA");
		s4.addItem("����ѡ��ģʽB");
		s4.addItem("��ѡ��ģʽA");
		s4.addItem("��ѡ��ģʽB");

		s5.addItem("����ѡ��ģʽ");
		s5.addItem("��ѡ��ģʽ");

		s6Type.addItem("����ѡ��ģʽ");
		s6Type.addItem("��ѡ��ģʽ");

		s6Express.addItem("��ʼ��������");
		s6Express.addItem("���¿�����");

		s6Province.addItem("��ѡ��ʡ��");
		s6City.addItem("��ѡ����");
		s6County.addItem("��ѡ����");

		s6Status.addItem("δǩ��");

		HashMap<String, String> hm = GetData
				.getSingleData("address\\province.txt");// ��ȡʡ����Ϣ
		for (String key : hm.keySet()) {// ��ʡ����Ϣ��ӵ�������
			s6Province.addItem(hm.get(key));
		}
	}

	@Override
	public void itemStateChanged(java.awt.event.ItemEvent e) {// Ϊ��ʵ��ʡ�������������Ϳ�źͶ���ǩ��״̬����
		// TODO Auto-generated method stub
		String temp = ((JComboBox) e.getSource()).getSelectedItem().toString();// ��ȡ��������Ϣ

		if (temp.equals("���¿�����")) {
			s6Status.removeItemListener(this);
			s6Status.removeAllItems();
			s6Status.addItemListener(this);
			s6Status.addItem("δǩ��");
			s6Status.addItem("ǩ��");
		}
		if (temp.equals("��ʼ��������")) {
			s6Status.removeItemListener(this);
			s6Status.removeAllItems();
			s6Status.addItemListener(this);
			s6Status.addItem("δǩ��");
		}

		String tempProvince = null;
		String tempCity = null;
		HashMap<String, String> province = null;
		try {
			province = GetData.getSingleData("address\\province.txt");// ��ȡʡ����Ϣ,����HashMap���͵�����
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (String key : province.keySet()) {// ����ʡ����Ϣ
			if (temp.equals(province.get(key))) {// ƥ��ʡ����Ϣ��ʵ�֣�ʡ_�е�����
				s6City.removeItemListener(this);
				s6City.removeAllItems();
				s6City.addItemListener(this);
				try {
					HashMap<String, String> city = GetData
							.getSingleData("address\\" + province.get(key)
									+ "_��.txt");// ��ȡ��Ӧʡ�������е���Ϣ
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
			if ("��ѡ��ʡ��".equals(temp)) {
				s6City.removeItemListener(this);
				s6City.removeAllItems();
				s6City.addItemListener(this);
				s6City.addItem("��ѡ����");
				s6County.addItem("��ѡ����");
			}
		}
		tempProvince = (String) s6Province.getSelectedItem();
		tempCity = (String) s6City.getSelectedItem();

		HashMap<String, String> city = null;
		if ("��ѡ��ʡ��".equals(tempProvince)) {

		} else {
			try {
				city = GetData.getSingleData("address\\" + tempProvince
						+ "_��.txt");// ��ȡ��Ӧʡ�������е���Ϣ
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			for (String key : city.keySet()) {// ������Ӧ��ʡ�����е���
				// System.out.println(city.get(key));
				if (temp.equals(city.get(key))) {
					HashMap<String, String> county = null;// ��ȡ��_����Ϣ
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
					for (String key1 : county.keySet()) {// ������_������

						s6County.addItem(county.get(key1));
						// System.out.println(county.get(key));
					}
				}
			}
		}

	}

	private void initView() throws IOException {
		initBox();// wei
		// �رճ���
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		// ��ֹ�������
		setResizable(false);

		// ���ó��򴰿ھ�����ʾ
		Point p = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getCenterPoint();
		setBounds(p.x - WIDTH / 2, p.y - HEIGHT / 2, WIDTH, HEIGHT);
		this.setLayout(null);

		setTitle("��λ��");
	}

	private void initComponents() {
		// ������ʾ
		dataView.setFocusable(false);
		scrollDataView.setBounds(10, 10, 475, 200);
		add(scrollDataView);

		// ��������
		serialPortPanel.setBorder(BorderFactory.createTitledBorder("��������"));
		serialPortPanel.setBounds(10, 220, 475, 60);
		serialPortPanel.setLayout(null);
		add(serialPortPanel);

		serialPortLabel.setForeground(Color.gray);// ���ڱ�ǩ����
		serialPortLabel.setBounds(10, 25, 40, 20);
		serialPortPanel.add(serialPortLabel);

		commChoice.setFocusable(false);// ���������б�����
		commChoice.setBounds(35, 25, 100, 20);
		serialPortPanel.add(commChoice);

		baudrateLabel.setForeground(Color.gray);// �����ʱ�ǩ����
		baudrateLabel.setBounds(180, 25, 40, 20);
		serialPortPanel.add(baudrateLabel);

		baudrateChoice.setFocusable(false);// �����������б�����
		baudrateChoice.setBounds(220, 25, 100, 20);
		serialPortPanel.add(baudrateChoice);

		serialPortOperate.setFocusable(false);// �򿪴���
		serialPortOperate.setBounds(375, 25, 90, 20);// x y �� ��
		serialPortPanel.add(serialPortOperate);

		// ������/д����֮ǰҪ�����Ľ���
		operatePanel
				.setBorder(BorderFactory.createTitledBorder("������/д����֮ǰҪ����"));
		operatePanel.setBounds(10, 305, 475, 110);
		operatePanel.setLayout(null);
		add(operatePanel);

		sendData1.setFocusable(false);// �������Ͱ�ť����
		sendData1.setBounds(115, 25, 90, 20);
		operatePanel.add(sendData1);
		s1.setFocusable(false);// s1�����б�����
		s1.setBounds(10, 25, 100, 20);
		operatePanel.add(s1);

		sendData2.setFocusable(false);// Ѱ����ť����
		sendData2.setBounds(375, 25, 90, 20);
		operatePanel.add(sendData2);
		s2.setFocusable(false);// s2�����б�����
		s2.setBounds(270, 25, 100, 20);
		operatePanel.add(s2);

		sendData3.setFocusable(false);// ѡ����ť����
		sendData3.setBounds(115, 70, 90, 20);
		operatePanel.add(sendData3);
		s3.setFocusable(false);// s3�����б�����
		s3.setBounds(10, 70, 100, 20);
		operatePanel.add(s3);

		String info1 = "����12λ��֤��Կ";
		keyInput.setText(info1);
		keyInput.addFocusListener(new MyFocusListener(info1, keyInput));
		sendData4.setFocusable(false);// ��֤��Կ��ť����
		sendData4.setBounds(375, 70, 90, 20);
		operatePanel.add(sendData4);
		keyInput.setBounds(270, 55, 100, 20);
		operatePanel.add(keyInput);
		s4.setFocusable(false);// s4�����б�����
		s4.setBounds(270, 80, 100, 20);
		operatePanel.add(s4);

		// ������/д���ݽ���
		operatePanel1.setBorder(BorderFactory.createTitledBorder("��/д"));
		operatePanel1.setBounds(10, 440, 475, 210);
		operatePanel1.setLayout(null);
		add(operatePanel1);
		
		operatePanel11.setBorder(BorderFactory.createTitledBorder("������"));//�����ݽ���
		operatePanel11.setBounds(5,20,465,80);
		operatePanel11.setLayout(null);
		operatePanel1.add(operatePanel11);
		
		

		s5.setFocusable(false);// s5�����б�����
		s5.setBounds(5, 20, 90, 20);
		operatePanel11.add(s5);
		sendData5.setFocusable(false);// �����������ݰ�ť����
		sendData5.setBounds(100, 20, 115, 20);
		operatePanel11.add(sendData5);
		sendData7.setFocusable(false);// ��ʵʱ��Ϣ��ť
		sendData7.setBounds(215, 20, 105, 20);
		operatePanel11.add(sendData7);
		
		String name = "����������";
		nameInput.setText(name);
		nameInput.addFocusListener(new MyFocusListener("", nameInput));// ��ӽ����¼���ӳ
		nameInput.addFocusListener(new MyFocusListener(name, nameInput));
		
		String password = "����������";
		passwordInput.setText(password);
		passwordInput.addFocusListener(new MyFocusListener("", passwordInput));// ��ӽ����¼���ӳ
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
		
		sendDataToServer.setFocusable(false);// �������������������ť
		sendDataToServer.setBounds(320, 35, 140, 20);
		operatePanel11.add(sendDataToServer);
		
		
		operatePanel12.setBorder(BorderFactory.createTitledBorder("д����"));//д���ݽ���
		operatePanel12.setBounds(5,105,465,95);
		operatePanel12.setLayout(null);
		operatePanel1.add(operatePanel12);
		
		String info2 = "��ʼ��ʱ�������ʱ������";
		dataInput2.setText(info2);
		dataInput3.addFocusListener(new MyFocusListener("", dataInput3));// ��ӽ����¼���ӳ
		dataInput2.addFocusListener(new MyFocusListener(info2, dataInput2));

		express.setForeground(Color.gray);
		express.setBounds(115, 20, 40, 20);
		operatePanel12.add(express);

		s6Express.setFocusable(false);
		s6Express.setBounds(140, 20, 120, 20);
		operatePanel12.add(s6Express);

		orderNumber.setForeground(Color.gray);// �����ű�ǩ����
		orderNumber.setBounds(270, 20, 40, 20);
		operatePanel12.add(orderNumber);
		dataInput2.setBounds(310, 20, 150, 20);// ������ �ı����������
		operatePanel12.add(dataInput2);
		dataInput3.setBounds(0, 0, 0, 0);// Ϊ��ʹdataInput1,dataInput2ͬʱ��ʾĬ����ʾ
		operatePanel.add(dataInput3);

		s6Type.setFocusable(false);// s5Type����������
		s6Type.setBounds(5, 20, 100, 20);
		operatePanel12.add(s6Type);
		s6Province.setFocusable(false);// s6Provice����������
		s6Province.setBounds(5, 45, 147, 20);
		operatePanel12.add(s6Province);
		s6City.setFocusable(false);// s6City����������
		s6City.setBounds(160, 45, 147, 20);
		operatePanel12.add(s6City);
		s6County.setFocusable(false);// s6County����������
		s6County.setBounds(315, 45, 145, 20);
		operatePanel12.add(s6County);
		status.setForeground(Color.gray);// ����ǩ��״̬��ǩ����
		status.setBounds(5, 70, 80, 20);
		operatePanel12.add(status);
		s6Status.setFocusable(false);// ����ǩ��״̬����������
		s6Status.setBounds(85, 70, 80, 20);
		operatePanel12.add(s6Status);
		sendData6.setFocusable(false); // д���ݰ�ť����
		sendData6.setBounds(185, 70, 100, 20);
		operatePanel12.add(sendData6);
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		commList = SerialPortManager.findPort();
		// ����Ƿ��п��ô��ڣ��������ѡ����
		if (commList == null || commList.size() < 1) {
			ShowUtils.warningMessage("û����������Ч���ڣ�");
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
				if ("�򿪴���".equals(serialPortOperate.getText())
						&& serialport == null) {
					openSerialPort(e);
				} else {
					closeSerialPort(e);
				}
			}
		});

		sendData1.addActionListener(new ActionListener() {// ����������Ͱ�ť����¼�

					@Override
					public void actionPerformed(ActionEvent e) {
						sendData1(e);
					}
				});

		sendData2.addActionListener(new ActionListener() {// ���Ѱ����ť����¼�

					@Override
					public void actionPerformed(ActionEvent e) {
						sendData2(e);
					}
				});

		sendData3.addActionListener(new ActionListener() {// ���ѡ����ť����¼�

					@Override
					public void actionPerformed(ActionEvent e) {
						sendData3(e);
					}
				});

		sendData4.addActionListener(new ActionListener() {// �����֤��Կ��ť����¼�

					@Override
					public void actionPerformed(ActionEvent e) {
						sendData4(e);
					}
				});

		sendData5.addActionListener(new ActionListener() {// ��Ӷ����������ݰ�ť����¼�

					@Override
					public void actionPerformed(ActionEvent e) {
						sendData5(e);
					}
				});

		sendData6.addActionListener(new ActionListener() {// ���д���ݰ�ť����¼�

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
		sendData7.addActionListener(new ActionListener() {// ��Ӷ�ʵʱ���ݰ�ť����¼�

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
	 * �򿪴���
	 * 
	 * @param evt
	 *            ����¼�
	 */
	private void openSerialPort(java.awt.event.ActionEvent evt) {
		// ��ȡ��������
		String commName = (String) commChoice.getSelectedItem();
		// System.out.println(commName);
		// ��ȡ������
		int baudrate = 9600;
		String bps = (String) baudrateChoice.getSelectedItem();
		baudrate = Integer.parseInt(bps);

		// ��鴮�������Ƿ��ȡ��ȷ
		if (commName == null || commName.equals("")) {
			ShowUtils.warningMessage("û����������Ч���ڣ�");
		} else {
			try {
				serialport = SerialPortManager.openPort(commName, baudrate);
				if (serialport != null) {
					dataView.setText("�����Ѵ�" + "\r\n");
					serialPortOperate.setText("�رմ���");
				}
			} catch (SerialPortParameterFailure e) {
				e.printStackTrace();
			} catch (NotASerialPort e) {
				e.printStackTrace();
			} catch (NoSuchPort e) {
				e.printStackTrace();
			} catch (PortInUse e) {
				e.printStackTrace();
				ShowUtils.warningMessage("�����ѱ�ռ�ã�");
			}
		}

		try {
			SerialPortManager.addListener(serialport, new SerialListener());
		} catch (TooManyListeners e) {
			e.printStackTrace();
		}
	}

	/**
	 * �رմ���
	 * 
	 * @param evt
	 *            ����¼�
	 */
	private void closeSerialPort(java.awt.event.ActionEvent evt) {
		SerialPortManager.closePort(serialport);
		dataView.setText("�����ѹر�" + "\r\n");
		serialPortOperate.setText("�򿪴���");
	}

	/**
	 * ��������
	 * 
	 * @param evt
	 *            ����¼�
	 */
	private void sendData1(java.awt.event.ActionEvent evt) {//��������
		String name = (String) s1.getSelectedItem();
		String data = null;
		if ("��ѡ��ģʽδ���ߵĿ�".equals(name)) {
			temp = name.substring(0, 1);
			data = "EECC01020100010000000D0A";
		} else if ("����ѡ��ģʽ���еĿ�".equals(name)) {
			temp = name.substring(0, 1);
			data = "EECC01020000010100000D0A";
		} else if ("����ѡ��ģʽδ���ߵĿ�".equals(name)) {
			temp = name.substring(0, 1);
			data = "EECC01020000010000000D0A";
		} else {
			temp = name.substring(0, 1);// ��ѡ��ģʽ�������еĿ�
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

	private void sendData2(java.awt.event.ActionEvent evt) {//Ѱ��
		String name = (String) s2.getSelectedItem();
		String data = null;
		if ("����ѡ��ģʽ".equals(name) && temp.equals(name.substring(0, 1))) {
			data = "EECC020200000000000D0A";
			try {
				SerialPortManager.sendToPort(serialport, data);
			} catch (SendDataToSerialPortFailure e) {
				e.printStackTrace();
			} catch (SerialPortOutputStreamCloseFailure e) {
				e.printStackTrace();
			}
			readOrWriterContral += 2;
		} else if ("��ѡ��ģʽ".equals(name) && temp.equals(name.substring(0, 1))) {
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
			dataView.append("����ѡ��ģʽ �� ��ѡ��ģʽ��ƥ�䣬������ѡ��" + "\r\n");
		}

	}

	private void sendData3(java.awt.event.ActionEvent evt) {//ѡ��
		String name = (String) s3.getSelectedItem();
		String data = null;
		if ("����ѡ��ģʽ".equals(name) && temp.equals(name.substring(0, 1))) {
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
		} else if ("��ѡ��ģʽ".equals(name) && temp.equals(name.substring(0, 1))) {
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
			dataView.append("����ѡ��ģʽ �� ��ѡ��ģʽ��ƥ�䣬������ѡ��" + "\r\n");
		}

	}

	private void sendData4(java.awt.event.ActionEvent evt) {//��֤��Կ
		String name = (String) s4.getSelectedItem();
		String data = null;
		if ("����ѡ��ģʽA".equals(name) && temp.equals(name.substring(0, 1))) {
			data = "EECC0402000018typeExpressKey4cardNumber00000D0A";
			if (keyInput.getText().toString().length() > 12) {
				dataView.append("��Կ���ȴ���12λ����������"+ "\r\n");
			} else if (keyInput.getText().toString().length() < 12) {
				dataView.append("��Կ����С��12λ����������"+ "\r\n");
			} else {
				StringBuffer sb = new StringBuffer(data);
				cardNumber = cardNumber + cardNumber + cardNumber + cardNumber;
				String key = keyInput.getText().toString();
				String express = "31";
				String type = "01";
				sb.replace(28, 39, cardNumber);// �Ӻ���ǰ�滻
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
		} else if ("����ѡ��ģʽB".equals(name) && temp.equals(name.substring(0, 1))) {
			data = "EECC0402000018typeExpressKey4cardNumber00000D0A";
			if (keyInput.getText().toString().length() > 12) {
				dataView.append("��Կ���ȴ���12λ����������"+ "\r\n");
			} else if (keyInput.getText().toString().length() < 12) {
				dataView.append("��Կ����С��12λ����������"+ "\r\n");
			} else {
				StringBuffer sb = new StringBuffer(data);
				cardNumber = cardNumber + cardNumber + cardNumber + cardNumber;
				String key = keyInput.getText().toString();
				String express = "31";
				String type = "01";
				sb.replace(28, 39, cardNumber);// �Ӻ���ǰ�滻
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
		} else if ("��ѡ��ģʽA".equals(name) && temp.equals(name.substring(0, 1))) {
			data = "EECC0402010018typeExpressKey4cardNumber00000D0A";
			if (keyInput.getText().toString().length() > 12) {
				dataView.append("��Կ���ȴ���12λ����������"+ "\r\n");
			} else if (keyInput.getText().toString().length() < 12) {
				dataView.append("��Կ����С��12λ����������"+ "\r\n");
			} else {
				StringBuffer sb = new StringBuffer(data);
				cardNumber = cardNumber + cardNumber + cardNumber + cardNumber;
				String key = keyInput.getText().toString();
				String express = "31";
				String type = "01";
				sb.replace(28, 39, cardNumber);// �Ӻ���ǰ�滻
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
		} else if ("��ѡ��ģʽB".equals(name) && temp.equals(name.substring(0, 1))) {
			data = "EECC0402010018typeExpressKey4cardNumber00000D0A";
			if (keyInput.getText().toString().length() > 12) {
				dataView.append("��Կ���ȴ���12λ����������"+ "\r\n");
			} else if (keyInput.getText().toString().length() < 12) {
				dataView.append("��Կ����С��12λ����������"+ "\r\n");
			} else {
				StringBuffer sb = new StringBuffer(data);
				cardNumber = cardNumber + cardNumber + cardNumber + cardNumber;
				String key = keyInput.getText().toString();
				String express = "31";
				String type = "01";
				sb.replace(28, 39, cardNumber);// �Ӻ���ǰ�滻
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
			dataView.append("����ѡ��ģʽ �� ��ѡ��ģʽ��ƥ�䣬������ѡ��" + "\r\n");
		}

	}

	private void sendData5(java.awt.event.ActionEvent evt) {// ��ȡ�������¼�

		if (readOrWriterContral == 10) {
			String name = (String) s5.getSelectedItem();
			String data = null;
			if ("����ѡ��ģʽ".equals(name) && temp.equals(name.substring(0, 1))) {
				data = "EECC05020000010100000D0A";
				try {
					SerialPortManager.sendToPort(serialport, data);
				} catch (SendDataToSerialPortFailure e) {
					e.printStackTrace();
				} catch (SerialPortOutputStreamCloseFailure e) {
					e.printStackTrace();
				}
				contral = 0;// ��ǣ�Ϊ�����ֶ�д����Ӧ����Ϊ������
			} else if ("��ѡ��ģʽ".equals(name)
					&& temp.equals(name.substring(0, 1))) {
				data = "EECC05020100010100000D0A";
				try {
					SerialPortManager.sendToPort(serialport, data);
				} catch (SendDataToSerialPortFailure e) {
					e.printStackTrace();
				} catch (SerialPortOutputStreamCloseFailure e) {
					e.printStackTrace();
				}
				contral = 0;// ��ǣ�Ϊ�����ֶ�д����Ӧ����Ϊ������
			} else {
				dataView.append("����ѡ��ģʽ �� ��ѡ��ģʽ��ƥ�䣬������ѡ��" + "\r\n");
			}
			readOrWriterContral = 0;
		} else {
			dataView.append("û��ִ�б�Ҫ������������ִ�С��������͡�����Ѱ��������ѡ����������֤��Կ��" + "\r\n");
			readOrWriterContral = 0;
		}
	}

	private void sendData7(java.awt.event.ActionEvent evt) {// ��ȡʵʱ��Ϣ�¼�
		if (readOrWriterContral == 10) {
			String name = (String) s5.getSelectedItem();
			String data = null;
			if ("����ѡ��ģʽ".equals(name) && temp.equals(name.substring(0, 1))) {
				data = "EECC05020000018000000D0A";
				try {
					SerialPortManager.sendToPort(serialport, data);
				} catch (SendDataToSerialPortFailure e) {
					e.printStackTrace();
				} catch (SerialPortOutputStreamCloseFailure e) {
					e.printStackTrace();
				}
				contral = 1;// ��ǣ�Ϊ�����ֶ�д����Ӧ����Ϊʵʱ��Ϣ
			} else if ("��ѡ��ģʽ".equals(name)
					&& temp.equals(name.substring(0, 1))) {
				data = "EECC05020100018000000D0A";
				try {
					SerialPortManager.sendToPort(serialport, data);
				} catch (SendDataToSerialPortFailure e) {
					e.printStackTrace();
				} catch (SerialPortOutputStreamCloseFailure e) {
					e.printStackTrace();
				}
				contral = 1;// ��ǣ�Ϊ�����ֶ�д����Ӧ����Ϊʵʱ��Ϣ
			} else {
				dataView.append("����ѡ��ģʽ �� ��ѡ��ģʽ��ƥ�䣬������ѡ��" + "\r\n");
			}
			readOrWriterContral = 0;
		} else {
			dataView.append("û��ִ�б�Ҫ������������ִ�С��������͡�����Ѱ��������ѡ����������֤��Կ��" + "\r\n");
			readOrWriterContral = 0;
		}

	}

	private void sendDataToServer(java.awt.event.ActionEvent evt) {//���������������
		dataForServer = nameInput.getText().toString() + "," + passwordInput.getText().toString() + "," + dataForServer;
		/*
		 * arr[0]��ʾɨ��Ա����
		 * arr[1]��ʾɨ��Ա����
		 * arr[2]��ʾ������
		 * arr[3]��ʾʡ
		 * arr[4]��ʾ��
		 * arr[5]��ʾ��
		 * arr[6]��ʾǩ��״̬
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
		hm5.put("signFor",(arr[6] == "ǩ��" ? "true":"false"));
		/*for (String string : arr) {
			System.out.println(string);
		}*/
		if (serverContral == 2) {
			dataView.append("����ʧ�ܣ�ԭ��δ��ȡʵʱ��Ϣ" + "\r\n");
		} else if (serverContral == 3) {
			dataView.append("����ʧ�ܣ�ԭ��δ��ȡ��������Ϣ" + "\r\n");
		} else if (serverContral == 5) {
			/*SendToServer.sendDataToServer("http://localhost:8080/Group8_server/ScannerLoginServlet",hm1);//��½name
			SendToServer.sendDataToServer("http://localhost:8080/Group8_server/ScannerLoginServlet",hm2);//��½password
			SendToServer.sendDataToServer("http://localhost:8080/Group8_server/ChangeAddressServlet",hm3);//�޸�id
			SendToServer.sendDataToServer("http://localhost:8080/Group8_server/ChangeAddressServlet",hm4);//�޸�address
			SendToServer.sendDataToServer("http://localhost:8080/Group8_server/ExpressSignInServlet",hm5);//ǩ��״̬
*/			dataView.append("���ͳɹ������͵�����Ϊ" + dataForServer + "\r\n");
			serverContral = 0;
		} else {
			dataView.append("����ʧ�ܣ���Ҫ���»�ȡ��������Ϣ���͡�ʵʱ��Ϣ��" + "\r\n");
			serverContral = 0;
		}

	}

	private void sendData6(java.awt.event.ActionEvent evt) throws IOException {
		// String data = dataInput.getText().toString();
		if (readOrWriterContral == 10) {
			if (dataInput2.getText().toString().length() < 12) {
				dataView.append("������Ķ���������12λ" + "\r\n");
			} else if (dataInput2.getText().toString().length() > 18) {
				dataView.append("������Ķ����Ŷ���18λ" + "\r\n");
			} else {
				String finalData = "EECC0602select0011data00000D0A";// Ҫ���͵���������
				String data;// finalData�е�data
				String select = (String) s6Type.getSelectedItem() == "����ѡ��ģʽ" ? "00"
						: "01";// ����ѡ��ģʽ���ء�00������ѡ��ģʽ���ء�01��
				String express0 = (String) s6Express.getSelectedItem();
				String express;// ���
				String orderNumber;// �����ţ�12 ~ 18λ�����ַ�����
				if ("��ʼ��������".equals(express0)) {
					express0 = "1";// ����ʼ����Ϣ�������ţ��ջ���ַ��ǩ��״̬���洢�ڿ��1��ʼ�ĵط�
				} else {
					express0 = "128";// �����£�������ʵʱ��ַ��ǩ��״̬���洢�ڿ��65��ʼ�ĵط�
				}
				express = Ten2TwoStr.decimal2Binary(Integer.parseInt(express0));// ���ת�������ַ���

				if (express.length() == 4) {// �����С��15ʱ��ǰ���Զ�����4λ�����ơ�0��
					express = "0000" + express;
				}
				String number = dataInput2.getText().toString();
				String orderLength = "";
				if (number.length() == 12) {
					orderLength = "1001";// 1001Ϊ9�����ǵ���13��Ϊʲô������ʾ����Ϊ����λռ16���ֽڣ�128λ��
											// �������ţ�108λ��+��ַ��Ϣ��15λ��+����ǩ��״̬��1λ����=
											// 124λ��ֻʣ��4λ
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

				orderNumber = Str2Binary.toBinary(number);// ������ת�������ַ�������ʼ������ʱ���������д�붩����
				String address = MatchChinese.getBinary(
						(String) s6Province.getSelectedItem(),
						(String) s6City.getSelectedItem(),
						(String) s6County.getSelectedItem());// ʡ����ת�������ַ���
				String status = (String) s6Status.getSelectedItem() == "δǩ��" ? "0"
						: "1";// δǩ�շ��ء�0����ǩ�շ��ء�1��
				data = express + orderLength + orderNumber + address + status;// finalData�е�data�������ƣ�
				data = Two2Sixteen.binaryStrToHexStr(data);// ��data�������ƣ�ת��
															// data��ʮ�����ƣ�
				StringBuilder sb = new StringBuilder(finalData);
				sb.replace(18, 22, data);// ���滻����ģ�����ǰ�潲Ӱ����ַ�������
				sb.replace(8, 14, select);
				// sb.replace(16, 20, data);
				finalData = sb.toString();
				try {

					SerialPortManager.sendToPort(serialport, finalData);// ��������

				} catch (SendDataToSerialPortFailure e) {
					e.printStackTrace();
				} catch (SerialPortOutputStreamCloseFailure e) {
					e.printStackTrace();
				}
			}
			readOrWriterContral = 0;
		} else {
			dataView.append("û��ִ�б�Ҫ������������ִ�С��������͡�����Ѱ��������ѡ����������֤��Կ��" + "\r\n");
			readOrWriterContral = 0;
		}

	}

	private class SerialListener implements SerialPortEventListener {
		/**
		 * �����ص��Ĵ����¼�
		 */
		public void serialEvent(SerialPortEvent serialPortEvent) {

			switch (serialPortEvent.getEventType()) {

			case SerialPortEvent.BI: // 10 ͨѶ�ж�
				ShowUtils.errorMessage("�봮���豸ͨѶ�ж�");
				break;

			case SerialPortEvent.OE: // 7 ��λ�����������

			case SerialPortEvent.FE: // 9 ֡����

			case SerialPortEvent.PE: // 8 ��żУ�����

			case SerialPortEvent.CD: // 6 �ز����

			case SerialPortEvent.CTS: // 3 �������������

			case SerialPortEvent.DSR: // 4 ����������׼������

			case SerialPortEvent.RI: // 5 ����ָʾ

			case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 ��������������
				break;

			case SerialPortEvent.DATA_AVAILABLE: // 1 ���ڴ��ڿ�������
				byte[] data = null;
				try {
					if (serialport == null) {
						ShowUtils.errorMessage("���ڶ���Ϊ�գ�����ʧ�ܣ�");
					} else {
						// ��ȡ��������
						data = SerialPortManager.readFromPort(serialport);
						String tempData = new String(data);
						dataView.append("��������Ӧ���ݣ�" + tempData + "\r\n");

						if (tempData.substring(4, 6).equals("01")) {
							dataView.append("������ʾ��" + "�رմ���ʧ��" + "\r\n");
						} else if (tempData.substring(4, 6).equals("02")) {
							dataView.append("������ʾ��" + "��Ĭʧ�� " + "\r\n");
						} else if (tempData.substring(4, 6).equals("0F")) {
							dataView.append("������ʾ��" + "û�ж�����ǩ/�� " + "\r\n");
						} else if (tempData.substring(4, 6).equals("10")) {
							dataView.append("������ʾ��" + "���֧�� " + "\r\n");
						} else if (tempData.substring(4, 6).equals("11")) {
							dataView.append("������ʾ��" + "��������� " + "\r\n");
						} else if (tempData.substring(4, 6).equals("12")) {
							dataView.append("������ʾ��"
									+ "Hardware Type not compatible" + "\r\n");
						} else if (tempData.substring(4, 6).equals("00")) {

							if (tempData.substring(6, 8).equals("01")) {// ����������Ӧ����
								cardType = tempData.substring(14, 18);
								dataView.append("��������Ӧ�Ŀ�Ƭ����Ϊ��" + cardType
										+ "\r\n");
							} else if (tempData.substring(6, 8).equals("02")) {// Ѱ�����ؿ�������
								cardNumber = tempData.substring(14, 22);
								dataView.append("��������Ӧ�Ŀ���Ϊ��" + cardNumber
										+ "\r\n");
							} else if (tempData.substring(6, 8).equals("03")) {// ѡ��������Ӧ
								dataView.append("ѡ��ʱ����������������Ӧ��" + "\r\n");
							} else if (tempData.substring(6, 8).equals("04")) {// ��֤��Կ������Ӧ
								dataView.append("��֤��Կʱ����������������Ӧ��" + "\r\n");
							} else if (tempData.substring(6, 8).equals("05")) {// ��������Ӧ
								if (contral == 0) {// ��ʾ��д����Ӧ����Ϊ������
									int orderNumberLength = Integer.parseInt(
											tempData.substring(14, 15), 16) + 3;// Ϊʲô��3����Ϊ��������ֵ����ʵ��ֵС3
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
									dataView.append("������Ķ�����Ϊ��" + orderNumber
											+ "\r\n");
									finalOrderNumber = orderNumber;
									serverContral += 2;// Ϊ�˿����Ƿ������server�������ݣ�serverContral
														// == 5�ǣ���ʾ���ݣ������� +
														// ʵʱ��Ϣ������
								} else if (contral == 1) {// ��ʾ��д����Ӧ����Ϊʵʱ��Ϣ
									String orderNumber = tempData.substring(42,
											46);// 16�����ַ���
									orderNumber = Sixteen2Two
											.hexString2binaryString(orderNumber);// ת��2�����ַ���
									String province = orderNumber.substring(0,
											5);// ��ȡʡ��2���Ʊ���
									String city = orderNumber.substring(5, 10);// ��ȡ�е�2���Ʊ���
									String county = orderNumber.substring(10,
											15);// ��ȡ�ص�2���Ʊ���
									String type = orderNumber.substring(15)
											.equals("1") ? "��ǩ��" : "δǩ��";// ��ȡ������Ϣ��2���Ʊ���
									String address = MatchCode.getAddress(
											province, city, county);
									finalAddress = address;
									finalStatus = type;
									dataView.append("�������ʵʱ��Ϣ��" + address + ","
											+ type + "\r\n");
									serverContral += 3;// Ϊ�˿����Ƿ������server�������ݣ�serverContral
														// == 5�ǣ���ʾ���ݣ������� +
														// ʵʱ��Ϣ������
								}
								if (serverContral == 5) {// ��ʾ�������󶩵��š� ��
															// ������ʵʱ��Ϣ��
															// ��ִ����һ�Σ���Ϣ�Ѿ���ȡ����
									dataForServer = finalOrderNumber + ","
											+ finalAddress + "," + finalStatus;
								}
							}
						}

					}
				} catch (Exception e) {
					ShowUtils.errorMessage(e.toString());
					// ������ȡ����ʱ��ʾ������Ϣ���˳�ϵͳ
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
	public void focusGained(FocusEvent e) {// ��ý����ʱ��,�����ʾ����
		String temp = jtf.getText();
		if (temp.equals(info)) {
			jtf.setText("");
		}
	}

	@Override
	public void focusLost(FocusEvent e) {// ʧȥ�����ʱ��,�ж����Ϊ��,����ʾ��ʾ����
		String temp = jtf.getText();
		if (temp.equals("")) {
			jtf.setText(info);
		}
	}
}