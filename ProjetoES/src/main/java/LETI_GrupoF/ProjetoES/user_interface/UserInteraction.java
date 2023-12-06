package LETI_GrupoF.ProjetoES.user_interface;

import LETI_GrupoF.ProjetoES.Horario;
import LETI_GrupoF.ProjetoES.HtmlCreator;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta classe fornece interatividade à página acessada pelo usuário.
 */
public class UserInteraction {

	// Pagina da GUI
	private final SubmitFilePage submitFilePage;
	private HtmlCreator htmlCreator;
	private ScheduleQualityCalculationPage scheduleQualityCalculationPage;
	private ColumnsOrderingPage columnsOrderingPage;
	
	/**
	 *
	 * @param args
	 */
	// Para fazer correr o programa
	public static void main(String[] args) {
		SwingUtilities.invokeLater(UserInteraction::new);
	}

	/**
	 * Construtor para inicializar a pagina GUI.
	 */
	public UserInteraction() {
		submitFilePage = new SubmitFilePage();
		submitFilePage.setVisible(true);
		setUpContinueAndSubmitButton();
	}

	private void setUpColumnsOrderingPageButtons(ColumnsOrderingPage columnsOrderingPage, Horario horario) {
		// Define o comportamento do boão quando o mesmo é clicado
		columnsOrderingPage.getOpenScheduleButton().addActionListener(e -> {
				htmlCreator = new HtmlCreator(horario, columnsOrderingPage.getUserOrderedColumnTitles());
				openSchedule();
		});

		columnsOrderingPage.getScheduleQualityButton().addActionListener(e -> {
			scheduleQualityCalculationPage = new ScheduleQualityCalculationPage(new ArrayList<>(List.of("Inscritos no turno", "Capacidade Normal de sala", "Capacidade de Exame de sala", "Características da sala pedida para a aula"
			, "Sala atribuída à aula")),  columnsOrderingPage);
			scheduleQualityCalculationPage.setVisible(true);
			columnsOrderingPage.setVisible(false);
		});
	}


	/**
	 * Este metodo define o comportamento do botao de continuar e submeter.
	 */

	private void setUpContinueAndSubmitButton () {
		submitFilePage.getContinueButton().addActionListener(e -> {
			// Vai buscar o input do utilizador
			String input = submitFilePage.getCsvFileLocationTextField().getText();

			if (input != null && !input.isEmpty()) {

				// Verifica se o input é um URL ou um caminho para um ficheiro
				if (input.matches("^https?://.*")) {
					submitFilePage.setRemoteFile(true);
					try {
						URL remoteFile = new URL(input);
						if (saveToLocalFile(remoteFile.openStream(), "HorarioRemoto.csv")) {
							Horario horario = new Horario("HorarioRemoto.csv", get);
							columnsOrderingPage = new ColumnsOrderingPage(horario.getColumnTitles(), submitFilePage);
							setUpColumnsOrderingPageButtons(columnsOrderingPage, horario);
							columnsOrderingPage.setVisible(true);
							submitFilePage.setVisible(false);
						} else {
							JOptionPane.showMessageDialog(submitFilePage, "Error processing remote file, please try again",
									"Error", JOptionPane.ERROR_MESSAGE);
						}
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
				}
				// Neste caso o input é um caminho para um ficheiro local
				else {
					File file = new File(input);
					if (file.exists()) {
						// Se existir usa o csv para gerar os dados para a pagina HTML e depois abre a
						// pagina
						Horario horario = new Horario(input, );
						columnsOrderingPage = new ColumnsOrderingPage(horario.getColumnTitles(), submitFilePage);
						setUpColumnsOrderingPageButtons(columnsOrderingPage, horario);
						columnsOrderingPage.setVisible(true);
						submitFilePage.setVisible(false);
					} else {
						// No caso de não existir o ficheiro aparece uma mensagem de erro
						JOptionPane.showMessageDialog(submitFilePage, "File does not exist: " + file, "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				// No caso de não existir o ficheiro aparece uma mensagem de erro
				JOptionPane.showMessageDialog(submitFilePage, "Invalid file path.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	/**
	 * Este metodo retorna os titulos das colunas da tabela
	 * ordenados pelo usuario na GUI.
	 */

//	public List<String> getUserOrderedColumnTitles() {
//		return submitFilePage.getUserOrderedColumnTitles();
//	}

	public HtmlCreator getHtmlCreator() {
		return htmlCreator;
	}
	/**
	 * Este metodo salva o arquivo remoto em um arquivo local.
	 */
	private boolean saveToLocalFile(InputStream inputStream, String localFilePath) {
		try {
			File localFile = new File(localFilePath);
			localFile.createNewFile();

			FileOutputStream outputStream = new FileOutputStream(localFile);
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			inputStream.close();
			outputStream.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Este metodo abre a pagina HTML com a tabela no navegador e verifica se a pagina HTML foi gerada com sucesso.
	 */
	private void openSchedule() {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			File file = new File(htmlCreator.getHtmlPath());
			if (file.exists()) {
				try {
					if (htmlCreator.generateHtmlPage()) {
						System.out.println("HTML page generated successfully");
						desktop.browse(file.toURI());
					} else {
						System.out.println("Error generating HTML page");
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				// No caso de não existir o ficheiro aparece uma mensagem de erro
				JOptionPane.showMessageDialog(submitFilePage, "File does not exist: " + file, "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(submitFilePage, "Desktop is not supported on this platform", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
