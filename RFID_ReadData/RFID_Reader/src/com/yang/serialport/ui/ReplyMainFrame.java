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
 * ������
 * 
 * @author yangle
 */
public class ReplyMainFrame extends JFrame {

	/**
	 * ���������
	 */
	public static final int WIDTH = 500;

	/**
	 * �������߶�
	 */
	public static final int HEIGHT = 410;

	private JTextArea dataView = new JTextArea();
	private JScrollPane scrollDataView = new JScrollPane(dataView);
	private static String cardNumber;
	// �����������
	private JPanel serialPortPanel = new JPanel();// ����һ���������õ�����
	private JPanel replyPanel = new JPanel();// ����һ����Ӧ������
	private JLabel serialPortLabel = new JLabel("����");// �������Label�������ͨ��Container���е�add()����
	private JLabel baudrateLabel = new JLabel("������");
	private JComboBox baudrateChoice = new JComboBox();// ������������
	private JComboBox replyTypeChoice = new JComboBox();
	private JButton serialPortOperate = new JButton("�򿪴���");
	private JLabel cardNumberLabel = new JLabel("����");// ���ű�ǩ
	private JTextField dataInput1 = new JTextField();// �������dataInput��ʾ��ʾ��Ϣ
	private JTextField dataInput = new JTextField();// �������뿨�ŵĵ����ı���
	private JLabel replyLabel = new JLabel("��Ӧ");
	private JComboBox commChoice = new JComboBox();// COM�����б��齨

	private List<String> commList = null;
	private SerialPort serialport; 
	public ReplyMainFrame() throws IOException {//���췽��
		initView();
		initComponents();
		actionListener();
		initData();
	}

	private void initView() throws IOException {
		// �رճ���
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		// ��ֹ�������
		setResizable(false);

		// ���ó��򴰿ھ�����ʾ
		Point p = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getCenterPoint();
		setBounds(p.x - WIDTH / 2, p.y - HEIGHT / 2, WIDTH, HEIGHT);
		this.setLayout(null);

		setTitle("��д��");
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
		commChoice.setBounds(35, 25, 60, 20);
		serialPortPanel.add(commChoice);

		baudrateLabel.setForeground(Color.gray);// �����ʱ�ǩ����
		baudrateLabel.setBounds(100, 25, 40, 20);
		serialPortPanel.add(baudrateLabel);
		baudrateChoice.setFocusable(false);// �����������б�����
		baudrateChoice.setBounds(140, 25, 60, 20);
		serialPortPanel.add(baudrateChoice);

		serialPortOperate.setFocusable(false);// �򿪴���
		serialPortOperate.setBounds(340, 25, 90, 20);// x y �� ��
		serialPortPanel.add(serialPortOperate);

		// ��Ӧ����
		replyPanel.setBorder(BorderFactory.createTitledBorder("��Ӧ����"));
		replyPanel.setBounds(10, 305, 475, 60);
		replyPanel.setLayout(null);
		add(replyPanel);

		String info2 = "��������Ӧ�Ŀ���<8λ>";
		dataInput.setText(info2);
		dataInput1.addFocusListener(new MyFocusListener("", dataInput));// ��ӽ����¼���ӳ
		dataInput.addFocusListener(new MyFocusListener(info2, dataInput));
		cardNumberLabel.setForeground(Color.gray);// ���ű�ǩ
		cardNumberLabel.setBounds(10, 25, 40, 20);
		replyPanel.add(cardNumberLabel);
		dataInput.setBounds(35, 25, 170, 20);// �����ı�������
		replyPanel.add(dataInput);
		dataInput1.setBounds(0, 0, 0, 0);// ��Ͽ����ı�����ʾĬ����ʾ
		replyPanel.add(dataInput1);
		replyLabel.setForeground(Color.gray);// ��Ӧ��ǩ����
		replyLabel.setBounds(305, 25, 40, 20);
		replyPanel.add(replyLabel);
		replyTypeChoice.setFocusable(false);// ��Ӧ�����б�����
		replyTypeChoice.setBounds(335, 25, 120, 20);
		replyPanel.add(replyTypeChoice);

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

		replyTypeChoice.addItem("����ִ�гɹ�");
		replyTypeChoice.addItem("�رմ���ʧ��");
		replyTypeChoice.addItem("��Ĭʧ��");
		replyTypeChoice.addItem("û�ж�����ǩ/��");
		replyTypeChoice.addItem("���֧��");
		replyTypeChoice.addItem("���������");
		replyTypeChoice.addItem("Hardware Type not compatible");
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
				byte[] data0 = null;
				try {
					if (serialport == null) {
						ShowUtils.errorMessage("���ڶ���Ϊ�գ�����ʧ�ܣ�");
					} else {
						String finalData = "EECCstatusCMD02lengthDATA00000D0A";
						String status;
						String CMD;
						String length;
						String DATA;
						// ��ȡ��������
						data0 = SerialPortManager.readFromPort(serialport);
						String data = new String(data0);
						dataView.append("��λ���������ݣ�" + data + "\r\n");
						String temp = (String) replyTypeChoice
								.getSelectedItem();
						if ("����ִ�гɹ�".equals(temp)) {
							status = "00";
						} else if ("�رմ���ʧ��".equals(temp)) {
							status = "01";
						} else if ("��Ĭʧ��".equals(temp)) {
							status = "02";
						} else if ("û�ж�����ǩ/��".equals(temp)) {
							status = "0F";
						} else if ("���֧��".equals(temp)) {
							status = "10";
						} else if ("���������".equals(temp)) {
							status = "11";
						} else {
							status = "12";
						}
						// System.out.println(status);
						CMD = data.substring(4, 6);
						// System.out.println(CMD);
						if ("01".equals(CMD)) {
							length = "0002";
							DATA = "00" + data.substring(8, 10);// ���ֽڿ�Ƭ����
							// System.out.println(DATA);
							// String finalData
							// ="EECCstatusCMD02lengthDATA0000D0A";
							StringBuilder tempData = new StringBuilder(
									finalData);
							tempData.replace(21, 25, DATA);// �Ӻ���ǰ�滻����Ϊ�滻�Ĺ����У�����һֱ���ڱ仯
							tempData.replace(15, 21, length);
							tempData.replace(10, 13, CMD);
							tempData.replace(4, 10, status);
							finalData = tempData.toString();
							// System.out.println(finalData);
							SerialPortManager.sendToPort(serialport, finalData);// ��Ӧ
						}
						if ("02".equals(CMD)) {
							length = "0004";
							if (dataInput.getText().toString().length() > 8) {
								dataView.append("���ų��ȴ���8λ" + "\r\n");
							} else if (dataInput.getText().toString().length() < 8) {
								dataView.append("���ų���С��8λ" + "\r\n");
							} else {
								DATA = dataInput.getText().toString();// 4�ֽڿ���
								// System.out.println(DATA);
								// String finalData
								// ="EECCstatusCMD02lengthDATA0000D0A";
								StringBuilder tempData = new StringBuilder(
										finalData);
								tempData.replace(21, 25, DATA);// �Ӻ���ǰ�滻����Ϊ�滻�Ĺ����У�����һֱ���ڱ仯
								tempData.replace(15, 21, length);
								tempData.replace(10, 13, CMD);
								tempData.replace(4, 10, status);
								finalData = tempData.toString();
								// System.out.println(finalData);
								SerialPortManager.sendToPort(serialport,
										finalData);// ��Ӧ
							}
						}
						if ("03".equals(CMD)) {
							length = "0000";
							cardNumber = data.substring(14, 22);// ��λ��ѡ��Ŀ���
						}
						if ("04".equals(CMD)) {
							length = "0000";
						}
						if ("05".equals(CMD)) {
							length = "0011";
							BufferedReader br = new BufferedReader(
									new FileReader("simulationCard\\" + cardNumber + "\\"
											+ data.substring(14, 16) + ".txt"));// ģ��RFID��д���ӱ�ǩ�ж�����

							DATA = br.readLine();
							// System.out.println(DATA);
							br.close();
							DATA = DATA.substring(16, 48);
							// System.out.println(DATA);
							// String finalData
							// ="EECCstatusCMD02lengthDATA0000D0A";
							StringBuilder tempData = new StringBuilder(
									finalData);
							tempData.replace(21, 25, DATA);// �Ӻ���ǰ�滻����Ϊ�滻�Ĺ����У�����һֱ���ڱ仯
							tempData.replace(15, 21, length);
							tempData.replace(10, 13, CMD);
							tempData.replace(4, 10, status);
							finalData = tempData.toString();
							SerialPortManager.sendToPort(serialport, finalData);// ��Ӧ

						}
						if ("06".equals(CMD)) {
							length = "0000";
							// System.out.println(data.substring(14, 16));
							FileWriter fw = new FileWriter("simulationCard\\" + cardNumber + "\\"
									+ data.substring(14, 16) + ".txt");// ģ��RFID��д�����ǩ��д����
							fw.write(data);
							fw.close();
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
