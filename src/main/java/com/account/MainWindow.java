package com.account;

import com.ex.calculate.ExpensesCompute;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class MainWindow extends JFrame {

    // ── Core components ────────────────────────
    private final ExpensesCompute bean         = new ExpensesCompute();
    private final ExpensesChart   expensesChart = new ExpensesChart();
    private JPanel                chartPanel;
    private JPanel                categoryPanel;
    private JButton               activeBtn    = null;
    private final List<String>    monthList    = new ArrayList<>();

    // ── Colors ─────────────────────────────────
    private final Color COL_DARK   = new Color(60, 63, 65);
    private final Color COL_LIGHT  = new Color(245, 245, 250);
    private final Color COL_WHITE  = Color.WHITE;
    private final Color COL_ACCENT = new Color(99, 102, 241);
    private final Color COL_TEXT   = new Color(180, 180, 180);

    private final Color[] CAT_COLORS = {
        new Color(255, 99,  71),
        new Color(147, 112, 219),
        new Color(60,  179, 113),
        new Color(70,  130, 180),
        new Color(255, 165,   0),
        new Color(220,  20,  60),
        new Color(100, 149, 237)
    };

    // ───────────────────────────────────────────
    // CONSTRUCTOR
    // ───────────────────────────────────────────
    public MainWindow() {
        setTitle("Account Dashboard - Shawn and Izzudin");
        setSize(1100, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(COL_LIGHT);

        add(buildStatsPanel(),  BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);

        if (!monthList.isEmpty()) {
            refreshDashboard(monthList.get(0));
        }

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ───────────────────────────────────────────
    // STATS PANEL — top dark bar
    // ───────────────────────────────────────────
    private JPanel buildStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 1, 0));
        panel.setBackground(COL_DARK);
        panel.setPreferredSize(new Dimension(1100, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        int yearlyTotal = 0;
        int monthCount  = 0;
        int janTotal    = 0;
        int decTotal    = 0;

        try {
            Statement st = SQLConnection.getInstance().getSQLConnection();
            ResultSet rs;

            rs = st.executeQuery(
                "SELECT SUM(amount) AS total FROM expenses WHERE year=2026");
            if (rs.next()) yearlyTotal = rs.getInt("total");

            rs = st.executeQuery(
                "SELECT COUNT(DISTINCT month) AS cnt FROM expenses WHERE year=2026");
            if (rs.next()) monthCount = rs.getInt("cnt");

            rs = st.executeQuery(
                "SELECT SUM(amount) AS total FROM expenses " +
                "WHERE year=2026 AND month='January'");
            if (rs.next()) janTotal = rs.getInt("total");

            rs = st.executeQuery(
                "SELECT SUM(amount) AS total FROM expenses " +
                "WHERE year=2026 AND month='December'");
            if (rs.next()) decTotal = rs.getInt("total");

        } catch (Exception e) {
            System.out.println("Stats error: " + e.getMessage());
        }

        double avg = bean.avgMonthlyExpenses(yearlyTotal, monthCount);
        double pct = bean.percentageChangeBetweenMonths(decTotal, janTotal);
        int    dif = bean.monthToMonthComparison(decTotal, janTotal);

        panel.add(createStatCard("Yearly Total",
                String.format("RM %.2f", (double) yearlyTotal)));
        panel.add(createStatCard("Avg Monthly",
                String.format("RM %.2f", avg)));
        panel.add(createStatCard("Jan → Dec Diff",
                String.format("RM %.2f", (double) dif)));
        panel.add(createStatCard("Jan → Dec Change",
                String.format("%.2f%%", pct)));

        return panel;
    }

    // ───────────────────────────────────────────
    // CENTER PANEL
    // ───────────────────────────────────────────
    private JPanel buildCenterPanel() {
        JPanel center = new JPanel(new BorderLayout(0, 0));
        center.setBackground(COL_LIGHT);

        center.add(buildMonthSelector(), BorderLayout.NORTH);

        // Two charts stacked vertically
        chartPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        chartPanel.setBackground(COL_WHITE);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        center.add(chartPanel, BorderLayout.CENTER);

        categoryPanel = new JPanel();
        categoryPanel.setBackground(COL_DARK);
        categoryPanel.setPreferredSize(new Dimension(1100, 110));
        center.add(categoryPanel, BorderLayout.SOUTH);

        return center;
    }

    // ───────────────────────────────────────────
    // MONTH SELECTOR — from DB
    // ───────────────────────────────────────────
    private JPanel buildMonthSelector() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        panel.setBackground(COL_WHITE);
        panel.setPreferredSize(new Dimension(1100, 50));

        try {
            Statement st = SQLConnection.getInstance().getSQLConnection();
            ResultSet rs = st.executeQuery(
                "SELECT DISTINCT month FROM expenses WHERE year=2026 " +
                "ORDER BY FIELD(month,'January','February','March','April'," +
                "'May','June','July','August','September','October'," +
                "'November','December')"
            );
            while (rs.next()) monthList.add(rs.getString("month"));
        } catch (Exception e) {
            System.out.println("Month fetch error: " + e.getMessage());
        }

        for (int i = 0; i < monthList.size(); i++) {
            final String month = monthList.get(i);
            String shortName   = month.substring(0, 3);

            JButton btn = new JButton(shortName);
            btn.setPreferredSize(new Dimension(72, 30));
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            styleMonthButton(btn, false);

            btn.addActionListener(e -> {
                styleMonthButton(activeBtn, false);
                styleMonthButton(btn, true);
                activeBtn = btn;
                refreshDashboard(month);
            });

            if (i == 0) {
                activeBtn = btn;
                styleMonthButton(btn, true);
            }

            panel.add(btn);
        }

        return panel;
    }

    // ───────────────────────────────────────────
    // REFRESH — called on every month change
    // ───────────────────────────────────────────
    private void refreshDashboard(String month) {
        chartPanel.removeAll();
        chartPanel.add(expensesChart.buildMonthlyRingChart(month));
        chartPanel.add(expensesChart.buildMonthlyBarChart(month));
        chartPanel.revalidate();
        chartPanel.repaint();

        categoryPanel.removeAll();
        buildCategoryCards(month);
        categoryPanel.revalidate();
        categoryPanel.repaint();
    }

    // ───────────────────────────────────────────
    // CATEGORY CARDS — fully from DB
    // ───────────────────────────────────────────
    private void buildCategoryCards(String month) {
        categoryPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        try {
            Statement st = SQLConnection.getInstance().getSQLConnection();
            ResultSet rs = st.executeQuery(
                "SELECT category, amount FROM expenses " +
                "WHERE year=2026 AND month='" + month + "' " +
                "ORDER BY amount DESC"
            );

            int colorIndex = 0;
            while (rs.next()) {
                String category = rs.getString("category");
                int    amount   = rs.getInt("amount");
                int    computed = bean.monthlyTotalExpenses(new int[]{amount});
                Color cardColor = expensesChart.getCategoryColor(category);

                String shortName = category.length() > 10
                    ? category.substring(0, 10) + "…"
                    : category;

                JPanel card = new JPanel(new BorderLayout());
                card.setPreferredSize(new Dimension(120, 75));
                card.setBackground(cardColor);
                card.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

                JLabel nameLabel = new JLabel(shortName, SwingConstants.CENTER);
                nameLabel.setFont(new Font("Arial", Font.PLAIN, 10));
                nameLabel.setForeground(COL_WHITE);

                JLabel amtLabel = new JLabel(
                    String.format("RM %.2f", (double) computed),
                    SwingConstants.CENTER
                );
                amtLabel.setFont(new Font("Arial", Font.BOLD, 12));
                amtLabel.setForeground(COL_WHITE);

                card.add(nameLabel, BorderLayout.NORTH);
                card.add(amtLabel,  BorderLayout.CENTER);
                categoryPanel.add(card);
            }

        } catch (Exception e) {
            System.out.println("Category error: " + e.getMessage());
        }
    }

    // ───────────────────────────────────────────
    // HELPERS
    // ───────────────────────────────────────────
    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(80, 83, 85));
        card.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setForeground(COL_TEXT);
        t.setFont(new Font("Arial", Font.PLAIN, 11));

        JLabel v = new JLabel(value, SwingConstants.CENTER);
        v.setForeground(COL_WHITE);
        v.setFont(new Font("Arial", Font.BOLD, 16));

        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return card;
    }

    private void styleMonthButton(JButton btn, boolean active) {
        if (btn == null) return;
        if (active) {
            btn.setBackground(COL_ACCENT);
            btn.setForeground(COL_WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 11));
        } else {
            btn.setBackground(new Color(240, 240, 240));
            btn.setForeground(COL_DARK);
            btn.setFont(new Font("Arial", Font.PLAIN, 11));
        }
    }

    // ───────────────────────────────────────────
    // MAIN
    // ───────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow());
    }
}