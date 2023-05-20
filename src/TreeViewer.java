import javax.imageio.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import rbtree.RBTree;

public class TreeViewer extends JPanel {
	private RBTree<Integer> tree = new RBTree<Integer>();
	private TreePanel treePanel = new TreePanel(tree);

	public TreeViewer(){
		treePanel.setBackground(new Color(181, 177, 185));
		initViews();
	}

	private void setMidPoint(JScrollPane scrollPane){ scrollPane.getViewport().setViewPosition(new Point(4100, 0)); }

	private void setTopPanel(){
		JPanel panel = new JPanel();
		panel.setBackground(new Color(208, 172, 238));
		JLabel red = new JLabel("Крассно");
		red.setForeground(new Color(255, 0, 0));
		JLabel a = new JLabel("-");
		a.setForeground(new Color(114, 110, 110));
		JLabel black = new JLabel("чёрное");
		black.setForeground(new Color(0, 0, 0));
		JLabel tre = new JLabel(" дерево");
		tre.setForeground(new Color(140, 232, 69));
		panel.add(red);
		panel.add(a);
		panel.add(black);
		panel.add(tre);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(panel, BorderLayout.NORTH);
	}
	private double scale = 1.0; // масштаб по умолчанию
	public void setScale(double scale) {
		this.scale = scale;
		treePanel.repaint();
		//treePanel.scale(scale, scale);
	}

	private void setBottomPanel(){
		final JTextField mTextField = new JTextField(15);
		final JButton btn_ins = new JButton();
		setupButton(btn_ins, "add");
		final JButton btn_del = new JButton();
		setupButton(btn_del, "delete");
		final JButton btn_clear = new JButton();
		setupButton(btn_clear, "clear");
		final JButton btn_info = new JButton();
		setupButton(btn_info, "search");

		JPanel panel = new JPanel();
		panel.add(btn_info);
		panel.add(btn_ins);
		panel.add(mTextField);
		panel.add(btn_del);
		panel.add(btn_clear);
		panel.setBackground(Color.WHITE);
		add(panel, BorderLayout.SOUTH);

		btn_ins.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent actionEvent){
				if(mTextField.getText().equals("")) return;
				Integer toInsert = Integer.parseInt(mTextField.getText());
				if(tree.contains(toInsert)) JOptionPane.showMessageDialog(null, "Элемент уже присутствует в данном дереве!");
				else {
					tree.insert(toInsert);
					treePanel.repaint();
					mTextField.requestFocus();
					mTextField.selectAll();
				}
			}
		});

		btn_del.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent actionEvent){
				if(mTextField.getText().equals("")) return;
				Integer toDelete = Integer.parseInt(mTextField.getText());
				if(!tree.contains(toDelete)) JOptionPane.showMessageDialog(null, "Данный элемент отсутствует в дереве!");
				else {
					tree.remove(toDelete);
					treePanel.repaint();
					mTextField.requestFocus();
					mTextField.selectAll();
				}
			}
		});

		btn_clear.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent actionEvent){
				if (tree.isEmpty()) JOptionPane.showMessageDialog(null, "Дерево пусто!");
				else tree.clear();
				treePanel.setSearch(null);
				treePanel.repaint();
			}
		});

		btn_info.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent actionEvent){
				if(mTextField.getText().equals("")) return;
				Integer toSearch = Integer.parseInt(mTextField.getText());
				if(!tree.contains(toSearch)) JOptionPane.showMessageDialog(null, "Данный элемент отсутствует в дереве!");
				else {
					treePanel.setSearch(toSearch);
					treePanel.repaint();
					mTextField.requestFocus();
					mTextField.selectAll();
				}
			}

		});

		mTextField.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent actionEvent){ btn_ins.doClick(); }
		});
	}

	private void setScrollPane() {
		treePanel.setPreferredSize(new Dimension(9000, 4096)); //установка размера
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(treePanel);
		scroll.setPreferredSize(new Dimension(750, 500));
		setMidPoint(scroll);
		add(scroll, BorderLayout.CENTER);
	}

	private void initViews() {
		super.setLayout(new BorderLayout());
		setScrollPane();
		//setTopPanel();
		setBottomPanel();
	}

	private void setupButton(JButton button, String imgSrc) {
		try {
			Image icon = ImageIO.read(getClass().getResource("./resources/" + imgSrc + ".png"));
			button.setIcon(new ImageIcon(icon));
			button.setBorderPainted(false);
			button.setFocusPainted(false);
			button.setContentAreaFilled(false);
		} catch (IOException e){ e.printStackTrace(); }
	}

	public static void main(String... args) {
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e){ e.printStackTrace(); }
		JFrame j = new JFrame();
		j.setTitle("Крассно-чёрное дерево");
		try { j.setIconImage(ImageIO.read(TreeViewer.class.getResource("/resources/logo.png"))); } catch (IOException e) { e.printStackTrace(); }
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.add(new TreeViewer());
		j.pack();
		j.setVisible(true);
	}
}