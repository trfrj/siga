<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Informações">

<c:set var="count" value="${0}"/>
<div class="gt-bd clearfix">
	<div class="gt-content clearfix">
		<h2>Pesquisa de Conhecimentos</h2>
		<c:choose>
		<c:when test="${lista.size() != 0}"> 
		<div class="gt-content-box gt-for-table">
			<table class="gt-table">
				<thead>
					<tr>
						<th style="width:12% !important;" rowspan="2">Número</th>
						<th colspan="3">Conhecimento</th>
						<th colspan="1">Situação</th>
					</tr>
					<tr>
						<th rowspan="2" style="width:10%!important;">Data Criação</th>
						<th colspan="2" style="padding:0;">
							<table>
								<thead>
									<tr><th style="text-align: center !important;" colspan="2">Autor</th></tr>
									<tr>
										<th style="border:none;width:50%!important;">Lotação</th>
										<th style="border:none;width:50%!important;">Pessoa</th>
									</tr>
								</thead>
							</table>
						</th>
						<th style="padding:0;">
							<table>
								<thead>
									<tr>
										<th width="30%" rowspan="3" style="border:none;">Data</th>
										<th style="text-align: center !important;" width="45%" colspan="2">Responsável</th>
										<th width="25%" rowspan="3" style="border:none;">Situação</th>
									</tr>
									<tr>
										<th style="border:none;">Lotação</th>
										<th style="border:none;">Pessoa</th>
									</tr>
								</thead>
							</table>
						</th>
						<th rowspan="2">Tipo</th>
						<th rowspan="2">Título</th>
						<th rowspan="2"></th>
					</tr>
				</thead>
				<tbody>
					<c:set var="k" value="${1}"/>
					<c:forEach items="${lista}" var="i">
					<tr class="even">
						<td style="width:12% !important;"><a href="${linkTo[AppController].exibir[i.siglaCompacta]}">${i.sigla}</a></td>
						<td style="width:10%!important;">${i.dtIniString}</td>
						<td style="width:6%!important;"><span
							title="${i.lotacao.descricao}">${i.lotacao.sigla}</span>
						</td>
						<td style="width:6%!important;"><span title="${i.autor.descricao} - ${i.autor.sigla}">${i.autor.nomeAbreviado}</span>
						</td>
						<td style="padding:0; width:30%!important;">
							<table class="gt-table-lista">
								<c:set var="cont" value="${0}"/>
								<c:forEach items="${marcas}" var="m">
								<c:if test="${filtro.situacao.idMarcador == m.cpMarcador.idMarcador || filtro.situacao.idMarcador == null}">
									<c:set var="j" value="${cont == 0 ? 0 : k}"/>
									<tr class="status-${j}">
										<td style="width:73px!important;">${m.dtIniMarcaDDMMYYYY}</td>
										<td style="width:50px!important;">
											<span title="${m.dpLotacaoIni?.descricao}">${m.dpLotacaoIni.sigla}</span>
										</td>
										<td style="width:50px!important;">
											<span title="${m.dpPessoaIni?.descricao}">${m.dpPessoaIni.sigla}</span>
										</td style="width:20%!important;">
										<td>${m.cpMarcador.descrMarcador}</td>
									</tr>
									<c:set var="cont" value="${cont + 1}"/>
									</c:if>
								</c:forEach>
							</table>
						</td>
						<td style="width:11%!important;">${i.tipo.nome}</td>
						<td>
							<a href="${linkTo[AppController].exibir[i.sigla]}">${i.arq.titulo}</a>
						</td>
						<td width="2%">
								<c:if test="${cont > 1}">
									<img style="width: 13px;" id="imgPlus-${k}"  src="/siga/css/famfamfam/icons/plus_toggle.png" alt="mais" title="Ver Detalhes" />
									<img style="width: 13px;" id="imgMinus-${k}" src="/siga/css/famfamfam/icons/minus_toggle.png" alt="menos" title="Ocultar Detalhes"/>	
								</c:if>
						</td>
					</tr>
					<c:set var="k" value="${k + 1}"/>
					</c:forEach>
				</tbody>
			</table>
		</div>
		</c:when>
		<c:when test="${filtro.pesquisa}">
			<p class="gt-notice-box">A pesquisa não retornou resultados.</p>
		</c:when>
		</c:choose>
		<c:set var="count" value="${estatistica - lista.size()}"/>
		<c:if test="${count > 0}"> 
			<br />	
			<h6 style="background:#d8d8c0;padding:3px 10px;">${count} Conhecimento${count.pluralize()} fo${count.pluralize('i','ram')} Cancelado${count.pluralize()}, por isso não const${count.pluralize('a','am')} no resultado acima.</h6>
		</c:if>
		<c:set var="count" value="${0}"/>
		<!-- </table></div> -->
		<br>
		<div class="gt-content-box gt-for-table">

			<form id="listar" name="listar"
				onsubmit="javascript: return limpaCampos()" method="GET"
				class="form100">
				<input type="hidden" name="filtro.pesquisa" value="true" />
				<table class="gt-form-table">
					<colgroup>
						<col style="width: 11em;">
						<col>
					</colgroup>
					<input type="hidden" name="popup" value="">
					<input type="hidden" name="propriedade" value="">
					<input type="hidden" name="postback" value="1" id="postback">
					<input type="hidden" name="apenasRefresh" value="0"
						id="apenasRefresh">
					<input type="hidden" name="p.offset" value="0" id="p_offset">
					<tbody>
						<tr class="header">
							<td align="center" valign="top" colspan="4">Dados do Conhecimento</td>
						</tr>
						<tr>
							<td>Situação:</td>
							<td>#{select name:'filtro.situacao', items:marcadores,
								labelProperty:'descrMarcador', valueProperty:'idMarcador',
								value:filtro?.situacao?.idMarcador, id:'selectSituacao'} #{option null}Todas#{/option}
								#{/select} *{&nbsp;&nbsp;&nbsp;&nbsp;Pessoa/Lotação:
								#{pessoaLotaSelecao nomeSelPessoa:'marcado',
								nomeSelLotacao:'lotaMarcado', value:informacao?.autor /}</td>}*
						</tr>
						<tr>
							<td>Órgão:</td>
							<td>#{select name:'filtro.orgaoUsu', items:orgaosusuarios,
								labelProperty:'descricao', valueProperty:'id',
								value:filtro?.orgaoUsu?.id, id:'selectOrgao'} #{option null}Todas#{/option} #{/select}</td>
						</tr>
						<tr id="trTipo" style="display: ">
							<td>Tipo:</td>
							<td>#{select name:'filtro.tipo', items:tiposinformacao,
								labelProperty:'nome', valueProperty:'id',
								value:filtro?.tipo?.id, id:'selectTipo'} #{option null}Todas#{/option} #{/select}</td>
						</tr>
						<tr>
							<td>Ano de Emissão:</td>
							<td>#{select name:'filtro.ano', items:anos,
								labelProperty:'toString()', valueProperty:'toString()',
								value:filtro?.ano, id:'selectAno'} #{option null}Todos#{/option} #{/select}
								&nbsp;&nbsp;&nbsp;&nbsp;Número: <input type="text"
								name="filtro.numero" size="7" maxlength="6" value="${filtro.numero}"
								id="filtronumero">
								<span id="dicaNumero" style="font-size:smaller;">Ex: JFRJ-GC-2013/<b style="background:#cccccc;">00152</b></span>
							</td>
						</tr>
						<tr>
							<td>Data de Criação:</td>
							<td>de&nbsp;&nbsp; <input type="text" name="filtro.dtCriacaoIni"
								value="${filtro.dtCriacaoIni}" id="dtIniString"
								onblur="javascript:verifica_data(this,0);">
								&nbsp;&nbsp;até &nbsp;&nbsp;<input type="text" name="filtro.dtCriacaoFim"
								value="${filtro.dtCriacaoFim}" id="dtFimString"
								onblur="javascript:verifica_data(this,0);"></td>
						</tr>
						<tr>
							<td>Autor:</td>
							*{<td>#{pessoaLotaSelecao nomeSelPessoa:'autor',
								nomeSelLotacao:'lotaAutor', value:informacao?.autor /}</td>}*
							<td>#{pessoaLotaSelecao nomeSelPessoa:'filtro.autor',
								nomeSelLotacao:'filtro.lotacao',
								valuePessoa:filtro.autor,
								valueLotacao:filtro.lotacao /}</td>
						</tr>
						*{<tr>
							<td>Cadastrante:</td>
							<td>#{pessoaLotaSelecao nomeSelPessoa:'cadastrante',
								nomeSelLotacao:'lotaCadastrante', value:informacao?.autor /}</td>
						</tr>}*
						<tr>
							<td>Data da Situação:</td>
							<td>de&nbsp;&nbsp; <input type="text" name="filtro.dtIni"
								value="${filtro.dtIni}" id="dtSituacaoIniString"
								onblur="javascript:verifica_data(this,0);">
								&nbsp;&nbsp;até &nbsp;&nbsp;<input type="text" name="filtro.dtFim"
								value="${filtro.dtFim}" id="dtSituacaoFimString"
								onblur="javascript:verifica_data(this,0);"></td>
						</tr>
						<tr>
							<td>Responsável:</td>
							*{<td>#{pessoaLotaSelecao nomeSelPessoa:'responsavel',
								nomeSelLotacao:'lotaResponsavel', value:informacao?.autor /}</td>}*
							<td>#{pessoaLotaSelecao nomeSelPessoa:'filtro.responsavel',
								nomeSelLotacao:'filtro.lotaResponsavel',
								valuePessoa:filtro.responsavel,
								valueLotacao:filtro.lotaResponsavel /}</td>
						</tr>

						<!-- A lista de par -->
						<tr>
							<td>Classificação:</td>
							<td>#{selecao tipo:'tag', nome:'filtro.tag', value:filtro.tag /}</td>
						</tr>
						<tr>
							<td class="tdLabel"><label for="descrDocumento"
								class="label">Título:</label>
							</td>
							<td><input type="text" name="filtro.titulo" size="80"
								value="${filtro.titulo}" id="titulo"></td>
						</tr>
						<tr>
							<td class="tdLabel"><label class="label" for="fullText">Conteúdo:</label>
							</td>
							<td><input type="text" id="conteudo" value="${filtro.conteudo}" size="80"
								name="filtro.conteudo"></td>
						</tr>
						<tr>
							<td><input type="submit"
								onclick="javascript:  if (isCarregando()) return false; carrega();"
								class="gt-btn-medium gt-btn-left" style="cursor: pointer;" value="Buscar">
							</td>
							<td><input type="button" id="btn-clear"								
								class="gt-btn-medium" style="cursor: pointer;" value="Limpar">
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	</div>
</div>

<script type="text/javascript">
		$(document).ready(function(){
			//adiciona uma dica para o preenchimento - número do conhecimento
			$("#dicaNumero").hide();
			$("[id^='imgMinus-']").hide();
			$("[class^='status-']").hide();
			$(".status-0").show();
			$("#filtronumero").focus(function (){
				$("#dicaNumero").show("fast");
			})
			.focusout(function(){
				$("#dicaNumero").hide("fast");
			});	

			$("[id^='imgMinus-'],[id^='imgPlus-']").mouseenter(function(){
				$(this).css({"cursor": "pointer","opacity":"0.8","filter":"alpha(opacity=70)"});
			}).mouseleave(function(){
				$(this).css({"cursor": "default","opacity":"1","filter":"alpha(opacity=100)"});
			});
			$("[id^='imgMinus-']").click(function(){	
				var idImg = $(this).attr("id");
				var id = idImg.split("-")[1];
				$(this).hide("fast");
				$("#imgPlus-" + id).show("fast");
				$(".status-" + id).hide("fast");	
			});		
			$("[id^='imgPlus-']").click(function(){	
				var idImg = $(this).attr("id");
				var id = idImg.split("-")[1];
				$(this).hide("fast");
				$("#imgMinus-" + id).show("fast");
				$(".status-" + id).show("fast");	
			});	
			$("#btn-clear").click(function(){	
				$("#selectSituacao").val('');
				$("#selectOrgao").val('');
				$("#selectTipo").val('');
				$("#selectAno").val('');
								
				//$("#filtroautorfiltrolotacao").val('');
				//$("#filtroresponsavelfiltrolotaResponsavel").val('');
							
				$("#filtrolotacaoSpan").empty();
				$("#filtrolotaResponsavelSpan").empty();
				$("#filtrotagSpan").empty();

				$("#filtrolotacao_sigla").val('');
				$("#filtrolotaResponsavel_sigla").val('');
				$("#filtrotag_sigla").val('');
				
				$("#filtronumero").val('');
				$("#dtIniString").val('');
				$("#dtFimString").val('');
				$("#dtSituacaoIniString").val('');
				$("#dtSituacaoFimString").val('');
				$("#titulo").val('');
				$("#conteudo").val('');
				$("#descrMarcador").val('');			
			});	
		});
</script>
<style>
	.gt-table-lista tr td{border-top: 1px solid #ccc !important;}
	.gt-table-lista tr:first-child td {border:none !important;}
</style>
</siga:pagina>

