package LETI_GrupoF.ProjetoES;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TestHtmlCreator {
	static HtmlCreator horarioMEA1, horarioNaoExistente;
	static String pageFilePath = "Horario.html";

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		horarioNaoExistente = new HtmlCreator("ProjetoEs/HorarioNaoExiste.html");
		horarioMEA1 = new HtmlCreator(pageFilePath);
	}

	@Test
	void testHtmlCreator() {
		assertNotNull(horarioMEA1);
		assertNotNull(horarioNaoExistente);
	}

	@Test
	void testGenerateHtmlPage() {
		assertTrue(horarioMEA1.generateHtmlPage());
		assertFalse(horarioNaoExistente.generateHtmlPage());
	}

}
