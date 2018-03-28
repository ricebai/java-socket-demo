package org.async;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AsyncServer {
	ServerSocket s;
	Socket socket = null;
	JLabel la, la1;
	JFrame fr;

	public AsyncServer() throws Exception {

		fr = new JFrame("Server");
		fr.setLayout(new BorderLayout());
		
		JPanel p = new JPanel();
		p.setLayout(null);
		p.setBackground(Color.white);
		p.add(la = new JLabel("B", 0));
		p.add(la1 = new JLabel("R", 0));
		fr.add(p, BorderLayout.CENTER);
		fr.add(new JLabel("请按动方向键移动色块！"), BorderLayout.SOUTH);
		
		la1.setSize(20, 20);
		la1.setOpaque(true);
		la.setForeground(Color.white);
		la.setFocusable(true);
		la.setBounds(0, 0, 20, 20);
		la.setBackground(Color.blue);
		la.setOpaque(true);
		fr.getContentPane().setBackground(Color.white);
		la.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int space = 2;
				if (e.getKeyCode() == 38) {
					la.setLocation(la.getX(), la.getY() - space);
				} else if (e.getKeyCode() == 39) {
					la.setLocation(la.getX() + space, la.getY());
				} else if (e.getKeyCode() == 40) {
					la.setLocation(la.getX(), la.getY() + space);
				} else if (e.getKeyCode() == 37) {
					la.setLocation(la.getX() - space, la.getY());
				}
			}
		});

		fr.setSize(new Dimension(300, 200));
		fr.setDefaultCloseOperation(3);

		new Thread(new Runnable() {

			@Override
			public void run() {
				fr.setVisible(true);
			}
		}).start();

		while(true){
			try{
				s = new ServerSocket(8081);
			}catch(Exception e){
				JOptionPane.showMessageDialog(fr, "端口被占用，请不要重复启动Server类！");
				System.exit(0);
				throw e;
			}
			System.out.println("ServerSocket Start:" + s);
			socket = s.accept();
			System.out.println("Connection accept socket:" + socket);
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			try {
				while (true) {
					String str = br.readLine();
					String[] ss = str.split(";");
					int rgb = Integer.parseInt(ss[0]);
					int x = Integer.parseInt(ss[1]);
					int y = Integer.parseInt(ss[2]);
					Thread.sleep(10);
					la1.setLocation(x, y);
					la1.setBackground(new Color(rgb));

					String msg = Color.blue.getRGB() + ";" + la.getX() + ";" + la.getY();
					pw.println(msg);
					pw.flush();
				}
			}catch(Exception e){
				System.out.println("连接中断...");
			}finally {
				// 关闭流
				br.close();
				pw.close();
				// 关闭端口
				s.close();
				System.out.println("close..");
			}
		}
	}

	public static void main(String[] args) throws Exception {
		// 先启动Server
		new AsyncServer();
	}
}
