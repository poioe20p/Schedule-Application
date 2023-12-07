package LETI_GrupoF.ProjetoES;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveState {
	private String SaveStateFilePath = "SaveState.txt";

	/**
	 * Este método tem como finalidade armazenar um horário, que possibilita ao
	 * utilizador a sua reutilização caso este queira retomar a sua sessão anterior.
	 *
	 * @param csvFilePath Indica qual o horario usado pelo utilizador na sessao
	 * @param ordemCampos Um mapa que define a ordem e a posicao dos campos no
	 *                    horario. Cada chave representa um campo e o valor
	 *                    associado indica a posicao do mesmo.
	 */

	void guardarHorario(String csvFilePath, Map<String, Integer> ordemCampos) {

		List<Map.Entry<String, Integer>> entryList = new ArrayList<>(ordemCampos.entrySet());
		entryList.sort(Comparator.comparing(Map.Entry::getValue));
		try {
			FileWriter file = new FileWriter(SaveStateFilePath);
			PrintWriter writer = new PrintWriter(file);

			writer.println(csvFilePath);

			for (Map.Entry<String, Integer> entry : ordemCampos.entrySet()) {
				writer.println(entry.getKey() + ":" + entry.getValue());
			}
			writer.println("FOC");
			writer.close();

		} catch (IOException e) {
			System.err.println("An error occurred while writing to the file: " + e.getMessage());
		}

	}

	/**
	 * Este metodo tem como objetivo armazenar as metricas, preservando as formulas
	 * personalizadas introduzidas pelo utilizador. Permitindo assim restaurar o
	 * estado anterior das formulas caso o utilizador pretenda recuperar a sessao
	 * anterior
	 *
//	 * @param formula A formula criada pelo utilizador que sera guardada. Permite
	 *                recuperar a formula caso o utilizador retome a sessão.
	 */

	void guardarMetricas(Metrica metrica) {

		try {
			FileWriter file = new FileWriter(SaveStateFilePath);
			PrintWriter writer = new PrintWriter(file);

			writer.println(metrica.getFormula());
			writer.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Recupera o horario anterior, permitindo que o utilizador retome a sua sessao
	 * anterior.
	 *
	 * @return Um objeto do tipo Horario representando o horario previamente
	 *         armazenado. Retorna null se não houver um horario anteriormente
	 *         armazenado.
	 */

	Horario RecuperarHorarioAntigo() {

		String FilePath = null;
		Map<String, Integer> oCampos = new HashMap<>();
		List<Metrica> metricas = new ArrayList<>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(SaveStateFilePath));

			FilePath = reader.readLine();

			String linha = reader.readLine();
			while (!linha.trim().equals("FOC")) {
				String[] partes = linha.split(":");
				oCampos.put(partes[0], Integer.parseInt(partes[1]));
			}

			while (linha != null) {
				Metrica novaMetrica = new Metrica(linha.trim());
				metricas.add(novaMetrica);
			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();

		}

		Horario horarioRecuperado = new Horario(FilePath);
		horarioRecuperado.setOrdemCampos(oCampos);

		for (int i = 0; i != metricas.size(); i++) {
			horarioRecuperado.adicionarMetrica(metricas.get(i));
		}

		return horarioRecuperado;

	}

	/**
	 * Limpa o conteudo do arquivo SaveState.txt, removendo todas as informacoes
	 * previamente armazenadas. Esta funcao e utilizada quando o utilizador opta por
	 * nao retomar a sessao anterior.
	 *
	 */
	void LimparSaveState() {
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(SaveStateFilePath, false);
			fileWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}