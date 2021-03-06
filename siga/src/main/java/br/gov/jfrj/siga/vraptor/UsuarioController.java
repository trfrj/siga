package br.gov.jfrj.siga.vraptor;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.integracao.ldap.IntegracaoLdap;

@Resource
public class UsuarioController extends SigaController {

	private static final Logger LOG = Logger.getLogger(UsuarioAction.class);

	public UsuarioController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);

		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
	}
	
	@Get("/app/usuario/trocar_senha")
	public void trocaSenha() {
		result.include("baseTeste", Boolean.valueOf(System.getProperty("isBaseTest").trim()));
	}

	@Post("/app/usuario/trocar_senha_gravar")
	public void gravarTrocaSenha(UsuarioAction usuario) throws Exception {
		String msgAD = "";
		String senhaAtual = usuario.getSenhaAtual();
		String senhaNova = usuario.getSenhaNova();
		String senhaConfirma = usuario.getSenhaConfirma();
		String nomeUsuario = usuario.getNomeUsuario().toUpperCase();
		
		CpIdentidade idNova = Cp.getInstance().getBL().trocarSenhaDeIdentidade(
				senhaAtual, senhaNova, senhaConfirma, nomeUsuario,
				getIdentidadeCadastrante());
		
		boolean senhaTrocadaAD = false;
		
		if ("on".equals(usuario.getTrocarSenhaRede())) {
			senhaTrocadaAD = IntegracaoLdap.getInstancia().atualizarSenhaLdap(idNova,senhaNova);	
		}

		if (isIntegradoAD(nomeUsuario) && senhaTrocadaAD){
			msgAD = "<br/><br/><br/>OBS: A senha de rede e e-mail tamb�m foi alterada.";
		}
		
		if (isIntegradoAD(nomeUsuario) && !senhaTrocadaAD){
			msgAD = "<br/><br/><br/>ATEN��O: A senha de rede e e-mail N�O foi alterada embora o seu �rg�o esteja configurado para integrar as senhas do SIGA, rede e e-mail.";
		}
		
		result.include("mensagem", "A senha foi alterada com sucesso" + msgAD);	
		result.include("volta", "troca");
		result.include("titulo", "Troca de Senha");
		result.redirectTo("/app/principal");
	}

	@Get("/app/usuario/incluir_usuario")
	public void incluirUsuario() {
		result.include("baseTeste", Boolean.valueOf(System.getProperty("isBaseTest").trim()));
		result.include("titulo", "Novo Usu�rio");
		result.include("proxima_acao", "incluir_usuario_gravar");
		result.forwardTo("/WEB-INF/page/usuario/esqueciSenha.jsp");
		
	}
	
	@Post("/app/usuario/incluir_usuario_gravar")
	public void gravarIncluirUsuario(UsuarioAction usuario) throws Exception {
		String msgComplemento = "";
		String[] senhaGerada = new String[1];
		boolean senhaTrocadaAD = false;
		boolean isIntegradoAoAD = isIntegradoAD(usuario.getMatricula());
		CpIdentidade idNova = null;
		switch (usuario.getMetodo()) {
		case 1:
			
			idNova = Cp.getInstance().getBL().criarIdentidade(usuario.getMatricula(), usuario.getCpf(),
					getIdentidadeCadastrante(), usuario.getSenhaNova(), senhaGerada, isIntegradoAoAD);
			if (isIntegradoAoAD){
				senhaTrocadaAD = IntegracaoLdap.getInstancia().atualizarSenhaLdap(idNova,usuario.getSenhaNova());
			}
			break;
		case 2:
			if (!Cp.getInstance().getBL().podeAlterarSenha(usuario.getAuxiliar1(), usuario.getCpf1(), usuario.getSenha1(),
					usuario.getAuxiliar2(), usuario.getCpf2(), usuario.getSenha2(), usuario.getMatricula(), usuario.getCpf(),
					usuario.getSenhaNova())){
				String mensagem = "N�o foi poss�vel alterar a senha!<br/>" +
								  "1) As pessoas informadas n�o podem ser as mesmas;<br/>" +
								  "2) Verifique se as matr�culas e senhas foram informadas corretamente;<br/>" +
								  "3) Verifique se as pessoas s�o da mesma lota��o ou da lota��o imediatamente superior em rela��o � matr�cula que ter� a senha alterada;<br/>";
				result.include("mensagem", mensagem);
				result.redirectTo("/app/usuario/incluir_usuario");
			}else{
				idNova = Cp.getInstance().getBL().criarIdentidade(usuario.getMatricula(), usuario.getCpf(),
						getIdentidadeCadastrante(), usuario.getSenhaNova(), senhaGerada,
						isIntegradoAoAD);
			}
			break;
		default:
			result.include("mensagem", "M�todo inv�lido!");
			result.redirectTo("/app/usuario/incluir_usuario");
		}
		
		if (isIntegradoAoAD){
			
			if (senhaTrocadaAD){
				msgComplemento = "<br/> Aten��o: Sua senha de rede e e-mail foi definida com sucesso.";
			}else{
				msgComplemento = "<br/> ATEN��O: A senha de rede e e-mail N�O foi definida embora o seu �rg�o esteja configurado para integrar as senhas do SIGA, rede e e-mail.";
			}

			
		}else{
			msgComplemento = "<br/> O seu login e senha foram enviados para seu email.";
		}

		result.include("mensagem", "Usu�rio cadastrado com sucesso." + msgComplemento);
		result.include("titulo", "Novo Usu�rio");
		result.include("volta", "incluir");
		result.redirectTo("/app/usuario/incluir_usuario");
	}
	
	@Get("/app/usuario/esqueci_senha")
	public void esqueciSenha() {
		result.include("baseTeste", Boolean.valueOf(System.getProperty("isBaseTest").trim()));
		result.include("titulo", "Esqueci Minha Senha");
		result.include("proxima_acao", "esqueci_senha_gravar");
	}
	
	@Post("/app/usuario/esqueci_senha_gravar")
	public void gravarEsqueciSenha(UsuarioAction usuario) throws Exception {
		String msgAD = "";
		boolean senhaTrocadaAD = false;
		
		switch (usuario.getMetodo()) {
		case 1:
//			verificarMetodoIntegracaoAD(usuario.getMatricula());
			String[] senhaGerada = new String[1];
			Cp.getInstance().getBL().alterarSenhaDeIdentidade(usuario.getMatricula(),
					usuario.getCpf(), getIdentidadeCadastrante(),senhaGerada);
			break;
		case 2:
			if (!Cp.getInstance().getBL().podeAlterarSenha(usuario.getAuxiliar1(), usuario.getCpf1(),
					usuario.getSenha1(), usuario.getAuxiliar2(), usuario.getCpf2(), usuario.getSenha2(),
					usuario.getMatricula(), usuario.getCpf(), usuario.getSenhaNova())){
				String mensagem = "N�o foi poss�vel alterar a senha!<br/>" +
						"1) As pessoas informadas n�o podem ser as mesmas;<br/>" +
						"2) Verifique se as matr�culas e senhas foram informadas corretamente;<br/>" +
						"3) Verifique se as pessoas s�o da mesma lota��o ou da lota��o imediatamente superior em rela��o � matr�cula que ter� a senha alterada;<br/>";
				result.include("mensagem", mensagem);
				result.redirectTo("/app/usuario/esqueci_senha");
				return;
			}
		
			CpIdentidade idAux1 = dao.consultaIdentidadeCadastrante(usuario.getAuxiliar1(), true);
			Cp.getInstance().getBL().definirSenhaDeIdentidade(usuario.getSenhaNova(), usuario.getSenhaConfirma(),
							usuario.getMatricula(), usuario.getAuxiliar1(), usuario.getAuxiliar2(), idAux1);
//			senhaTrocadaAD = IntegracaoLdap.getInstancia().atualizarSenhaLdap(idNovaDefinida,senhaNova);
			break;

		default:
			result.include("mensagem", "M�todo inv�lido!");
			result.redirectTo("/app/esqueci_senha");
			return;
		}

		if (isIntegradoAD(usuario.getMatricula()) && senhaTrocadaAD){
			msgAD = "<br/><br/><br/>OBS: A senha de rede e e-mail tamb�m foi alterada.";
		}
		
		if (isIntegradoAD(usuario.getMatricula()) && !senhaTrocadaAD){
			msgAD = "<br/><br/><br/>ATEN��O: A senha de rede e e-mail N�O foi alterada embora o seu �rg�o esteja configurado para integrar as senhas do SIGA, rede e e-mail.";
		}
		
		result.include("mensagem", "A Senha foi alterada com sucesso e foi enviada para seu email" + msgAD);
		result.include("volta", "esqueci");
		result.include("titulo", "Esqueci Minha Senha");
		result.redirectTo("/app/esqueci_senha");
	}

	
	@Get("/app/usuario/integracao_ldap")
	public void isIntegradoLdap(String matricula) throws AplicacaoException {
		try{
			String retorno = isIntegradoAD(matricula) ? "" : "0";
			result.use(Results.http()).body(retorno);
		}catch(Exception e){
			result.use(Results.http()).body(e.getMessage());
		}
	}

	private boolean isIntegradoAD(String matricula) throws AplicacaoException {
		boolean result = false;
		CpOrgaoUsuario orgaoFlt = new CpOrgaoUsuario();
		
		if (matricula == null || matricula.length() < 2) {
			LOG.warn( "A matr�cula informada � nula ou inv�lida" );
			throw new AplicacaoException( "A matr�cula informada � nula ou inv�lida." );
		}
		
		orgaoFlt.setSiglaOrgaoUsu(matricula.substring(0, 2));		
		CpOrgaoUsuario orgaoUsu = dao.consultarPorSigla(orgaoFlt);
		
		if (orgaoUsu != null) {
			result = IntegracaoLdap.getInstancia().integrarComLdap(orgaoUsu);
		}
		
		return result;
	}
	
	@Get("/app/usuario/check_email_valido")
	public void checkEmailValido(String matricula) throws AplicacaoException {
		try{
			if(isEmailValido(matricula)) {
				result.use(Results.page()).forwardTo("/sigalibs/ajax_vazio.jsp");
				return;
			}
			result.use(Results.page()).forwardTo("/sigalibs/ajax_retorno.jsp");
			
		}catch(Exception e){
			result.include("ajaxMsgErro", e.getMessage());
			result.use(Results.page()).forwardTo("/sigalibs/ajax_msg_erro.jsp");
		}
	}
	
	private boolean isEmailValido(String matricula) {
		
		CpOrgaoUsuario orgaoFlt = new CpOrgaoUsuario();
		
		if ( matricula == null || matricula.length() < 2 ) {
			LOG.warn( "A matr�cula informada � nula ou inv�lida" );
			throw new AplicacaoException( "A matr�cula informada � nula ou inv�lida." );
		}
		
		orgaoFlt.setSiglaOrgaoUsu(matricula.substring(0, 2));		
		CpOrgaoUsuario orgaoUsu = dao.consultarPorSigla(orgaoFlt);
		
		if (orgaoUsu == null){
			throw new AplicacaoException("O �rg�o informado � nulo ou inv�lido." );
		}

		List<DpPessoa> lstPessoa = null;
		try{
			lstPessoa = dao.consultarPorMatriculaEOrgao(Long.valueOf(matricula.substring(2)), orgaoUsu.getId(), false, false);
		}catch(Exception e){
			throw new AplicacaoException("Formato de matr�cula inv�lida." );
		}

		if (lstPessoa.size() == 0){
			throw new AplicacaoException("O usu�rio n�o est� cadastrado no banco de dados." );
		}
		
		if (lstPessoa != null && lstPessoa.size() == 1) {
			DpPessoa p = lstPessoa.get(0);
			if (p.getEmailPessoaAtual() != null && p.getEmailPessoaAtual().trim().length() > 0){
				return true;
			}else{
				throw new AplicacaoException("Voc� ainda n�o possui um e-mail v�lido. Tente mais tarde." );
			}
		}
		
		return false;
	}
	
}
