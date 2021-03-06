package br.gov.jfrj.siga.integration.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.gov.jfrj.siga.page.objects.EstatisticaProcedimentoPage;
import br.gov.jfrj.siga.page.objects.OperacoesDocumentoPage;
import br.gov.jfrj.siga.page.objects.PesquisaDocumentoPage;
import br.gov.jfrj.siga.page.objects.PrincipalPage;
import br.gov.jfrj.siga.page.objects.SolicitacaoEletronicaContratacaoPage;
import br.gov.jfrj.siga.page.objects.TarefaPage;

public class WorkflowDigitalIT extends IntegrationTestBase {
	private String codigoDocumento;
	private TarefaPage tarefaPage;
	private String inicioTarefa; 
	private String descricaoTarefa;
	private String url;
	
	public WorkflowDigitalIT() throws FileNotFoundException, IOException {
		super();
	}
	
	@BeforeClass(enabled = true)
	public void login() {
		efetuaLogin();		
		tarefaPage = PageFactory.initElements(driver, TarefaPage.class);
		operacoesDocumentoPage = PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	@BeforeClass(enabled = true)
	public void solicitacaoEletronicaContratacao() {
		try {
			// No SIGA-DOC, criar documento: 
			PrincipalPage principalPage = PageFactory.initElements(driver, PrincipalPage.class);
			principalPage.clicarBotaoNovoDocumentoEx();
			
			// Solicita��o Eletr�nica de Contrata��o - Digital - Selecionar Tipo "Solicita��o Eletr�nica de Contrata��o" 
			// Preencher campos que ainda estiverem vazios - Clicar OK
			SolicitacaoEletronicaContratacaoPage secPage = PageFactory.initElements(driver, SolicitacaoEletronicaContratacaoPage.class);
			secPage.criaSolicitacaoEletronicaContratacao(propDocumentos);
			
			// Clicar em Finalizar
			operacoesDocumentoPage.clicarLinkFinalizar();
			
			// Assinar Digitalmente (simular assinatura)
			codigoDocumento = operacoesDocumentoPage.getTextoVisualizacaoDocumento("/html/body/div[4]/div/h2");
			assinarDigitalmente(codigoDocumento, "N� " + codigoDocumento);
			
			codigoDocumento = operacoesDocumentoPage.getTextoVisualizacaoDocumento("/html/body/div[4]/div/h2");
			
			// Garantir que "Tarefa: Anexar Arquivos" apare�a na tela
			WebElement tarefaElement = util.getWebElement(driver, By.xpath("//td[contains(., 'Tarefa: Anexar Arquivos')]"));
			if(tarefaElement == null) {
				throw new SkipException("Tarefa n�o encontrada!");
			}
			
			// Garantir que o link "Prosseguir" apare�a na tela
			WebElement linkProsseguir = util.getWebElement(driver, By.xpath("//a[contains(text(), 'Prosseguir')]"));		
			if(linkProsseguir == null) {
				throw new SkipException("Link Prosseguir n�o encontrado!");
			}
			
			// Armazenando nome da tarefa
			String tarefa = tarefaElement.getText();
			descricaoTarefa = tarefa.substring(tarefa.indexOf("Tarefa: "), tarefa.indexOf(" sendo")).substring(8);
			
			// Armazenando in�cio da tarefa
			String procedimento = util.getWebElement(driver, By.xpath("//td[contains(.,'Procedimento:')]")).getText();
			inicioTarefa = procedimento.substring(procedimento.indexOf("(") + 1, procedimento.indexOf(")"));				
		} catch(Exception e) {
			e.printStackTrace();
			throw new SkipException("Exce��o no m�todo setUp!");
		}
	}
	
	@BeforeMethod(enabled = true)
	public void paginaInicial(Method method) {

			System.out.println("BeforeMethod: " + method.getName() + " - Titulo p�gina: " + driver.getTitle());
			
			if(!method.getName().equals("pagamento") && !method.getName().equals("relatorioContratacao")) {							
				if(!driver.getCurrentUrl().contains("sigawf")) {
					// Ir para a p�gina inicial atrav�s do menu SIGA > P�gina Inicial
					util.getWebElement(driver, By.linkText("SIGA")).sendKeys(Keys.ENTER);
					util.getWebElement(driver, By.linkText("P�gina Inicial")).sendKeys(Keys.ENTER);
					util.getWebElement(driver, By.cssSelector("a.gt-btn-small.gt-btn-right"));	
					
					WebElement linkTarefa = util.getWebElement(driver, By.xpath("//div[h2 = 'Tarefas']//tbody/tr[td[1]/a[text() = '" + descricaoTarefa + "'] "
							+ "and td[last()][contains(., '" + inicioTarefa +"')]]/td/a"));
					linkTarefa.click();			
				}
			} else {			
				if(method.getName().equals("pagamento")) {
					try {
						geraProcesso();
					} catch(Exception e) {
						e.printStackTrace();
						throw new SkipException("Exce��o no m�todo geraProcesso!");
					}
				}
				
				url = driver.getCurrentUrl();
				System.out.println("Url: " + url);
				// Ir para a m�dulo workflow atrav�s do menu SIGA > M�dulos > Workflow
				util.getWebElement(driver, By.linkText("SIGA")).sendKeys(Keys.ENTER);
				util.getWebElement(driver, By.linkText("M�dulos")).sendKeys(Keys.ENTER);
				util.getWebElement(driver, By.linkText("Workflow")).sendKeys(Keys.ENTER);
				util.getWebElement(driver, By.xpath("//h3[ text() = 'Quadro de tarefas']"));				
			}
	}
		
	@Test(enabled = true, priority = 1)
	public void comentario() {
		tarefaPage.adicionarComentario(propDocumentos);
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//td[text() = '" + propDocumentos.getProperty("comentario") +"']")), 
				"Texto '" + propDocumentos.getProperty("comentario") + "' n�o encontrado!");
	}
	
	@Test(enabled = true, priority = 1)
	public void designacaoTarefa() {
		// Deletar a pessoa e deixar somente a lota��o - Alterar a prioridade para Alta - Clicar em Designar
		tarefaPage.designarTarefaLotacao(propDocumentos);	
		
		// Garantir que o atendente da tarefa seja a lota��o designada 
		WebElement tarefa = util.getWebElement(driver, By.xpath("//div[h2 = 'Tarefas']//tbody/tr[td[1]/a[text() = 'Anexar Arquivos'] and td[last()][contains(., '" + inicioTarefa +"')]]"));
		List<WebElement> colunas = tarefa.findElements(By.tagName("td"));
		
		Assert.assertTrue(colunas.get(1).getText().equals(propDocumentos.getProperty("lotacao")));
		
		// E a prioridade seja alta no quadro de tarefas
		Assert.assertTrue(colunas.get(2).getText().equals("Alta"));
		
		// Clicar no link "Anexar Arquivos"
		util.getWebElement(driver, colunas.get(0), By.linkText("Anexar Arquivos")).click();
		
		// Clicar em Pegar tarefa para mim
		tarefaPage.pegarTarefaParaMim();
		
		// Garantir que o nome da pessoa apare�a na tela
		Assert.assertNotNull(util.getWebElement(driver, By.id("atorSelSpan")), "Nome da pessoa n�o encontrado!");
		// Garantir que o bot�o "Pegar tarefa para mim" n�o apare�a na tela 
		Assert.assertTrue(util.isElementInvisible(driver, By.xpath("//input[@value='Pegar tarefa para mim']")), "Bot�o 'Pegar tarefa para mim' ainda est� sendo exibido!");
	}
	
	@Test(enabled = true, priority = 2)
	public void execucaoTarefaSigaDoc() {	
		util.getClickableElement(driver, By.partialLinkText(codigoDocumento)).click();
		
		// Clicar em Prosseguir
		util.getWebElement(driver, By.partialLinkText("Prosseguir �")).click();
		
		// Garantir que "Tarefa: Verificar programa��o anual" apare�a na tela
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//td[contains(.,'Tarefa: Verificar programa��o anual')]")),
				"'Tarefa: Verificar programa��o anual' n�o encontrada!");
		
		// Garantir que os bot�es "Sim" e "N�o" apare�am na tela
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//a[contains(text(), 'Sim �')]")), "Link 'Sim' n�o encontrado!");
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//a[contains(text(), 'N�o �')]")), "Link 'N�o' n�o encontrado!");
		
		// Clicar no link do documento
		WebElement procedimento = util.getWebElement(driver, By.xpath("//td[contains(.,'Procedimento: Contrata��o: fase de an�lise')]"));
		inicioTarefa = procedimento.getText().substring(procedimento.getText().indexOf("(") + 1, procedimento.getText().indexOf(")"));
		descricaoTarefa = "Verificar programa��o anual";		
	}
	
	@Test(enabled = true, priority = 3)
	public void execucaoTarefaWorkflow() {		
		// Clicar em Sim
		util.getClickableElement(driver, By.xpath("//input[contains(@value, 'Sim')]")).click();
		
		// Garantir que "Tarefa: Realizar cota��o" apare�a na tela
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//div[h3 = 'Dados da Tarefa']//p[contains(.,'Tarefa: Realizar cota��o')]")),
				"'Tarefa: Realizar cota��o' n�o encontrada!");
		
		// Garantir que o link "Retificar SEC" e "Prosseguir" apare�am na tela		
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//input[contains(@value, 'Retificar SEC')]")), "Bot�o 'Retificar SEC' n�o encontrado!");
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//input[contains(@value, 'Prosseguir')]")), "Bot�o 'Prosseguir' n�o encontrado!");
		
		// Armazenando dados do in�cio da tarefa e descri��o da tarefa
		String inicio = util.getWebElement(driver, By.xpath("//div[h3 = 'Dados da Tarefa']//p[contains(.,'In�cio:')]")).getText();
		inicioTarefa = inicio.substring(inicio.indexOf(":") + 1, inicio.length()).trim();
		descricaoTarefa = "Realizar cota��o";
	}
		
	public void geraProcesso() {
		// Ir no menu Documento > Pesquisar
		util.getWebElement(driver, By.linkText("SIGA")).sendKeys(Keys.ENTER);
		util.getWebElement(driver, By.linkText("M�dulos")).sendKeys(Keys.ENTER);
		util.getWebElement(driver, By.linkText("Documentos")).sendKeys(Keys.ENTER);
		
		util.trocaURL(url);
		url = driver.getCurrentUrl();
		System.out.println("Url: " + url);
		
		// Situa��o: aguardando andamento - Tipo: Portaria - Clicar em "buscar"
		PesquisaDocumentoPage pesquisaDocumentoPage = PageFactory.initElements(driver, PesquisaDocumentoPage.class);
		pesquisaDocumentoPage.buscaPortaria();
		
		// Clicar em "autuar"	
		// Preencher subscritor - Alterar Modelo para: "Contrato com Exclusividade" - Clicar em Ok
		super.autuar(Boolean.TRUE, "Contrato com Exclusividade");
		
		// Finalizar e assinar Processo criado
		operacoesDocumentoPage.clicarLinkFinalizar();
		// Assinar Digitalmente (simular assinatura)
		codigoDocumento = operacoesDocumentoPage.getTextoVisualizacaoDocumento("/html/body/div[4]/div/h2");
		assinarDigitalmente(codigoDocumento, propDocumentos.getProperty("descricao"));
	}	
	
	@Test(enabled = true, priority = 4)
	public void pagamento() {		
		util.trocaURL(url);
		url = driver.getCurrentUrl();
		System.out.println("Url: " + url);
		
		// Ir no menu Procedimentos > Iniciar > Pagamento
		util.getWebElement(driver, By.linkText("Procedimentos")).sendKeys(Keys.ENTER);
		util.getWebElement(driver, By.linkText("Iniciar")).sendKeys(Keys.ENTER);
		util.getWebElement(driver, By.linkText("Pagamento")).sendKeys(Keys.ENTER);
		
		util.trocaURL(url);
		url = driver.getCurrentUrl();
		System.out.println("Url: " + url);
		
		// Colar a sigla copiada no campo de Execu��o da Tarefa
		// Clicar em prosseguir
		tarefaPage.prosseguirPagamento(codigoDocumento);
		
		// Garantir que "Tarefa: � registro de pre�os - pagamento" apare�a na tela
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//div[h3 = 'Dados da Tarefa']//p[contains(.,'Tarefa: � registro de pre�os - pagamento')]")),
				"'Tarefa:  � registro de pre�os - pagamento' n�o encontrada!");
		
		// Garantir que os bot�es "Sim" e "N�o" apare�am na tela
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//input[contains(@value, 'Sim') and @type = 'submit']")), "Bot�o 'Sim' n�o encontrado!");
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//input[contains(@value, 'Nao') and @type = 'submit']")), "Bot�o 'N�o' n�o encontrado!");
		
		// Clicar no link da sigla do Processo
		util.getClickableElement(driver, By.partialLinkText(codigoDocumento)).click();
		
		// Garantir que "Tarefa: � registro de pre�os - pagamento" apare�a na tela
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//td[contains(.,'Tarefa: � registro de pre�os - pagamento')]")),
				"'Tarefa: � registro de pre�os - pagamento' n�o encontrada!");
		
		// Garantir que os bot�es "Sim" e "N�o" apare�am na tela
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//a[contains(text(), 'Sim �')]")), "Link 'Sim' n�o encontrado!");
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//a[contains(text(), 'Nao �')]")), "Link 'N�o' n�o encontrado!");
	}	
	
	@Test(enabled = true, priority = 5)
	public void relatorioContratacao() {
		util.trocaURL(url);
		url = driver.getCurrentUrl();
		System.out.println("Url: " + url);
		
		// Ir no menu Relat�rios > Apresentar M�tricas > Contrata��o: fase de an�lise
		util.getWebElement(driver, By.linkText("Relat�rios")).sendKeys(Keys.ENTER);
		util.getWebElement(driver, By.linkText("Apresentar M�tricas")).sendKeys(Keys.ENTER);
		util.getWebElement(driver, By.linkText("Contrata��o: fase de an�lise")).sendKeys(Keys.ENTER);
		
		util.trocaURL(url);
		url = driver.getCurrentUrl();
		System.out.println("Url: " + url);
		
		// Alterar o relat�rio para "Tempo de documentos" - Inserir datas nos campos "Procedimento iniciado" e "Procedimento finalizado"
		// Clicar em Gerar relat�rio
		EstatisticaProcedimentoPage ePage = PageFactory.initElements(driver, EstatisticaProcedimentoPage.class);
		ePage.gerarRelatorioContratacao(propDocumentos.getProperty("dataInicialRelatorio"), propDocumentos.getProperty("dataFinalRelatorio"));
		
		util.trocaURL(url);
		url = driver.getCurrentUrl();
		System.out.println("Url: " + url);
		// Garantir que n�o retorne um erro 500
		util.isPDF(driver);		
	}
}