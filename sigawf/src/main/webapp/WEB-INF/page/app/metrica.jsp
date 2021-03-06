<%@ include file="/WEB-INF/page/include.jsp"%>

<script type="text/javascript" language="Javascript1.1">
	function sbmt() {
		frmRelatorio.action = '${linkTo[MedirController].relatorio}';
		frmRelatorio.submit();
	}
</script>

<script type="text/javascript">
	function exibirOpcoesExtras() {
		if ($("#selecaoRelatorio").val() == '2'
				|| $("#selecaoRelatorio").val() == '3') {
			$("#opcoesExtras").show();
		} else {
			$("#opcoesExtras").hide();
		}
		if ($("#selecaoRelatorio").val() == '1') {
			$("#opcaoPercentualMediaTruncada").show();
		} else {
			$("#opcaoPercentualMediaTruncada").hide();
		}
		if ($("#selecaoRelatorio").val() == '3') {
			$("#opcaoGrupos").show();
		} else {
			$("#opcaoGrupos").hide();
		}

	}
</script>

<siga:pagina titulo="Estat�stica de procedimento">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>${titulo_pagina}</h2>
			<div class="gt-content-box gt-for-table">
				<form name="frmRelatorio" method="get" class="form">
					<table class="gt-form-table">
						<input type="hidden" name="pdId" value="${pdId}" />
						<input type="hidden" name="orgao" value="${pdId}" />
						<input type="hidden" name="procedimento" value="${procedimento}" />
						<tr>
							<td>�rg�o</td>
							<td>${orgao}</td>
						</tr>
						<tr>
							<td>Procedimento</td>
							<td>${procedimento}</td>
						</tr>
						<tr>
							<siga:select
								list="#{'1':'Estat�sticas gerais', '2':'Tempo de documentos','3':'Tempo de documentos detalhado'}"
								label="Relat�rio" name="selecaoRelatorio"
								onchange="javascript:exibirOpcoesExtras()" />
						</tr>
						<tr>
							<td width="25%">Procedimento iniciado de:</td>
							<td><input type="text" name="dataInicialDe"
								onblur="javascript:verifica_data(this, true);comparaData(dataInicialDe,dataInicialAte);
				comparaData(dataInicialAte,dataFinalDe)"
								size="12" maxlength="10" /> at� <input type="text"
								name="dataInicialAte"
								onblur="javascript:verifica_data(this, true);comparaData(dataInicialDe,dataInicialAte);
				comparaData(dataInicialAte,dataFinalDe)"
								size="12" maxlength="10" /></td>
						</tr>
						<tr>
							<td>Procedimento finalizado de:</td>
							<td><input type="text" name="dataFinalDe"
								onblur="javascript:verifica_data(this,true);comparaData(dataInicialDe,dataInicialAte);
				comparaData(dataInicialAte,dataFinalDe);"
								size="12" maxlength="10" /> at� <input type="text"
								name="dataFinalAte"
								onblur="javascript:verifica_data(this,true);comparaData(dataInicialDe,dataInicialAte);
				comparaData(dataInicialAte,dataFinalDe);"
								size="12" maxlength="10" /></td>
						</tr>
						<tr id="opcoesExtras" style="display: none">
							<td><input type="checkbox" id="incluirAbertos"
								name="incluirAbertos" style="float: left"
								class="gt-form-checkbox"></input> <label>&nbsp;Incluir
									Procedimentos Abertos</label></td>
						</tr>
						<tr id="opcaoPercentualMediaTruncada">
							<td><label>Percentual&nbsp;da&nbsp;M�dia&nbsp;Truncada
									(entre ${minMediaTruncada}% e ${maxMediaTruncada}%):</label></td>
							<td><input type="text" id="percentualMediaTruncada"
								name="percentualMediaTruncada" style="float: left"
								class="gt-form-text" value="${minMediaTruncada}"></input></td>
						</tr>
						<tr id="opcaoGrupos" style="display: none">
							<td>Agrupar tarefas (opcional)</td>
							<td><label>Tarefa inicial:</label> <siga:select id="grpIni"
									name="grpIni" list="lstGruposIni" listValue="name" listKey="id"
									headerKey="-1" headerValue="[Escolha uma tarefa]"
									theme="simple"/> <label>Tarefa final:</label> <siga:select
									id="grpFim" name="grpFim" list="lstGruposFim" listValue="name"
									listKey="id" headerKey="-1" headerValue="[Escolha uma tarefa]"
									theme="simple"/></td>
						</tr>

						<tr>
							<td colspan="2"><input type="button"
								onclick="javascript:sbmt()" value="Gerar relat�rio"
								class="gt-btn-medium gt-btn-left" /></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
