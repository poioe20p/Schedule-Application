package LETI_GrupoF.ProjetoES.user_interface;

import LETI_GrupoF.ProjetoES.Horario;
import LETI_GrupoF.ProjetoES.Metrica;

import javax.swing.*;
import javax.swing.table.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.awt.*;

public class ScheduleQualityTable extends JFrame {

    private final Horario horario;
    private final JButton nextPageButton;
    private final JButton previousPageButton;
    private final JButton openMetricScheduleButtonsMap;
    private final MetricTableModel metricTable;
    private int currentPage = 1;
    private final JLabel pageInfoLabel = new JLabel();
    private final JTable table;
    private List<Metrica> data;


    public ScheduleQualityTable(Horario horario, JFrame previousFrame) {
        this.horario = horario;
        data = new ArrayList<>(horario.getMetricas().keySet());
        LayoutDefinable.basicLayout("Schedule Quality Table", this, LayoutDefinable.getColor("gray"));
        previousFrame.setVisible(false);
        metricTable = new MetricTableModel();
        table = new JTable(metricTable);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
//        table.setRowHeight(25);

        //Usar isto
        //table.getSelectedRow();

        JButton backButton = LayoutDefinable.defineButtonLayout(LayoutDefinable.getColor("red"), LayoutDefinable.getColor("white"),
                "Back", new Dimension(100, 50));
        backButton.addActionListener(e -> {
            previousFrame.setVisible(true);
            this.setVisible(false);
            this.dispose();
        });

        openMetricScheduleButtonsMap = LayoutDefinable.defineButtonLayout(LayoutDefinable.getColor("blue"), LayoutDefinable.getColor("white"),
                "Open Schedule", new Dimension(120, 50));

        nextPageButton = LayoutDefinable.defineButtonLayout(LayoutDefinable.getColor("blue"), LayoutDefinable.getColor("white"),
                "Next", new Dimension(100, 50));
        previousPageButton = LayoutDefinable.defineButtonLayout(LayoutDefinable.getColor("blue"), LayoutDefinable.getColor("white")
                , "Previous", new Dimension(100, 50));
        setUpTableNavigationButtons();

        JPanel scheduleQualityTablePanel = new JPanel();
        scheduleQualityTablePanel.add(openMetricScheduleButtonsMap);
        scheduleQualityTablePanel.add(previousPageButton);
        scheduleQualityTablePanel.add(pageInfoLabel);
        scheduleQualityTablePanel.add(nextPageButton);
        scheduleQualityTablePanel.add(backButton);

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(scheduleQualityTablePanel, BorderLayout.SOUTH);
        updateTable();
    }

    private void setUpTableNavigationButtons() {
        previousPageButton.addActionListener(e -> {
            if(currentPage > 1) {
                currentPage--;
                updateTable();
            }
        });

        nextPageButton.addActionListener(e -> {
            if(currentPage < (int) Math.ceil((double) horario.getMetricas().size() / 15)) {
                currentPage++;
                updateTable();
            }
        });
    }

    private void updateTable() {
        metricTable.updateData(currentPage);
        int totalPages = (int) Math.ceil((double) horario.getMetricas().size() / 15);
        pageInfoLabel.setText(String.format("Page %d of %d", currentPage, totalPages <= 0 ? 1 : totalPages));
        updateButtonState();
    }

    private void updateButtonState() {
        previousPageButton.setEnabled(currentPage > 1);
        nextPageButton.setEnabled(currentPage < (int) Math.ceil((double) horario.getMetricas().size() / 15));
    }

    public JButton getOpenMetricScheduleButton() {
        return openMetricScheduleButtonsMap;
    }

    //Apartir daqui consigo saber qual foi a linha selecionada e sabendo qual foi a linha selecionada consigo também a metrica e logo os valores de aulas
    public JTable getTable() {
    	return table;
    }

    public List<Metrica> getData() {
    	return data;
    }


    class MetricTableModel extends AbstractTableModel {
        private final List<Metrica> metricas;
        public MetricTableModel() {
            this.metricas = new ArrayList<>(horario.getMetricas().keySet());
        }

        private final String[] columnNames = {"Metrica", "Total do calculo da metrica"};

        @Override
        public int getRowCount() {
            return metricas.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Metrica metrica = metricas.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> metrica.getFormula();
                case 1 -> horario.getMetricas().get(metrica);
                default -> null;
            };
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        private void updateData(int currentPage) {
            metricas.clear();
            int start = (currentPage - 1) * 15;
            int end = Math.min(start + 15, data.size());
            for (int i = start; i < end; i++) {
                metricas.add(data.get(i));
            }
            fireTableDataChanged();
        }
    }

    public static void main(String[] args) {
        Horario horario = new Horario("ProjetoES/HorarioDeExemplo.csv");
        Map<String, Integer> metricas = new LinkedHashMap<>();
        int i = 0;
        for(String s: horario.getColumnTitles()) {
            metricas.put(s, i);
        }
        horario.setOrdemCampos(metricas);
        ScheduleQualityTable scheduleQualityTable = new ScheduleQualityTable(horario, new JFrame());
        scheduleQualityTable.setVisible(true);
    }
}