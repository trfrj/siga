/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

/*
 * Criado em  23/11/2005
 *
 */
package br.gov.jfrj.siga.vraptor;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpModelo;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.ModeloDao;

@Resource
public class ModeloController extends SigaController {

	public ModeloController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);

		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
	}

	public CpModelo daoMod(long id) {
		return dao().consultar(id, CpModelo.class, false);
	}

	@Get("/app/modelo/listar")
	public void lista() throws Exception {
		assertAcesso("FE:Ferramentas;MODVER:Visualizar modelos");
		result.include("itens", dao().consultaCpModelos());
	}

	@Post("/app/modelo/gravar")
	public void gravar(Integer id, String conteudo) throws Exception {
		assertAcesso("FE:Ferramentas;MODEDITAR:Editar modelos");

		if (id != null) {
			CpModelo mod = daoMod(id);
			Cp.getInstance().getBL().alterarCpModelo(mod, conteudo,
					getIdentidadeCadastrante());
		} else {
			try {
				ModeloDao.iniciarTransacao();
				CpModelo mod = new CpModelo();
				mod.setConteudoBlobString(conteudo);
				if (paramLong("idOrgUsu") != null)
					mod.setCpOrgaoUsuario(dao().consultar(paramLong("idOrgUsu"),
							CpOrgaoUsuario.class, false));
				mod.setHisDtIni(dao().consultarDataEHoraDoServidor());
				dao().gravarComHistorico(mod, getIdentidadeCadastrante());
				ModeloDao.commitTransacao();
			} catch (Exception e) {
				ModeloDao.rollbackTransacao();
				throw new AplicacaoException(
						"N�o foi poss�vel gravar o modelo.", 9, e);
			}
		}
		
		result.redirectTo(this).lista();
	}
}