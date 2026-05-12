package com.account;

import com.ex.calculate.ExpensesCompute;
import java.awt.*;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.*;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class ExpensesChart {

    private final ExpensesCompute bean = new ExpensesCompute();

    // ── Single source of truth for colors ─────
    // category name → its color, built once from DB
    private final Map<String, Color> categoryColorMap = new LinkedHashMap<>();

    private final Color[] PALETTE = {
        new Color(99,  102, 241),  // indigo
        new Color(236,  72, 153),  // pink
        new Color(16,  185, 129),  // emerald
        new Color(245, 158,  11),  // amber
        new Color(59,  130, 246),  // blue
        new Color(239,  68,  68),  // red
        new Color(139,  92, 246)   // violet
    };

    // ───────────────────────────────────────────
    // CONSTRUCTOR — build color map from DB once
    // ───────────────────────────────────────────
    public ExpensesChart() {
        try {
            Statement st = SQLConnection.getInstance().getSQLConnection();
            ResultSet rs = st.executeQuery(
                "SELECT DISTINCT category FROM expenses " +
                "WHERE year=2026 ORDER BY category"
            );
            int idx = 0;
            while (rs.next()) {
                String cat = rs.getString("category");
                categoryColorMap.put(cat, PALETTE[idx % PALETTE.length]);
                idx++;
            }
        } catch (Exception e) {
            System.out.println("Color map error: " + e.getMessage());
        }
    }

    // ───────────────────────────────────────────
    // PUBLIC — get color for any category
    // Used by MainWindow for category cards too
    // ───────────────────────────────────────────
    public Color getCategoryColor(String category) {
        return categoryColorMap.getOrDefault(category, Color.GRAY);
    }

    // ───────────────────────────────────────────
    // RING CHART
    // ───────────────────────────────────────────
    public ChartPanel buildMonthlyRingChart(String month) {

        DefaultPieDataset dataset = new DefaultPieDataset();

        try {
            Statement st = SQLConnection.getInstance().getSQLConnection();
            ResultSet rs = st.executeQuery(
                "SELECT category, amount FROM expenses " +
                "WHERE year=2026 AND month='" + month + "' " +
                "ORDER BY amount DESC"
            );
            while (rs.next()) {
                int computed = bean.monthlyTotalExpenses(
                    new int[]{rs.getInt("amount")}
                );
                dataset.setValue(rs.getString("category"), computed);
            }
        } catch (Exception e) {
            System.out.println("Ring chart error: " + e.getMessage());
        }

        // Monthly total for subtitle
        int monthlyTotal = 0;
        try {
            Statement st = SQLConnection.getInstance().getSQLConnection();
            ResultSet rs = st.executeQuery(
                "SELECT SUM(amount) AS total FROM expenses " +
                "WHERE year=2026 AND month='" + month + "'"
            );
            if (rs.next()) monthlyTotal = rs.getInt("total");
        } catch (Exception e) {
            System.out.println("Monthly total error: " + e.getMessage());
        }

        JFreeChart chart = ChartFactory.createRingChart(
            "Expenses — " + month,
            dataset, true, true, false
        );

        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 16));
        chart.addSubtitle(new TextTitle(
            String.format("This month's spending:   RM %.2f", (double) monthlyTotal),
            new Font("Arial", Font.BOLD, 13)
        ));

        RingPlot plot = (RingPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setShadowPaint(null);
        plot.setLabelFont(new Font("Arial", Font.PLAIN, 11));
        plot.setSectionDepth(0.35);

        // ── Use shared color map ───────────────
        for (Map.Entry<String, Color> entry : categoryColorMap.entrySet()) {
            plot.setSectionPaint(entry.getKey(), entry.getValue());
        }

        ChartPanel cp = new ChartPanel(chart);
        cp.setBackground(Color.WHITE);
        return cp;
    }

    // ───────────────────────────────────────────
    // BAR CHART
    // ───────────────────────────────────────────
    public ChartPanel buildMonthlyBarChart(String month) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Keep track of category order for color assignment
        java.util.List<String> categoryOrder = new java.util.ArrayList<>();

        try {
            Statement st = SQLConnection.getInstance().getSQLConnection();
            ResultSet rs = st.executeQuery(
                "SELECT category, amount FROM expenses " +
                "WHERE year=2026 AND month='" + month + "' " +
                "ORDER BY amount DESC"
            );
            while (rs.next()) {
                String cat = rs.getString("category");
                int computed = bean.yearlyTotalByCategory(
                    new int[]{rs.getInt("amount")}
                );
                dataset.addValue(computed, "Amount (RM)", cat);
                categoryOrder.add(cat);
            }
        } catch (Exception e) {
            System.out.println("Bar chart error: " + e.getMessage());
        }

        JFreeChart chart = ChartFactory.createBarChart(
            "Expenses by Category — " + month,
            "Category",
            "Amount (RM)",
            dataset,
            PlotOrientation.VERTICAL,
            false, true, false
        );

        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 14));

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(new Color(230, 230, 235));
        plot.setDomainGridlinesVisible(false);
        plot.setOutlineVisible(false);

        plot.getDomainAxis().setLabelFont(new Font("Arial", Font.BOLD, 12));
        plot.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 10));
        plot.getDomainAxis().setCategoryLabelPositions(
            CategoryLabelPositions.UP_45
        );
        plot.getRangeAxis().setLabelFont(new Font("Arial", Font.BOLD, 12));
        plot.getRangeAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 10));

        // ── Use shared color map per bar ───────
        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int col) {
                if (col < categoryOrder.size()) {
                    return getCategoryColor(categoryOrder.get(col));
                }
                return Color.GRAY;
            }
        };
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setMaximumBarWidth(0.08);
        renderer.setItemMargin(0.15);
        renderer.setDrawBarOutline(false);
        plot.setRenderer(renderer);

        ChartPanel cp = new ChartPanel(chart);
        cp.setBackground(Color.WHITE);
        return cp;
    }
}