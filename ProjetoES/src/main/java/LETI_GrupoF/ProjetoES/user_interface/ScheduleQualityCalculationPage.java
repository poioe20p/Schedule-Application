package LETI_GrupoF.ProjetoES.user_interface;

import LETI_GrupoF.ProjetoES.Horario;
import LETI_GrupoF.ProjetoES.Metrica;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Stream;

/**
 * A classe ScheduleQualityCalculationPage representa a página de cálculo de
 * qualidade do horário na interface gráfica. Estende o JFrame e implementa a
 * interface LayoutDefinable.
 */

public class ScheduleQualityCalculationPage extends JFrame implements LayoutDefinable {

	private JButton calculateScheduleQuality;
	private GridBagConstraints gbc = new GridBagConstraints();
	private JComboBox<String> listOfMatOperators;
	private JComboBox<String> listOfMatOperators2;
	private JTextField integerField;
	private JComboBox<String> listOfVariables;
	private JComboBox<String> listOfVariables2;
	private DefaultListModel<String> variableListModel;
	private List<String> metricas = new ArrayList<>();
	private Horario horario;
	private List<String> variablesForFormula = new ArrayList<>();
	private JPanel formulaCreatingPanel;
	private JButton addFormula;

	/**
	 * Construtor da classe ScheduleQualityCalculationPage.
	 *
	 * @param variablesForFormula Lista de variáveis disponíveis para fórmulas.
	 * @param previousFrame       JFrame anterior à página de cálculo de qualidade
	 *                            do horário.
	 * @param horario             Horario contendo informações sobre o horário.
	 */

	public ScheduleQualityCalculationPage(List<String> variablesForFormula, JFrame previousFrame, Horario horario) {
		LayoutDefinable.basicLayout("Schedule Quality", this, Color.darkGray);
		setLayout(new BorderLayout());

		this.horario = horario;
		this.variablesForFormula = variablesForFormula;

		// Panel que corresponde ao separador que contém os elementos para criação de
		// variaveis
		JPanel createFormulaPanel = new JPanel(new GridLayout(2, 1));

		gbc = resetGBC(gbc);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 0.5;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.PAGE_START;
		createFormulaPanel.add(topFormulaCreatingPanel(variablesForFormula));
		gbc.gridy = 1;
		createFormulaPanel.add(bottomFormulaCreatingPanel(previousFrame));

//        Panel que corresponde ao separador que contém o display das fórmulas criadas
		JPanel createdFormulaDisplayPanel = createdFormulasDisplayPanel();
		JTabbedPane scheduleQualityTabs = new JTabbedPane();
		scheduleQualityTabs.add("Formula Creation Tab", createFormulaPanel);
		scheduleQualityTabs.add("Created Formulas", createdFormulaDisplayPanel);

		if (previousFrame instanceof SubmitFilePage) {
			updateMetricTable();
		}
		add(scheduleQualityTabs, BorderLayout.CENTER);
	}

	private JPanel createdFormulasDisplayPanel() {
		JPanel formulaListDisplayPanel = new JPanel(new GridBagLayout());
		formulaListDisplayPanel.setBackground(Color.darkGray);

		// Elementos do panel que corresponde ao separador que contém o display das
		// fórmulas criadas
		variableListModel = new DefaultListModel<>();
		JList<String> metricList = new JList<>(variableListModel);

		JScrollPane metriclistPane = new JScrollPane(metricList);
		metriclistPane.setFont((new Font("Arial", Font.BOLD, 60)));

		JLabel paneLabel = new JLabel("Your Created Formulas");
		paneLabel.setFont(new Font("Arial", Font.BOLD, 30));
		paneLabel.setBackground(Color.darkGray);

		gbc = resetGBC(gbc);
		gbc.gridwidth = 2;
		formulaListDisplayPanel.add(paneLabel, gbc);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1;
		gbc.weightx = 1;
		gbc.gridy = 1;
		formulaListDisplayPanel.add(metriclistPane, gbc);
		return formulaListDisplayPanel;
	}

	/**
	 * Cria o painel superior para a criação de fórmulas.
	 *
	 * @param variablesForFormula Lista de variáveis disponíveis para fórmulas.
	 * @return JPanel representando o painel superior para a criação de fórmulas.
	 */

	private JPanel topFormulaCreatingPanel(List<String> variablesForFormula) {
		formulaCreatingPanel = new JPanel(new GridBagLayout());
		formulaCreatingPanel.setBackground(Color.darkGray);
		gbc = resetGBC(gbc);

		JTextArea pageFunctionalityDescription = LayoutDefinable.defineTextAreaLayout(
				"Choose variables and mathematical operatores to create a metric for schedule quality "
						+ "calculation.",
				"Arial", Font.PLAIN, 25, Color.darkGray, Color.WHITE);
		variablesForFormula.add(0, "Variable");
		listOfVariables = new JComboBox<>(variablesForFormula.toArray(new String[0]));
		listOfVariables.setPreferredSize(new Dimension(200, 40));
		listOfVariables2 = new JComboBox<>(variablesForFormula.toArray(new String[0]));
		listOfVariables2.addItem("Ignore");
		listOfVariables2.removeItem("Variable");
		listOfVariables2.setPreferredSize(new Dimension(200, 40));
		listOfVariables2.setEnabled(false);
		listOfMatOperators = new JComboBox<>(new String[] { "Operation", "=", "!=", "+", "-", "*", "/", "Ignore" });
		listOfMatOperators.setPreferredSize(new Dimension(200, 40));
		listOfMatOperators.setEnabled(false);
		listOfMatOperators2 = new JComboBox<>(
				new String[] { "Condition Operator (Optional)", ">", "<", "=", "Ignore" });
		listOfMatOperators2.setPreferredSize(new Dimension(200, 40));
		listOfMatOperators2.setEnabled(false);

		// Permite garantir que o utilizador apenas entra um inteiro na ultima coluna
		integerField = new JTextField("Optional Field");
		integerField.setColumns(13);
		integerField.setEnabled(false);

		updateList();

        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.BOTH;
        formulaCreatingPanel.add(pageFunctionalityDescription, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;
        formulaCreatingPanel.add(listOfVariables, gbc);
        gbc.gridx++;
        formulaCreatingPanel.add(listOfMatOperators, gbc);
        gbc.gridx++;
        formulaCreatingPanel.add(listOfVariables2, gbc);
        gbc.gridx++;
        formulaCreatingPanel.add(listOfMatOperators2, gbc);
        gbc.gridx++;
        formulaCreatingPanel.add(integerField, gbc);;
        return formulaCreatingPanel;
    }

	/**
	 * Cria o painel inferior para a criação de fórmulas.
	 *
	 * @param previousFrame JFrame anterior à página de cálculo de qualidade do
	 *                      horário.
	 * @return JPanel representando o painel inferior para a criação de fórmulas.
	 */

	private JPanel bottomFormulaCreatingPanel(JFrame previousFrame) {
		JPanel formulaCreatingPanel = new JPanel(new GridBagLayout());
		formulaCreatingPanel.setBackground(Color.darkGray);
		gbc = resetGBC(gbc);

        JButton removeLastAddedFormula = LayoutDefinable.defineButtonLayout(Color.RED,
                Color.white, "Remove Formula", new Dimension(130, 40));
        removeLastAddedFormula.addActionListener(e -> updateFormulaListPane(-1,  null));
        addFormula = LayoutDefinable.defineButtonLayout(Color.GREEN,
                Color.WHITE, "Add Formula", new Dimension(130, 40));
        addFormula.addActionListener(e -> updateFormulaListPane(1, getVariablesFromFormula()));
        addFormula.setEnabled(false);
        calculateScheduleQuality = LayoutDefinable.defineButtonLayout(Color.BLUE,
                Color.WHITE, "Schedule Quality", new Dimension(130, 40));
        JButton goBackButton = LayoutDefinable.defineButtonLayout(Color.RED,
                Color.white, "Go Back", new Dimension(130, 40));
        goBackButton.addActionListener(e -> {
            previousFrame.setVisible(true);
            getScheduleMetrics();
            updateMetricTable();
            LayoutDefinable.setVisibility(this, false);
        });
        JButton resetListOptions = LayoutDefinable.defineButtonLayout(Color.BLACK,
                Color.WHITE, "Reset Options", new Dimension(130, 40));
        resetListOptions.addActionListener(rLO -> {
            listOfMatOperators2.setEnabled(false);
            listOfMatOperators.setEnabled(false);
            listOfVariables2.setEnabled(false);
            integerField.setEnabled(false);
            listOfVariables2.setModel(new DefaultComboBoxModel<>(variablesForFormula.toArray(new String[0])));
            listOfVariables.setModel(new DefaultComboBoxModel<>(variablesForFormula.toArray(new String[0])));
        });

		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(15, 15, 15, 15);
		formulaCreatingPanel.add(addFormula, gbc);
		gbc.gridx++;
		formulaCreatingPanel.add(removeLastAddedFormula, gbc);
		gbc.gridx++;
		formulaCreatingPanel.add(resetListOptions, gbc);
		gbc.gridx++;
		formulaCreatingPanel.add(calculateScheduleQuality, gbc);
		gbc.gridx++;
		formulaCreatingPanel.add(goBackButton, gbc);
		return formulaCreatingPanel;
	}

	/**
	 * Atualiza o painel de lista de fórmulas com uma operação específica.
	 *
	 * @param operation Operação a ser realizada (1 para adicionar, -1 para
	 *                  remover).
	 * @param formula   Lista de strings representando a fórmula.
	 */

	private void updateFormulaListPane(int operation, List<String> formula) {
		if (formula == null)
			return;
		else {
			StringBuilder formulaEmString = new StringBuilder("Métrica: ");
			StringBuilder metrica = new StringBuilder();
			if (operation == -1) {
				if (!variableListModel.isEmpty())
					variableListModel.remove(variableListModel.size() - 1);
			} else {
				for (int i = 0; i < formula.size(); i++) {
					formulaEmString.append(formula.get(i)).append(" ");
					if (i != (formula.size() - 1)) {
						metrica.append(formula.get(i)).append(";");
					} else {
						metrica.append(formula.get(i));
					}
				}
				metricas.add(metrica.toString());
				variableListModel.addElement(String.valueOf(formulaEmString));
			}
		}
		listOfMatOperators2.setEnabled(false);
		listOfMatOperators.setEnabled(false);
		listOfVariables2.setEnabled(false);
		integerField.setEnabled(false);
		listOfVariables2.setModel(new DefaultComboBoxModel<>(variablesForFormula.toArray(new String[0])));
		listOfVariables.setModel(new DefaultComboBoxModel<>(variablesForFormula.toArray(new String[0])));
	}

	/**
	 * Obtém a lista de variáveis a partir dos campos de entrada.
	 *
	 * @return Lista de strings representando a fórmula.
	 */

	private List<String> getVariablesFromFormula() {
		List<String> formula = new ArrayList<>();
		String variable1 = (String) listOfVariables.getSelectedItem();
		String variable2 = (String) listOfVariables2.getSelectedItem();
		String matOperator1 = (String) listOfMatOperators.getSelectedItem();
		String matOperator2 = (String) listOfMatOperators2.getSelectedItem();
		String input = integerField.getText();

		if (variable1.equals("Variable") || matOperator1.equals("Operation")
				|| matOperator2.equals("Condition Operator (Optional)")) {
			JOptionPane.showMessageDialog(this, "Please select a variable and or a operator", "Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		} else {
			if (matOperator2.equals("Ignore")) {
				formula.add(variable1);
				formula.add(matOperator1);
				formula.add(variable2);
			} else if (matOperator1.equals("Ignore")) {
				formula.add(variable1);
				formula.add(matOperator2);
				formula.add(input);
			} else {
				formula.add(variable1);
				formula.add(matOperator1);
				formula.add(variable2);
				formula.add(matOperator2);
				formula.add(input);
			}
		}

		addFormula.setEnabled(false);
		return formula;
	}

	/**
	 * Obtém o botão de cálculo de qualidade do horário.
	 *
	 * @return O JButton para cálculo de qualidade do horário.
	 */

	public JButton getCalculateScheduleQualityButton() {
		return calculateScheduleQuality;
	}

	/**
	 * Obtém a lista de métricas associadas à qualidade do horário.
	 *
	 * @return Lista de objetos Metrica representando as métricas do horário.
	 */

	public List<Metrica> getScheduleMetrics() {
		List<Metrica> metricasDoUtilizador = new ArrayList<>();
		for (String metrica : new ArrayList<>(new LinkedHashSet<>(metricas))) {
			metricasDoUtilizador.add(new Metrica(metrica));
		}
		return metricasDoUtilizador;
	}

	/**
	 * Este metodo verifica se a variavel selecionada pelo utilizador tem valores
	 * inteiros associados ou não, devolvendo true se tiver e false caso contrário.
	 *
	 * @param metricVariable variavel selecionada pelo utilizador
	 * @return true se a variavel selecionada pelo utilizador tiver valores inteiros
	 *         associados e false caso contrário
	 */
	private boolean hasIntegerValues(String metricVariable) {
		List<List<String>> horarioCompleto = horario.getHorario();
		for (String header : horario.getColumnTitles()) {
			if (metricVariable.equals(header)) {
				try {
					Integer.parseInt(horarioCompleto.get(0).get(horario.getColumnTitles().indexOf(header)));
					return true;
				} catch (NumberFormatException e) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Este metodo atualiza as listas de variaveis e operadores matematicos de
	 * acordo com as variaveis que o utilizador seleciona tendo em conta o tipo de
	 * variavel selecionada na lista. Pois pretende-se que o utilizador apenas possa
	 * fazer operaçõe com variaveis do mesmo tipo.
	 *
	 */

	private void updateList() {
		listOfVariables.addActionListener(lV -> {
			String variable = (String) listOfVariables.getSelectedItem();
			if (!variable.equals("Variable")) {
				setUpNoActionList(listOfVariables2, hasIntegerValues(variable), variable);
				listOfMatOperators.setEnabled(true);
			} else {
				listOfMatOperators.setModel(new DefaultComboBoxModel<>(
						new String[] { "Operation", "=", "!=", "+", "-", "*", "/", "Ignore" }));
				listOfVariables2.setModel(new DefaultComboBoxModel<>(variablesForFormula.toArray(new String[0])));
				listOfVariables2.setEnabled(false);
				listOfMatOperators.setEnabled(false);
				listOfMatOperators2.setEnabled(false);
				integerField.setEnabled(false);
			}
		});

		listOfMatOperators.addActionListener(lM -> {
			String matOperator = (String) listOfMatOperators.getSelectedItem();
			if (matOperator.equals("Ignore")) {
				listOfVariables2.setEnabled(false);
				listOfMatOperators2.setEnabled(true);
			} else if (!matOperator.equals("Operation")) {
				listOfVariables2.setEnabled(true);
			}
		});

		listOfVariables2.addActionListener(lV2 -> {
			String variable = (String) listOfVariables2.getSelectedItem();
			if (!variable.equals("Variable") && !variable.equals("Ignore")) {
				listOfMatOperators2.setEnabled(true);
			}
		});

		listOfMatOperators2.addActionListener(lM2 -> {
			String matOperator = (String) listOfMatOperators2.getSelectedItem();
			if (!matOperator.equals("Ignore") && !matOperator.equals("Condition Operator (Optional)")) {
				integerField.setEnabled(true);
			} else if (matOperator.equals("Ignore")) {
				// This case is when only the 1st 3 are used.
				if (listOfMatOperators.isEnabled() && listOfVariables2.isEnabled()) {
					addFormula.setEnabled(true);
				}
			}
		});

		integerField.addActionListener(iF -> {
			String variableType = (String) listOfVariables.getSelectedItem();
			String input = integerField.getText();
			if (hasIntegerValues(variableType)) {
				try {
					int value = Integer.parseInt(input);
					addFormula.setEnabled(true);
				} catch (NumberFormatException e) {
					addFormula.setEnabled(false);
					JOptionPane.showMessageDialog(this, "Please enter an integer value", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				addFormula.setEnabled(true);
			}
		});

	}

	/**
	 * Configura a lista de variáveis sem acionar eventos.
	 *
	 * @param listVariableToChange  Lista de variáveis a ser alterada.
	 * @param isIntegerTypeVariable Indica se a variável é do tipo inteiro.
	 * @param variableToRemove      Variável a ser removida da lista.
	 */

	private void setUpNoActionList(JComboBox<String> listVariableToChange, boolean isIntegerTypeVariable,
			String variableToRemove) {
		List<String> aux = isIntegerTypeVariable ? getListWithIntegerOnlyVariables(variablesForFormula)
				: getListWithNonIntegerOnlyVariables(variablesForFormula);
		aux.remove(variableToRemove);
		if (isIntegerTypeVariable) {
			listOfMatOperators
					.setModel(new DefaultComboBoxModel<>(new String[] { "Operation", "+", "-", "*", "/", "Ignore" }));
		} else {
			listOfMatOperators
					.setModel(new DefaultComboBoxModel<>(new String[] { "Operation", "-", "=", "!=", "Ignore" }));
		}
		listVariableToChange.setModel(new DefaultComboBoxModel<>(aux.toArray(new String[0])));
	}

	/**
	 * Obtém uma lista apenas com variáveis do tipo inteiro.
	 *
	 * @param variables Lista de variáveis.
	 * @return Lista de variáveis do tipo inteiro.
	 */

	private List<String> getListWithIntegerOnlyVariables(List<String> variables) {
		List<String> integerOnlyVariables = new ArrayList<>();
		for (String variable : variables) {
			if (hasIntegerValues(variable)) {
				integerOnlyVariables.add(variable);
			}
		}
		return integerOnlyVariables;
	}

	/**
	 * Obtém uma lista apenas com variáveis que não são do tipo inteiro.
	 *
	 * @param variables Lista de variáveis.
	 * @return Lista de variáveis que não são do tipo inteiro.
	 */

	private List<String> getListWithNonIntegerOnlyVariables(List<String> variables) {
		List<String> nonIntegerOnlyVariables = new ArrayList<>();
		for (String variable : variables) {
			if (!hasIntegerValues(variable)) {
				nonIntegerOnlyVariables.add(variable);
			}
		}
		return nonIntegerOnlyVariables;
	}

	/**
	 * Atualiza a tabela de métricas.
	 */

	private void updateMetricTable() {
		List<Metrica> metricasDoHorario = new ArrayList<>(horario.getMetricas().keySet());
		for (Metrica metrica : metricasDoHorario) {
			variableListModel.addElement(metrica.getFormula());
		}
	}

}
