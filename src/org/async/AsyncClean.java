package org.async;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class AsyncClean {
	private Socket socket;
	private JLabel la, la1;
	JFrame fr;

	public AsyncClean() throws Exception {
		try{
			socket = new Socket("localhost", 8081);
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "请先启动Server类！");
			throw e;
		}

		fr = new JFrame("Clean");
		fr.setLayout(null);
		fr.add(la = new JLabel("R", 0));
		la.setFocusable(true);
		fr.add(la1 = new JLabel("B", 0));
		la1.setForeground(Color.white);
		la1.setOpaque(true);
		la1.setSize(20, 20);
		la.setBounds(0, 0, 20, 20);
		la.setBackground(Color.red);
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
		fr.setLocation(new Point(0, 200));
		fr.setDefaultCloseOperation(3);
		new Thread(new Runnable() {

			@Override
			public void run() {
				fr.setVisible(true);
			}
		}).start();

		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

		while (true) {

			String msg = Color.red.getRGB() + ";" + la.getX() + ";" + la.getY();
			pw.println(msg);
			pw.flush();

			try {
				String str = br.readLine();
				String[] ss = str.split(";");
				int rgb = Integer.parseInt(ss[0]);
				int x = Integer.parseInt(ss[1]);
				int y = Integer.parseInt(ss[2]);

				la1.setLocation(x, y);
				la1.setBackground(new Color(rgb));
				Thread.sleep(10);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(fr, "连接中断...");
				System.exit(0);
				throw e1;
			}
		}
	}

	public static void main(String[] args) throws Exception {
		new AsyncClean();
	}
}
