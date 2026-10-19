import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class QuizBattle extends JFrame {
    String[][] Q = { // question, 4 options, sahi option ka index
        {"Java ka creator kaun hai?", "James Gosling", "Dennis Ritchie", "Guido van Rossum", "Bjarne Stroustrup", "0"},
        {"Java file ka extension?", ".jav", ".java", ".class", ".jv", "1"},
        {"JVM ka full form?", "Java Visual Machine", "Java Virtual Machine", "Java Variable Method", "Joint Virtual Memory", "1"},
        {"Inheritance ke liye keyword?", "implements", "super", "extends", "inherits", "2"},
        {"Kaunsa primitive type nahi hai?", "int", "boolean", "String", "char", "2"},
        {"Array ka index kahan se start hota hai?", "0", "1", "-1", "2", "0"},
        {"Java mein entry point method?", "start()", "main()", "run()", "init()", "1"},
        {"Swing kis package mein hai?", "java.awt", "javax.swing", "java.util", "java.io", "1"},
        {"Quick Sort ki average complexity?", "O(n)", "O(n^2)", "O(n log n)", "O(log n)", "2"},
        {"Constructor ka return type?", "void", "int", "String", "Koi nahi", "3"}
    };
    String[] name = new String[2];
    int[] score = new int[2];
    int q = 0, time = 15;
    JLabel info = new JLabel(), clock = new JLabel(), ques = new JLabel();
    JRadioButton[] opt = new JRadioButton[4];
    ButtonGroup bg = new ButtonGroup();
    javax.swing.Timer timer;

    QuizBattle() {
        for (int i = 0; i < 2; i++) {
            String n = JOptionPane.showInputDialog("Player " + (i + 1) + " ka naam:");
            name[i] = (n == null || n.isBlank()) ? "Player" + (i + 1) : n.replace(",", "");
        }
        setTitle("Byte Battlers - Mini Quiz Battle");
        setSize(520, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel p = new JPanel(new GridLayout(7, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        info.setFont(new Font("Arial", Font.BOLD, 14));
        ques.setFont(new Font("Arial", Font.BOLD, 16));
        clock.setForeground(Color.RED);
        p.add(info); p.add(clock); p.add(ques);
        for (int i = 0; i < 4; i++) { opt[i] = new JRadioButton(); bg.add(opt[i]); p.add(opt[i]); }

        JButton next = new JButton("Submit / Next");
        next.addActionListener(e -> check());
        add(p, BorderLayout.CENTER);
        add(next, BorderLayout.SOUTH);

        timer = new javax.swing.Timer(1000, e -> {
            clock.setText("Time: " + (--time));
            if (time == 0) check();
        });
        show(q);
        setVisible(true);
    }

    void show(int n) {
        bg.clearSelection();
        info.setText(name[n % 2] + " ki baari  |  " + name[0] + ": " + score[0] + "  " + name[1] + ": " + score[1]);
        ques.setText("Q" + (n + 1) + ". " + Q[n][0]);
        for (int i = 0; i < 4; i++) opt[i].setText(Q[n][i + 1]);
        time = 15;
        clock.setText("Time: 15");
        timer.restart();
    }

    void check() {
        timer.stop();
        int ans = Integer.parseInt(Q[q][5]);
        if (opt[ans].isSelected()) score[q % 2] += 10;
        if (++q == Q.length) end(); else show(q);
    }

    void end() {
        String w = score[0] == score[1] ? "Draw!" : "Winner: " + name[score[0] > score[1] ? 0 : 1];
        try (FileWriter f = new FileWriter("leaderboard.txt", true)) {
            for (int i = 0; i < 2; i++) f.write(name[i] + "," + score[i] + "\n");
        } catch (IOException e) { }
        JOptionPane.showMessageDialog(this, w + "\n" + name[0] + ": " + score[0] + "\n" + name[1] + ": " + score[1] + "\n\n" + board());
        System.exit(0);
    }

    String board() {
        java.util.List<String> l = new ArrayList<>();
        try { l = new ArrayList<>(Files.readAllLines(Paths.get("leaderboard.txt"))); } catch (IOException e) { }
        l.sort((a, b) -> Integer.parseInt(b.split(",")[1]) - Integer.parseInt(a.split(",")[1]));
        StringBuilder s = new StringBuilder("--- Top 5 ---\n");
        for (int i = 0; i < Math.min(5, l.size()); i++) s.append((i + 1) + ". " + l.get(i).replace(",", " : ") + "\n");
        return s.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizBattle::new);
    }
}
